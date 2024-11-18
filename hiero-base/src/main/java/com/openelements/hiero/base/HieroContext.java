package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.protocol.TransactionType;
import java.time.Duration;
import org.jspecify.annotations.NonNull;

public interface HieroContext {

    @NonNull
    Account getOperatorAccount();

    @NonNull
    Client getClient();

    @NonNull
    Hbar getDefaultMaxTransactionFee();

    @NonNull
    Duration getDefaultTransactionValidDuration();

    void setDefaultMaxTransactionFee(@NonNull Hbar maxTransactionFee);

    void setDefaultTransactionValidDuration(@NonNull Duration transactionValidDuration);

    @NonNull
    Hbar getDefaultMaxTransactionFee(@NonNull TransactionType transactionType);

    @NonNull
    Duration getDefaultTransactionValidDuration(@NonNull TransactionType transactionType);

    void setDefaultMaxTransactionFee(@NonNull TransactionType transactionType, @NonNull Hbar maxTransactionFee);

    void setDefaultTransactionValidDuration(@NonNull TransactionType transactionType,
            @NonNull Duration transactionValidDuration);
}
