package com.openelements.hedera.spring.test;

import com.openelements.hedera.base.mirrornode.MirrorNodeClient;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import com.openelements.hedera.spring.EnableHedera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableHedera
@SpringBootConfiguration
@ComponentScan
public class TestConfig {


    @Bean
    HederaTestUtils hederaTestUtils(MirrorNodeClient mirrorNodeClient, ProtocolLayerClient protocolLayerClient) {
        HederaTestUtils testUtils = new HederaTestUtils(mirrorNodeClient, protocolLayerClient);
        testUtils.init();
        return testUtils;
    }
}
