package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.FileId;
import edu.umd.cs.findbugs.annotations.NonNull;

public interface FileServiceClient {

    @NonNull
    FileId createFile(@NonNull byte[] contents) throws HederaException;

    @NonNull
    default byte[] readFile(@NonNull String fileId) throws HederaException {
        return readFile(FileId.fromString(fileId));
    }

    @NonNull
    byte[] readFile(@NonNull FileId fileId) throws HederaException;

    default void deleteFile(@NonNull String fileId) throws HederaException {
        deleteFile(FileId.fromString(fileId));
    }

    void deleteFile(@NonNull FileId fileId) throws HederaException;
}
