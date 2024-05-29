package com.openelements.spring.hedera;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TransactionId;

public record HederaTransactionResponse(AccountId nodeId, TransactionId transactionId, byte[] transactionHash) {
}
