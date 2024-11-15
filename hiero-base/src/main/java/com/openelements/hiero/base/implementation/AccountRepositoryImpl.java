package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.AccountRepository;
import com.openelements.hiero.base.HederaException;
import com.openelements.hiero.base.mirrornode.AccountInfo;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {
    private final MirrorNodeClient mirrorNodeClient;

    public AccountRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
    }

    @Override
    public Optional<AccountInfo> findById(@NonNull AccountId accountId) throws HederaException {
        return mirrorNodeClient.queryAccount(accountId);
    }
}
