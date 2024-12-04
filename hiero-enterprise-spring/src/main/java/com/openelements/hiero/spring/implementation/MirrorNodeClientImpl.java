package com.openelements.hiero.spring.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.ExchangeRate;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.TransactionInfo;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
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
    public Page<Nft> queryNftsByAccount(@NonNull final AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        final String path = "/api/v1/accounts/" + accountId + "/nfts";
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> getNfts(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Page<Nft> queryNftsByAccountAndTokenId(@NonNull final AccountId accountId, @NonNull final TokenId tokenId) {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        final String path = "/api/v1/tokens/" + tokenId + "/nfts/?account.id=" + accountId;
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> getNfts(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) {
        final String path = "/api/v1/tokens/" + tokenId + "/nfts";
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> getNfts(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull final TokenId tokenId, @NonNull final long serialNumber)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        if (serialNumber <= 0) {
            throw new IllegalArgumentException("serialNumber must be positive");
        }
        final JsonNode jsonNode = doGetCall("/api/v1/tokens/" + tokenId + "/nfts/" + serialNumber);
        return jsonNodeToOptionalNft(jsonNode);
    }

    @Override
    public Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull final AccountId accountId,
            @NonNull final TokenId tokenId, final long serialNumber) throws HieroException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        return queryNftsByTokenIdAndSerial(tokenId, serialNumber)
                .filter(nft -> Objects.equals(nft.owner(), accountId));
    }

    @Override
    public Page<TransactionInfo> queryTransactionsByAccount(@NonNull final AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        final String path = "/api/v1/transactions?account.id=" + accountId.toString();
        final Function<JsonNode, List<TransactionInfo>> dataExtractionFunction = this::extractTransactionInfoFromJsonNode;
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Optional<TransactionInfo> queryTransaction(@NonNull final String transactionId) throws HieroException {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        final JsonNode jsonNode = doGetCall("/api/v1/transactions/" + transactionId);
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        return Optional.of(new TransactionInfo(transactionId));
    }

    @Override
    public @NonNull Optional<AccountInfo> queryAccount(@NonNull AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        final JsonNode jsonNode = doGetCall("/api/v1/accounts/" + accountId);
        return jsonNodeToOptionalAccountINfo(jsonNode);
    }

    @Override
    public @NonNull Optional<ExchangeRates> queryExchangeRates() throws HieroException {
        final JsonNode jsonNode = doGetCall("/api/v1/network/exchangerate");
        return jsonNodeToOptionalExchangeRates(jsonNode);
    }

    @Override
    public @NonNull List<NetworkFee> queryNetworkFees() throws HieroException {
        final JsonNode jsonNode = doGetCall("/api/v1/network/fees");
        return jsonNodeToListNetworkFee(jsonNode);
    }

    @Override
    public @NonNull Optional<NetworkStake> queryNetworkStake() throws HieroException {
        final JsonNode jsonNode = doGetCall("/api/v1/network/stake");
        return jsonNodeToOptionalNetworkStake(jsonNode);
    }

    @Override
    public @NonNull Optional<NetworkSupplies> queryNetworkSupplies() throws HieroException {
        final JsonNode jsonNode = doGetCall("/api/v1/network/supply");
        return jsonNodeToOptionalNetworkSupplies(jsonNode);
    }

    private JsonNode doGetCall(String path, Map<String, ?> params) throws HieroException {
        return doGetCall(builder -> {
            UriBuilder uriBuilder = builder.path(path);
            for (Map.Entry<String, ?> entry : params.entrySet()) {
                uriBuilder = uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
            return uriBuilder.build();
        });
    }

    private JsonNode doGetCall(String path) throws HieroException {
        return doGetCall(builder -> builder.path(path).build());
    }

    private JsonNode doGetCall(Function<UriBuilder, URI> uriFunction) throws HieroException {
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
            throw new HieroException("Error parsing body as JSON: " + body, e);
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

    private Optional<Nft> jsonNodeToOptionalNft(final JsonNode jsonNode) throws HieroException {
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonNodeToNft(jsonNode));
        } catch (final Exception e) {
            throw new HieroException("Error parsing NFT from JSON '" + jsonNode + "'", e);
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

    private @NonNull Optional<AccountInfo> jsonNodeToOptionalAccountINfo(JsonNode jsonNode) throws HieroException {
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonNodeToAccountInfo(jsonNode));
        } catch (final Exception e) {
            throw new HieroException("Error parsing AccountInfo from JSON '" + jsonNode + "'", e);
        }
    }

    private AccountInfo jsonNodeToAccountInfo(JsonNode jsonNode) {
        try {
            final AccountId accountId = AccountId.fromString(jsonNode.get("account").asText());
            final String evmAddress = jsonNode.get("evm_address").asText();
            final long ethereumNonce = jsonNode.get("ethereum_nonce").asLong();
            final long pendingReward = jsonNode.get("pending_reward").asLong();
            final long balance = jsonNode.get("balance").get("balance").asLong();
            return new AccountInfo(accountId, evmAddress, balance, ethereumNonce, pendingReward);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing NFT from JSON '" + jsonNode + "'", e);
        }
    }

    private @NonNull Optional<ExchangeRates> jsonNodeToOptionalExchangeRates(JsonNode jsonNode) throws HieroException {
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonNodeToExchangeRates(jsonNode));
        } catch (final Exception e) {
            throw new HieroException("Error parsing ExchangeRates from JSON '" + jsonNode + "'", e);
        }
    }

    private ExchangeRates jsonNodeToExchangeRates(JsonNode jsonNode) {
        try {
            final int currentCentEquivalent = jsonNode.get("current_rate").get("cent_equivalent").asInt();
            final int currentHbarEquivalent = jsonNode.get("current_rate").get("hbar_equivalent").asInt();
            final Instant currentExpirationTime = Instant.ofEpochSecond(
                    jsonNode.get("current_rate").get("expiration_time").asLong()
            );

            final int nextCentEquivalent = jsonNode.get("next_rate").get("cent_equivalent").asInt();
            final int nextHbarEquivalent = jsonNode.get("next_rate").get("hbar_equivalent").asInt();
            final Instant nextExpirationTime = Instant.ofEpochSecond(
                    jsonNode.get("next_rate").get("expiration_time").asLong()
            );

            return new ExchangeRates(
                    new ExchangeRate(currentCentEquivalent, currentHbarEquivalent, currentExpirationTime),
                    new ExchangeRate(nextCentEquivalent, nextHbarEquivalent, nextExpirationTime)
            );
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing ExchangeRates from JSON '" + jsonNode + "'", e);
        }
    }

    private List<NetworkFee> jsonNodeToListNetworkFee(JsonNode jsonNode) throws HieroException {
        if (jsonNode == null || !jsonNode.fieldNames().hasNext() || !jsonNode.has("fees")) {
            return List.of();
        }

        final JsonNode feesNode = jsonNode.get("fees");
        if (!feesNode.isArray()) {
            throw new IllegalArgumentException("Fees node is not an array: " + feesNode);
        }

        try {
            return StreamSupport
                    .stream(Spliterators.spliteratorUnknownSize(feesNode.iterator(), Spliterator.ORDERED), false)
                    .map(this::jsonNodeToNetworkFee)
                    .toList();
        } catch (final Exception e) {
            throw new HieroException("Error parsing NetworkFees from JSON '" + jsonNode + "'", e);
        }
    }

    private NetworkFee jsonNodeToNetworkFee(JsonNode jsonNode) {
        try {
            final long gas = jsonNode.get("gas").asLong();
            final String transactionType = jsonNode.get("transaction_type").asText();

            return new NetworkFee(gas, transactionType);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing NetworkFee from JSON '" + jsonNode + "'", e);
        }
    }

    private @NonNull Optional<NetworkStake> jsonNodeToOptionalNetworkStake(JsonNode jsonNode) throws HieroException {
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonNodeToNetworkStake(jsonNode));
        } catch (final Exception e) {
            throw new HieroException("Error parsing NetworkStake from JSON '" + jsonNode + "'", e);
        }
    }

    private NetworkStake jsonNodeToNetworkStake(JsonNode jsonNode) {
        try {
            final long maxStakeReward = jsonNode.get("max_stake_rewarded").asLong();
            final long maxStakeRewardPerHbar = jsonNode.get("max_staking_reward_rate_per_hbar").asLong();
            final long maxTotalReward = jsonNode.get("max_total_reward").asLong();
            final double nodeRewardFeeFraction = jsonNode.get("node_reward_fee_fraction").asDouble();
            final long reservedStakingRewards = jsonNode.get("reserved_staking_rewards").asLong();
            final long rewardBalanceThreshold = jsonNode.get("reward_balance_threshold").asLong();
            final long stakeTotal = jsonNode.get("stake_total").asLong();
            final long stakingPeriodDuration = jsonNode.get("staking_period_duration").asLong();
            final long stakingPeriodsStored = jsonNode.get("staking_periods_stored").asLong();
            final double stakingRewardFeeFraction = jsonNode.get("staking_reward_fee_fraction").asDouble();
            final long stakingRewardRate = jsonNode.get("staking_reward_rate").asLong();
            final long stakingStartThreshold = jsonNode.get("staking_start_threshold").asLong();
            final long unreservedStakingRewardBalance = jsonNode.get("unreserved_staking_reward_balance").asLong();

            return new NetworkStake(
                    maxStakeReward,
                    maxStakeRewardPerHbar,
                    maxTotalReward,
                    nodeRewardFeeFraction,
                    reservedStakingRewards,
                    rewardBalanceThreshold,
                    stakeTotal,
                    stakingPeriodDuration,
                    stakingPeriodsStored,
                    stakingRewardFeeFraction,
                    stakingRewardRate,
                    stakingStartThreshold,
                    unreservedStakingRewardBalance
            );
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing NetworkStake from JSON '" + jsonNode + "'", e);
        }
    }

    private @NonNull Optional<NetworkSupplies> jsonNodeToOptionalNetworkSupplies(JsonNode jsonNode)
            throws HieroException {
        if (jsonNode == null || !jsonNode.fieldNames().hasNext()) {
            return Optional.empty();
        }
        try {
            return Optional.of(jsonNodeToNetworkSupplies(jsonNode));
        } catch (final Exception e) {
            throw new HieroException("Error parsing NetworkSupplies from JSON '" + jsonNode + "'", e);
        }
    }

    private NetworkSupplies jsonNodeToNetworkSupplies(JsonNode jsonNode) {
        try {
            final String releasedSupply = jsonNode.get("released_supply").asText();
            final String totalSupply = jsonNode.get("total_supply").asText();

            return new NetworkSupplies(releasedSupply, totalSupply);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing NetworkSupplies from JSON '" + jsonNode + "'", e);
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

    private List<TransactionInfo> extractTransactionInfoFromJsonNode(JsonNode jsonNode) {
        if (!jsonNode.has("transactions")) {
            return List.of();
        }
        final JsonNode transactionsNode = jsonNode.get("transactions");
        if (!transactionsNode.isArray()) {
            throw new IllegalArgumentException("Transactions node is not an array: " + transactionsNode);
        }
        return StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(transactionsNode.iterator(), Spliterator.ORDERED), false)
                .map(transactionNode -> {
                    try {
                        final String transactionId = transactionNode.get("transaction_id").asText();
                        return new TransactionInfo(transactionId);
                    } catch (final Exception e) {
                        throw new RuntimeException("Error parsing transaction from JSON '" + transactionNode + "'", e);
                    }
                }).toList();
    }
}
