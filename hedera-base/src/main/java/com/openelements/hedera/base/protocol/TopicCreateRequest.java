package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;
import org.jspecify.annotations.NonNull;

public record TopicCreateRequest(@NonNull Hbar maxTransactionFee,

                                 @NonNull Duration transactionValidDuration) implements TransactionRequest {

    public TopicCreateRequest {

    }

    public static TopicCreateRequest of() {
        return new TopicCreateRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION);
    }


}
