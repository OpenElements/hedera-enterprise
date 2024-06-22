package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.openelements.spring.hedera.api.HederaClient;
import static com.openelements.spring.hedera.api.data.ContractParam.*;

import com.openelements.spring.hedera.api.data.ContractParam;
import com.openelements.spring.hedera.api.protocol.ContractCallRequest;
import com.openelements.spring.hedera.api.protocol.ContractCallResult;
import com.openelements.spring.hedera.api.protocol.ContractCreateRequest;
import com.openelements.spring.hedera.api.protocol.ContractCreateResult;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class ContractServiceTest {

    @Autowired
    private HederaClient hederaClient;

    @Test
    void testContractCreate() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());
        final String content = Files.readString(path, StandardCharsets.UTF_8);
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        final FileId fileId = hederaClient.uploadFile(bytes);
        final ContractCreateRequest request = ContractCreateRequest.of(fileId);

        //when
        final ContractCreateResult contractCreateResult = hederaClient.executeContractCreateTransaction(request);

        //then
        Assertions.assertNotNull(contractCreateResult);
        Assertions.assertNotNull(contractCreateResult.transactionId());
    }

    @Test
    void testContractCreateByFileId() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());
        final String content = Files.readString(path, StandardCharsets.UTF_8);
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        final FileId fileId = hederaClient.uploadFile(bytes);

        //when
        final ContractId contract = hederaClient.createContract(fileId);

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testContractCreateByBytes() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());
        final String content = Files.readString(path, StandardCharsets.UTF_8);
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        //when
        final ContractId contract = hederaClient.createContract(bytes);

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testContractCreateSimple() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());

        //when
        final ContractId contract = hederaClient.createContract(path);

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testContractCreateSimpleWithLargeContract() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/large_contract.bin").getPath());

        //when
        final ContractId contract = hederaClient.createContract(path);

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testContractWithConstructorParam() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/string_param_constructor_contract.bin").getPath());

        //when
        final ContractId contract = hederaClient.createContract(path, string("Hello"));

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testCallFunction() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/uint_getter_setter_contract.bin").getPath());
        final ContractId contract = hederaClient.createContract(path);
        final ContractCallRequest request = ContractCallRequest.of(contract, "get");

        //when
        final ContractCallResult result = hederaClient.executeContractCallTransaction(request);

        //then
        Assertions.assertNotNull(result);
    }

    @Test
    void testCallFunctionWithParam() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/uint_getter_setter_contract.bin").getPath());
        final ContractId contract = hederaClient.createContract(path);

        //when
        final ContractCallRequest request = ContractCallRequest.of(contract, "set", int256(123));
        final ContractCallResult result = hederaClient.executeContractCallTransaction(request);

        //then
        Assertions.assertNotNull(result);
    }


}
