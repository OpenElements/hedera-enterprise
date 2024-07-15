package com.openelements.hedera.spring.test;

import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.HederaException;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class FileClientTests {

    @Autowired
    private FileClient fileClient;

    @Test
    void testNullParams() {
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.createFile(null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.readFile((String)null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.readFile((FileId) null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.deleteFile((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.deleteFile((FileId) null));
    }

    @Test
    void testCreateEmptyFile() throws Exception {
        //given
        final byte[] contents = new byte[0];

        //when
        final FileId fileId = fileClient.createFile(contents);

        //then
        Assertions.assertNotNull(fileId);
    }

    @Test
    void testCreateSmallFile() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();

        //when
        final FileId fileId = fileClient.createFile(contents);

        //then
        Assertions.assertNotNull(fileId);
    }

    @Test
    void testCreateLargeFile() throws Exception {
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

    @Test
    void testReadFileByFileId() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);

        //when
        final byte[] readContents = fileClient.readFile(fileId);

        //then
        Assertions.assertArrayEquals(contents, readContents);
    }

    @Test
    void testReadFileByStringId() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final String fileId = fileClient.createFile(contents).toString();

        //when
        final byte[] readContents = fileClient.readFile(fileId);

        //then
        Assertions.assertArrayEquals(contents, readContents);
    }

    @Test
    void testReadLargeFileByStringId() throws Exception {
        //given
        final byte[] contents = IntStream.range(0, 500).mapToObj(i -> "Hello, Hedera!")
                .reduce((a, b) -> a + b)
                .get()
                .getBytes();
        final String fileId = fileClient.createFile(contents).toString();

        //when
        final byte[] readContents = fileClient.readFile(fileId);

        //then
        Assertions.assertArrayEquals(contents, readContents);
    }

    @Test
    void testDeleteFileByFileId() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);

        //then
        Assertions.assertDoesNotThrow(() -> fileClient.deleteFile(fileId));
    }

    @Test
    void testDeleteFileByStringId() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final String fileId = fileClient.createFile(contents).toString();

        //then
        Assertions.assertDoesNotThrow(() -> fileClient.deleteFile(fileId));
    }

    @Test
    void testReadNotExistingFile() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);

        //when
        fileClient.deleteFile(fileId);


        //then
        Assertions.assertThrows(HederaException.class, () -> fileClient.readFile(fileId));
    }

    @Test
    void testDeleteNotExistingFile() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);

        //when
        fileClient.deleteFile(fileId);

        //when
        Assertions.assertThrows(HederaException.class, () -> fileClient.deleteFile(fileId));
    }
}
