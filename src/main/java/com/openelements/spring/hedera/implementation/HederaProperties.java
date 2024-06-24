package com.openelements.spring.hedera.implementation;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.hedera"
)
public class HederaProperties {

    private String accountId;

    private String privateKey;

    private String network = "mainnet";

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

    public String getNetwork() {
        return this.network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
