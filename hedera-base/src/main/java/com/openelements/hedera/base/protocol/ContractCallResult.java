package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;

public record ContractCallResult(TransactionId transactionId, Status status, byte[] transactionHash, Instant consensusTimestamp, Hbar transactionFee, ContractFunctionResult contractFunctionResult) implements TransactionRecord {

}
