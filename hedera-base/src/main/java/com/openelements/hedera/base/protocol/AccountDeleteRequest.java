package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.ContractParam;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record AccountDeleteRequest(Hbar maxTransactionFee,
                                   Duration transactionValidDuration,
                                   @NonNull AccountId accountId, @Nullable AccountId transferFoundsToAccount) implements TransactionRequest {

    public AccountDeleteRequest {
        Objects.requireNonNull(accountId, "accountId is required");
    }

    @NonNull
    public static AccountDeleteRequest of(@NonNull String accountId) {
        return of(AccountId.fromString(accountId));
    }

    @NonNull
    public static AccountDeleteRequest of(@NonNull String accountId, @NonNull String transferFoundsToAccount) {
        Objects.requireNonNull(accountId, "accountId must not be null");
        final AccountId realAccountId = AccountId.fromString(accountId);
        if(transferFoundsToAccount == null) {
            return of(realAccountId);
        }
        final AccountId realTransferFoundsToAccount = AccountId.fromString(transferFoundsToAccount);
        return of(realAccountId, realTransferFoundsToAccount);
    }

    @NonNull
    public static AccountDeleteRequest of(@NonNull AccountId accountId) {
        return of(accountId, null);
    }

    @NonNull
    public static AccountDeleteRequest of(@NonNull AccountId accountId, @Nullable AccountId transferFoundsToAccount) {
        return new AccountDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, accountId, transferFoundsToAccount);
    }

}
