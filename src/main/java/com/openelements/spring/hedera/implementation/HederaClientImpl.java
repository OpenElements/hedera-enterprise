package com.openelements.spring.hedera.implementation;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.PrecheckStatusException;
import com.hedera.hashgraph.sdk.Query;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionReceiptQuery;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.openelements.spring.hedera.AccountBalanceRequest;
import com.openelements.spring.hedera.AccountBalanceResult;
import com.openelements.spring.hedera.HederaClient;
import com.openelements.spring.hedera.HederaException;
import com.openelements.spring.hedera.HederaTransactionResponse;
import com.openelements.spring.hedera.HederaTransactionResult;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class HederaClientImpl implements HederaClient {

    private final Client client;

    public HederaClientImpl(Client client) {this.client = client;}

    @Override
    public HederaTransactionResponse callCreateAccountTransaction(final AccountCreateTransaction transaction) throws HederaException {
       return execute(transaction);
    }

    @Override
    public HederaTransactionResponse callCreateFileTransaction(final FileCreateTransaction transaction) throws HederaException {
        return execute(transaction);
    }

    @Override
    public HederaTransactionResult requestResult(HederaTransactionResponse transactionResponse) throws HederaException {
        TransactionReceiptQuery query = new TransactionReceiptQuery();
        query.setTransactionId(transactionResponse.transactionId());
        query.setNodeAccountIds(Collections.singletonList(transactionResponse.nodeId()));
        final TransactionReceipt receipt = execute(query);
        return new HederaTransactionResult(receipt.accountId, receipt.transactionId, receipt.status, receipt.exchangeRate,
                receipt.fileId, receipt.contractId, receipt.topicId, receipt.tokenId, receipt.topicSequenceNumber,
                receipt.topicRunningHash, receipt.totalSupply, receipt.scheduleId, receipt.scheduledTransactionId,
                receipt.serials, receipt.duplicates, receipt.children);
    }

    @Override
    public AccountBalanceResult executeAccountBalanceQuery(AccountBalanceRequest request) throws HederaException {
        final AccountBalance balance = execute(new AccountBalanceQuery().setAccountId(request.accountId()));
        return new AccountBalanceResult(balance.hbars);
    }

    private <R, Q extends Query<R, Q>> R execute(Q query) throws HederaException {
        try {
            return query.execute(client);
        } catch (PrecheckStatusException | TimeoutException e) {
            throw new HederaException("Failed to execute transaction", e);
        }
    }

    private <T extends Transaction<T>> HederaTransactionResponse execute(T transaction) throws HederaException {
        try {
            final TransactionResponse response = transaction.execute(client);
            return new HederaTransactionResponse(response.nodeId, response.transactionId, response.transactionHash);
        } catch (PrecheckStatusException | TimeoutException e) {
            throw new HederaException("Failed to execute transaction", e);
        }
    }
}
