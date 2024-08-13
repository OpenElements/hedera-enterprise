package com.openelements.hedera.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtocolLayerDataCreation {

    @Test
    void testAccountBalanceRequestCreation() {
        Assertions.assertDoesNotThrow(() -> AccountBalanceRequest.of("0.0.12345"));
        Assertions.assertDoesNotThrow(() -> AccountBalanceRequest.of(new AccountId(0, 0, 12345)));
        Assertions.assertDoesNotThrow(() -> new AccountBalanceRequest(new AccountId(0, 0, 12345), null, null));
        Assertions.assertThrows(NullPointerException.class, () -> AccountBalanceRequest.of((String)null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountBalanceRequest(null, null, null));
    }

    @Test
    void testAccountBalanceResponseCreation() {
        Assertions.assertDoesNotThrow(() -> AccountBalanceResponse.of(Hbar.fromTinybars(1000)));
        Assertions.assertDoesNotThrow(() -> new AccountBalanceResponse(Hbar.fromTinybars(1000)));
        Assertions.assertThrows(NullPointerException.class, () -> AccountBalanceResponse.of(null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountBalanceResponse(null));
    }
}
