package com.openelements.spring.hedera.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hedera.base.HederaClient;
import com.openelements.hedera.base.implementation.HederaClientImpl;
import com.openelements.hedera.base.implementation.HederaNetwork;
import com.openelements.spring.hedera.api.ContractVerificationClient;
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

    @Bean
    public HederaNetwork hederaNetwork(final HederaProperties properties) {
        if(properties.getNetwork() == null) {
            throw new IllegalArgumentException("'spring.hedera.network' property must be set");
        }
        final HederaNetworkProperties networkProperties = properties.getNetwork();
        if(Arrays.stream(HederaNetwork.values()).anyMatch(v -> Objects.equals(v.getName(), networkProperties.getName()))) {
            try {
                return HederaNetwork.valueOf(networkProperties.getName().toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not parse 'spring.hedera.network.name' property", e);
            }
        }
        return HederaNetwork.CUSTOM;
    }

    @Bean
    public PrivateKey privateKey(final HederaProperties properties) {
        final String privateKey = properties.getPrivateKey();
        if(privateKey == null) {
            throw new IllegalArgumentException("'spring.hedera.privateKey' property must be set");
        }
        if(privateKey.isBlank()) {
            throw new IllegalArgumentException("'spring.hedera.privateKey' property must not be blank");
        }
        try {
            return PrivateKey.fromString(properties.getPrivateKey());
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'spring.hedera.privateKey' property", e);
        }
    }

    @Bean
    public AccountId accountId(final HederaProperties properties) {
        final String accountId = properties.getAccountId();
        if(accountId == null) {
            throw new IllegalArgumentException("'spring.hedera.accountId' property must be set");
        }
        if(accountId.isBlank()) {
            throw new IllegalArgumentException("'spring.hedera.accountId' property must not be blank");
        }
        try {
            return AccountId.fromString(properties.getAccountId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'spring.hedera.accountId' property", e);
        }
    }

    @Bean
    public Client client(final HederaProperties properties, AccountId accountId, PrivateKey privateKey, HederaNetwork hederaNetwork) {
        try {
            final Client client;
            if(hederaNetwork != HederaNetwork.CUSTOM) {
                try {
                    log.debug("Hedera network '{}' will be used", hederaNetwork.getName());
                    client = Client.forName(hederaNetwork.getName());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Can not create client for network " + hederaNetwork.getName(), e);
                }
            } else {
                if(properties.getNetwork() == null) {
                    throw new IllegalArgumentException("'spring.hedera.network' property must be set");
                }
                final HederaNetworkProperties networkProperties = properties.getNetwork();
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
            log.debug("Client created for account: {}", accountId);
            return client;
        } catch (Exception e) {
            throw new IllegalStateException("Can not create Hedera specific configuration", e);
        }
    }

    @Bean
    public HederaClient hederaClient(final Client client) {
        return new HederaClientImpl(client);
    }

    @Bean
    public ContractVerificationClient contractVerificationClient(HederaNetwork hederaNetwork) {
        if(hederaNetwork == HederaNetwork.CUSTOM) {
            throw new IllegalArgumentException("Contract verification is not supported for custom networks");
        }
        return new ContractVerificationClientImplementation(hederaNetwork.getChainId() + "");
    }
}
