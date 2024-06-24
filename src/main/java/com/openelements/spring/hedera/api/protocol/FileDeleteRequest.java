package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import jakarta.annotation.Nonnull;
import java.time.Duration;
import java.util.Objects;

public record FileDeleteRequest(Hbar maxTransactionFee,
                                Duration transactionValidDuration,
                                @Nonnull FileId fileId) implements TransactionRequest {

    public FileDeleteRequest {
        Objects.requireNonNull(fileId, "fileId must not be null");
    }

    @Nonnull
    public static FileDeleteRequest of(@Nonnull String fileId) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId));
    }

    @Nonnull
    public static FileDeleteRequest of(@Nonnull FileId fileId) {
        return new FileDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId);
    }
}
