package com.openelements.spring.hedera.implementation;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.FileAppendTransaction;
import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.Query;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.openelements.spring.hedera.api.HederaClient;
import com.openelements.spring.hedera.api.HederaException;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResult;
import com.openelements.spring.hedera.api.protocol.FileAppendRequest;
import com.openelements.spring.hedera.api.protocol.FileAppendResult;
import com.openelements.spring.hedera.api.protocol.FileContentsRequest;
import com.openelements.spring.hedera.api.protocol.FileContentsResponse;
import com.openelements.spring.hedera.api.protocol.FileCreateRequest;
import com.openelements.spring.hedera.api.protocol.FileCreateResult;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class HederaClientImpl implements HederaClient {

    private final Client client;

    public HederaClientImpl(Client client) {this.client = client;}

    @Override
    public AccountBalanceResult executeAccountBalanceQuery(AccountBalanceRequest request) throws HederaException {
        final AccountBalance balance = execute(new AccountBalanceQuery().setAccountId(request.accountId()));
        return new AccountBalanceResult(balance.hbars);
    }

    public FileContentsResponse executeFileContentsQuery(FileContentsRequest request) throws HederaException {
        final ByteString byteString = execute(new FileContentsQuery().setFileId(request.fileId()));
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

        final TransactionReceipt receipt = execute(transaction);
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

        final TransactionReceipt receipt = execute(transaction);
        return new FileAppendResult(receipt.transactionId, receipt.status, receipt.fileId);
    }

    private <T extends Transaction<T>, R> TransactionReceipt execute(T transaction) throws HederaException {
        try {
            final TransactionResponse response = transaction.execute(client);
            return response.getReceipt(client);
        } catch (Exception e) {
            throw new HederaException("Failed to execute transaction", e);
        }
    }

    private <R, Q extends Query<R, Q>> R execute(Q query) throws HederaException {
        try {
            return query.execute(client);
        } catch (PrecheckStatusException | TimeoutException e) {
            throw new HederaException("Failed to execute query", e);
        }
    }

}
