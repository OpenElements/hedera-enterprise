package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.HieroNetwork;
import com.openelements.hiero.microprofile.HieroNetworkConfiguration;
import com.openelements.hiero.microprofile.HieroOperatorConfiguration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HieroConfigImpl implements HieroConfig {

    private final static Logger log = LoggerFactory.getLogger(HieroConfigImpl.class);

    private final Account operatorAccount;

    private final String networkName;

    private final List<String> mirrorNodeAddresses;

    private final Set<ConsensusNode> consensusNodes;

    private final HieroNetwork hieroNetwork;

    public HieroConfigImpl(@NonNull final HieroOperatorConfiguration configuration,
            @NonNull final HieroNetworkConfiguration networkConfiguration) {
        Objects.requireNonNull(configuration, "configuration must not be null");
        Objects.requireNonNull(networkConfiguration, "networkConfiguration must not be null");

        final AccountId operatorAccountId = AccountId.fromString(configuration.getAccountId());
        final PrivateKey operatorPrivateKey = PrivateKey.fromString(configuration.getPrivateKey());
        operatorAccount = Account.of(operatorAccountId, operatorPrivateKey);
        networkName = networkConfiguration.getName().orElse(null);
        mirrorNodeAddresses = networkConfiguration.getMirrornode().map(List::of).orElse(List.of());
        consensusNodes = Collections.unmodifiableSet(networkConfiguration.getNodes());
        hieroNetwork = HieroNetwork.findByName(networkName)
                .orElse(HieroNetwork.CUSTOM);
    }

    @Override
    public @NonNull Account getOperatorAccount() {
        return operatorAccount;
    }

    @Override
    public @NonNull Optional<String> getNetworkName() {
        return Optional.ofNullable(networkName);
    }

    @Override
    public @NonNull List<String> getMirrorNodeAddresses() {
        return mirrorNodeAddresses;
    }

    @Override
    public @NonNull Set<ConsensusNode> getConsensusNodes() {
        return consensusNodes;
    }

    @Override
    public @NonNull HieroNetwork getNetwork() {
        return hieroNetwork;
    }
}
