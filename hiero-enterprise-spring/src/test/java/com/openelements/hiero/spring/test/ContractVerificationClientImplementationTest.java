package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.verification.ContractVerificationClient;
import com.openelements.hiero.base.verification.ContractVerificationState;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.implementation.HieroNetwork;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
class ContractVerificationClientImplementationTest {

    @Autowired
    private HieroNetwork hieroNetwork;

    @Autowired
    private SmartContractClient smartContractClient;

    @Autowired
    private ContractVerificationClient verificationClient;

    private Path getResource(String resource) {
        return Path.of(ContractVerificationClientImplementationTest.class.getResource(resource).getPath());
    }

    private boolean isNotSupportedChain() {
        return hieroNetwork == HieroNetwork.CUSTOM;
    }

    @Test
    @DisabledIf(value = "isNotSupportedChain", disabledReason = "Verification is currently not supported for custom chains")
    void test() throws Exception {
        //given
        final String contractName = "HelloWorld";
        final Path binPath = getResource("/HelloWorld.bin");
        final Path solPath = getResource("/HelloWorld.sol");
        final String contractSource = Files.readString(solPath, StandardCharsets.UTF_8);
        final Path metadataPath = getResource("/HelloWorld.metadata.json");
        final String contractMetadata = Files.readString(metadataPath, StandardCharsets.UTF_8);
        final ContractId contractId = smartContractClient.createContract(binPath);

        //when
        final ContractVerificationState state = verificationClient.verify(
                contractId, contractName, contractSource, contractMetadata);

        //then
        Assertions.assertEquals(ContractVerificationState.FULL, state);
    }

}
