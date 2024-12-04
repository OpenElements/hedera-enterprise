package com.openelements.hiero.base.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public interface TransactionResult {
    TransactionId transactionId();

    Status status();

}
