package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import jakarta.annotation.Nonnull;
import java.time.Duration;
import java.util.Objects;

public record FileAppendRequest(Hbar maxTransactionFee,
                                Duration transactionValidDuration,
                                @Nonnull FileId fileId,
                                @Nonnull byte[] contents,
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

    @Nonnull
    public static FileAppendRequest of(@Nonnull FileId fileId, @Nonnull byte[] contents) {
        return new FileAppendRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId, contents, DEFAULT_FILE_MEMO);
    }

    @Nonnull
    public static FileAppendRequest of(@Nonnull String fileId, @Nonnull byte[] contents) {
        Objects.requireNonNull(fileId, "FileId must not be null");
        return of(FileId.fromString(fileId), contents);
    }
}
