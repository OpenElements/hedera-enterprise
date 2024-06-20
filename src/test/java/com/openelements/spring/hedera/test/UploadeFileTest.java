package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Status;
import com.openelements.spring.hedera.api.HederaClient;
import com.openelements.spring.hedera.api.protocol.FileCreateRequest;
import com.openelements.spring.hedera.api.protocol.FileCreateResult;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)

public class UploadeFileTest {

    @Autowired
    private HederaClient hederaClient;

    @Test
    void uploadeFile() throws Exception {

        //given
        final byte[] contents = IntStream.range(0, 500).mapToObj(i -> "Hello, Hedera!")
                .reduce((a, b) -> a + b)
                .orElse("")
                .getBytes();

        //when
        final FileId fileId = hederaClient.uploadFile(contents);

        //then
        Assertions.assertNotNull(fileId);
    }
}
