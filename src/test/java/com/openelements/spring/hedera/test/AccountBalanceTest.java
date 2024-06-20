package com.openelements.spring.hedera.test;

import com.openelements.spring.hedera.api.HederaClient;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class AccountBalanceTest {

    @Autowired
    private HederaClient hederaClient;

    @Test
    void testGetBalance() throws Exception {
        //given
        AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of("0.0.4457570");

        //when
        final AccountBalanceResult accountBalanceResult = hederaClient.executeAccountBalanceQuery(
                accountBalanceRequest);

        //then
        Assertions.assertNotNull(accountBalanceResult);
        Assertions.assertNotNull(accountBalanceResult.hbars());
        Assertions.assertTrue(accountBalanceResult.hbars().toTinybars() > 0);
    }
}
