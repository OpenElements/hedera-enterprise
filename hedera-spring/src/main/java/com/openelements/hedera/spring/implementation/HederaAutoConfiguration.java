package com.openelements.hedera.spring.implementation;

import com.hedera.hashgraph.sdk.Client;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.AccountRepository;
import com.openelements.hedera.base.ContractVerificationClient;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.NetworkRepository;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;
import com.openelements.hedera.base.SmartContractClient;
import com.openelements.hedera.base.config.HieroConfig;
import com.openelements.hedera.base.implementation.AccountClientImpl;
import com.openelements.hedera.base.implementation.AccountRepositoryImpl;
import com.openelements.hedera.base.implementation.FileClientImpl;
import com.openelements.hedera.base.implementation.HederaNetwork;
import com.openelements.hedera.base.implementation.NetworkRepositoryImpl;
import com.openelements.hedera.base.implementation.NftClientImpl;
import com.openelements.hedera.base.implementation.NftRepositoryImpl;
import com.openelements.hedera.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hedera.base.implementation.SmartContractClientImpl;
import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import java.net.URI;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@EnableConfigurationProperties({HederaProperties.class, HederaNetworkProperties.class})
public class HederaAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(HederaAutoConfiguration.class);

    @Bean
    HieroConfig hieroConfig(final HederaProperties properties) {
        return new HieroConfigImpl(properties);
    }

    @Bean
    HederaNetwork hederaNetwork(final HieroConfig hieroConfig) {
        return hieroConfig.getNetwork();
    }

    @Bean
    Account operationalAccount(final HieroConfig hieroConfig) {
        return hieroConfig.getOperatorAccount();
    }

    @Bean
    Client client(final HieroConfig hieroConfig) {
        try {
            return hieroConfig.createClient();
        } catch (Exception e) {
            throw new IllegalStateException("Can not create Hedera specific configuration", e);
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
    @ConditionalOnProperty(prefix = "spring.hedera", name = "mirrorNodeSupported",
            havingValue = "true", matchIfMissing = true)
    MirrorNodeClient mirrorNodeClient(final HieroConfig hieroConfig) {
        final String mirrorNodeEndpoint;
        if (!hieroConfig.getMirrornodeAddresses().isEmpty()) {
            mirrorNodeEndpoint = hieroConfig.getMirrornodeAddresses().get(0);
        } else if (hieroConfig.getNetwork().getMirrornodeEndpoint() != null) {
            mirrorNodeEndpoint = hieroConfig.getNetwork().getMirrornodeEndpoint();
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
    @ConditionalOnProperty(prefix = "spring.hedera", name = "mirrorNodeSupported",
            havingValue = "true", matchIfMissing = true)
    NftRepository nftRepository(final MirrorNodeClient mirrorNodeClient) {
        return new NftRepositoryImpl(mirrorNodeClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.hedera", name = "mirrorNodeSupported",
            havingValue = "true", matchIfMissing = true)
    AccountRepository accountRepository(final MirrorNodeClient mirrorNodeClient) {
        return new AccountRepositoryImpl(mirrorNodeClient);
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.hedera", name = "mirrorNodeSupported",
            havingValue = "true", matchIfMissing = true)
    NetworkRepository networkRepository(final MirrorNodeClient mirrorNodeClient) {
        return new NetworkRepositoryImpl(mirrorNodeClient);
    }

    @Bean
    ContractVerificationClient contractVerificationClient(final HieroConfig hieroConfig) {
        return new ContractVerificationClientImplementation(hieroConfig.getNetwork());
    }
}
