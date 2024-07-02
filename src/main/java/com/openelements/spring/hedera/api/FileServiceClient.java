package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.FileId;
import org.springframework.stereotype.Service;

@Service
public interface FileServiceClient {

    FileId createFile(byte[] contents) throws HederaException;

    default byte[] readFile(String fileId) throws HederaException {
        return readFile(FileId.fromString(fileId));
    }

    byte[] readFile(FileId fileId) throws HederaException;

    default void deleteFile(String fileId) throws HederaException {
        deleteFile(FileId.fromString(fileId));
    }

    void deleteFile(FileId fileId) throws HederaException;
}
