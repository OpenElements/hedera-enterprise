package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Duration;

public record ContractCreateResult(TransactionId transactionId, Status status, ContractId contractId) implements TransactionResult {

}
