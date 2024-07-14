package com.openelements.spring.hedera.test;

import com.openelements.hedera.base.FileServiceClient;
import com.openelements.hedera.base.HederaClient;
import com.openelements.hedera.base.SmartContractServiceClient;
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
    private FileServiceClient fileServiceClient;

    @Autowired
    private SmartContractServiceClient smartContractServiceClient;

    @Test
    void testServices() throws Exception {
        Assertions.assertNotNull(hederaClient);
        //Assertions.assertNotNull(contractVerificationClient);
        Assertions.assertNotNull(fileServiceClient);
        Assertions.assertNotNull(smartContractServiceClient);
    }
}
