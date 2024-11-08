package com.openelements.hedera.microprofile.test;

import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.microprofile.ClientProvider;
import io.helidon.microprofile.tests.junit5.AddBean;
import io.helidon.microprofile.tests.junit5.Configuration;
import io.helidon.microprofile.tests.junit5.HelidonTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@HelidonTest
@AddBean(ClientProvider.class)
@Configuration(useExisting = true)
public class FileClientTests {

    @BeforeAll
    static void setup() {
        final Config build = ConfigProviderResolver.instance()
                .getBuilder().withSources(new TestConfigSource()).build();
        ConfigProviderResolver.instance().registerConfig(build, Thread.currentThread().getContextClassLoader());
    }

    @Inject
    private FileClient fileClient;

    @Test
    void testFileClient() throws Exception {
        Assertions.assertNotNull(fileClient);
        fileClient.createFile(new byte[0]);
    }
}
