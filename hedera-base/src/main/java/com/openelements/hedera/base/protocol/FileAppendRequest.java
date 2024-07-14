package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Duration;
import java.util.Objects;

public record FileAppendRequest(Hbar maxTransactionFee,
                                Duration transactionValidDuration,
                                @NonNull FileId fileId,
                                @NonNull byte[] contents,
                                String fileMemo) implements TransactionRequest {

    private static final String DEFAULT_FILE_MEMO = "";

    private static final int FILE_CREATE_MAX_BYTES = 2048;

    public FileAppendRequest {
        Objects.requireNonNull(fileId, "FileId is required");
        Objects.requireNonNull(contents, "File contents are required");
        if (contents.length > FILE_CREATE_MAX_BYTES) {
            throw new IllegalArgumentException("File contents must be less than " + FILE_CREATE_MAX_BYTES + " bytes");
        }
    }

    @NonNull
    public static FileAppendRequest of(@NonNull FileId fileId, @NonNull byte[] contents) {
        return new FileAppendRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId, contents, DEFAULT_FILE_MEMO);
    }

    @NonNull
    public static FileAppendRequest of(@NonNull String fileId, @NonNull byte[] contents) {
        Objects.requireNonNull(fileId, "FileId must not be null");
        return of(FileId.fromString(fileId), contents);
    }
}
