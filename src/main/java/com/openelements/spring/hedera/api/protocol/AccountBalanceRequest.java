package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import jakarta.annotation.Nonnull;
import java.util.Objects;

public record AccountBalanceRequest(@Nonnull AccountId accountId, Hbar queryPayment, Hbar maxQueryPayment) implements QueryRequest {

    public AccountBalanceRequest {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId must not be null");
        }
    }

    @Nonnull
    public static AccountBalanceRequest of(AccountId accountId) {
        return new AccountBalanceRequest(accountId, null, null);
    }

    @Nonnull
    public static AccountBalanceRequest of(@Nonnull String accountId) {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return of(AccountId.fromString(accountId));
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
