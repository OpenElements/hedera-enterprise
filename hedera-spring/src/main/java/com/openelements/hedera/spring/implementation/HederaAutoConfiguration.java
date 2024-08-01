package com.openelements.hedera.spring.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;
import com.openelements.hedera.base.SmartContractClient;
import com.openelements.hedera.base.implementation.AccountClientImpl;
import com.openelements.hedera.base.implementation.FileClientImpl;
import com.openelements.hedera.base.implementation.HederaNetwork;
import com.openelements.hedera.base.implementation.NftClientImpl;
import com.openelements.hedera.base.implementation.NftRepositoryImpl;
import com.openelements.hedera.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hedera.base.implementation.SmartContractClientImpl;
import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import com.openelements.hedera.base.ContractVerificationClient;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({HederaProperties.class, HederaNetworkProperties.class})
public class HederaAutoConfiguration {

    private final static Logger log = LoggerFactory.getLogger(HederaAutoConfiguration.class);

    @Bean
    HederaNetwork hederaNetwork(final HederaProperties properties) {
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
    PrivateKey privateKey(final HederaProperties properties) {
        final String privateKey = properties.getPrivateKey();
        if(privateKey == null) {
            throw new IllegalArgumentException("'spring.hedera.privateKey' property must be set");
        }
        if(privateKey.isBlank()) {
            throw new IllegalArgumentException("'spring.hedera.privateKey' property must not be blank");
        }
        try {
            return PrivateKey.fromString(privateKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'spring.hedera.privateKey' property", e);
        }
    }

    @Bean
    AccountId accountId(final HederaProperties properties) {
        final String accountId = properties.getAccountId();
        if(accountId == null) {
            throw new IllegalArgumentException("'spring.hedera.accountId' property must be set");
        }
        if(accountId.isBlank()) {
            throw new IllegalArgumentException("'spring.hedera.accountId' property must not be blank");
        }
        try {
            return AccountId.fromString(accountId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'spring.hedera.accountId' property", e);
        }
    }

    @Bean
    Client client(final HederaProperties properties, AccountId accountId, PrivateKey privateKey, HederaNetwork hederaNetwork) {
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
    ProtocolLayerClient protocolLevelClient(final Client client) {
        return new ProtocolLayerClientImpl(client);
    }

    @Bean
    FileClient fileClient(final ProtocolLayerClient protocolLayerClient) {
        return new FileClientImpl(protocolLayerClient);
    }

    @Bean
    SmartContractClient smartContractClient(final ProtocolLayerClient protocolLayerClient, FileClient fileClient) {
        return new SmartContractClientImpl(protocolLayerClient, fileClient);
    }

    @Bean
    AccountClient accountClient(final ProtocolLayerClient protocolLayerClient) {
        return new AccountClientImpl(protocolLayerClient);
    }

    @Bean
    NftClient nftClient(final ProtocolLayerClient protocolLayerClient, AccountId adminAccount, PrivateKey adminSupplyKey) {
        return new NftClientImpl(protocolLayerClient, adminAccount, adminSupplyKey);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.hedera", name = "mirrorNodeSupported",
            havingValue="true", matchIfMissing=true)
    MirrorNodeClient mirrorNodeClient(Client client) {
        return new MirrorNodeClientImpl(client);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.hedera", name = "mirrorNodeSupported",
            havingValue="true", matchIfMissing=true)
    NftRepository nftRepository(MirrorNodeClient mirrorNodeClient) {
        return new NftRepositoryImpl(mirrorNodeClient);
    }

    @Bean
    ContractVerificationClient contractVerificationClient(HederaNetwork hederaNetwork) {
        return new ContractVerificationClientImplementation(hederaNetwork);
    }
}
