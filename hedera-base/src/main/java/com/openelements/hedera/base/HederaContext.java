package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.protocol.TransactionType;
import java.time.Duration;
import org.jspecify.annotations.NonNull;

public interface HederaContext {

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
