package com.openelements.hedera.spring.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
import com.openelements.hedera.base.mirrornode.Page;
import com.openelements.hedera.base.mirrornode.TransactionInfo;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

public class MirrorNodeClientImpl implements MirrorNodeClient {

    private final ObjectMapper objectMapper;

    private final RestClient restClient;

    /**
     * Constructor.
     *
     * @param restClientBuilder the builder for the REST client that must have the base URL set
     */
    public MirrorNodeClientImpl(final RestClient.Builder restClientBuilder) {
        Objects.requireNonNull(restClientBuilder, "restClientBuilder must not be null");
        objectMapper = new ObjectMapper();
        restClient = restClientBuilder.build();

    }

    @Override
    public Page<Nft> queryNftsByAccount(@NonNull final AccountId accountId) throws HederaException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        final String path = "/api/v1/accounts/" + accountId + "/nfts";
        
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> getNfts(node);
       

        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path,dataExtractionFunction);
    }

    @Override
	public Page<Nft> queryNftsByAccountAndTokenId(@NonNull final AccountId accountId, @NonNull final TokenId tokenId)
			throws HederaException {
		Objects.requireNonNull(accountId, "newAccountId must not be null");
		Objects.requireNonNull(tokenId, "tokenId must not be null");

		final String path = "/api/v1/tokens/" + tokenId + "/nfts/" + accountId;

		final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> getNfts(node);

		return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);

	}

    @Override
    public Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HederaException {
        final String path = "/api/v1/tokens/" + tokenId + "/nfts";
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> getNfts(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull final TokenId tokenId, @NonNull final long serialNumber)
            throws HederaException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        if (serialNumber <= 0) {
            throw new IllegalArgumentException("serialNumber must be positive");
        }
        final JsonNode jsonNode = doGetCall("/api/v1/tokens/" + tokenId + "/nfts/" + serialNumber);
        return jsonNodeToOptionalNft(jsonNode);
    }

    @Override
    public Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull final AccountId accountId,
            @NonNull final TokenId tokenId, final long serialNumber) throws HederaException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        return queryNftsByTokenIdAndSerial(tokenId, serialNumber)
                .filter(nft -> Objects.equals(nft.owner(), accountId));
    }

    @Override
    public Optional<TransactionInfo> queryTransaction(@NonNull final String transactionId) throws HederaException {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        final JsonNode jsonNode = doGetCall("/api/v1/transactions/" + transactionId);
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        return Optional.of(new TransactionInfo(transactionId));
    }

    private JsonNode doGetCall(String path, Map<String, ?> params) throws HederaException {
        return doGetCall(builder -> {
            UriBuilder uriBuilder = builder.path(path);
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                uriBuilder = uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
            return uriBuilder.build();
        });
    }

    private JsonNode doGetCall(String path) throws HederaException {
        return doGetCall(builder -> builder.path(path).build());
    }

    private JsonNode doGetCall(Function<UriBuilder, URI> uriFunction) throws HederaException {
        final ResponseEntity<String> responseEntity = restClient.get()
                .uri(uriBuilder -> uriFunction.apply(uriBuilder))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if (!HttpStatus.NOT_FOUND.equals(response.getStatusCode())) {
                        throw new RuntimeException("Client error: " + response.getStatusText());
                    }
                })
                .toEntity(String.class);
        final String body = responseEntity.getBody();
        try {
            if (HttpStatus.NOT_FOUND.equals(responseEntity.getStatusCode())) {
                return objectMapper.readTree("{}");
            }
            return objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new HederaException("Error parsing body as JSON: " + body, e);
        }
    }

    private List<Nft> jsonNodeToNftList(final JsonNode rootNode) {
        if (rootNode == null || !rootNode.fieldNames().hasNext()) {
            return List.of();
        }
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(rootNode.get("nfts").iterator(), Spliterator.ORDERED),
                false).map(nftNode -> {
            try {
                return jsonNodeToNft(nftNode);
            } catch (final Exception e) {
                throw new RuntimeException("Error parsing NFT from JSON '" + nftNode + "'", e);
            }
        }).toList();
    }

    private Optional<Nft> jsonNodeToOptionalNft(final JsonNode jsonNode) throws HederaException {
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonNodeToNft(jsonNode));
        } catch (final Exception e) {
            throw new HederaException("Error parsing NFT from JSON '" + jsonNode + "'", e);
        }
    }

    private Nft jsonNodeToNft(final JsonNode jsonNode) throws IOException {
        try {
            final TokenId parsedTokenId = TokenId.fromString(jsonNode.get("token_id").asText());
            final AccountId account = AccountId.fromString(jsonNode.get("account_id").asText());
            final long serial = jsonNode.get("serial_number").asLong();
            final byte[] metadata = jsonNode.get("metadata").binaryValue();
            return new Nft(parsedTokenId, serial, account, metadata);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing NFT from JSON '" + jsonNode + "'", e);
        }
    }

    private List<Nft> getNfts(final JsonNode jsonNode) {
        if (!jsonNode.has("nfts")) {
            return List.of();
        }
        final JsonNode nftsNode = jsonNode.get("nfts");
        if (!nftsNode.isArray()) {
            throw new IllegalArgumentException("NFTs node is not an array: " + nftsNode);
        }
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(nftsNode.iterator(), Spliterator.ORDERED),
                        false)
                .map(nftNode -> {
                    try {
                        return jsonNodeToNft(nftNode);
                    } catch (final Exception e) {
                        throw new RuntimeException("Error parsing NFT from JSON '" + nftNode + "'", e);
                    }
                }).toList();
    }
}
