package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.readFile((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.readFile((FileId) null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.deleteFile((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.deleteFile((FileId) null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.getExpirationTime(null));
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.getSize(null));
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
    void testUpdateFileByFileId() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);
        final String newContent = "Hello, Hedera! Updated";

        //when
        fileClient.updateFile(fileId, newContent.getBytes());

        //then
        final byte[] readContents = fileClient.readFile(fileId);
        Assertions.assertArrayEquals(newContent.getBytes(), readContents);
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
    @Disabled("Looks like a deleted file is still accessible. Need to investigate further.")
    void testReadNotExistingFile() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);

        //when
        fileClient.deleteFile(fileId);

        //then
        Assertions.assertThrows(HieroException.class, () -> fileClient.readFile(fileId));
    }

    @Test
    void testDeleteNotExistingFile() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);

        //when
        fileClient.deleteFile(fileId);

        //when
        Assertions.assertThrows(HieroException.class, () -> fileClient.deleteFile(fileId));
    }

    @Test
    void testDeleteState() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileId fileId = fileClient.createFile(contents);
        fileClient.deleteFile(fileId);

        //when
        final boolean deleted = fileClient.isDeleted(fileId);

        //when
        Assertions.assertTrue(deleted);
    }

    @Test
    void testGetExpirationTime() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final Instant definedExpirationTime = Instant.now().plus(Duration.ofDays(2));
        final FileId fileId = fileClient.createFile(contents, definedExpirationTime);

        //when
        final Instant expirationTime = fileClient.getExpirationTime(fileId);

        //then
        Assertions.assertTrue(expirationTime.isAfter(definedExpirationTime.minusSeconds(1)));
        Assertions.assertTrue(expirationTime.isBefore(definedExpirationTime.plusSeconds(1)));
    }

    @Test
    void testGetExpirationTimeForFailures() {
        //given
        final FileId invalidFileId = FileId.fromString("1.2.3");
        //then
        Assertions.assertThrows(HieroException.class, () -> fileClient.getExpirationTime(invalidFileId));
    }

    @Test
    @Disabled("Always fails with AUTORENEW_DURATION_NOT_IN_RANGE. Needs to be investigated further.")
    void testUpdateExpirationTime() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final Instant definedExpirationTime = Instant.now().plus(Duration.of(20, ChronoUnit.MINUTES));
        final FileId fileId = fileClient.createFile(contents);
        fileClient.updateExpirationTime(fileId, definedExpirationTime);

        //when
        final Instant expirationTime = fileClient.getExpirationTime(fileId);

        //then
        Assertions.assertTrue(expirationTime.isAfter(definedExpirationTime.minusSeconds(1)));
        Assertions.assertTrue(expirationTime.isBefore(definedExpirationTime.plusSeconds(1)));
    }

    @Test
    @Disabled("Always fails with AUTORENEW_DURATION_NOT_IN_RANGE. Needs to be investigated further.")
    void testUpdateExpirationTimeDoesNotChangeContent() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final Instant definedExpirationTime = Instant.now().plus(Duration.of(20, ChronoUnit.MINUTES));
        final FileId fileId = fileClient.createFile(contents);
        fileClient.updateExpirationTime(fileId, definedExpirationTime);

        final byte[] result = fileClient.readFile(fileId);

        //then
        Assertions.assertArrayEquals(contents, result);
    }

    @Test
    void testGetFileSize() throws HieroException {
        final byte[] contents = "Hello, Hiero!".getBytes();
        final FileId fileId = fileClient.createFile(contents);
        final int size = fileClient.getSize(fileId);

        Assertions.assertEquals(size, contents.length);
    }

    @Test
    void testGetFileSizeThrowsExceptionForInvalidId() {
        final FileId invalidFileId = FileId.fromString("1.2.3");
        Assertions.assertThrows(HieroException.class, () -> fileClient.getSize(invalidFileId));
    }
}
