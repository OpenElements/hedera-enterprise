package com.openelements.hedera.base.test;

import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.AccountCreateRequest;
import com.openelements.hedera.base.protocol.AccountCreateResult;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProtocolLayerClientAccountTests {

    private static Client client;

    private static ProtocolLayerClient protocolLayerClient;

    @BeforeAll
    static void init() {
        final Dotenv dotenv = Dotenv.load();
        final String accountIdAsString = dotenv.get("hedera.accountId");
        final String privateKeyAsString = dotenv.get("hedera.privateKey");
        final AccountId accountId = AccountId.fromString(accountIdAsString);
        final PrivateKey privateKey = PrivateKey.fromString(privateKeyAsString);
        final PublicKey publicKey = privateKey.getPublicKey();
        final Account operationalAccount = new Account(accountId, publicKey, privateKey);
        client = Client.forTestnet();
        client.setOperator(accountId, privateKey);
        protocolLayerClient = new ProtocolLayerClientImpl(client, operationalAccount);
    }

    @Test
    void testAccountBalanceRequest() throws Exception {
        //given
        final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of();
        final AccountCreateResult accountCreateResult = protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);
        final AccountId accountId = accountCreateResult.newAccount().accountId();

        //when
        final AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of(accountId);
        final AccountBalanceResponse accountBalanceResponse = protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest);

        //then
        Assertions.assertNotNull(accountBalanceResponse);
        Assertions.assertNotNull(accountBalanceResponse.hbars());
        Assertions.assertEquals(0L, accountBalanceResponse.hbars().toTinybars());
    }

}
