package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Status;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.protocol.FileContentsRequest;
import com.openelements.hedera.base.protocol.FileContentsResponse;
import com.openelements.hedera.base.protocol.FileCreateRequest;
import com.openelements.hedera.base.protocol.FileCreateResult;
import com.openelements.hedera.base.protocol.FileDeleteRequest;
import com.openelements.hedera.base.protocol.FileDeleteResult;
import com.openelements.hedera.base.protocol.ProtocolLevelClient;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class FileServiceTest {

    @Autowired
    private ProtocolLevelClient protocolLevelClient;

    @Autowired
    private FileClient fileClient;

    @Test
    void testCreateFile() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileCreateRequest request = FileCreateRequest.of(contents);

        //when
        final FileCreateResult result = protocolLevelClient.executeFileCreateTransaction(request);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.transactionId());
        Assertions.assertEquals(Status.SUCCESS, result.status());
        Assertions.assertNotNull(result.fileId());
    }

    @Test
    void testFileContents() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileCreateRequest request = FileCreateRequest.of(contents);
        final FileCreateResult result = protocolLevelClient.executeFileCreateTransaction(request);
        final FileId fileId = result.fileId();
        final FileContentsRequest contentsRequest = FileContentsRequest.of(fileId);

        //when
        final FileContentsResponse fileContentsResponse = protocolLevelClient.executeFileContentsQuery(contentsRequest);

        //then
        Assertions.assertNotNull(fileContentsResponse);
        Assertions.assertArrayEquals(contents, fileContentsResponse.contents());
    }

    @Test
    void testFileDelete() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileCreateRequest request = FileCreateRequest.of(contents);
        final FileCreateResult result = protocolLevelClient.executeFileCreateTransaction(request);
        final FileId fileId = result.fileId();
        final FileDeleteRequest deleteRequest = FileDeleteRequest.of(fileId);

        //when
        final FileDeleteResult deleteResponse = protocolLevelClient.executeFileDeleteTransaction(deleteRequest);

        //then
        Assertions.assertNotNull(deleteResponse);
    }

    @Test
    void testSimpleUpload() throws Exception {
        //given
        final byte[] contents = IntStream.range(0, 500).mapToObj(i -> "Hello, Hedera!")
                .reduce((a, b) -> a + b)
                .get()
                .getBytes();

        //when
        final FileId fileId = fileClient.createFile(contents);

        //then
        Assertions.assertNotNull(fileId);
    }
}
