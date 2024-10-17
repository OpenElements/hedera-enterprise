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
import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.Query;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenBurnTransaction;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.TopicCreateTransaction;
import com.hedera.hashgraph.sdk.TopicDeleteTransaction;
import com.hedera.hashgraph.sdk.TopicMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;
import com.openelements.hedera.base.Account;
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
import com.openelements.hedera.base.protocol.TokenAssociateRequest;
import com.openelements.hedera.base.protocol.TokenAssociateResult;
import com.openelements.hedera.base.protocol.TokenBurnRequest;
import com.openelements.hedera.base.protocol.TokenBurnResult;
import com.openelements.hedera.base.protocol.TokenCreateRequest;
import com.openelements.hedera.base.protocol.TokenCreateResult;
import com.openelements.hedera.base.protocol.TokenMintRequest;
import com.openelements.hedera.base.protocol.TokenMintResult;
import com.openelements.hedera.base.protocol.TokenTransferRequest;
import com.openelements.hedera.base.protocol.TokenTransferResult;
import com.openelements.hedera.base.protocol.TopicCreateRequest;
import com.openelements.hedera.base.protocol.TopicCreateResult;
import com.openelements.hedera.base.protocol.TopicDeleteRequest;
import com.openelements.hedera.base.protocol.TopicDeleteResult;
import com.openelements.hedera.base.protocol.TopicSubmitMessageRequest;
import com.openelements.hedera.base.protocol.TopicSubmitMessageResult;
import com.openelements.hedera.base.protocol.TransactionListener;
import com.openelements.hedera.base.protocol.TransactionType;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolLayerClientImpl implements ProtocolLayerClient {

    private static final Logger log = LoggerFactory.getLogger(ProtocolLayerClientImpl.class);

    public static final int DEFAULT_GAS = 1_000_000;

    private final Client client;

    private final List<TransactionListener> listeners;

    private final Account operationalAccount;

    public ProtocolLayerClientImpl(@NonNull final Client client, @NonNull final Account operationalAccount) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.operationalAccount = Objects.requireNonNull(operationalAccount, "operationalAccount must not be null");
        listeners = new CopyOnWriteArrayList<>();
    }

    @Override
    public AccountBalanceResponse executeAccountBalanceQuery(@NonNull final AccountBalanceRequest request)
            throws HederaException {
        final AccountBalanceQuery query = new AccountBalanceQuery().setAccountId(request.accountId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final AccountBalance balance = executeQueryAndWait(query);
        return new AccountBalanceResponse(balance.hbars);
    }

    @Override
    public FileContentsResponse executeFileContentsQuery(@NonNull final FileContentsRequest request)
            throws HederaException {
        final FileContentsQuery query = new FileContentsQuery().setFileId(request.fileId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final ByteString byteString = executeQueryAndWait(query);
        final byte[] bytes = byteString.toByteArray();
        return new FileContentsResponse(request.fileId(), bytes);
    }

    @Override
    public FileInfoResponse executeFileInfoQuery(@NonNull final FileInfoRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final FileInfoQuery query = new FileInfoQuery().setFileId(request.fileId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final FileInfo fileInfo = executeQueryAndWait(query);
        if (fileInfo.size > Integer.MAX_VALUE) {
            throw new HederaException("File size is too large to be represented as an integer");
        }
        return new FileInfoResponse(request.fileId(), (int) fileInfo.size, fileInfo.isDeleted, fileInfo.expirationTime);
    }

    @Override
    public FileCreateResult executeFileCreateTransaction(@NonNull final FileCreateRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.contents(), "content must not be null");
        if (request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HederaException(
                    "File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE
                            + " bytes. Use FileAppend for larger files.");
        }
        final FileCreateTransaction transaction = new FileCreateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setContents(request.contents())
                .setTransactionMemo(request.fileMemo())
                .setKeys(Objects.requireNonNull(client.getOperatorPublicKey()));
        if (request.expirationTime() != null) {
            transaction.setExpirationTime(request.expirationTime());
        }

        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileCreateResult(receipt.transactionId, receipt.status, receipt.fileId);
    }

    @Override
    public FileUpdateResult executeFileUpdateRequestTransaction(@NonNull final FileUpdateRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        if (request.contents() != null && request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HederaException(
                    "File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE
                            + " bytes. Use FileAppend for larger files.");
        }
        final FileUpdateTransaction transaction = new FileUpdateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setFileId(request.fileId())
                .setTransactionMemo(request.fileMemo());
        if (request.contents() != null) {
            transaction.setContents(request.contents());
        }
        if (request.expirationTime() != null) {
            transaction.setExpirationTime(request.expirationTime());
        }
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileUpdateResult(receipt.transactionId, receipt.status);
    }

    @Override
    public FileAppendResult executeFileAppendRequestTransaction(@NonNull final FileAppendRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.contents(), "content must not be null");
        if (request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HederaException(
                    "File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE
                            + " bytes. Use multiple FileAppend for larger files.");
        }
        final FileAppendTransaction transaction = new FileAppendTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setFileId(request.fileId())
                .setContents(request.contents())
                .setTransactionMemo(request.fileMemo());

        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileAppendResult(receipt.transactionId, receipt.status);
    }

    @Override
    public FileDeleteResult executeFileDeleteTransaction(@NonNull final FileDeleteRequest request)
            throws HederaException {
        final FileDeleteTransaction transaction = new FileDeleteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setFileId(request.fileId());
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileDeleteResult(receipt.transactionId, receipt.status);
    }

    @Override
    public ContractCreateResult executeContractCreateTransaction(@NonNull final ContractCreateRequest request)
            throws HederaException {
        final ContractFunctionParameters constructorParams = createParameters(request.constructorParams());
        final ContractCreateTransaction transaction = new ContractCreateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setBytecodeFileId(request.fileId())
                .setGas(DEFAULT_GAS)
                .setConstructorParameters(constructorParams);
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new ContractCreateResult(receipt.transactionId, receipt.status, receipt.contractId);
    }

    @Override
    public ContractDeleteResult executeContractDeleteTransaction(@NonNull final ContractDeleteRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setContractId(request.contractId());
        if (request.transferFeeToContractId() != null) {
            transaction.setTransferContractId(request.transferFeeToContractId());
        }
        if (request.transferFeeToAccountId() != null) {
            transaction.setTransferAccountId(request.transferFeeToAccountId());
        }
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new ContractDeleteResult(receipt.transactionId, receipt.status);
    }

    @Override
    @NonNull
    public ContractCallResult executeContractCallTransaction(@NonNull final ContractCallRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final ContractFunctionParameters functionParams = createParameters(request.constructorParams());
        final ContractExecuteTransaction transaction = new ContractExecuteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setContractId(request.contractId())
                .setFunction(request.functionName(), functionParams)
                .setGas(DEFAULT_GAS);
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new ContractCallResult(record.transactionId, record.receipt.status, record.transactionHash.toByteArray(),
                record.consensusTimestamp, record.transactionFee, record.contractFunctionResult);
    }

    @Override
    @NonNull
    public AccountCreateResult executeAccountCreateTransaction(@NonNull final AccountCreateRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final PrivateKey privateKey = PrivateKey.generateED25519();
        final PublicKey publicKey = privateKey.getPublicKey();
        final AccountCreateTransaction transaction = new AccountCreateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setKey(publicKey)
                .setInitialBalance(request.initialBalance());
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        final Account newAccount = Account.of(record.receipt.accountId, publicKey, privateKey);
        return new AccountCreateResult(record.transactionId, record.receipt.status,
                record.transactionHash.toByteArray(), record.consensusTimestamp, record.transactionFee, newAccount);
    }

    @Override
    @NonNull
    public AccountDeleteResult executeAccountDeleteTransaction(@NonNull final AccountDeleteRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        final AccountDeleteTransaction transaction = new AccountDeleteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setAccountId(request.toDelete().accountId());
        if (request.transferFoundsToAccount() != null) {
            transaction.setTransferAccountId(request.transferFoundsToAccount().accountId());
            sign(transaction, request.toDelete().privateKey(), request.transferFoundsToAccount().privateKey());
        } else {
            transaction.setTransferAccountId(operationalAccount.accountId());
            sign(transaction, request.toDelete().privateKey(), operationalAccount.privateKey());
        }
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new AccountDeleteResult(record.transactionId, record.receipt.status,
                record.transactionHash.toByteArray(), record.consensusTimestamp, record.transactionFee);
    }

    public TopicCreateResult executeTopicCreateTransaction(@NonNull final TopicCreateRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicCreateTransaction transaction = new TopicCreateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TopicCreateResult(receipt.transactionId, receipt.status, receipt.topicId);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute create topic transaction", e);
        }
    }

    public TopicDeleteResult executeTopicDeleteTransaction(@NonNull final TopicDeleteRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicDeleteTransaction transaction = new TopicDeleteTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTopicId(request.topicId());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TopicDeleteResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute delete topic transaction", e);
        }
    }

    public TopicSubmitMessageResult executeTopicMessageSubmitTransaction(
            @NonNull final TopicSubmitMessageRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicMessageSubmitTransaction transaction = new TopicMessageSubmitTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTopicId(request.topicId())
                    .setMessage(request.message());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TopicSubmitMessageResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute submit message transaction", e);
        }
    }

    public TokenCreateResult executeTokenCreateTransaction(@NonNull final TokenCreateRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenCreateTransaction transaction = new TokenCreateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenName(request.name())
                    .setTokenSymbol(request.symbol())
                    .setTreasuryAccountId(request.treasuryAccountId())
                    .setTokenType(request.tokenType())
                    .setSupplyKey(request.supplyKey());
            sign(transaction, request.treasuryKey(), request.supplyKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenCreateResult(receipt.transactionId, receipt.status, receipt.tokenId);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute create token transaction", e);
        }
    }

    public TokenAssociateResult executeTokenAssociateTransaction(@NonNull final TokenAssociateRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenAssociateTransaction transaction = new TokenAssociateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenIds(List.of(request.tokenId()))
                    .setAccountId(request.accountId());
            sign(transaction, request.accountPrivateKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenAssociateResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute associate token transaction", e);
        }
    }

    public TokenBurnResult executeBurnTokenTransaction(@NonNull final TokenBurnRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenBurnTransaction transaction = new TokenBurnTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenId(request.tokenId());
            if (request.amount() != null) {
                transaction.setAmount(request.amount());
            } else if (request.serials() != null) {
                transaction.setSerials(List.copyOf(request.serials()));
            } else {
                throw new IllegalArgumentException("either amount or serial must be provided");
            }

            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenBurnResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute burn token transaction", e);
        }
    }

    public TokenMintResult executeMintTokenTransaction(@NonNull final TokenMintRequest request) throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenMintTransaction transaction = new TokenMintTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenId(request.tokenId());
            if (request.amount() != null) {
                transaction.setAmount(request.amount());
            } else if (request.metadata() != null) {
                transaction.setMetadata(request.metadata());
            } else {
                throw new IllegalArgumentException("either amount or metadata must be provided");
            }
            sign(transaction, request.supplyKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenMintResult(receipt.transactionId, receipt.status, receipt.serials);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute mint token transaction", e);
        }
    }

    public TokenTransferResult executeTransferTransactionForNft(@NonNull final TokenTransferRequest request)
            throws HederaException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TransferTransaction transaction = new TransferTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration());
            request.serials().forEach(
                    serial -> transaction.addNftTransfer(new NftId(request.tokenId(), serial), request.sender(),
                            request.receiver()));
            sign(transaction, request.senderKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenTransferResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HederaException("Failed to execute transfer nft transaction", e);
        }
    }

    @NonNull
    private <T extends Transaction<T>> Transaction<T> sign(Transaction<T> transaction, final PrivateKey... keys) {
        if (keys != null) {
            transaction.freezeWith(client);
            for (PrivateKey key : keys) {
                transaction.sign(key);
            }
        }
        return transaction;
    }

    @NonNull
    private ContractFunctionParameters createParameters(@NonNull final List<ContractParam<?>> params) {
        Objects.requireNonNull(params, "params must not be null");
        final ContractFunctionParameters constructorParams = new ContractFunctionParameters();
        final Consumer<ContractParam> consumer = param -> param.supplier()
                .addParamToFunctionParameters(param.value(), constructorParams);
        params.forEach(consumer);
        return constructorParams;
    }

    @NonNull
    private <T extends Transaction<T>> TransactionReceipt executeTransactionAndWaitOnReceipt(
            @NonNull final T transaction) throws HederaException {
        Objects.requireNonNull(transaction, "transaction must not be null");
        try {
            log.debug("Sending transaction of type {}", transaction.getClass().getSimpleName());
            final TransactionResponse response = transaction.execute(client);
            listeners.forEach(listener -> {
                try {
                    listener.transactionSubmitted(TransactionType.ACCOUNT_CREATE, response.transactionId);
                } catch (Exception e) {
                    log.error("Failed to notify listener", e);
                }
            });
            try {
                log.debug("Waiting for receipt of transaction '{}' of type {}", response.transactionId,
                        transaction.getClass().getSimpleName());
                final TransactionReceipt receipt = response.getReceipt(client);
                listeners.forEach(listener -> {
                    try {
                        listener.transactionHandled(TransactionType.ACCOUNT_CREATE, response.transactionId,
                                receipt.status);
                    } catch (Exception e) {
                        log.error("Failed to notify listener", e);
                    }
                });
                return receipt;
            } catch (Exception e) {
                throw new HederaException(
                        "Failed to receive receipt of transaction '" + response.transactionId + "' of type "
                                + transaction.getClass(), e);
            }
        } catch (final Exception e) {
            throw new HederaException("Failed to execute transaction of type " + transaction.getClass().getSimpleName(),
                    e);
        }
    }

    @NonNull
    private <T extends Transaction<T>> TransactionRecord executeTransactionAndWaitOnRecord(@NonNull final T transaction)
            throws HederaException {
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        try {
            log.debug("Waiting for record of transaction '{}' of type {}", receipt.transactionId,
                    transaction.getClass().getSimpleName());
            return receipt.transactionId.getRecord(client);
        } catch (final Exception e) {
            throw new HederaException("Failed to receive record of transaction '" + receipt.transactionId + "' of type "
                    + transaction.getClass(), e);
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

    @NonNull
    @Override
    public Runnable addTransactionListener(@NonNull TransactionListener listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }
}
