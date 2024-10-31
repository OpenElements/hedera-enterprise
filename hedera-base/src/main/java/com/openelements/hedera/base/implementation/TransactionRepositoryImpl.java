package com.openelements.hedera.base.implementation;

import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.TransactionRepository;
import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
import com.openelements.hedera.base.mirrornode.Page;
import com.openelements.hedera.base.mirrornode.TransactionInfo;


public class TransactionRepositoryImpl implements TransactionRepository {
    private final MirrorNodeClient mirrorNodeClient;

    public TransactionRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
    }

    @NonNull
    @Override
    public Page<TransactionInfo> findByAccount(@NonNull final AccountId accountId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return this.mirrorNodeClient.queryTransactionsByAccount(accountId);
    }

    @NonNull
    @Override
    public Optional<TransactionInfo> findById(@NonNull final String transactionId) throws HederaException {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        return this.mirrorNodeClient.queryTransaction(transactionId);
    }
}
