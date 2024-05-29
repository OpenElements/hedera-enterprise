package com.openelements.spring.hedera;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.FileCreateTransaction;


/**
 * Interface for interacting with the Hedera network.
 */
public interface HederaClient {

    /**
     * Calls a create account transaction.
     * @param transaction The transaction to call.
     * @return The response of the transaction.
     * @throws HederaException If the transaction fails.
     */
    HederaTransactionResponse callCreateAccountTransaction(final AccountCreateTransaction transaction) throws HederaException;

    /**
     * Calls a create file transaction.
     * @param transaction The transaction to call.
     * @return The response of the transaction.
     * @throws HederaException If the transaction fails.
     */
    HederaTransactionResponse callCreateFileTransaction(final FileCreateTransaction transaction) throws HederaException;

    /**
     * Calls a create file transaction.
     * @param transactionResponse The response of the transaction.
     * @return The result of the transaction.
     * @throws HederaException If the transaction fails.
     */
    HederaTransactionResult requestResult(HederaTransactionResponse transactionResponse) throws HederaException;

    /**
     * Executes an account balance query.
     * @param query The query to execute.
     * @return The result of the query.
     * @throws HederaException If the query fails.
     */
    AccountBalanceResult executeAccountBalanceQuery(final AccountBalanceQuery query) throws HederaException;
}
