package com.openelements.hedera.base.test;


import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.AccountCreateRequest;
import com.openelements.hedera.base.protocol.AccountCreateResult;
import com.openelements.hedera.base.protocol.AccountDeleteRequest;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProtocolLayerClientAccountTests {

    private static HederaTestContext hederaTestContext;

    private static ProtocolLayerClient protocolLayerClient;

    @BeforeAll
    static void init() {
        hederaTestContext = new HederaTestContext();
        protocolLayerClient = new ProtocolLayerClientImpl(hederaTestContext.getClient(), hederaTestContext.getOperationalAccount());
    }

    @Test
    void testAccountBalanceRequest() throws Exception {
        //given
        final Hbar amount = Hbar.from(1000L);
        final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of(amount);
        final AccountCreateResult accountCreateResult = protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);
        final AccountId accountId = accountCreateResult.newAccount().accountId();

        //when
        final AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of(accountId);
        final AccountBalanceResponse accountBalanceResponse = protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest);

        //then
        Assertions.assertNotNull(accountBalanceResponse);
        Assertions.assertNotNull(accountBalanceResponse.hbars());
        Assertions.assertEquals(amount, accountBalanceResponse.hbars());
    }

    @Test
    void testAccountBalanceRequestForZeroBalance() throws Exception {
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

    @Test
    void testAccountBalanceRequestForNotExistingAccount() throws Exception {
        //given
        final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of();
        final AccountCreateResult accountCreateResult = protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);
        final Account account = accountCreateResult.newAccount();
        AccountDeleteRequest accountDeleteRequest = AccountDeleteRequest.of(account);
        protocolLayerClient.executeAccountDeleteTransaction(accountDeleteRequest);

        //when
        final AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of(account.accountId());

        //then
        Assertions.assertThrows(HederaException.class, () ->protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest));
    }

}
