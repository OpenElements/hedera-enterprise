package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.spring.hedera.AccountBalanceRequest;
import com.openelements.spring.hedera.AccountBalanceResult;
import com.openelements.spring.hedera.HederaClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountBalanceTest {

    @Autowired
    private HederaClient hederaClient;

    @Test
    void testGetBalance() throws Exception {
        //given
        AccountId accountId = AccountId.fromString("0.0.4457570");
        AccountBalanceRequest accountBalanceRequest = new AccountBalanceRequest(accountId);

        //when
        final AccountBalanceResult accountBalanceResult = hederaClient.executeAccountBalanceQuery(
                accountBalanceRequest);

        //then
        Assertions.assertEquals(Hbar.from(1000), accountBalanceResult.hbars());
    }
}
