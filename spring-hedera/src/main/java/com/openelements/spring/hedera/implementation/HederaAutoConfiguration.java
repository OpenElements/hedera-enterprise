package com.openelements.spring.hedera.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hedera.base.HederaClient;
import com.openelements.hedera.base.implementation.HederaClientImpl;
import com.openelements.hedera.base.implementation.HederaNetwork;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({HederaProperties.class, HederaNetworkProperties.class})
public class HederaAutoConfiguration {

    private final static Logger log = LoggerFactory.getLogger(HederaAutoConfiguration.class);

    private final AccountId accountId;

    private final PrivateKey privateKey;

    private final Client client;

    public HederaAutoConfiguration(final HederaProperties properties) {
        try {
            if(properties.getAccountId() == null) {
                throw new IllegalArgumentException("'spring.hedera.accountId' property must be set");
            }
            if(properties.getPrivateKey() == null) {
                throw new IllegalArgumentException("'spring.hedera.privateKey' property must be set");
            }
            if(properties.getNetwork() == null) {
                throw new IllegalArgumentException("'spring.hedera.network' property must be set");
            }
            try {
                accountId = AccountId.fromString(properties.getAccountId());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not parse 'spring.hedera.accountId' property", e);
            }
            log.debug("Hedera account ID: {}", accountId.toString());
            try {
                privateKey = PrivateKey.fromString(properties.getPrivateKey());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not parse 'spring.hedera.privateKey' property", e);
            }

            final HederaNetworkProperties networkProperties = properties.getNetwork();
            if(Arrays.stream(HederaNetwork.values()).anyMatch(v -> Objects.equals(v.getName(), networkProperties.getName()))) {
                try {
                    final HederaNetwork network = HederaNetwork.valueOf(networkProperties.getName().toUpperCase());
                    log.debug("Hedera network: {}", network.getName());
                    client = Client.forName(network.getName());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Can not parse 'spring.hedera.network.name' property", e);
                }
            } else {
               final Map<String, AccountId> nodes = new HashMap<>();
               networkProperties.getNodes().forEach(node -> nodes.put(node.getIp() + ":" + node.getPort(), AccountId.fromString(node.getAccount())));
               if(log.isDebugEnabled()) {
                     nodes.forEach((k, v) -> log.debug("Node: {} -> {}", k, v.toString()));
               }
               client = Client.forNetwork(nodes);
                if(networkProperties.getMirrorNode() != null) {
                    client.setMirrorNetwork(List.of(networkProperties.getMirrorNode()));
                    log.debug("Mirror Node: {}", networkProperties.getMirrorNode());
                }
            }
            client.setOperator(accountId, privateKey);
        } catch (Exception e) {
            throw new IllegalStateException("Can not create Hedera specific configuration", e);
        }
    }

    @Bean
    public Client client() {
        return client;
    }

    @Bean
    public HederaClient hederaClient(final Client client) {
        return new HederaClientImpl(client);
    }
}
