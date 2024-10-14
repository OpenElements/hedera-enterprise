package com.openelements.hedera.microprofile.test;

import com.openelements.hedera.base.FileClient;
import javax.inject.Inject;
import org.junit.jupiter.api.Assertions;

//@QuarkusTest
public class FileClientTests {

    @Inject
    private FileClient fileClient;

    //@Test
    void testFileClient() {
        Assertions.assertNotNull(fileClient);
    }
}
