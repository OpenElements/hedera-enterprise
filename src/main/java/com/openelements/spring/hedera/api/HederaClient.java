package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResult;
import com.openelements.spring.hedera.api.protocol.FileCreateRequest;
import com.openelements.spring.hedera.api.protocol.FileCreateResult;
import com.openelements.spring.hedera.api.protocol.HederaTransactionResponse;
import com.openelements.spring.hedera.api.protocol.HederaTransactionResult;


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
    HederaTransactionResponse callCreateAccountTransaction(AccountCreateTransaction transaction) throws HederaException;

    /**
     * Calls a create file transaction.
     * @param transaction The transaction to call.
     * @return The response of the transaction.
     * @throws HederaException If the transaction fails.
     */
    HederaTransactionResponse callCreateFileTransaction(FileCreateTransaction transaction) throws HederaException;

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
    AccountBalanceResult executeAccountBalanceQuery(AccountBalanceRequest request) throws HederaException;

    /**
     * Executes a file create transaction.
     * @param request The request to execute.
     * @return The result of the transaction.
     * @throws HederaException If the transaction fails.
     */
    FileCreateResult executeFileCreateTransaction(FileCreateRequest request) throws HederaException;
}
