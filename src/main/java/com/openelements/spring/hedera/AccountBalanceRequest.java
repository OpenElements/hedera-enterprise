package com.openelements.spring.hedera;

import com.hedera.hashgraph.sdk.AccountId;

public record AccountBalanceRequest(AccountId accountId) {
}
