package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;

public record AccountBalanceRequest(AccountId accountId, Hbar queryPayment, Hbar maxQueryPayment) implements QueryRequest {

    public AccountBalanceRequest {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId must not be null");
        }
    }

    public static AccountBalanceRequest of(String accountId) {
        return new AccountBalanceRequest(AccountId.fromString(accountId), null, null);
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
