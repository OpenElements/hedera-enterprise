package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hedera network.
 * This interface provides methods for interacting with Hedera accounts, like creating and deleting accounts.
 * An implementation of this interface is using an internal account to interact with the Hedera network.
 * That account is the account that is used to pay for the transactions that are sent to the Hedera network and called 'operator account'.
 */
public interface AccountClient {

    /**
     * Creates a new Hedera account.
     * The account is created with an initial balance of 0 hbar.
     * The account is created by the operator account.
     *
     * @return the created account
     * @throws HederaException if the account could not be created
     */
    @NonNull
    default Account createAccount() throws HederaException {
        return createAccount(Hbar.ZERO);
    }

    /**
     * Creates a new Hedera account with the given initial balance.
     * The account is created by the operator account.
     *
     * @param initialBalance the initial balance of the account
     * @return the created account
     * @throws HederaException if the account could not be created
     */
    @NonNull
    Account createAccount(@NonNull Hbar initialBalance) throws HederaException;

    /**
     * Creates a new Hedera account with the given initial balance (in HBAR).
     * The account is created by the operator account.
     *
     * @param initialBalanceInHbar the initial balance of the account in HBAR
     * @return the created account
     * @throws HederaException if the account could not be created
     */
    @NonNull
    default Account createAccount(long initialBalanceInHbar) throws HederaException {
        if(initialBalanceInHbar < 0) {
            throw new IllegalArgumentException("initialBalanceInHbar must be non-negative");
        }
        return createAccount(Hbar.from(initialBalanceInHbar));
    }

    /**
     * Deletes the account with the given ID. All fees of that account are transferred to the operator account.
     * @param account the account to delete
     * @throws HederaException if the account could not be deleted
     */
    void deleteAccount(@NonNull Account account) throws HederaException;

    /**
     * Deletes the account with the given ID. All fees of that account are transferred to the given toAccount.
     * @param account the account to delete
     * @param toAccount the account to transfer the fees to
     * @throws HederaException if the account could not be deleted
     */
    void deleteAccount(@NonNull Account account, @NonNull Account toAccount) throws HederaException;

    /**
     * Returns the balance of the given account.
     * @param accountId the ID of the account
     * @return the balance of the account
     * @throws HederaException if the balance could not be retrieved
     */
    @NonNull
    Hbar getAccountBalance(@NonNull AccountId accountId) throws HederaException;

    /**
     * Returns the balance of the given account.
     * @param accountId the ID of the account
     * @return the balance of the account
     * @throws HederaException if the balance could not be retrieved
     */
    @NonNull
    default Hbar getAccountBalance(@NonNull String accountId) throws HederaException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        return getAccountBalance(AccountId.fromString(accountId));
    }
}
