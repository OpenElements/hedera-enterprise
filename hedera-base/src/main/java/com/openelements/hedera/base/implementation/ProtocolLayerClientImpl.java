package com.openelements.hedera.base.implementation;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.AccountDeleteTransaction;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractDeleteTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.FileAppendTransaction;
import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.FileDeleteTransaction;
import com.hedera.hashgraph.sdk.FileInfo;
import com.hedera.hashgraph.sdk.FileInfoQuery;
import com.hedera.hashgraph.sdk.FileUpdateTransaction;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.Query;
import com.hedera.hashgraph.sdk.TopicCreateTransaction;
import com.hedera.hashgraph.sdk.TopicDeleteTransaction;
import com.hedera.hashgraph.sdk.TopicMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.openelements.hedera.base.ContractParam;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.AccountCreateRequest;
import com.openelements.hedera.base.protocol.AccountCreateResult;
import com.openelements.hedera.base.protocol.AccountDeleteRequest;
import com.openelements.hedera.base.protocol.AccountDeleteResult;
import com.openelements.hedera.base.protocol.ContractCallRequest;
import com.openelements.hedera.base.protocol.ContractCallResult;
import com.openelements.hedera.base.protocol.ContractCreateRequest;
import com.openelements.hedera.base.protocol.ContractCreateResult;
import com.openelements.hedera.base.protocol.ContractDeleteRequest;
import com.openelements.hedera.base.protocol.ContractDeleteResult;
import com.openelements.hedera.base.protocol.FileAppendRequest;
import com.openelements.hedera.base.protocol.FileAppendResult;
import com.openelements.hedera.base.protocol.FileContentsRequest;
import com.openelements.hedera.base.protocol.FileContentsResponse;
import com.openelements.hedera.base.protocol.FileCreateRequest;
import com.openelements.hedera.base.protocol.FileCreateResult;
import com.openelements.hedera.base.protocol.FileDeleteRequest;
import com.openelements.hedera.base.protocol.FileDeleteResult;
import com.openelements.hedera.base.protocol.FileInfoRequest;
import com.openelements.hedera.base.protocol.FileInfoResponse;
import com.openelements.hedera.base.protocol.FileUpdateRequest;
import com.openelements.hedera.base.protocol.FileUpdateResult;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import com.openelements.hedera.base.protocol.TopicCreateResult;
import com.openelements.hedera.base.protocol.TopicDeleteRequest;
import com.openelements.hedera.base.protocol.TopicDeleteResult;
import com.openelements.hedera.base.protocol.TopicSubmitMessageRequest;
import com.openelements.hedera.base.protocol.TopicSubmitMessageResult;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolLayerClientImpl implements ProtocolLayerClient {

    private final static Logger log = LoggerFactory.getLogger(ProtocolLayerClientImpl.class);

    public static final int DEFAULT_GAS = 1_000_000;

    private final Client client;

    public ProtocolLayerClientImpl(@NonNull final Client client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    @Override
    public AccountBalanceResponse executeAccountBalanceQuery(final AccountBalanceRequest request) throws HederaException {
        final AccountBalanceQuery query = new AccountBalanceQuery().setAccountId(request.accountId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final AccountBalance balance = executeQueryAndWait(query);
        return new AccountBalanceResponse(balance.hbars);
    }

    @Override
    public FileContentsResponse executeFileContentsQuery(final FileContentsRequest request) throws HederaException {
        final FileContentsQuery query = new FileContentsQuery().setFileId(request.fileId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final ByteString byteString = executeQueryAndWait(query);
        final byte[] bytes = byteString.toByteArray();
        return new FileContentsResponse(request.fileId(), bytes);
    }

    @Override
    public FileInfoResponse executeFileInfoQuery(final FileInfoRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final FileInfoQuery query = new FileInfoQuery().setFileId(request.fileId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final FileInfo fileInfo = executeQueryAndWait(query);
        if(fileInfo.size > Integer.MAX_VALUE) {
            throw new HederaException("File size is too large to be represented as an integer");
        }
        return new FileInfoResponse(request.fileId(), (int) fileInfo.size, fileInfo.isDeleted, fileInfo.expirationTime);
    }

    @Override
    public FileCreateResult executeFileCreateTransaction(final FileCreateRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.contents(), "content must not be null");
        if(request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HederaException("File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE + " bytes. Use FileAppend for larger files.");
        }
        final FileCreateTransaction transaction = new FileCreateTransaction()
                .setContents(request.contents())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setTransactionMemo(request.fileMemo())
                .setKeys(Objects.requireNonNull(client.getOperatorPublicKey()));
        if(request.expirationTime() != null) {
            transaction.setExpirationTime(request.expirationTime());
        }

        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileCreateResult(receipt.transactionId, receipt.status, receipt.fileId);
    }

    @Override
    public FileUpdateResult executeFileUpdateRequestTransaction(final FileUpdateRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        if(request.contents() != null && request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HederaException("File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE + " bytes. Use FileAppend for larger files.");
        }
        final FileUpdateTransaction transaction = new FileUpdateTransaction()
                .setFileId(request.fileId())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setTransactionMemo(request.fileMemo());
        if(request.contents() != null) {
            transaction.setContents(request.contents());
        }
        if(request.expirationTime() != null) {
            transaction.setExpirationTime(request.expirationTime());
        }
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileUpdateResult(receipt.transactionId, receipt.status);
    }

    @Override
    public FileAppendResult executeFileAppendRequestTransaction(final FileAppendRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.contents(), "content must not be null");
        if(request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HederaException("File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE + " bytes. Use multiple FileAppend for larger files.");
        }
        final FileAppendTransaction transaction = new FileAppendTransaction()
                .setFileId(request.fileId())
                .setContents(request.contents())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setTransactionMemo(request.fileMemo());

        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileAppendResult(receipt.transactionId, receipt.status, receipt.fileId);
    }

    @Override
    public FileDeleteResult executeFileDeleteTransaction(final FileDeleteRequest request) throws HederaException {
        final FileDeleteTransaction transaction = new FileDeleteTransaction()
                .setFileId(request.fileId())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration());
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileDeleteResult(receipt.transactionId, receipt.status);
    }

    @Override
    public ContractCreateResult executeContractCreateTransaction(final ContractCreateRequest request) throws HederaException {
        final ContractFunctionParameters constructorParams = createParameters(request.constructorParams());
        final ContractCreateTransaction transaction = new ContractCreateTransaction()
                .setBytecodeFileId(request.fileId())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setGas(DEFAULT_GAS)
                .setTransactionValidDuration(request.transactionValidDuration())
                .setConstructorParameters(constructorParams);
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new ContractCreateResult(receipt.transactionId, receipt.status, receipt.contractId);
    }

    @Override
    public ContractDeleteResult executeContractDeleteTransaction(@NonNull final ContractDeleteRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                .setContractId(request.contractId())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration());
        if(request.transferFeeToContractId() != null) {
            transaction.setTransferContractId(request.transferFeeToContractId());
        }
        if(request.transferFeeToAccountId() != null) {
            transaction.setTransferAccountId(request.transferFeeToAccountId());
        }
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new ContractDeleteResult(receipt.transactionId, receipt.status);
    }

    @Override
    @NonNull
    public ContractCallResult executeContractCallTransaction(@NonNull final ContractCallRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final ContractFunctionParameters functionParams = createParameters(request.constructorParams());
        final ContractExecuteTransaction transaction = new ContractExecuteTransaction()
                .setContractId(request.contractId())
                .setFunction(request.functionName(), functionParams)
                .setMaxTransactionFee(request.maxTransactionFee())
                .setGas(DEFAULT_GAS)
                .setTransactionValidDuration(request.transactionValidDuration());
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new ContractCallResult(record.transactionId, record.receipt.status, record.transactionHash, record.consensusTimestamp, record.transactionFee, record.contractFunctionResult);
    }

    @Override
    @NonNull
    public AccountCreateResult executeAccountCreateTransaction(@NonNull final AccountCreateRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final PrivateKey privateKey = PrivateKey.generateED25519();
        final PublicKey publicKey = privateKey.getPublicKey();
        final AccountCreateTransaction transaction = new AccountCreateTransaction();
        transaction.setInitialBalance(request.initialBalance());
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new AccountCreateResult(record.transactionId, record.receipt.status, record.transactionHash, record.consensusTimestamp, record.transactionFee, record.receipt.accountId, publicKey, privateKey);
    }

    @Override
    @NonNull
    public AccountDeleteResult executeAccountDeleteTransaction(@NonNull final AccountDeleteRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final AccountDeleteTransaction transaction = new AccountDeleteTransaction();
        transaction.setAccountId(request.accountId());
        if(request.transferFoundsToAccount() != null) {
            transaction.setTransferAccountId(request.transferFoundsToAccount());
        }
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new AccountDeleteResult(record.transactionId, record.receipt.status, record.transactionHash, record.consensusTimestamp, record.transactionFee);
    }

    public TopicCreateResult executeTopicCreateTransaction() throws HederaException {
        try {
            final TopicCreateTransaction transaction = new TopicCreateTransaction();
            final TransactionResponse response = transaction.execute(client);
            final TransactionReceipt receipt = response.getReceipt(client);
            return new TopicCreateResult(receipt.transactionId, receipt.status, receipt.topicId);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute create topic transaction", e);
        }
    }

    public TopicDeleteResult executeTopicDeleteTransaction(@NonNull final TopicDeleteRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicDeleteTransaction transaction = new TopicDeleteTransaction();
            transaction.setTopicId(request.topicId());
            final TransactionResponse response = transaction.execute(client);
            final TransactionReceipt receipt = response.getReceipt(client);
            return new TopicDeleteResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute create topic transaction", e);
        }
    }

    public TopicSubmitMessageResult executeTopicMessageSubmitTransaction(@NonNull final TopicSubmitMessageRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicMessageSubmitTransaction transaction = new TopicMessageSubmitTransaction();
            transaction.setTopicId(request.topicId());
            transaction.setMessage(request.message());
            final TransactionResponse response = transaction.execute(client);
            final TransactionReceipt receipt = response.getReceipt(client);
            return new TopicSubmitMessageResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute create topic transaction", e);
        }
    }

    @NonNull
    private ContractFunctionParameters createParameters(@NonNull List<ContractParam<?>> params) {
        Objects.requireNonNull(params, "params must not be null");
        final ContractFunctionParameters constructorParams = new ContractFunctionParameters();
        final Consumer<ContractParam> consumer = param -> param.supplier().addParam(param.value(), constructorParams);
        params.forEach(consumer);
        return constructorParams;
    }

    @NonNull
    private <T extends Transaction<T>> TransactionReceipt executeTransactionAndWaitOnReceipt(@NonNull final T transaction) throws HederaException {
        Objects.requireNonNull(transaction, "transaction must not be null");
        try {
            log.debug("Sending transaction of type {}", transaction.getClass().getSimpleName());
            final TransactionResponse response = transaction.execute(client);
            try {
                log.debug("Waiting for receipt of transaction '{}' of type {}", response.transactionId, transaction.getClass().getSimpleName());
                return response.getReceipt(client);
            } catch (Exception e) {
                throw new HederaException("Failed to receive receipt of transaction '" + response.transactionId + "' of type " + transaction.getClass(), e);
            }
        } catch (final Exception e) {
            throw new HederaException("Failed to execute transaction of type " + transaction.getClass().getSimpleName(), e);
        }
    }

    @NonNull
    private <T extends Transaction<T>> TransactionRecord executeTransactionAndWaitOnRecord(@NonNull final T transaction) throws HederaException {
        Objects.requireNonNull(transaction, "transaction must not be null");
        try {
            log.debug("Sending transaction of type {}", transaction.getClass().getSimpleName());
            final TransactionResponse response = transaction.execute(client);
            try {
                log.debug("Waiting for record of transaction '{}' of type {}", response.transactionId, transaction.getClass().getSimpleName());
                return response.getRecord(client);
            } catch (final Exception e) {
                throw new HederaException("Failed to receive record of transaction '" + response.transactionId + "' of type " + transaction.getClass(), e);
            }
        } catch (final Exception e) {
            throw new HederaException("Failed to execute transaction of type " + transaction.getClass().getSimpleName(), e);
        }
    }

    @NonNull
    private <R, Q extends Query<R, Q>> R executeQueryAndWait(@NonNull final Q query) throws HederaException {
        Objects.requireNonNull(query, "query must not be null");
        try {
            log.debug("Sending query of type {}", query.getClass().getSimpleName());
            return query.execute(client);
        } catch (Exception e) {
            throw new HederaException("Failed to execute query", e);
        }
    }
}
