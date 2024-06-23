package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;

public record FileContentsRequest(FileId fileId, Hbar queryPayment, Hbar maxQueryPayment) implements QueryRequest{

    public FileContentsRequest {
        if (fileId == null) {
            throw new IllegalArgumentException("fileId must not be null");
        }
    }

    public static FileContentsRequest of(String fileId) {
        return of(FileId.fromString(fileId));
    }

    public static FileContentsRequest of(FileId fileId) {
        return new FileContentsRequest(fileId, null, null);
    }

    @Override
    public Hbar queryPayment() {
        return queryPayment;
    }

    @Override
    public Hbar maxQueryPayment() {
        return maxQueryPayment;
    }
}
