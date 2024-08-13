package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.ContractParam;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record AccountDeleteRequest(@NonNull Hbar maxTransactionFee,
                                   @NonNull Duration transactionValidDuration,
                                   @NonNull Account toDelete,
                                   @Nullable Account transferFoundsToAccount) implements TransactionRequest {

    public AccountDeleteRequest {
        Objects.requireNonNull(maxTransactionFee, "maxTransactionFee is required");
        Objects.requireNonNull(transactionValidDuration, "transactionValidDuration is required");
        Objects.requireNonNull(toDelete, "toDelete is required");
        if (maxTransactionFee.toTinybars() < 0) {
            throw new IllegalArgumentException("maxTransactionFee must be non-negative");
        }
        if (transactionValidDuration.isNegative() || transactionValidDuration.isZero()) {
            throw new IllegalArgumentException("transactionValidDuration must be positive");
        }
    }

    @NonNull
    public static AccountDeleteRequest of(@NonNull Account toDelete) {
        return of(toDelete, null);
    }

    @NonNull
    public static AccountDeleteRequest of(@NonNull Account toDelete, @Nullable Account transferFoundsToAccount) {
        return new AccountDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, toDelete, transferFoundsToAccount);
    }

}
