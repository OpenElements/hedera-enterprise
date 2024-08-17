package com.openelements.hedera.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.proto.ContractFunctionResultOrBuilder;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.ContractParam;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.AccountCreateRequest;
import com.openelements.hedera.base.protocol.AccountCreateResult;
import com.openelements.hedera.base.protocol.AccountDeleteRequest;
import com.openelements.hedera.base.protocol.AccountDeleteResult;
import com.openelements.hedera.base.protocol.ContractCallRequest;
import com.openelements.hedera.base.protocol.ContractCallResult;
import com.openelements.hedera.base.protocol.ContractCreateRequest;
import com.openelements.hedera.base.protocol.ContractCreateResult;
import com.openelements.hedera.base.protocol.ContractDeleteRequest;
import com.openelements.hedera.base.protocol.ContractDeleteResult;
import java.lang.reflect.Constructor;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
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

    @Test
    void testContractCallRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String contractIdString = "0.0.12345";
        final ContractId contractId = ContractId.fromString(contractIdString);
        final String functionName = "functionName";
        final ContractParam<Long> contractParam = ContractParam.int32(1);
        final List<ContractParam<?>> constructorParams = List.of(contractParam);

        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractIdString, functionName));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractIdString, functionName, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractIdString, functionName, constructorParams));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractId, functionName));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractId, functionName, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractId, functionName, constructorParams));
        Assertions.assertDoesNotThrow(() -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, functionName, constructorParams));

        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of((String) null, functionName));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractIdString, null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractIdString, functionName, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractIdString, functionName, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of((ContractId) null, functionName));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractId, null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractId, functionName, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractId, functionName, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallRequest(null, transactionValidDuration, contractId, functionName, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallRequest(maxTransactionFee, null, contractId, functionName, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, null, functionName, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, null, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, functionName, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallRequest(Hbar.from(-100), transactionValidDuration, contractId, functionName, constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallRequest(maxTransactionFee, Duration.ZERO, contractId, functionName, constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallRequest(maxTransactionFee, Duration.ofSeconds(-1), contractId, functionName, constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, "", constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, "   ", constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, " blankPrefix", constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, "blankSuffix ", constructorParams));
    }

    @Test
    void testContractCallResultCreation() throws Exception {
        //given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final byte[] transactionHash = new byte[]{};
        final Instant consensusTimestamp = Instant.now();
        final Hbar transactionFee = Hbar.fromTinybars(1000);
        final com.hedera.hashgraph.sdk.proto.ContractFunctionResult contractFunctionResultProto = com.hedera.hashgraph.sdk.proto.ContractFunctionResult.newBuilder().build();
        final Constructor<ContractFunctionResult> constructor = ContractFunctionResult.class.getDeclaredConstructor(
                ContractFunctionResultOrBuilder.class);
        constructor.setAccessible(true);
        ContractFunctionResult contractFunctionResult = constructor.newInstance(contractFunctionResultProto);

        //then
        Assertions.assertDoesNotThrow(() -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp, transactionFee, contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallResult(null, status, transactionHash, consensusTimestamp, transactionFee, contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallResult(transactionId, null, transactionHash, consensusTimestamp, transactionFee, contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallResult(transactionId, status, null, consensusTimestamp, transactionFee, contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallResult(transactionId, status, transactionHash, null, transactionFee, contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp, null, contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp, transactionFee, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp, Hbar.fromTinybars(-1), contractFunctionResult));
    }

    @Test
    void testContractCreateRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String fileIdString = "0.0.12345";
        final FileId fileId = FileId.fromString(fileIdString);
        final ContractParam<Long> contractParam = ContractParam.int32(1);
        final List<ContractParam<?>> constructorParams = List.of(contractParam);

        //then
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileIdString));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileIdString, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileIdString, constructorParams));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileId));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileId, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileId, constructorParams));
        Assertions.assertDoesNotThrow(() -> new ContractCreateRequest(maxTransactionFee, transactionValidDuration, fileId, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((String) null, contractParam));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((String) null, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of(fileIdString, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of(fileIdString, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((FileId) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((FileId) null, contractParam));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((FileId) null, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of(fileId, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of(fileId, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateRequest(null, transactionValidDuration, fileId, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateRequest(maxTransactionFee, null, fileId, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateRequest(maxTransactionFee, transactionValidDuration, null, constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateRequest(maxTransactionFee, transactionValidDuration, fileId, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCreateRequest(Hbar.from(-100), transactionValidDuration, fileId, constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCreateRequest(maxTransactionFee, Duration.ZERO, fileId, constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractCreateRequest(maxTransactionFee, Duration.ofSeconds(-1), fileId, constructorParams));
    }

    @Test
    void testContractCreateResultCreation() {
        //given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final ContractId contractId = ContractId.fromString("0.0.12345");

        //then
        Assertions.assertDoesNotThrow(() -> new ContractCreateResult(transactionId, status, contractId));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateResult(null, status, contractId));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateResult(transactionId, null, contractId));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateResult(transactionId, status, null));
    }

    @Test
    void testContractDeleteRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String contractIdAsString = "0.0.12345";
        final ContractId contractId = ContractId.fromString(contractIdAsString);
        final ContractId transferFeeToContractId = ContractId.fromString("0.0.54321");
        final AccountId transferFeeToAccountId = new AccountId(0, 0, 54321);

        //then
        Assertions.assertDoesNotThrow(() -> ContractDeleteRequest.of(contractIdAsString));
        Assertions.assertDoesNotThrow(() -> ContractDeleteRequest.of(contractId));
        Assertions.assertDoesNotThrow(() -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId, transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertDoesNotThrow(() -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId, null, transferFeeToAccountId));
        Assertions.assertDoesNotThrow(() -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId, transferFeeToContractId, null));
        Assertions.assertDoesNotThrow(() -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId, null, null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractDeleteRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractDeleteRequest.of((ContractId) null));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractDeleteRequest(null, transactionValidDuration, contractId, transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractDeleteRequest(maxTransactionFee, null, contractId, transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, null, transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractDeleteRequest(Hbar.from(-100), transactionValidDuration, contractId, transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractDeleteRequest(maxTransactionFee, Duration.ZERO, contractId, transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new ContractDeleteRequest(maxTransactionFee, Duration.ofSeconds(-1), contractId, transferFeeToContractId, transferFeeToAccountId));
    }

    @Test
    void testContractDeleteResultCreation() {
        //given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //then
        Assertions.assertDoesNotThrow(() -> new ContractDeleteResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractDeleteResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractDeleteResult(transactionId, null));
    }
}
