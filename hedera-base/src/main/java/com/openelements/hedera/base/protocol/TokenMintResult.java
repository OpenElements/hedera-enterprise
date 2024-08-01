package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;
import java.util.List;

public record TokenMintResult(TransactionId transactionId, Status status, List<Long> serials) implements TransactionResult{

}
