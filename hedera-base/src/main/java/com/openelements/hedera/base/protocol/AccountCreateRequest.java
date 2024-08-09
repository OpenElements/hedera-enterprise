package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.time.Duration;
import java.util.Objects;

public record AccountCreateRequest(Hbar maxTransactionFee,
                                   Duration transactionValidDuration,
                                   @NonNull Hbar initialBalance) implements TransactionRequest {

    public AccountCreateRequest {
        Objects.requireNonNull(initialBalance, "initialBalance is required");
    }

    @NonNull
    public static AccountCreateRequest of(@NonNull Hbar initialBalance) {
        return new AccountCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, initialBalance);
    }

    public static AccountCreateRequest of() {
        return of(Hbar.ZERO);
    }


}
