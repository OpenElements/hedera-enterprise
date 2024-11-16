package com.openelements.hedera.microprofile.test;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
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

        final String hederaAccountIdByEnv = System.getenv("HEDERA_ACCOUNT_ID");
        if (hederaAccountIdByEnv != null) {
            properties.put("hiero.accountId", hederaAccountIdByEnv);
        } else {
            properties.put("hiero.accountId", Dotenv.load().get("hedera.accountId"));
        }

        final String hederaPrivateKeyByEnv = System.getenv("HEDERA_PRIVATE_KEY");
        if (hederaPrivateKeyByEnv != null) {
            properties.put("hiero.privateKey", hederaPrivateKeyByEnv);
        } else {
            properties.put("hiero.privateKey", Dotenv.load().get("hedera.privateKey"));
        }

        final String hederaNetwork = System.getenv("HEDERA_NETWORK");
        if (hederaNetwork != null) {
            properties.put("hiero.network.name", hederaNetwork);
            //TODO: Hardcoded for Solo tests,should be fixed later
            if (hederaNetwork == "solo") {
                properties.put("hiero.network.nodes", "127.0.0.1:50211:0.0.3");
                properties.put("hiero.network.mirrornode", "http://localhost:8080");
            }
        } else {
            properties.put("hiero.network.name", Dotenv.load().get("hedera.network.name"));
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
