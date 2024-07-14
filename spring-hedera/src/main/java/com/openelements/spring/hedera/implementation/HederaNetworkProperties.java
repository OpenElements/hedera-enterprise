package com.openelements.spring.hedera.implementation;

import com.openelements.hedera.base.implementation.HederaNode;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "spring.hedera.network"
)
public class HederaNetworkProperties {

    private String name = "mainnet";

    private List<HederaNode> nodes;

    private String mirrorNode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMirrorNode() {
        return mirrorNode;
    }

    public void setMirrorNode(String mirrorNode) {
        this.mirrorNode = mirrorNode;
    }

    public List<HederaNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<HederaNode> nodes) {
        this.nodes = nodes;
    }
}
