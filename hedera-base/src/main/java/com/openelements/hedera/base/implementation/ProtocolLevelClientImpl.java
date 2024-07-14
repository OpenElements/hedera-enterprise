package com.openelements.hedera.base.implementation;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.FileAppendTransaction;
import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.FileDeleteTransaction;
import com.hedera.hashgraph.sdk.Query;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.openelements.hedera.base.ContractParam;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.ContractCallRequest;
import com.openelements.hedera.base.protocol.ContractCallResult;
import com.openelements.hedera.base.protocol.ContractCreateRequest;
import com.openelements.hedera.base.protocol.ContractCreateResult;
import com.openelements.hedera.base.protocol.FileAppendRequest;
import com.openelements.hedera.base.protocol.FileAppendResult;
import com.openelements.hedera.base.protocol.FileContentsRequest;
import com.openelements.hedera.base.protocol.FileContentsResponse;
import com.openelements.hedera.base.protocol.FileCreateRequest;
import com.openelements.hedera.base.protocol.FileCreateResult;
import com.openelements.hedera.base.protocol.FileDeleteRequest;
import com.openelements.hedera.base.protocol.FileDeleteResult;
import com.openelements.hedera.base.protocol.ProtocolLevelClient;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolLevelClientImpl implements ProtocolLevelClient {


    private final static Logger log = LoggerFactory.getLogger(ProtocolLevelClientImpl.class);

    public static final int DEFAULT_GAS = 1_000_000;

    private final Client client;

    public ProtocolLevelClientImpl(@NonNull final Client client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    @Override
    public AccountBalanceResponse executeAccountBalanceQuery(AccountBalanceRequest request) throws HederaException {
        final AccountBalanceQuery query = new AccountBalanceQuery().setAccountId(request.accountId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final AccountBalance balance = executeQueryAndWait(query);
        return new AccountBalanceResponse(balance.hbars);
    }

    public FileContentsResponse executeFileContentsQuery(FileContentsRequest request) throws HederaException {
        final FileContentsQuery query = new FileContentsQuery().setFileId(request.fileId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final ByteString byteString = executeQueryAndWait(query);
        final byte[] bytes = byteString.toByteArray();
        return new FileContentsResponse(bytes);
    }

    @Override
    public FileCreateResult executeFileCreateTransaction(FileCreateRequest request) throws HederaException {
        final FileCreateTransaction transaction = new FileCreateTransaction()
                .setContents(request.contents())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setTransactionMemo(request.fileMemo())
                .setKeys(Objects.requireNonNull(client.getOperatorPublicKey()));

        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileCreateResult(receipt.transactionId, receipt.status, receipt.fileId);
    }

    @Override
    public FileAppendResult executeFileAppendRequestTransaction(FileAppendRequest request) throws HederaException {
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
    public FileDeleteResult executeFileDeleteTransaction(FileDeleteRequest request) throws HederaException {
        FileDeleteTransaction transaction = new FileDeleteTransaction()
                .setFileId(request.fileId())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration());
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileDeleteResult(receipt.transactionId, receipt.status);
    }

    @Override
    public ContractCreateResult executeContractCreateTransaction(ContractCreateRequest request) throws HederaException {
        ContractFunctionParameters constructorParams = createParameters(request.constructorParams());
        ContractCreateTransaction transaction = new ContractCreateTransaction()
                .setBytecodeFileId(request.fileId())
                .setMaxTransactionFee(request.maxTransactionFee())
                .setGas(DEFAULT_GAS)
                .setTransactionValidDuration(request.transactionValidDuration())
                .setConstructorParameters(constructorParams);
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new ContractCreateResult(receipt.transactionId, receipt.status, receipt.contractId);
    }

    @Override
    public ContractCallResult executeContractCallTransaction(ContractCallRequest request) throws HederaException {
        ContractFunctionParameters functionParams = createParameters(request.constructorParams());
        ContractExecuteTransaction transaction = new ContractExecuteTransaction()
                .setContractId(request.contractId())
                .setFunction(request.functionName(), functionParams)
                .setMaxTransactionFee(request.maxTransactionFee())
                .setGas(DEFAULT_GAS)
                .setTransactionValidDuration(request.transactionValidDuration());
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new ContractCallResult(record.transactionId, record.receipt.status, record.transactionHash, record.consensusTimestamp, record.transactionFee, record.contractFunctionResult);
    }

    private ContractFunctionParameters createParameters(List<ContractParam<?>> params) {
        Objects.requireNonNull(params, "params must not be null");
        final ContractFunctionParameters constructorParams = new ContractFunctionParameters();
        final Consumer<ContractParam> consumer = param -> param.supplier().addParam(param.value(), constructorParams);
        params.forEach(consumer);
        return constructorParams;
    }

    private <T extends Transaction<T>> TransactionReceipt executeTransactionAndWaitOnReceipt(T transaction) throws HederaException {
        try {
            log.debug("Sending transaction of type {}", transaction.getClass().getSimpleName());
            final TransactionResponse response = transaction.execute(client);
            try {
                log.debug("Waiting for receipt of transaction '{}' of type {}", response.transactionId, transaction.getClass().getSimpleName());
                return response.getReceipt(client);
            } catch (Exception e) {
                throw new HederaException("Failed to receive receipt of transaction '" + response.transactionId + "' of type " + transaction.getClass(), e);
            }
        } catch (Exception e) {
            throw new HederaException("Failed to execute transaction of type " + transaction.getClass().getSimpleName(), e);
        }
    }

    private <T extends Transaction<T>> TransactionRecord executeTransactionAndWaitOnRecord(T transaction) throws HederaException {
        try {
            log.debug("Sending transaction of type {}", transaction.getClass().getSimpleName());
            final TransactionResponse response = transaction.execute(client);
            try {
                log.debug("Waiting for record of transaction '{}' of type {}", response.transactionId, transaction.getClass().getSimpleName());
                return response.getRecord(client);
            } catch (Exception e) {
                throw new HederaException("Failed to receive record of transaction '" + response.transactionId + "' of type " + transaction.getClass(), e);
            }
        } catch (Exception e) {
            throw new HederaException("Failed to execute transaction of type " + transaction.getClass().getSimpleName(), e);
        }
    }

    private <R, Q extends Query<R, Q>> R executeQueryAndWait(Q query) throws HederaException {
        try {
            log.debug("Sending query of type {}", query.getClass().getSimpleName());
            return query.execute(client);
        } catch (Exception e) {
            throw new HederaException("Failed to execute query", e);
        }
    }
}
