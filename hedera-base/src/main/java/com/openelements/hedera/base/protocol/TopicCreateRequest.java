package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;

public record TopicCreateRequest(Hbar maxTransactionFee,

                                 Duration transactionValidDuration) implements TransactionRequest {

    public TopicCreateRequest {

    }

    public static TopicCreateRequest of() {
        return new TopicCreateRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE, TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION);
    }


}
