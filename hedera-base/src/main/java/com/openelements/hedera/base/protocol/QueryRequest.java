package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;

public interface QueryRequest {
    Hbar queryPayment();

    Hbar maxQueryPayment();

}
