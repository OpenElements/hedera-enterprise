package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public record FileDeleteResponse(TransactionId transactionId, Status status) implements TransactionResult {

    public static FileDeleteResponse create(TransactionId transactionId) {
        return new FileDeleteResponse(transactionId, Status.SUCCESS);
    }
}

