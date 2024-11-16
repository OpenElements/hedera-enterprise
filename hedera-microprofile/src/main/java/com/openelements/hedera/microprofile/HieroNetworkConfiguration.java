package com.openelements.hedera.microprofile;

import com.hedera.hashgraph.sdk.AccountId;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "hiero.network")
@Dependent
public class HieroNetworkConfiguration {

    public record Node(String ip, String port, String account) {

        String getAddress() {
            return ip + ":" + port;
        }

        AccountId getAccountId() {
            return AccountId.fromString(account);
        }
    }

    private Optional<String> name;

    @Inject
    @ConfigProperty(name = "nodes")
    private Optional<String> nodes;

    @ConfigProperty(name = "mirrornode")
    private Optional<String> mirrornode;

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getMirrornode() {
        return mirrornode;
    }

    public Set<Node> getNodes() {
        return nodes.map(n -> n.split(","))
                .map(n -> Stream.of(n))
                .orElse(Stream.empty())
                .map(n -> {
                    // 172.234.134.4:8080:0.0.3
                    final String[] split = n.split(":");
                    if (split.length != 3) {
                        throw new IllegalStateException("Can not parse node for '" + n + "'");
                    }
                    final String ip = split[0];
                    final String port = split[1];
                    final String account = split[2];
                    return new Node(ip, port, account);
                }).collect(Collectors.toUnmodifiableSet());
    }
}
