package com.openelements.hiero.base.data;

import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TransactionInfo(@NonNull String transactionId) {

    public TransactionInfo {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
    }
}
