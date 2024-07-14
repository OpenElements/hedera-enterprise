package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public record FileCreateResult(TransactionId transactionId, Status status, FileId fileId) implements TransactionResult{

}
