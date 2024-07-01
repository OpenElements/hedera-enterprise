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
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());
        final ContractId contract = hederaClient.createContract(path);
        final String expected = "Hello, World!";

        //when
        final ContractFunctionResult result = hederaClient.callContractFunction(contract, "checkString",
                ContractParam.string(expected));

        //then
        Assertions.assertEquals(expected, result.getString(0));
    }
}
