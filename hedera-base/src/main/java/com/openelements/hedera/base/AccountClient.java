package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;
import java.util.Objects;

public interface AccountClient {

    @NonNull
    default Account createAccount() throws HederaException {
        return createAccount(Hbar.ZERO);
    }

    @NonNull
    Account createAccount(@NonNull Hbar initialBalance) throws HederaException;

    @NonNull
    default Account createAccount(long initialBalanceInHbar) throws HederaException {
        if(initialBalanceInHbar < 0) {
            throw new IllegalArgumentException("initialBalanceInHbar must be non-negative");
        }
        return createAccount(Hbar.from(initialBalanceInHbar));
    }

    void deleteAccount(@NonNull AccountId account) throws HederaException;

    default void deleteAccount(@NonNull String accountId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        deleteAccount(AccountId.fromString(accountId));
    }

    @NonNull
    Hbar getAccountBalance(@NonNull AccountId account) throws HederaException;

    @NonNull
    default Hbar getAccountBalance(@NonNull String accountId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return getAccountBalance(AccountId.fromString(accountId));
    }
}
