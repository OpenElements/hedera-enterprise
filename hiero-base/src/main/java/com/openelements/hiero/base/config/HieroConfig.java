package com.openelements.hiero.base.config;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.openelements.hiero.base.Account;
import com.openelements.hiero.base.implementation.HieroNetwork;
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

    @NonNull HieroNetwork getNetwork();

    @NonNull
    default Client createClient() {
        final HieroNetwork hieroNetwork = getNetwork();
        if (hieroNetwork != HieroNetwork.CUSTOM) {
            try {
                log.debug("Hiero network '{}' will be used", hieroNetwork.getName());

                //TODO: Hack since the Client is still Hedera specific and not migrated to Hiero
                Client client = Client.forName(hieroNetwork.getName().substring("hedera-".length()));
                client.setOperator(getOperatorAccount().accountId(), getOperatorAccount().privateKey());
                return client;
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not create client for network " + hieroNetwork.getName(),
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
