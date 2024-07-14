package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hedera.base.HederaException;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public interface ContractVerificationClient {

    /**
     * Check the verification state of a contract.
     * @param contractId contract to check
     * @return verification state
     * @throws HederaException if an error happens in communication with the Hedera network
     */
    ContractVerificationState checkVerification(ContractId contractId) throws HederaException;

    /**
     * Check the verification state of a file that is part of a contract.
     * @param contractId contract to check
     * @param fileName file name
     * @param fileContent file content
     * @return true if the contract contains is verified and contains a file with the given name and content
     * @throws IllegalStateException if contract is not verified
     * @throws HederaException if an error happens in communication with the Hedera network
     */
    boolean checkVerification(ContractId contractId, String fileName, String fileContent) throws HederaException;

    /**
     * Try to verify a contract.
     * @param contractId contract to verify
     * @param contractName contract name
     * @param contractSource contract source code
     * @param contractMetadata contract metadata
     * @return
     * @throws IllegalStateException if contract is already verified
     * @throws HederaException if an error happens in communication with the Hedera network
     */
    default ContractVerificationState verify(ContractId contractId, String contractName, String contractSource, String contractMetadata) throws HederaException {
        return verify(contractId, contractName, Map.of(contractName + ".sol", contractSource, "metadata.json", contractMetadata));
    }

    /**
     * Try to verify a contract.
     * @param contractId contract to verify
     * @param contractName contract name
     * @param files contract files
     * @return verification state
     * @throws IllegalStateException if contract is already verified
     * @throws HederaException if an error happens in communication with the Hedera network
     */
    ContractVerificationState verify(ContractId contractId, String contractName, Map<String, String> files)  throws HederaException;
}
