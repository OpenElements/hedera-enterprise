package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hedera.base.data.ContractVerificationState;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface ContractVerificationClient {

    /**
     * Check the verification state of a contract.
     * @param contractId contract to check
     * @return verification state
     */
    ContractVerificationState checkVerification(ContractId contractId);

    /**
     * Check the verification state of a file that is part of a contract.
     * @param contractId contract to check
     * @param fileName file name
     * @param fileContent file content
     * @return true if the contract contains is verified and contains a file with the given name and content
     * @throws IllegalStateException if contract is not verified
     */
    boolean checkVerification(ContractId contractId, String fileName, String fileContent);

    /**
     * Try to verify a contract.
     * @param contractId contract to verify
     * @param contractName contract name
     * @param contractSource contract source code
     * @param contractMetadata contract metadata
     * @return
     * @throws IllegalStateException if contract is already verified
     */
    default ContractVerificationState verify(ContractId contractId, String contractName, String contractSource, String contractMetadata) {
        return verify(contractId, contractName, Map.of(contractName + ".sol", contractSource, "metadata.json", contractMetadata));
    }

    ContractVerificationState verify(ContractId contractId, String contractName, Map<String, String> files);
}
