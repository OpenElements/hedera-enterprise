package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;

public record TokenCreateResult(TransactionId transactionId, Status status, TokenId tokenId) implements TransactionResult{

}
