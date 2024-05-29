package com.openelements.spring.hedera.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.spring.hedera.HederaClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({HederaProperties.class})
public class HederaAutoConfiguration {

    private final HederaProperties properties;

    private final AccountId accountId;

    private final PrivateKey privateKey;

    private final HederaNetwork network;

    public HederaAutoConfiguration(final HederaProperties properties) {
        this.properties = properties;
        accountId = AccountId.fromString(properties.getAccountId());
        privateKey = PrivateKey.fromString(properties.getPrivateKey());
        network = HederaNetwork.valueOf(properties.getNetwork().toUpperCase());
    }

    @Bean
    public Client client() {
        return Client.forName(network.getName()).setOperator(accountId, privateKey);
    }

    @Bean
    public HederaClient hederaClient(final Client client) {
        return new HederaClientImpl(client);
    }
}
