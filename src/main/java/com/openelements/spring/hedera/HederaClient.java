package com.openelements.spring.hedera;

import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.FileCreateTransaction;


public interface HederaClient {

    HederaTransactionResponse callCreateAccountTransaction(final AccountCreateTransaction transaction) throws HederaException;

    HederaTransactionResponse callCreateFileTransaction(final FileCreateTransaction transaction) throws HederaException;

    HederaTransactionResult requestResult(HederaTransactionResponse transactionResponse) throws HederaException;

    AccountBalance executeAccountBalanceQuery(final AccountBalanceQuery query) throws HederaException;
}
