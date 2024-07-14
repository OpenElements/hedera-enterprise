package com.openelements.hedera.base.implementation;

import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.protocol.FileAppendRequest;
import com.openelements.hedera.base.protocol.FileAppendResult;
import com.openelements.hedera.base.protocol.FileContentsRequest;
import com.openelements.hedera.base.protocol.FileContentsResponse;
import com.openelements.hedera.base.protocol.FileCreateRequest;
import com.openelements.hedera.base.protocol.FileCreateResult;
import com.openelements.hedera.base.protocol.FileDeleteRequest;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileClientImpl implements FileClient {

    private final static Logger log = LoggerFactory.getLogger(FileClientImpl.class);

    private final ProtocolLayerClient protocolLayerClient;

    public FileClientImpl(@NonNull final ProtocolLayerClient protocolLayerClient) {
        this.protocolLayerClient = Objects.requireNonNull(protocolLayerClient, "protocolLevelClient must not be null");
    }

    @Override
    public FileId createFile(byte[] contents) throws HederaException {
        if(contents.length <= FileCreateRequest.FILE_CREATE_MAX_BYTES) {
            final FileCreateRequest request = FileCreateRequest.of(contents);
            final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
            return result.fileId();
        } else {
            if(log.isDebugEnabled()) {
                final int appendCount = Math.floorDiv(contents.length, FileCreateRequest.FILE_CREATE_MAX_BYTES);
                log.debug("Content of size {} is to big for 1 FileCreate transaction. Will append {} FileAppend transactions", contents.length, appendCount);
            }
            byte[] start = Arrays.copyOf(contents, FileCreateRequest.FILE_CREATE_MAX_BYTES);
            final FileCreateRequest request = FileCreateRequest.of(start);
            final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
            FileId fileId = result.fileId();
            byte[] remaining = Arrays.copyOfRange(contents, FileCreateRequest.FILE_CREATE_MAX_BYTES, contents.length);
            while(remaining.length > 0) {
                final int length = Math.min(remaining.length, FileCreateRequest.FILE_CREATE_MAX_BYTES);
                byte[] next = Arrays.copyOf(remaining, length);
                final FileAppendRequest appendRequest = FileAppendRequest.of(fileId, next);
                final FileAppendResult appendResult = protocolLayerClient.executeFileAppendRequestTransaction(appendRequest);
                remaining = Arrays.copyOfRange(remaining, length, remaining.length);
            }
            return fileId;
        }
    }

    @NonNull
    @Override
    public byte[] readFile(@NonNull FileId fileId) throws HederaException {
        try {
            final FileContentsRequest request = FileContentsRequest.of(fileId);
            final FileContentsResponse response = protocolLayerClient.executeFileContentsQuery(request);
            return response.contents();
        } catch (Exception e) {
            throw new HederaException("Failed to read file with fileId " + fileId, e);
        }
    }

    @Override
    public void deleteFile(@NonNull FileId fileId) throws HederaException {
        try {
            final FileDeleteRequest request = FileDeleteRequest.of(fileId);
            protocolLayerClient.executeFileDeleteTransaction(request);
        } catch (Exception e) {
            throw new HederaException("Failed to delete file with fileId " + fileId, e);
        }
    }
}
