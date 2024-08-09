package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.ContractId;
import org.jspecify.annotations.NonNull;
import java.util.Map;

/**
 * Client for verifying contracts on the Hedera network.
 * This client is used to check the verification state of a smart contract and to verify a smart contract.
 * Currently only the Hedera mainnet, testnet and previewnet is supported.
 */
public interface ContractVerificationClient {

    /**
     * Check the verification state of a contract.
     * @param contractId contract to check
     * @return verification state
     * @throws HederaException if an error happens in communication with the Hedera network
     */
    @NonNull
    ContractVerificationState checkVerification(@NonNull ContractId contractId) throws HederaException;

    /**
     * Check the verification state of a file that is part of a contract.
     * @param contractId contract to check
     * @param fileName file name
     * @param fileContent file content
     * @return true if the contract contains is verified and contains a file with the given name and content
     * @throws IllegalStateException if contract is not verified
     * @throws HederaException if an error happens in communication with the Hedera network
     */
    boolean checkVerification(@NonNull ContractId contractId, @NonNull String fileName, @NonNull String fileContent) throws HederaException;

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
    @NonNull
    default ContractVerificationState verify(@NonNull final ContractId contractId, @NonNull final String contractName, @NonNull final String contractSource, final String contractMetadata) throws HederaException {
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
    @NonNull
    ContractVerificationState verify(@NonNull ContractId contractId, @NonNull String contractName, @NonNull Map<String, String> files)  throws HederaException;
}
