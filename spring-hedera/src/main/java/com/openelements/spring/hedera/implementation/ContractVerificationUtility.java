package com.openelements.spring.hedera.implementation;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.spring.hedera.api.ContractVerificationState;
import com.openelements.spring.hedera.api.ContractVerificationClient;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to help with contract verification.
 */
public class ContractVerificationUtility {

    private final static Logger log = LoggerFactory.getLogger(ContractVerificationUtility.class);

    private final ContractVerificationClient verificationClient;

    public ContractVerificationUtility(ContractVerificationClient verificationClient) {
        this.verificationClient = verificationClient;
    }

    /**
     * Check verification of a contract and all its files. If the contract has not been verified yet, it will be verified.
     * @param contractId contract to verify
     * @param contractName contract name
     * @param contractSource contract source code
     * @param contractMetadata contract metadata
     */
    void doFullVerification(ContractId contractId, String contractName, String contractSource, String contractMetadata) {
        doFullVerification(contractId, contractName, Map.of(contractName + ".sol", contractSource, "metadata.json", contractMetadata));
    }

    /**
     * Check verification of a contract and all its files. If the contract has not been verified yet, it will be verified.
     * @param contractId contract to verify
     * @param contractName contract name
     * @param files map of file names to file contents
     */
    void doFullVerification(ContractId contractId, String contractName, Map<String, String> files) {
        final ContractVerificationState state = verificationClient.checkVerification(contractId);
        if(state == ContractVerificationState.FULL) {
            log.debug("Contract {} is already fully verified", contractId);
        } else {
            log.debug("Contract {} is not fully verified, will start verification", contractId);
            final ContractVerificationState newState = verificationClient.verify(contractId, contractName, files);
            if(newState == ContractVerificationState.FULL) {
                log.debug("Contract {} is now fully verified", contractId);
            } else {
                throw new IllegalStateException("Contract " + contractId + " is not fully verified, state is " + newState);
            }
        }
        files.forEach((fileName, fileContent) -> {
            if(!verificationClient.checkVerification(contractId, fileName, fileContent)) {
                throw new IllegalStateException("file " + fileName + " is invalid");
            }
        });
    }

}
