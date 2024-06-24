package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.Objects;

public record FileContentsRequest(@Nonnull FileId fileId, @Nullable Hbar queryPayment, @Nullable Hbar maxQueryPayment) implements QueryRequest {

    public FileContentsRequest {
        Objects.requireNonNull(fileId, "fileId must not be null");
    }

    @Nonnull
    public static FileContentsRequest of(@Nonnull String fileId) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId));
    }

    @Nonnull
    public static FileContentsRequest of(@Nonnull FileId fileId) {
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
