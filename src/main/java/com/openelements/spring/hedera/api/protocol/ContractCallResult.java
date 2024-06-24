package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public record ContractCallResult(TransactionId transactionId, Status status, ContractFunctionResult contractFunctionResult) implements TransactionResult {

}
