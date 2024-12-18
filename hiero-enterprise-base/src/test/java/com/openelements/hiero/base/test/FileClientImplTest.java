package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.implementation.FileClientImpl;
import com.openelements.hiero.base.protocol.FileInfoRequest;
import com.openelements.hiero.base.protocol.FileInfoResponse;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FileClientImplTest {

    @Mock
    private ProtocolLayerClient protocolLayerClient;

    private FileClientImpl fileClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        fileClient = new FileClientImpl(protocolLayerClient);
    }

    @Test
    public void testIsDeleted_FileIsDeleted() throws HieroException {
        // Given
        FileId fileId = FileId.fromString("0.0.123"); // Correct format
        FileInfoResponse response = mock(FileInfoResponse.class);

        when(protocolLayerClient.executeFileInfoQuery(any(FileInfoRequest.class))).thenReturn(response);
        when(response.deleted()).thenReturn(true);

        // When
        boolean result = fileClient.isDeleted(fileId);

        // Then
        assertTrue(result, "The file should be marked as deleted.");
    }

    @Test
    public void testIsDeleted_FileIsNotDeleted() throws HieroException {
        // Given
        FileId fileId = FileId.fromString("0.0.123"); // Correct format
        FileInfoResponse response = mock(FileInfoResponse.class);

        when(protocolLayerClient.executeFileInfoQuery(any(FileInfoRequest.class))).thenReturn(response);
        when(response.deleted()).thenReturn(false);

        // When
        boolean result = fileClient.isDeleted(fileId);

        // Then
        assertFalse(result, "The file should not be marked as deleted.");
    }

    @Test
    public void testIsDeleted_NullFileId() {
        // When
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> fileClient.isDeleted(null) // Explicitly pass null
        );

        // Then
        assertEquals("fileId must not be null", exception.getMessage());
    }

    @Test
    public void testIsDeleted_HieroExceptionThrown() throws HieroException {
        // Given
        FileId fileId = FileId.fromString("0.0.123");
        when(protocolLayerClient.executeFileInfoQuery(any(FileInfoRequest.class)))
                .thenThrow(new HieroException("Error processing request"));

        // When
        HieroException exception = assertThrows(
            HieroException.class,
            () -> fileClient.isDeleted(fileId)
        );

        // Then
        assertEquals("Error processing request", exception.getMessage());
    }

    @Test
    void testGetFileSize() throws HieroException {
        // mocks
        final int size = 10;
        final FileInfoResponse response = Mockito.mock(FileInfoResponse.class);

        // given
        final FileId fileId = FileId.fromString("1.2.3");

        // then
        when(response.size()).thenReturn(size);
        when(protocolLayerClient.executeFileInfoQuery(any(FileInfoRequest.class)))
                .thenReturn(response);

        final int result = fileClient.getSize(fileId);

        verify(protocolLayerClient, times(1))
                .executeFileInfoQuery(any(FileInfoRequest.class));
        verify(response, times(1)).size();
        Assertions.assertEquals(size, result);
    }

    @Test
    void testGetFileSizeThrowsExceptionForInvalidId() throws HieroException {
        // given
        final FileId fileId = FileId.fromString("1.2.3");

        // then
        when(protocolLayerClient.executeFileInfoQuery(any(FileInfoRequest.class)))
                .thenThrow(new HieroException("Failed to execute query"));

        Assertions.assertThrows(HieroException.class, () -> fileClient.getSize(fileId));
    }

    @Test
    void testGetFileSizeThrowsExceptionForNullId() {
        Assertions.assertThrows(NullPointerException.class, () -> fileClient.getSize(null));
    }
}
