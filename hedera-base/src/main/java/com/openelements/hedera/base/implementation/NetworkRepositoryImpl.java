package com.openelements.hedera.base.implementation;

import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.NetworkRepository;
import com.openelements.hedera.base.mirrornode.ExchangeRates;
import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
import com.openelements.hedera.base.mirrornode.NetworkFee;
import com.openelements.hedera.base.mirrornode.NetworkStake;
import com.openelements.hedera.base.mirrornode.NetworkSupplies;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class NetworkRepositoryImpl implements NetworkRepository {
    private final MirrorNodeClient mirrorNodeClient;

    public NetworkRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
    }

    @Override
    public Optional<ExchangeRates> exchangeRates() throws HederaException {
        return mirrorNodeClient.queryExchangeRates();
    }

    @Override
    public List<NetworkFee> fees() throws HederaException {
        return mirrorNodeClient.queryNetworkFees();
    }

    @Override
    public Optional<NetworkStake> stake() throws HederaException {
        return mirrorNodeClient.queryNetworkStake();
    }

    @Override
    public Optional<NetworkSupplies> supplies() throws HederaException {
        return mirrorNodeClient.queryNetworkSupplies();
    }
}
