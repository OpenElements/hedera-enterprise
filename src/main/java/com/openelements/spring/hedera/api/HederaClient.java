package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResult;
import com.openelements.spring.hedera.api.protocol.FileContentsRequest;
import com.openelements.spring.hedera.api.protocol.FileContentsResponse;
import com.openelements.spring.hedera.api.protocol.FileCreateRequest;
import com.openelements.spring.hedera.api.protocol.FileCreateResult;


/**
 * Interface for interacting with the Hedera network.
 */
public interface HederaClient {

    /**
     * Executes an account balance query.
     * @param query The query to execute.
     * @return The result of the query.
     * @throws HederaException If the query fails.
     */
    AccountBalanceResult executeAccountBalanceQuery(AccountBalanceRequest request) throws HederaException;

    FileContentsResponse executeFileContentsQuery(FileContentsRequest request) throws HederaException;

    /**
     * Executes a file create transaction.
     * @param request The request to execute.
     * @return The result of the transaction.
     * @throws HederaException If the transaction fails.
     */
    FileCreateResult executeFileCreateTransaction(FileCreateRequest request) throws HederaException;
}
