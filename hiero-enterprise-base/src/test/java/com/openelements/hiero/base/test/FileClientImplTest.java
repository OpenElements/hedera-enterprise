package com.openelements.hiero.base.test;
import com.openelements.hiero.base.implementation.FileClientImpl;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.protocol.FileInfoRequest;
import com.openelements.hiero.base.protocol.FileInfoResponse;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.hedera.hashgraph.sdk.FileId; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
        MockitoAnnotations.openMocks(this); // Use openMocks() instead of initMocks()
        fileClient = new FileClientImpl(protocolLayerClient);
    }

    @Test
    public void testIsDeleted_FileIsDeleted() throws HieroException {
        // Given
        FileId fileId = FileId.fromString("0.0.123");  // Correct format
        FileInfoResponse response = mock(FileInfoResponse.class);
        when(protocolLayerClient.executeFileInfoQuery(any(FileInfoRequest.class))).thenReturn(response);
        when(response.deleted()).thenReturn(true);

        // When
        boolean result = fileClient.isDeleted(fileId);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsDeleted_FileIsNotDeleted() throws HieroException {
        // Given
        FileId fileId = FileId.fromString("0.0.123");  // Correct format
        FileInfoResponse response = mock(FileInfoResponse.class);
        when(protocolLayerClient.executeFileInfoQuery(any(FileInfoRequest.class))).thenReturn(response);
        when(response.deleted()).thenReturn(false);

        // When
        boolean result = fileClient.isDeleted(fileId);

        // Then
        assertFalse(result);
    }

    @Test
    public void testIsDeleted_NullFileId() {
        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fileClient.isDeleted(null); // This should throw an exception
        });

        // Then
        assertEquals("fileId must not be null", exception.getMessage());
    }
}

