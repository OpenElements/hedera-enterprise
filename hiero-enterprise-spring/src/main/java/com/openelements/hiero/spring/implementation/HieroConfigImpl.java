package com.openelements.hiero.spring.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.config.ConsensusNode;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.implementation.HieroNetwork;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;

public class HieroConfigImpl implements HieroConfig {

    private final Account operatorAccount;

    private final String networkName;

    private final List<String> mirrorNodeAddresses;

    private final Set<ConsensusNode> consensusNodes;

    private final HieroNetwork hieroNetwork;

    public HieroConfigImpl(@NonNull final HieroProperties properties) {
        Objects.requireNonNull(properties, "properties must not be null");

        Objects.requireNonNull(properties.getPrivateKey(), "privateKey must not be null");
        final AccountId operatorAccountId = parseAccountId(properties.getAccountId());

        Objects.requireNonNull(properties.getAccountId(), "accountId must not be null");
        final PrivateKey operatorPrivateKey = parsePrivateKey(properties.getPrivateKey());

        operatorAccount = Account.of(operatorAccountId, operatorPrivateKey);
        networkName = properties.getNetwork().getName();
        hieroNetwork = HieroNetwork.findByName(networkName)
                .orElse(HieroNetwork.CUSTOM);
        final String mirrorNodeAddress = properties.getNetwork().getMirrorNode();
        if (mirrorNodeAddress != null && !mirrorNodeAddress.isBlank()) {
            mirrorNodeAddresses = List.of(mirrorNodeAddress);
        } else {
            mirrorNodeAddresses = List.of();
        }
        final List<HieroNode> nodes = properties.getNetwork().getNodes();
        if (nodes == null || nodes.isEmpty()) {
            consensusNodes = Set.of();
        } else {
            consensusNodes = properties.getNetwork().getNodes().stream()
                    .map(node -> new ConsensusNode(node.getIp(), node.getPort() + "", node.getAccount()))
                    .collect(Collectors.toUnmodifiableSet());
        }
    }

    private static AccountId parseAccountId(final String accountId) {
        try {
            return AccountId.fromString(accountId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'accountId' property: '" + accountId + "'", e);
        }
    }

    private static PrivateKey parsePrivateKey(final String privateKey) {
        try {
            return PrivateKey.fromString(privateKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'privateKey' property: '" + privateKey + "'", e);
        }
    }


    @Override
    public Account getOperatorAccount() {
        return operatorAccount;
    }

    @Override
    public Optional<String> getNetworkName() {
        return Optional.ofNullable(networkName);
    }

    @Override
    public List<String> getMirrornodeAddresses() {
        return mirrorNodeAddresses;
    }

    @Override
    public Set<ConsensusNode> getConsensusNodes() {
        return consensusNodes;
    }

    @Override
    public HieroNetwork getNetwork() {
        return hieroNetwork;
    }

}
