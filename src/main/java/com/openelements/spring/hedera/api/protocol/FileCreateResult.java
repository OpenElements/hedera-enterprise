package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Duration;

public record FileCreateResult(TransactionId transactionId, Status status, FileId fileId) implements TransactionResult{

    public static FileCreateResult create(TransactionId transactionId, FileId fileId) {
        return new FileCreateResult(transactionId, Status.SUCCESS, fileId);
    }
}
