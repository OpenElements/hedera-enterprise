package com.openelements.spring.hedera.test;

import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.SmartContractClient;
import com.openelements.hedera.base.protocol.ProtocolLevelClient;
import com.openelements.spring.hedera.api.ContractVerificationClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class ServicesTest {

    @Autowired
    private ProtocolLevelClient protocolLevelClient;

    @Autowired
    private ContractVerificationClient verificationClient;

    @Autowired
    private FileClient fileServiceClient;

    @Autowired
    private SmartContractClient smartContractServiceClient;

    @Test
    void testServices() throws Exception {
        Assertions.assertNotNull(protocolLevelClient);
        Assertions.assertNotNull(verificationClient);
        Assertions.assertNotNull(fileServiceClient);
        Assertions.assertNotNull(smartContractServiceClient);
    }
}
