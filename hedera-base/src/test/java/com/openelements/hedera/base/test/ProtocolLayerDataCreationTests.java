package com.openelements.hedera.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.AccountCreateRequest;
import com.openelements.hedera.base.protocol.AccountCreateResult;
import com.openelements.hedera.base.protocol.AccountDeleteRequest;
import com.openelements.hedera.base.protocol.AccountDeleteResult;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtocolLayerDataCreationTests {

    @Test
    void testAccountBalanceRequestCreation() {
        //given
        final String accountIdString = "0.0.12345";
        final AccountId accountId = AccountId.fromString(accountIdString);

        //then
        Assertions.assertDoesNotThrow(() -> AccountBalanceRequest.of(accountIdString));
        Assertions.assertDoesNotThrow(() -> AccountBalanceRequest.of(accountId));
        Assertions.assertDoesNotThrow(() -> new AccountBalanceRequest(accountId, null, null));
        Assertions.assertThrows(NullPointerException.class, () -> AccountBalanceRequest.of((String)null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountBalanceRequest(null, null, null));
    }

    @Test
    void testAccountBalanceResponseCreation() {
        //given
        final Hbar balance = Hbar.fromTinybars(1000);

        //then
        Assertions.assertDoesNotThrow(() -> AccountBalanceResponse.of(balance));
        Assertions.assertDoesNotThrow(() -> new AccountBalanceResponse(balance));
        Assertions.assertThrows(NullPointerException.class, () -> AccountBalanceResponse.of(null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountBalanceResponse(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> AccountBalanceResponse.of(Hbar.fromTinybars(-1000)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountBalanceResponse(Hbar.fromTinybars(-1000)));
    }

    @Test
    void testAccountCreateRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final Hbar initialBalance = Hbar.fromTinybars(1000);

        //then
        Assertions.assertDoesNotThrow(() -> AccountCreateRequest.of());
        Assertions.assertDoesNotThrow(() -> AccountCreateRequest.of(initialBalance));
        Assertions.assertDoesNotThrow(() -> new AccountCreateRequest(Hbar.fromTinybars(1000), transactionValidDuration, initialBalance));
        Assertions.assertThrows(NullPointerException.class, () -> AccountCreateRequest.of(null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateRequest(null, transactionValidDuration, initialBalance));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateRequest(maxTransactionFee, null, initialBalance));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateRequest(maxTransactionFee, transactionValidDuration, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> AccountCreateRequest.of(Hbar.fromTinybars(-1000)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountCreateRequest(maxTransactionFee, Duration.ZERO, initialBalance));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountCreateRequest(maxTransactionFee, Duration.ofSeconds(-10), initialBalance));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountCreateRequest(Hbar.fromTinybars(-1000), transactionValidDuration, initialBalance));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountCreateRequest(maxTransactionFee, transactionValidDuration, Hbar.fromTinybars(-1000)));
    }

    @Test
    void testAccountCreateResultCreation() {
        //given
        final AccountId accountId = new AccountId(0, 0, 12345);
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final Account account = Account.of(accountId, privateKey);
        final TransactionId transactionId = TransactionId.generate(accountId);
        final Status status = Status.SUCCESS;
        final byte[] transactionHash = new byte[]{};
        final Instant consensusTimestamp = Instant.now();
        final Hbar transactionFee = Hbar.fromTinybars(1000);

        //then
        Assertions.assertDoesNotThrow(() -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp, transactionFee, account));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateResult(null, status, transactionHash, consensusTimestamp, transactionFee, account));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateResult(transactionId, null, transactionHash, consensusTimestamp, transactionFee, account));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateResult(transactionId, status, null, consensusTimestamp, transactionFee, account));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateResult(transactionId, status, transactionHash, null, transactionFee, account));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp, null, account));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp, transactionFee, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp, Hbar.fromTinybars(-1000), account));
    }

    @Test
    void testAccountDeleteRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final Account toDelete = Account.of(new AccountId(0, 0, 12345), PrivateKey.generateECDSA());
        final Account transferFoundsToAccount = Account.of(new AccountId(0, 0, 54321), PrivateKey.generateECDSA());

        //then
        Assertions.assertDoesNotThrow(() -> AccountDeleteRequest.of(toDelete));
        Assertions.assertDoesNotThrow(() -> AccountDeleteRequest.of(toDelete, transferFoundsToAccount));
        Assertions.assertDoesNotThrow(() -> AccountDeleteRequest.of(toDelete, null));
        Assertions.assertDoesNotThrow(() -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, toDelete, transferFoundsToAccount));
        Assertions.assertDoesNotThrow(() -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, toDelete, null));
        Assertions.assertThrows(NullPointerException.class, () -> AccountDeleteRequest.of(null));
        Assertions.assertThrows(NullPointerException.class, () -> AccountDeleteRequest.of(null, transferFoundsToAccount));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountDeleteRequest(null, transactionValidDuration, toDelete, transferFoundsToAccount));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountDeleteRequest(maxTransactionFee, null, toDelete, transferFoundsToAccount));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, null, transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class, () -> AccountDeleteRequest.of(toDelete, toDelete));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountDeleteRequest(Hbar.from(-1000), transactionValidDuration, toDelete, transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountDeleteRequest(maxTransactionFee, Duration.ZERO, toDelete, transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountDeleteRequest(maxTransactionFee, Duration.ofSeconds(-10), toDelete, transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, toDelete, toDelete));
    }

    @Test
    void testAccountDeleteResultCreation() {
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final byte[] transactionHash = new byte[]{};
        final Instant consensusTimestamp = Instant.now();
        final Hbar transactionFee = Hbar.fromTinybars(1000);

        Assertions.assertDoesNotThrow(() -> new AccountDeleteResult(transactionId, status, transactionHash, consensusTimestamp, transactionFee));
        Assertions.assertDoesNotThrow(() -> new AccountDeleteResult(transactionId, status, transactionHash, null, null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountDeleteResult(null, status, transactionHash, consensusTimestamp, transactionFee));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountDeleteResult(transactionId, null, transactionHash, consensusTimestamp, transactionFee));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountDeleteResult(transactionId, status, null, consensusTimestamp, transactionFee));
    }
}
