package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TopicId;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record TopicDeleteRequest(Hbar maxTransactionFee,

                                 Duration transactionValidDuration,

                                 @NonNull TopicId topicId) implements TransactionRequest {

    public TopicDeleteRequest {
       Objects.requireNonNull(topicId, "TopicId cannot be null");
    }

    public static TopicDeleteRequest of(@NonNull TopicId topicId) {
        return new TopicDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, topicId);
    }
}
