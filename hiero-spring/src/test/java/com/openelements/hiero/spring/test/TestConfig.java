package com.openelements.hiero.spring.test;

import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.spring.EnableHedera;
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
