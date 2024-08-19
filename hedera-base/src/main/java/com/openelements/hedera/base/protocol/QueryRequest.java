package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Hbar;

public interface QueryRequest {
    Hbar queryPayment();

    Hbar maxQueryPayment();

}
