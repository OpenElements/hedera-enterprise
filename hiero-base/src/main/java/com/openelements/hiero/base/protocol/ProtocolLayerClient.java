package com.openelements.hiero.base.protocol;

import com.openelements.hiero.base.HieroException;
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
     * @throws HieroException if the query could not be executed
     */
    @NonNull
    AccountBalanceResponse executeAccountBalanceQuery(@NonNull AccountBalanceRequest request) throws HieroException;

    /**
     * Execute a file contents query.
     *
     * @param request the request
     * @return the response
     * @throws HieroException if the query could not be executed
     */
    @NonNull
    FileContentsResponse executeFileContentsQuery(@NonNull FileContentsRequest request) throws HieroException;

    /**
     * Execute a file append transaction.
     *
     * @param request the request
     * @return the result
     * @throws HieroException if the transaction could not be executed
     */
    @NonNull
    FileAppendResult executeFileAppendRequestTransaction(@NonNull FileAppendRequest request) throws HieroException;

    /**
     * Execute a file delete transaction.
     *
     * @param request the request
     * @return the result
     * @throws HieroException if the transaction could not be executed
     */
    @NonNull
    FileDeleteResult executeFileDeleteTransaction(@NonNull FileDeleteRequest request) throws HieroException;

    /**
     * Execute a file create transaction.
     *
     * @param request the request
     * @return the result
     * @throws HieroException if the transaction could not be executed
     */
    @NonNull
    FileCreateResult executeFileCreateTransaction(@NonNull FileCreateRequest request) throws HieroException;

    FileUpdateResult executeFileUpdateRequestTransaction(FileUpdateRequest request) throws HieroException;

    @NonNull
    FileInfoResponse executeFileInfoQuery(@NonNull FileInfoRequest request) throws HieroException;

    /**
     * Execute a contract create transaction.
     *
     * @param request the request
     * @return the result
     * @throws HieroException if the transaction could not be executed
     */
    @NonNull
    ContractCreateResult executeContractCreateTransaction(@NonNull ContractCreateRequest request)
            throws HieroException;

    /**
     * Execute a contract call transaction.
     *
     * @param request the request
     * @return the result
     * @throws HieroException if the transaction could not be executed
     */
    @NonNull
    ContractCallResult executeContractCallTransaction(@NonNull ContractCallRequest request) throws HieroException;

    @NonNull
    ContractDeleteResult executeContractDeleteTransaction(@NonNull final ContractDeleteRequest request)
            throws HieroException;

    @NonNull
    AccountCreateResult executeAccountCreateTransaction(@NonNull final AccountCreateRequest request)
            throws HieroException;

    @NonNull
    AccountDeleteResult executeAccountDeleteTransaction(@NonNull AccountDeleteRequest request) throws HieroException;

    @NonNull
    TokenCreateResult executeTokenCreateTransaction(@NonNull final TokenCreateRequest request) throws HieroException;

    @NonNull
    TokenAssociateResult executeTokenAssociateTransaction(@NonNull final TokenAssociateRequest request)
            throws HieroException;

    @NonNull
    TokenMintResult executeMintTokenTransaction(@NonNull final TokenMintRequest request) throws HieroException;

    @NonNull
    TokenBurnResult executeBurnTokenTransaction(@NonNull final TokenBurnRequest request) throws HieroException;

    @NonNull
    TokenTransferResult executeTransferTransactionForNft(@NonNull final TokenTransferRequest request)
            throws HieroException;

    @NonNull
    Runnable addTransactionListener(@NonNull TransactionListener listener);
}
