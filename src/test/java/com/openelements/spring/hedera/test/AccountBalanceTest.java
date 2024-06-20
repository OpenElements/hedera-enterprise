package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResult;
import com.openelements.spring.hedera.api.EnableHedera;
import com.openelements.spring.hedera.api.HederaClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@EnableHedera
@SpringBootTest
@SpringBootConfiguration
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
        Assertions.assertEquals(Hbar.from(1000), accountBalanceResult.hbars());
    }
}
