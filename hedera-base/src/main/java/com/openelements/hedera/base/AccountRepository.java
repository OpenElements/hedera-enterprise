package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hedera.base.mirrornode.AccountInfo;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Interface for interacting with a Hedera network. This interface provides methods for searching for Accounts.
 */
public interface AccountRepository {
    /**
     * Return the AccountInfo of a given accountId.
     *
     * @param accountId  id of the account
     * @return {@link Optional} containing the found AccountInfo or null
     * @throws HederaException if the search fails
     */
    Optional<AccountInfo> findById(@NonNull AccountId accountId) throws HederaException;

    /**
     * Return the AccountInfo of a given accountId.
     *
     * @param accountId  id of the account
     * @return {@link Optional} containing the found AccountInfo or null
     * @throws HederaException if the search fails
     */
    default Optional<AccountInfo> findById(@NonNull String accountId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return findById(AccountId.fromString(accountId));
    }
}
