package com.openelements.spring.hedera.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.spring.hedera.api.HederaClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({HederaProperties.class})
public class HederaAutoConfiguration {

    private final AccountId accountId;

    private final PrivateKey privateKey;

    private final HederaNetwork network;

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
            try {
                System.out.println("privateKey: '" + properties.getPrivateKey() + "'");
                privateKey = PrivateKey.fromString(properties.getPrivateKey());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not parse 'spring.hedera.privateKey' property", e);
            }
            try {
                network = HederaNetwork.valueOf(properties.getNetwork().toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not parse 'spring.hedera.network' property", e);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Can not create Hedera specific configuration", e);
        }
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
