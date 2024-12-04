package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.Client;
import com.openelements.hiero.base.data.Account;
import org.jspecify.annotations.NonNull;

public interface HieroContext {

    @NonNull
    Account getOperatorAccount();

    @NonNull
    Client getClient();
}
