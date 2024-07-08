package com.openelements.spring.hedera.test;

import com.openelements.spring.hedera.api.HederaClient;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class AccountBalanceTest {

    @Autowired
    private HederaClient hederaClient;

    @Value("${spring.hedera.accountId}")
    private String accountId;

    @Test
    void testGetBalance() throws Exception {
        //given
        AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of(accountId);

        //when
        final AccountBalanceResponse accountBalanceResult = hederaClient.executeAccountBalanceQuery(
                accountBalanceRequest);

        //then
        Assertions.assertNotNull(accountBalanceResult);
        Assertions.assertNotNull(accountBalanceResult.hbars());
        Assertions.assertTrue(accountBalanceResult.hbars().toTinybars() > 0);
        System.out.println("Balance: " + accountBalanceResult.hbars().toString() + " HBARs");
    }
}
