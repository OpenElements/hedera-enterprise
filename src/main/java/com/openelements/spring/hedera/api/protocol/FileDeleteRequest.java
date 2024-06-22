package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;

public record FileDeleteRequest(Hbar maxTransactionFee,
                                Duration transactionValidDuration,
                                FileId fileId) implements TransactionRequest {

    public FileDeleteRequest {
        if (fileId == null) {
            throw new IllegalArgumentException("fileId must not be null");
        }
    }

    public static FileDeleteRequest of(String fileId) {
        return of(FileId.fromString(fileId));
    }

    public static FileDeleteRequest of(FileId fileId) {
        return new FileDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId);
    }
}
