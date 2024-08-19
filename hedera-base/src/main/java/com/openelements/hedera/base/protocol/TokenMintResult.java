package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TokenMintResult(@NonNull TransactionId transactionId, @NonNull Status status, @NonNull List<Long> serials) implements TransactionResult{

    public TokenMintResult {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(serials, "serials must not be null");
    }
}
