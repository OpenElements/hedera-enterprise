package com.openelements.hiero.spring.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.ExchangeRate;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jspecify.annotations.NonNull;

public class MirrorNodeJsonConverterImpl implements MirrorNodeJsonConverter<JsonNode> {

    @Override
    public Optional<Nft> toNft(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final TokenId parsedTokenId = TokenId.fromString(node.get("token_id").asText());
            final AccountId account = AccountId.fromString(node.get("account_id").asText());
            final long serial = node.get("serial_number").asLong();
            final byte[] metadata = node.get("metadata").binaryValue();
            return Optional.of(new Nft(parsedTokenId, serial, account, metadata));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<NetworkSupplies> toNetworkSupplies(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final String releasedSupply = node.get("released_supply").asText();
            final String totalSupply = node.get("total_supply").asText();
            return Optional.of(new NetworkSupplies(releasedSupply, totalSupply));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<NetworkStake> toNetworkStake(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final long maxStakeReward = node.get("max_stake_rewarded").asLong();
            final long maxStakeRewardPerHbar = node.get("max_staking_reward_rate_per_hbar").asLong();
            final long maxTotalReward = node.get("max_total_reward").asLong();
            final double nodeRewardFeeFraction = node.get("node_reward_fee_fraction").asDouble();
            final long reservedStakingRewards = node.get("reserved_staking_rewards").asLong();
            final long rewardBalanceThreshold = node.get("reward_balance_threshold").asLong();
            final long stakeTotal = node.get("stake_total").asLong();
            final long stakingPeriodDuration = node.get("staking_period_duration").asLong();
            final long stakingPeriodsStored = node.get("staking_periods_stored").asLong();
            final double stakingRewardFeeFraction = node.get("staking_reward_fee_fraction").asDouble();
            final long stakingRewardRate = node.get("staking_reward_rate").asLong();
            final long stakingStartThreshold = node.get("staking_start_threshold").asLong();
            final long unreservedStakingRewardBalance = node.get("unreserved_staking_reward_balance").asLong();

            return Optional.of(new NetworkStake(
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
            ));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<ExchangeRates> toExchangeRates(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final int currentCentEquivalent = node.get("current_rate").get("cent_equivalent").asInt();
            final int currentHbarEquivalent = node.get("current_rate").get("hbar_equivalent").asInt();
            final Instant currentExpirationTime = Instant.ofEpochSecond(
                    node.get("current_rate").get("expiration_time").asLong()
            );

            final int nextCentEquivalent = node.get("next_rate").get("cent_equivalent").asInt();
            final int nextHbarEquivalent = node.get("next_rate").get("hbar_equivalent").asInt();
            final Instant nextExpirationTime = Instant.ofEpochSecond(
                    node.get("next_rate").get("expiration_time").asLong()
            );

            return Optional.of(new ExchangeRates(
                    new ExchangeRate(currentCentEquivalent, currentHbarEquivalent, currentExpirationTime),
                    new ExchangeRate(nextCentEquivalent, nextHbarEquivalent, nextExpirationTime)
            ));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<AccountInfo> toAccountInfo(final JsonNode node) {
        try {
            final AccountId accountId = AccountId.fromString(node.get("account").asText());
            final String evmAddress = node.get("evm_address").asText();
            final long ethereumNonce = node.get("ethereum_nonce").asLong();
            final long pendingReward = node.get("pending_reward").asLong();
            final long balance = node.get("balance").get("balance").asLong();
            return Optional.of(new AccountInfo(accountId, evmAddress, balance, ethereumNonce, pendingReward));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public List<NetworkFee> toNetworkFees(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return List.of();
        }

        if (!node.has("fees")) {
            return List.of();
        }

        final JsonNode feesNode = node.get("fees");
        return jsonArrayToStream(feesNode)
                .map(n -> {
                    try {
                        final long gas = n.get("gas").asLong();
                        final String transactionType = n.get("transaction_type").asText();
                        return new NetworkFee(gas, transactionType);
                    } catch (final Exception e) {
                        throw new JsonParseException(n, e);
                    }
                })
                .toList();
    }

    @Override
    public @NonNull List<TransactionInfo> toTransactionInfos(@NonNull JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return List.of();
        }

        if (!node.has("transactions")) {
            return List.of();
        }

        final JsonNode transactionsNode = node.get("transactions");
        return jsonArrayToStream(transactionsNode)
                .map(n -> {
                    try {
                        final String transactionId = n.get("transaction_id").asText();
                        return new TransactionInfo(transactionId);
                    } catch (final Exception e) {
                        throw new JsonParseException(n, e);
                    }
                }).toList();
    }

    @Override
    public List<Nft> toNfts(@NonNull JsonNode node) {
        if (!node.has("nfts")) {
            return List.of();
        }
        final JsonNode nftsNode = node.get("nfts");
        if (!nftsNode.isArray()) {
            throw new IllegalArgumentException("NFTs node is not an array: " + nftsNode);
        }
        Spliterator<JsonNode> spliterator = Spliterators.spliteratorUnknownSize(nftsNode.iterator(),
                Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .map(n -> toNft(n))
                .filter(optional -> optional.isPresent())
                .map(optional -> optional.get())
                .toList();
    }

    @NonNull
    private Stream<JsonNode> jsonArrayToStream(@NonNull final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (!node.isArray()) {
            throw new JsonParseException("not an array", node);
        }
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(node.iterator(), Spliterator.ORDERED), false);
    }
}
