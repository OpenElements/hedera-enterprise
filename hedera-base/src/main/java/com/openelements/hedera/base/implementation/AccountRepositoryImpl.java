package com.openelements.hedera.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hedera.base.AccountRepository;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.mirrornode.AccountInfo;
import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
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
