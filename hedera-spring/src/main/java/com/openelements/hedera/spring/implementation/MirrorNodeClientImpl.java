package com.openelements.hedera.spring.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
import com.openelements.hedera.base.mirrornode.TransactionInfo;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class MirrorNodeClientImpl implements MirrorNodeClient {

    private final ObjectMapper objectMapper;

    private final RestClient restClient;

    private final String mirrorNodeEndpoint;

    public MirrorNodeClientImpl(@NonNull final Client client) {
        Objects.requireNonNull(client, "client must not be null");
        final List<String> mirrorNetwork = client.getMirrorNetwork();
        if (mirrorNetwork.isEmpty()) {
            throw new IllegalArgumentException("No mirror network is configured");
        }
        mirrorNodeEndpoint = "https://" + mirrorNetwork.get(0);
        objectMapper = new ObjectMapper();
        restClient = RestClient.create();
    }

    @Override
    public List<Nft> queryNftsByAccount(@NonNull final AccountId accountId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        final String body = restClient.get()
                .uri(mirrorNodeEndpoint + "/api/v1/accounts/" + accountId + "/nfts")
                .header("accept", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("Client error: " + response.getStatusText());
                }).body(String.class);
        return parseJsonToList(body);
    }

    @Override
    public List<Nft> queryNftsByAccountAndTokenId(@NonNull final AccountId accountId, @NonNull final TokenId tokenId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        final String host = mirrorNodeEndpoint.substring(8).split("\\:")[0];
        final String port = mirrorNodeEndpoint.substring(8).split("\\:")[1];
        final String body = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                                .host(host)
                                .port(port)
                        .path("/api/v1/tokens/" + tokenId + "/nfts")
                        .queryParam("account.id", accountId)
                        .build())
                .header("accept", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("Client error: " + response.getStatusText());
                }).body(String.class);
        return parseJsonToList(body);
    }

    @Override
    public List<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HederaException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        final String body = restClient.get()
                .uri(mirrorNodeEndpoint + "/api/v1/tokens/" + tokenId + "/nfts")
                .header("accept", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new RuntimeException("Client error: " + response.getStatusText());
                }).body(String.class);
        return parseJsonToList(body);
    }

    private List<Nft> parseJsonToList(final String json) {
        try {
            final JsonNode rootNode = objectMapper.readTree(json);
            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(rootNode.get("nfts").iterator(), Spliterator.ORDERED),
                    false).map(nftNode -> {
                try {
                    return parseJson(nftNode.toString());
                } catch (final Exception e) {
                    throw new RuntimeException("Error parsing NFT", e);
                }
            }).toList();
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing body as JSON: " + json, e);
        }
    }

    private Nft parseJson(final String json) {
        try {
            final JsonNode nftNode = objectMapper.readTree(json);
            final TokenId parsedTokenId = TokenId.fromString(nftNode.get("token_id").asText());
            final AccountId account = AccountId.fromString(nftNode.get("account_id").asText());
            final long serial = nftNode.get("serial_number").asLong();
            final byte[] metadata = nftNode.get("metadata").binaryValue();
            return new Nft(parsedTokenId, serial, account, metadata);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing body as JSON: " + json, e);
        }
    }

    @Override
    public Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull final TokenId tokenId, @NonNull final long serialNumber) throws HederaException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        if(serialNumber <= 0) {
            throw new IllegalArgumentException("serialNumber must be positive");
        }
        final String body = restClient.get()
                .uri(mirrorNodeEndpoint + "/api/v1/tokens/" + tokenId + "/nfts/" + serialNumber)
                .header("accept", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if (!HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
                        throw new RuntimeException("Client error: " + response.getStatusText());
                    }
                }).body(String.class);
        if(body == null || body.equals("{\"_status\":{\"messages\":[{\"message\":\"Not found\"}]}}")) {
            return Optional.empty();
        }
        return Optional.ofNullable(parseJson(body));
    }

    @Override
    public Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull final AccountId accountId, @NonNull final TokenId tokenId, final long serialNumber) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryNftsByTokenIdAndSerial(tokenId, serialNumber)
                .filter(nft -> Objects.equals(nft.owner(), accountId));
    }

    @Override
    public Optional<TransactionInfo> queryTransaction(@NonNull final String transactionId) throws HederaException {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        final ResponseEntity<String> entity = restClient.get()
                .uri(mirrorNodeEndpoint + "/api/v1/transactions/" + transactionId)
                .header("accept", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if (!HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
                        throw new RuntimeException("Client error: " + response.getStatusText());
                    }
                }).toEntity(String.class);
        if(HttpStatus.NOT_FOUND.equals(entity.getStatusCode())) {
            return Optional.empty();
        }
        final String body = entity.getBody();
        //TODO: JSON PARSING
        return Optional.of(new TransactionInfo(transactionId));
    }
}
