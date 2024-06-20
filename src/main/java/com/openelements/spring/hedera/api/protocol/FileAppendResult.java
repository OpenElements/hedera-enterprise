package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public record FileAppendResult(TransactionId transactionId, Status status, FileId fileId) implements TransactionResult{
    public static FileAppendResult create(TransactionId transactionId, FileId fileId) {
        return new FileAppendResult(transactionId, Status.SUCCESS, fileId);
    }
}
