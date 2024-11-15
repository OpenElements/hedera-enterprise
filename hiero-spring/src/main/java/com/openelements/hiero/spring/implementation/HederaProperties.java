package com.openelements.hiero.spring.implementation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(
        prefix = "spring.hedera"
)
public class HederaProperties {

    /**
     * Hedera account ID to use for transactions.
     */
    private String accountId;

    /**
     * Hedera private key to use for transactions.
     */
    private String privateKey;

    @NestedConfigurationProperty
    private HederaNetworkProperties network = new HederaNetworkProperties();

    public String getAccountId() {
        return this.accountId;
    }

    public String getPrivateKey() {
        return this.privateKey;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public HederaNetworkProperties getNetwork() {
        return network;
    }

    public void setNetwork(HederaNetworkProperties network) {
        this.network = network;
    }
}
