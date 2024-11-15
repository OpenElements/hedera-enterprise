package com.openelements.hedera.microprofile;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "hiero.network")
@Dependent
public class HieroNetworkConfiguration {

    public record Node(String ip, String port, String account) {
    }

    private String name;

    @Inject
    @ConfigProperty(defaultValue = " ") //TODO: We need a better default value
    private String[] nodes;

    @ConfigProperty(defaultValue = " ") //TODO: We need a better default value
    private String mirrornode;

    public String getName() {
        return name;
    }

    public String getMirrornode() {
        return mirrornode;
    }

    public Set<Node> getNodes() {
        if (nodes == null) {
            return Set.of();
        }
        return Stream.of(nodes)
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
