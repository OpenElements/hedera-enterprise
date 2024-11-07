package com.openelements.hedera.microprofile.test;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class TestConfigSource implements ConfigSource {

    private final Map<String, String> properties;

    public TestConfigSource() {
        final Dotenv dotenv = Dotenv.load();
        properties = new HashMap<>();
        properties.put("mp.initializer.allow", "true");
        properties.put("mp.initializer.no-warn", "true");
        properties.put("hedera.accountId", dotenv.get("hedera.accountId"));
        properties.put("hedera.privateKey", dotenv.get("hedera.privateKey"));
        properties.put("hedera.network", dotenv.get("hedera.network"));
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
