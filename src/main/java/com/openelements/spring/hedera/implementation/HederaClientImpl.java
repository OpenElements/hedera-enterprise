package com.openelements.spring.hedera.implementation;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileAppendTransaction;
import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.FileDeleteTransaction;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Query;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.openelements.spring.hedera.api.HederaClient;
import com.openelements.spring.hedera.api.HederaException;
import com.openelements.spring.hedera.api.data.ContractParam;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResponse;
import com.openelements.spring.hedera.api.protocol.ContractCallRequest;
import com.openelements.spring.hedera.api.protocol.ContractCallResult;
import com.openelements.spring.hedera.api.protocol.ContractCreateRequest;
import com.openelements.spring.hedera.api.protocol.ContractCreateResult;
import com.openelements.spring.hedera.api.protocol.FileAppendRequest;
import com.openelements.spring.hedera.api.protocol.FileAppendResult;
import com.openelements.spring.hedera.api.protocol.FileContentsRequest;
import com.openelements.spring.hedera.api.protocol.FileContentsResponse;
import com.openelements.spring.hedera.api.protocol.FileCreateRequest;
import com.openelements.spring.hedera.api.protocol.FileCreateResult;
import com.openelements.spring.hedera.api.protocol.FileDeleteRequest;
import com.openelements.spring.hedera.api.protocol.FileDeleteResult;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HederaClientImpl implements HederaClient {

    private final static Logger log = LoggerFactory.getLogger(HederaClientImpl.class);

    public static final int DEFAULT_GAS = 1_000_000;
    private final Client client;

    public HederaClientImpl(Client client)
    {
        this.client = client;
    }

    @Override
    public FileId createFile(byte[] contents) throws HederaException {
        if(contents.length <= FileCreateRequest.FILE_CREATE_MAX_BYTES) {
            final FileCreateRequest request = FileCreateRequest.of(contents);
            final FileCreateResult result = executeFileCreateTransaction(request);
            return result.fileId();
        } else {
            if(log.isDebugEnabled()) {
                final int appendCount = Math.floorDiv(contents.length, FileCreateRequest.FILE_CREATE_MAX_BYTES);
                log.debug("Content is to big for 1 FileCreate transaction. Will append {} FileAppend transactions", appendCount);
            }
            byte[] start = Arrays.copyOf(contents, FileCreateRequest.FILE_CREATE_MAX_BYTES);
            final FileCreateRequest request = FileCreateRequest.of(start);
            final FileCreateResult result = executeFileCreateTransaction(request);
            FileId fileId = result.fileId();
            byte[] remaining = Arrays.copyOfRange(contents, FileCreateRequest.FILE_CREATE_MAX_BYTES, contents.length);
            while(remaining.length > 0) {
                final int length = Math.min(remaining.length, FileCreateRequest.FILE_CREATE_MAX_BYTES);
                byte[] next = Arrays.copyOf(remaining, length);
                final FileAppendRequest appendRequest = FileAppendRequest.of(fileId, next);
                final FileAppendResult appendResult = executeFileAppendRequestTransaction(appendRequest);
                remaining = Arrays.copyOfRange(remaining, length, remaining.length);
            }
            return fileId;
        }
    }

    @Override
    public ContractId createContract(Path pathToBin, ContractParam<?>... constructorParams) throws HederaException {
        try {
            String content = Files.readString(pathToBin, StandardCharsets.UTF_8);
            final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            return createContract(bytes, constructorParams);
        } catch (Exception e) {
            throw new HederaException("Failed to create contract", e);
        }
    }

    @Override
    public void deleteFile(FileId fileId) throws HederaException {
        final FileDeleteRequest request = FileDeleteRequest.of(fileId);
        executeFileDeleteTransaction(request);
    }

    @Override
    public byte[] readFile(FileId fileId) throws HederaException {
        final FileContentsRequest request = FileContentsRequest.of(fileId);
        final FileContentsResponse response = executeFileContentsQuery(request);
        return response.contents();
    }

    @Override
    public ContractId createContract(byte[] bytecode, ContractParam<?>... constructorParams) throws HederaException {
        try {
            final FileId fileId = createFile(bytecode);
            final ContractId contract = createContract(fileId, constructorParams);
            deleteFile(fileId);
            return contract;
        } catch (Exception e) {
            throw new HederaException("Failed to create contract", e);
        }
    }

    @Override
    public ContractId createContract(FileId fileId, ContractParam<?>... constructorParams) throws HederaException {
        final ContractCreateRequest request;
        if(constructorParams == null) {
            request = ContractCreateRequest.of(fileId);
        } else {
            request = ContractCreateRequest.of(fileId, Arrays.asList(constructorParams));
        }
        final ContractCreateResult result = executeContractCreateTransaction(request);
        return result.contractId();
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

    @Override
    public ContractFunctionResult callContractFunction(ContractId contractId, String functionName,
            ContractParam<?>... params) throws HederaException {
        final ContractCallRequest request = ContractCallRequest.of(contractId, functionName, params);
        return executeContractCallTransaction(request).contractFunctionResult();
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
                log.debug("Waiting for receipt of transaction '{}' of type {}", response.transactionId, transaction.getClass().getSimpleName());
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

    @Override
    public Client getClient() {
        return client;
    }
}
