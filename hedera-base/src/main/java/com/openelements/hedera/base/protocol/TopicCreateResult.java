package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;

public record TopicCreateResult(TransactionId transactionId, Status status, TopicId fileId) implements TransactionResult{

}
