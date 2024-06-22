package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;

public interface TransactionRequest {

    Hbar DEFAULT_MAX_TRANSACTION_FEE = new Hbar(10);

    Duration DEFAULT_TRANSACTION_VALID_DURATION = Duration.ofSeconds(120);

    Hbar maxTransactionFee();

    Duration transactionValidDuration();
}
