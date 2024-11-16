package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.Account;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.AccountRepository;
import com.openelements.hiero.base.mirrornode.AccountInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(classes = TestConfig.class)
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private HederaTestUtils hederaTestUtils;

    @Autowired
    private AccountClient accountClient;

    @Test
    void findById() throws Exception {
        //given
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Optional<AccountInfo> result = accountRepository.findById(newOwner);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());

    }
}
