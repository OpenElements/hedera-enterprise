package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Duration;
import java.util.Objects;

public record FileCreateRequest(Hbar maxTransactionFee,
                                Duration transactionValidDuration,
                                @NonNull byte[] contents,
                                String fileMemo) implements TransactionRequest {

    private static final String DEFAULT_FILE_MEMO = "";

    public static final int FILE_CREATE_MAX_BYTES = 2048;

    public FileCreateRequest {
        Objects.requireNonNull(contents, "File contents are required");
        if (contents.length > FILE_CREATE_MAX_BYTES) {
            throw new IllegalArgumentException("File contents must be less than " + FILE_CREATE_MAX_BYTES + " bytes");
        }
    }

    public static FileCreateRequest of(@NonNull byte[] contents) {
        return new FileCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contents, DEFAULT_FILE_MEMO);
    }
}
