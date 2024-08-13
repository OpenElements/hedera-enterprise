package com.openelements.hedera.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.AccountCreateRequest;
import com.openelements.hedera.base.protocol.AccountCreateResult;
import com.openelements.hedera.base.protocol.AccountDeleteRequest;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import org.jspecify.annotations.NonNull;
import java.util.Objects;

public class AccountClientImpl implements AccountClient {

    private final ProtocolLayerClient client;

    public AccountClientImpl(@NonNull final ProtocolLayerClient client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    @NonNull
    @Override
    public Account createAccount(@NonNull Hbar initialBalance) throws HederaException {
        final AccountCreateRequest request = AccountCreateRequest.of(initialBalance);
        final AccountCreateResult result = client.executeAccountCreateTransaction(request);
        return new Account(result.accountId(), result.publicKey(), result.privateKey());
    }

    @Override
    public void deleteAccount(@NonNull Account account) throws HederaException {
        final AccountDeleteRequest request = AccountDeleteRequest.of(account);
        client.executeAccountDeleteTransaction(request);
    }

    @Override
    public void deleteAccount(@NonNull Account account, @NonNull Account toAccount) throws HederaException {
        final AccountDeleteRequest request = AccountDeleteRequest.of(account, toAccount);
        client.executeAccountDeleteTransaction(request);
    }

    @NonNull
    @Override
    public Hbar getAccountBalance(@NonNull AccountId account) throws HederaException {
        final AccountBalanceRequest request = AccountBalanceRequest.of(account);
        final AccountBalanceResponse response = client.executeAccountBalanceQuery(request);
        return response.hbars();
    }
}
