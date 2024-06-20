package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;

public record FileAppendRequest(Hbar maxTransactionFee,
                                Duration transactionValidDuration,
                                FileId fileId,
                                byte[] contents,
                                String fileMemo) implements TransactionRequest {

    private static final String DEFAULT_FILE_MEMO = "";

    private static final int FILE_CREATE_MAX_BYTES = 2048;

    public FileAppendRequest {
        if(fileId == null) {
            throw new IllegalArgumentException("File ID cannot be null");
        }
        if (contents.length > FILE_CREATE_MAX_BYTES) {
            throw new IllegalArgumentException("File contents must be less than " + FILE_CREATE_MAX_BYTES + " bytes");
        }
    }

    public static FileAppendRequest of(FileId fileId, byte[] contents) {
        return new FileAppendRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId, contents, DEFAULT_FILE_MEMO);
    }

    public static FileAppendRequest of(String fileId, byte[] contents) {
        return of(FileId.fromString(fileId), contents);
    }
}
