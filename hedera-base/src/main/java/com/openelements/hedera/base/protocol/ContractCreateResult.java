package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public record ContractCreateResult(TransactionId transactionId, Status status, ContractId contractId) implements TransactionResult {

}
