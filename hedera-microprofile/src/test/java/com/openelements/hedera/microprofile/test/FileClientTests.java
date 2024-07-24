package com.openelements.hedera.microprofile.test;

import com.openelements.hedera.base.FileClient;
import io.quarkus.test.junit.QuarkusTest;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//@QuarkusTest
public class FileClientTests {

    @Inject
    private FileClient fileClient;

    //@Test
    void testFileClient() {
        Assertions.assertNotNull(fileClient);
    }
}
