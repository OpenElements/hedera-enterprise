package com.openelements.hedera.base.implementation;

public enum HederaNetwork {

    PREVIEWNET("previewnet", 297, "https://previewnet.mirrornode.hedera.com/", "https://previewnet.hashio.io/api"),
    TESTNET("testnet", 296, "https://testnet.mirrornode.hedera.com/", "https://testnet.hashio.io/api"),
    MAINNET("mainnet", 295, "https://mainnet.mirrornode.hedera.com/", "https://mainnet.hashio.io/api"),
    CUSTOM("custom", -1, null, null);

    /**
     * See
     * https://docs.web3j.io/4.8.7/smart_contracts/interacting_with_smart_contract/#specifying-the-chain-id-on-transactions-eip-155
     */
    private final long chainId;

    /**
     * URL for the Hash.io relay service. See https://swirldslabs.com/hashio/ for more information.
     */
    private final String relayUrl;

    /**
     * Name of the Hedera node. Compatible with {@link com.hedera.hashgraph.sdk.Client#forName(String)} and similar
     * methods.
     */
    private final String name;

    private final String mirrornodeEndpoint;

    HederaNetwork(final String name, final long chainId, final String mirrornodeEndpoint, final String relayUrl) {
        this.name = name;
        this.chainId = chainId;
        this.mirrornodeEndpoint = mirrornodeEndpoint;
        this.relayUrl = relayUrl;
    }

    public long getChainId() {
        return chainId;
    }

    public String getRelayUrl() {
        return relayUrl;
    }

    public String getName() {
        return name;
    }

    public String getMirrornodeEndpoint() {
        return mirrornodeEndpoint;
    }
}