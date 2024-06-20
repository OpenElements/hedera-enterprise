package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import jakarta.annotation.Nonnull;

public record AccountBalanceRequest(AccountId accountId) {

    public AccountBalanceRequest {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId must not be null");
        }
    }

    public static AccountBalanceRequest of(String accountId) {
        return new AccountBalanceRequest(AccountId.fromString(accountId));
    }
}
