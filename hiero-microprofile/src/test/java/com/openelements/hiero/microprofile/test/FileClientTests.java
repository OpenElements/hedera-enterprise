package com.openelements.hiero.microprofile.test;

import com.openelements.hiero.base.FileClient;
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
