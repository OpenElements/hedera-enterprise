package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public record FileDeleteResult(TransactionId transactionId, Status status) implements TransactionResult {

}

