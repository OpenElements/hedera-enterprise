package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.spring.hedera.api.HederaClient;
import com.openelements.spring.hedera.api.data.ContractParam;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class DatatypesTest {

    @Autowired
    private HederaClient hederaClient;

    @Test
    public void testString() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/datatypes.bin").getPath());
        final ContractId contract = hederaClient.createContract(path);
        final String expected = "Hello, World!";

        //when
        final ContractFunctionResult result = hederaClient.callContractFunction(contract, "checkString",
                ContractParam.string(expected));

        //then
        Assertions.assertEquals(expected, result.getString(0));
    }

    @Test
    public void testInt8() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/datatypes.bin").getPath());
        final ContractId contract = hederaClient.createContract(path);
        final byte expected = 7;

        //when
        final ContractFunctionResult result = hederaClient.callContractFunction(contract, "checkInt8",
                ContractParam.int8(expected));

        //then
        Assertions.assertEquals(expected, result.getInt8(0));
    }
}
