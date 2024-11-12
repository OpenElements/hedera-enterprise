package com.openelements.hedera.spring.test;

import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.NetworkRepository;
import com.openelements.hedera.base.mirrornode.ExchangeRates;
import com.openelements.hedera.base.mirrornode.NetworkFee;
import com.openelements.hedera.base.mirrornode.NetworkStake;
import com.openelements.hedera.base.mirrornode.NetworkSupplies;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = TestConfig.class)
public class NetworkRepositoryTest {
    @Autowired
    private NetworkRepository networkRepository;

    @Test
    void findExchangeRates() throws HederaException {
        Optional<ExchangeRates> result = networkRepository.exchangeRates();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void findNetworkFees() throws HederaException {
        List<NetworkFee> result = networkRepository.fees();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void findNetworkStake() throws HederaException {
        Optional<NetworkStake> result = networkRepository.stake();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    void findNetworkSupplies() throws HederaException {
        Optional<NetworkSupplies> result = networkRepository.supplies();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }
}
