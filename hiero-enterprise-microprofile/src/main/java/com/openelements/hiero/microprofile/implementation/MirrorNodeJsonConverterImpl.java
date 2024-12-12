package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import jakarta.json.JsonObject;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class MirrorNodeJsonConverterImpl implements MirrorNodeJsonConverter<JsonObject> {

    @Override
    public @NonNull Optional<Nft> toNft(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<NetworkSupplies> toNetworkSupplies(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<NetworkStake> toNetworkStake(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<ExchangeRates> toExchangeRates(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<AccountInfo> toAccountInfo(@NonNull JsonObject node) {
        try {
            final AccountId accountId = AccountId.fromString(node.getString("account"));
            final String evmAddress = node.getString("evm_address");
            final long ethereumNonce = node.getJsonNumber("ethereum_nonce").longValue();
            final long pendingReward = node.getJsonNumber("pending_reward").longValue();
            final long balance = node.getJsonObject("balance").getJsonNumber("balance").longValue();
            return Optional.of(new AccountInfo(accountId, evmAddress, balance, ethereumNonce, pendingReward));
        } catch (final Exception e) {
            throw new IllegalStateException("Can not parse JSON: " + node, e);
        }
    }

    @Override
    public @NonNull List<NetworkFee> toNetworkFees(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull List<TransactionInfo> toTransactionInfos(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Nft> toNfts(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }
}
