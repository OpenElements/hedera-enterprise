package com.openelements.spring.hedera.test;

import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.HederaClient;
import com.openelements.hedera.base.SmartContractClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class ServicesTest {

    @Autowired
    private HederaClient hederaClient;

    //@Autowired
    //private ContractVerificationClient contractVerificationClient;

    @Autowired
    private FileClient fileServiceClient;

    @Autowired
    private SmartContractClient smartContractServiceClient;

    @Test
    void testServices() throws Exception {
        Assertions.assertNotNull(hederaClient);
        //Assertions.assertNotNull(contractVerificationClient);
        Assertions.assertNotNull(fileServiceClient);
        Assertions.assertNotNull(smartContractServiceClient);
    }
}
