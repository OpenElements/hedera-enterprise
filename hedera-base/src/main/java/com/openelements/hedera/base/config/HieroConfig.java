package com.openelements.hedera.base.config;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.implementation.HederaNetwork;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface HieroConfig {

    final static Logger log = LoggerFactory.getLogger(HieroConfig.class);

    @NonNull Account getOperatorAccount();

    @NonNull Optional<String> getNetworkName();

    @NonNull List<String> getMirrornodeAddresses();

    @NonNull Set<ConsensusNode> getConsensusNodes();

    @NonNull HederaNetwork getNetwork();

    @NonNull
    default Client createClient() {
        final HederaNetwork hederaNetwork = getNetwork();
        if (hederaNetwork != HederaNetwork.CUSTOM) {
            try {
                log.debug("Hedera network '{}' will be used", hederaNetwork.getName());
                Client client = Client.forName(hederaNetwork.getName());
                client.setOperator(getOperatorAccount().accountId(), getOperatorAccount().privateKey());
                return client;
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not create client for network " + hederaNetwork.getName(),
                        e);
            }
        } else {
            try {
                final Map<String, AccountId> nodes = getConsensusNodes().stream()
                        .collect(Collectors.toMap(n -> n.getAddress(), n -> n.getAccountId()));
                final Client client = Client.forNetwork(nodes);
                client.setMirrorNetwork(getMirrornodeAddresses());
                client.setOperator(getOperatorAccount().accountId(), getOperatorAccount().privateKey());
                return client;
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not create client for custom network", e);
            }
        }
    }

}
