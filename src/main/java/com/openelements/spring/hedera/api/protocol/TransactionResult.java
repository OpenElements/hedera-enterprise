package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public interface TransactionResult {
    TransactionId transactionId();

    Status status();

}
