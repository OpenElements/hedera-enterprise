package com.openelements.hiero.microprofile.test;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestConfigSource implements ConfigSource {

    private final static Logger log = LoggerFactory.getLogger(TestConfigSource.class);

    private final Map<String, String> properties;

    public TestConfigSource() {
        properties = new HashMap<>();
        properties.put("mp.initializer.allow", "true");
        properties.put("mp.initializer.no-warn", "true");

        final String hieroAccountIdByEnv = System.getenv("HEDERA_ACCOUNT_ID");
        if (hieroAccountIdByEnv != null) {
            properties.put("hiero.accountId", hieroAccountIdByEnv);
        } else {
            properties.put("hiero.accountId", Dotenv.load().get("hiero.accountId"));
        }

        final String hieroPrivateKeyByEnv = System.getenv("HEDERA_PRIVATE_KEY");
        if (hieroPrivateKeyByEnv != null) {
            properties.put("hiero.privateKey", hieroPrivateKeyByEnv);
        } else {
            properties.put("hiero.privateKey", Dotenv.load().get("hiero.privateKey"));
        }

        final String hieroNetwork = System.getenv("HEDERA_NETWORK");
        if (hieroNetwork != null) {
            properties.put("hiero.network.name", hieroNetwork);
        } else {
            properties.put("hiero.network.name", Dotenv.load().get("hiero.network.name"));
        }

        //TODO: Hardcoded for Solo tests,should be fixed later
        if (Objects.equals(properties.get("hiero.network.name"), "solo")) {
            properties.put("hiero.network.nodes", "127.0.0.1:50211:0.0.3");
            properties.put("hiero.network.mirrornode", "http://localhost:8080");
        }

        properties.forEach((k, v) -> log.info("CONFIG: '" + k + "'->'" + v + "'"));
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return TestConfigSource.class.getName();
    }
}
