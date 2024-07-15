package com.openelements.hedera.spring.test;

import static com.openelements.hedera.base.ContractParam.int256;
import static com.openelements.hedera.base.ContractParam.string;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;

import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.SmartContractClient;
import com.openelements.hedera.base.protocol.ContractCallRequest;
import com.openelements.hedera.base.protocol.ContractCallResult;
import com.openelements.hedera.base.protocol.ContractCreateRequest;
import com.openelements.hedera.base.protocol.ContractCreateResult;
import com.openelements.hedera.base.protocol.FileCreateRequest;
import com.openelements.hedera.base.protocol.FileCreateResult;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class ContractServiceTest {

    @Autowired
    private ProtocolLayerClient protocolLayerClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private SmartContractClient smartContractClient;

    @Test
    void testContractCreateByFileId() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());
        final String content = Files.readString(path, StandardCharsets.UTF_8);
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        final FileId fileId = fileClient.createFile(bytes);

        //when
        final ContractId contract = smartContractClient.createContract(fileId);

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
        final ContractId contract = smartContractClient.createContract(bytes);

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testContractCreateSimple() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());

        //when
        final ContractId contract = smartContractClient.createContract(path);

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testContractCreateSimpleWithLargeContract() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/large_contract.bin").getPath());

        //when
        final ContractId contract = smartContractClient.createContract(path);

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testContractWithConstructorParam() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/string_param_constructor_contract.bin").getPath());

        //when
        final ContractId contract = smartContractClient.createContract(path, string("Hello"));

        //then
        Assertions.assertNotNull(contract);
    }

    @Test
    void testCallFunction() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/uint_getter_setter_contract.bin").getPath());
        final ContractId contract = smartContractClient.createContract(path);
        final ContractCallRequest request = ContractCallRequest.of(contract, "get");

        //when
        final ContractCallResult result = protocolLayerClient.executeContractCallTransaction(request);

        //then
        Assertions.assertNotNull(result);
    }

    @Test
    void testCallFunctionWithParam() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/uint_getter_setter_contract.bin").getPath());
        final ContractId contract = smartContractClient.createContract(path);

        //when
        final ContractCallRequest request = ContractCallRequest.of(contract, "set", int256(123));
        final ContractCallResult result = protocolLayerClient.executeContractCallTransaction(request);

        //then
        Assertions.assertNotNull(result);
    }

    @Test
    void testCallFunctionResult() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/uint_getter_setter_contract.bin").getPath());
        final ContractId contract = smartContractClient.createContract(path);


        final ContractCallRequest setRequest = ContractCallRequest.of(contract, "set", int256(123));





        final ContractCallResult setResult = protocolLayerClient.executeContractCallTransaction(setRequest);
        final ContractCallRequest getRequest = ContractCallRequest.of(contract, "get");

        //when
        final ContractCallResult getResult = protocolLayerClient.executeContractCallTransaction(getRequest);

        //then
        Assertions.assertNotNull(getResult);
        Assertions.assertEquals(BigInteger.valueOf(123), getResult.contractFunctionResult().getInt256(0));
    }

}
