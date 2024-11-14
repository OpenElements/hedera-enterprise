package com.openelements.hedera.microprofile.test;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class TestConfigSource implements ConfigSource {

    private final Map<String, String> properties;

    public TestConfigSource() {
        properties = new HashMap<>();
        properties.put("mp.initializer.allow", "true");
        properties.put("mp.initializer.no-warn", "true");

        final String hederaAccountIdByEnv = System.getenv("HEDERA_ACCOUNT_ID");
        if (hederaAccountIdByEnv != null) {
            properties.put("hedera.accountId", hederaAccountIdByEnv);
        } else {
            properties.put("hedera.accountId", Dotenv.load().get("hedera.accountId"));
        }

        final String hederaPrivateKeyByEnv = System.getenv("HEDERA_PRIVATE_KEY");
        if (hederaPrivateKeyByEnv != null) {
            properties.put("hedera.privateKey", hederaPrivateKeyByEnv);
        } else {
            properties.put("hedera.privateKey", Dotenv.load().get("hedera.privateKey"));
        }

        final String hederaNetwork = System.getenv("HEDERA_NETWORK");
        if (hederaNetwork != null) {
            properties.put("hedera.network", hederaNetwork);
        } else {
            properties.put("hedera.network", "testnet");
        }
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
