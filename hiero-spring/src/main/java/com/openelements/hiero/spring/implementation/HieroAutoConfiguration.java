package com.openelements.hiero.spring.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hiero.base.Account;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.ContractVerificationClient;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.NftClient;
import com.openelements.hiero.base.NftRepository;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.implementation.AccountClientImpl;
import com.openelements.hiero.base.implementation.FileClientImpl;
import com.openelements.hiero.base.implementation.HieroNetwork;
import com.openelements.hiero.base.implementation.NftClientImpl;
import com.openelements.hiero.base.implementation.NftRepositoryImpl;
import com.openelements.hiero.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hiero.base.implementation.SmartContractClientImpl;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import java.net.URI;
import java.net.URL;
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
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties({HieroProperties.class, HieroNetworkProperties.class})
public class HieroAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HieroAutoConfiguration.class);

    @Bean
    HieroNetwork hederaNetwork(final HieroProperties properties) {
        if (properties.getNetwork() == null) {
            throw new IllegalArgumentException("'spring.hiero.network' property must be set");
        }
        final HieroNetworkProperties networkProperties = properties.getNetwork();
        if (Arrays.stream(HieroNetwork.values())
                .anyMatch(v -> Objects.equals(v.getName(), networkProperties.getName()))) {
            try {
                return HieroNetwork.valueOf(networkProperties.getName().toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not parse 'spring.hiero.network.name' property", e);
            }
        }
        return HieroNetwork.CUSTOM;
    }

    @Bean
    PrivateKey privateKey(final HieroProperties properties) {
        final String privateKey = properties.getPrivateKey();
        if (privateKey == null) {
            throw new IllegalArgumentException("'spring.hiero.privateKey' property must be set");
        }
        if (privateKey.isBlank()) {
            throw new IllegalArgumentException("'spring.hiero.privateKey' property must not be blank");
        }
        try {
            return PrivateKey.fromString(privateKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'spring.hiero.privateKey' property", e);
        }
    }

    @Bean
    AccountId accountId(final HieroProperties properties) {
        final String accountId = properties.getAccountId();
        if (accountId == null) {
            throw new IllegalArgumentException("'spring.hiero.newAccountId' property must be set");
        }
        if (accountId.isBlank()) {
            throw new IllegalArgumentException("'spring.hiero.newAccountId' property must not be blank");
        }
        try {
            return AccountId.fromString(accountId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'spring.hiero.newAccountId' property", e);
        }
    }

    @Bean
    Account operationalAccount(final AccountId accountId, final PrivateKey privateKey) {
        return new Account(accountId, privateKey.getPublicKey(), privateKey);
    }

    @Bean
    Client client(final HieroProperties properties, AccountId accountId, PrivateKey privateKey,
            HieroNetwork hederaNetwork) {
        try {
            final Client client;
            if (hederaNetwork != HieroNetwork.CUSTOM) {
                try {
                    log.debug("Hiero network '{}' will be used", hederaNetwork.getName());
                    client = Client.forName(hederaNetwork.getName());
                } catch (Exception e) {
                    throw new IllegalArgumentException("Can not create client for network " + hederaNetwork.getName(),
                            e);
                }
            } else {
                if (properties.getNetwork() == null) {
                    throw new IllegalArgumentException("'spring.hiero.network' property must be set");
                }
                final HieroNetworkProperties networkProperties = properties.getNetwork();
                final Map<String, AccountId> nodes = new HashMap<>();
                networkProperties.getNodes().forEach(node -> nodes.put(node.getIp() + ":" + node.getPort(),
                        AccountId.fromString(node.getAccount())));
                if (log.isDebugEnabled()) {
                    nodes.forEach((k, v) -> log.debug("Node: {} -> {}", k, v.toString()));
                }
                client = Client.forNetwork(nodes);
                if (networkProperties.getMirrorNode() != null) {
                    client.setMirrorNetwork(List.of(networkProperties.getMirrorNode()));
                    log.debug("Mirror Node: {}", networkProperties.getMirrorNode());
                }
            }
            client.setOperator(accountId, privateKey);
            log.debug("Client created for account: {}", accountId);
            return client;
        } catch (Exception e) {
            throw new IllegalStateException("Can not create Hiero specific configuration", e);
        }
    }

    @Bean
    ProtocolLayerClient protocolLevelClient(final Client client, final Account operationalAccount) {
        return new ProtocolLayerClientImpl(client, operationalAccount);
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
    NftClient nftClient(final ProtocolLayerClient protocolLayerClient, Account operationalAccount) {
        return new NftClientImpl(protocolLayerClient, operationalAccount);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.hiero", name = "mirrorNodeSupported",
            havingValue = "true", matchIfMissing = true)
    MirrorNodeClient mirrorNodeClient(final HieroProperties properties, HieroNetwork hederaNetwork) {
        final String mirrorNodeEndpoint;
        if (properties.getNetwork().getMirrorNode() != null) {
            mirrorNodeEndpoint = properties.getNetwork().getMirrorNode();
        } else if (hederaNetwork.getMirrornodeEndpoint() != null) {
            mirrorNodeEndpoint = hederaNetwork.getMirrornodeEndpoint();
        } else {
            throw new IllegalArgumentException("Mirror node endpoint must be set");
        }

        final String baseUri;
        try {
            URL url = new URI(mirrorNodeEndpoint).toURL();
            final String mirrorNodeEndpointProtocol = url.getProtocol();
            final String mirrorNodeEndpointHost = url.getHost();
            final int mirrorNodeEndpointPort;
            if (mirrorNodeEndpointProtocol == "https" && url.getPort() == -1) {
                mirrorNodeEndpointPort = 443;
            } else if (mirrorNodeEndpointProtocol == "http" && url.getPort() == -1) {
                mirrorNodeEndpointPort = 80;
            } else if (url.getPort() == -1) {
                mirrorNodeEndpointPort = 443;
            } else {
                mirrorNodeEndpointPort = url.getPort();
            }
            baseUri = mirrorNodeEndpointProtocol + "://" + mirrorNodeEndpointHost + ":" + mirrorNodeEndpointPort;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing mirrorNodeEndpoint '" + mirrorNodeEndpoint + "'", e);
        }
        RestClient.Builder builder = RestClient.builder().baseUrl(baseUri);
        return new MirrorNodeClientImpl(builder);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.hiero", name = "mirrorNodeSupported",
            havingValue = "true", matchIfMissing = true)
    NftRepository nftRepository(final MirrorNodeClient mirrorNodeClient) {
        return new NftRepositoryImpl(mirrorNodeClient);
    }

    @Bean
    ContractVerificationClient contractVerificationClient(HieroNetwork hederaNetwork) {
        return new ContractVerificationClientImplementation(hederaNetwork);
    }
}
