package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;

public record FileContentsRequest(FileId fileId) {

    public FileContentsRequest {
        if (fileId == null) {
            throw new IllegalArgumentException("fileId must not be null");
        }
    }

    public static FileContentsRequest of(String fileId) {
        return new FileContentsRequest(FileId.fromString(fileId));
    }
}
