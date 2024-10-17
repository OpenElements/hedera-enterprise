package com.openelements.hiero.spring.implementation;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(
        prefix = "spring.hiero"
)
public class HieroProperties {

    /**
     * Hedera account ID to use for transactions.
     */
    private String accountId;

    /**
     * Hedera private key to use for transactions.
     */
    private String privateKey;

    @NestedConfigurationProperty
    private HieroNetworkProperties network = new HieroNetworkProperties();

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

    public HieroNetworkProperties getNetwork() {
        return network;
    }

    public void setNetwork(HieroNetworkProperties network) {
        this.network = network;
    }
}
