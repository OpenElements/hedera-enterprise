package com.openelements.hedera.base.protocol;

import com.openelements.hedera.base.HederaException;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with the Hedera network at the protocol level.
 */
public interface ProtocolLayerClient {

    /**
     * Execute an account balance query.
     *
     * @param request the request
     * @return the response
     * @throws HederaException if the query could not be executed
     */
    @NonNull
    AccountBalanceResponse executeAccountBalanceQuery(@NonNull AccountBalanceRequest request) throws HederaException;

    /**
     * Execute a file contents query.
     *
     * @param request the request
     * @return the response
     * @throws HederaException if the query could not be executed
     */
    @NonNull
    FileContentsResponse executeFileContentsQuery(@NonNull FileContentsRequest request) throws HederaException;

    /**
     * Execute a file append transaction.
     *
     * @param request the request
     * @return the result
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    FileAppendResult executeFileAppendRequestTransaction(@NonNull FileAppendRequest request) throws HederaException;

    /**
     * Execute a file delete transaction.
     *
     * @param request the request
     * @return the result
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    FileDeleteResult executeFileDeleteTransaction(@NonNull FileDeleteRequest request) throws HederaException;

    /**
     * Execute a file create transaction.
     *
     * @param request the request
     * @return the result
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    FileCreateResult executeFileCreateTransaction(@NonNull FileCreateRequest request) throws HederaException;

    /**
     * Execute a file update transaction.
     *
     * @param  request  the request containing the details of the file update
     * @return          the result of the file update transaction
     * @throws HederaException if the transaction could not be executed
     */
    FileUpdateResult executeFileUpdateRequestTransaction(FileUpdateRequest request) throws HederaException;

    /**
     * Execute a file info query.
     *
     * @param  request  the request containing the details of the file info query
     * @return          the response containing the information about the file
     * @throws HederaException if the query could not be executed
     */
    @NonNull
    FileInfoResponse executeFileInfoQuery(@NonNull FileInfoRequest request) throws HederaException;

    /**
     * Execute a contract create transaction.
     *
     * @param request the request
     * @return the result
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    ContractCreateResult executeContractCreateTransaction(@NonNull ContractCreateRequest request)
            throws HederaException;

    /**
     * Execute a contract call transaction.
     *
     * @param request the request
     * @return the result
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    ContractCallResult executeContractCallTransaction(@NonNull ContractCallRequest request) throws HederaException;

    /**
     * Executes a contract delete transaction.
     *
     * @param  request  the request containing the details of the contract delete transaction
     * @return          the result of the contract delete transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    ContractDeleteResult executeContractDeleteTransaction(@NonNull final ContractDeleteRequest request)
            throws HederaException;

    /**
     * Executes an account create transaction.
     *
     * @param  request  the request containing the details of the account create transaction
     * @return          the result of the account create transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    AccountCreateResult executeAccountCreateTransaction(@NonNull final AccountCreateRequest request)
            throws HederaException;

    /**
     * Executes an account delete transaction.
     *
     * @param  request  the request containing the details of the account delete transaction
     * @return          the result of the account delete transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    AccountDeleteResult executeAccountDeleteTransaction(@NonNull AccountDeleteRequest request) throws HederaException;

    /**
     * Executes a token create transaction.
     *
     * @param  request  the request containing the details of the token create transaction
     * @return          the result of the token create transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TokenCreateResult executeTokenCreateTransaction(@NonNull final TokenCreateRequest request) throws HederaException;

    /**
     * Executes a token associate transaction.
     *
     * @param  request  the request containing the details of the token associate transaction
     * @return          the result of the token associate transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TokenAssociateResult executeTokenAssociateTransaction(@NonNull final TokenAssociateRequest request)
            throws HederaException;

    /**
     * Executes a token mint transaction.
     *
     * @param  request  the request containing the details of the token mint transaction
     * @return          the result of the token mint transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TokenMintResult executeMintTokenTransaction(@NonNull final TokenMintRequest request) throws HederaException;

    /**
     * Executes a token burn transaction.
     *
     * @param  request  the request containing the details of the token burn transaction
     * @return          the result of the token burn transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TokenBurnResult executeBurnTokenTransaction(@NonNull final TokenBurnRequest request) throws HederaException;

    /**
     * Executes a transfer transaction for an NFT.
     *
     * @param  request  the request containing the details of the token transfer transaction
     * @return          the result of the token transfer transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TokenTransferResult executeTransferTransactionForNft(@NonNull final TokenTransferRequest request)
            throws HederaException;

    /**
     * Executes a topic create transaction.
     *
     * @param  request  the request containing the details of the topic create transaction
     * @return          the result of the topic create transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TopicCreateResult executeTopicCreateTransaction(@NonNull TopicCreateRequest request) throws HederaException;

    /**
     * Executes a topic delete transaction.
     *
     * @param  request  the request containing the details of the topic delete transaction
     * @return          the result of the topic delete transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TopicDeleteResult executeTopicDeleteTransaction(@NonNull TopicDeleteRequest request) throws HederaException;

    /**
     * Executes a topic message submit transaction.
     *
     * @param  request  the request containing the details of the topic message submit transaction
     * @return          the result of the topic message submit transaction
     * @throws HederaException if the transaction could not be executed
     */
    @NonNull
    TopicSubmitMessageResult executeTopicMessageSubmitTransaction(@NonNull TopicSubmitMessageRequest request)
            throws HederaException;

    /**
     * Adds a transaction listener to the protocol layer client. The listener will be notified when a transaction
     * is executed.
     *
     * @param  listener  the transaction listener to be added
     * @return          a Runnable object that can be used to remove the listener
     */
    @NonNull
    Runnable addTransactionListener(@NonNull TransactionListener listener);
}
