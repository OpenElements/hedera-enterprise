package com.openelements.spring.hedera;

import com.hedera.hashgraph.sdk.AccountId;

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
