package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.mirrornode.Page;
import com.openelements.hiero.base.mirrornode.TransactionInfo;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with transactions on a Hedera network. This interface provides methods for querying
 * transactions.
 */
public interface TransactionRepository {
    /**
     * Find all transactions associated with a specific account.
     *
     * @param accountId id of the account
     * @return page of transactions
     * @throws HederaException if the search fails
     */
    @NonNull
    Page<TransactionInfo> findByAccount(@NonNull AccountId accountId) throws HederaException;

    /**
     * Find all transactions associated with a specific account.
     *
     * @param accountId id of the account as a string
     * @return page of transactions
     * @throws HederaException if the search fails
     */
    @NonNull
    default Page<TransactionInfo> findByAccount(@NonNull String accountId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return findByAccount(AccountId.fromString(accountId));
    }

    /**
     * Find a specific transaction by its ID.
     *
     * @param transactionId the transaction ID
     * @return Optional containing the transaction if found
     * @throws HederaException if the search fails
     */
    @NonNull
    Optional<TransactionInfo> findById(@NonNull String transactionId) throws HederaException;
}
