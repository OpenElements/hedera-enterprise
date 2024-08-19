package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import java.nio.file.Path;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * A client for interacting with the smart contract service on the Hedera network.
 * An implementation of this interface is using an internal account to interact with the Hedera network.
 * That account is the account that is used to pay for the transactions that are sent to the Hedera network and called 'operator account'.
 */
public interface SmartContractClient {

    /**
     * Create a new smart contract based on the file the given file ID. The file must contain the bytecode for the contract.
     * @param fileId the ID of the file containing the contract bytecode
     * @param constructorParams the parameters to pass to the contract constructor
     * @return the ID of the new contract
     * @throws HederaException if the contract could not be created
     */
    @NonNull
    default ContractId createContract(@NonNull String fileId, @Nullable ContractParam<?>... constructorParams) throws HederaException {
        return createContract(FileId.fromString(fileId), constructorParams);
    }

    /**
     * Create a new smart contract based on the file the given file ID. The file must contain the bytecode for the contract.
     * @param fileId the ID of the file containing the contract bytecode
     * @param constructorParams the parameters to pass to the contract constructor
     * @return the ID of the new contract
     * @throws HederaException if the contract could not be created
     */
    @NonNull
    ContractId createContract(@NonNull FileId fileId, @Nullable ContractParam<?>... constructorParams) throws HederaException;

    /**
     * Create a new smart contract with the given contents. The contents must be the bytecode for the contract.
     * @param contents the contents of the contract
     * @param constructorParams the parameters to pass to the contract constructor
     * @return the ID of the new contract
     * @throws HederaException if the contract could not be created
     */
    @NonNull
    ContractId createContract(@NonNull byte[] contents, @Nullable ContractParam<?>... constructorParams) throws HederaException;

    /**
     * Create a new smart contract based on a file. The contents of the file must be the bytecode for the contract.
     * @param pathToBin the path to the file containing the contract bytecode
     * @param constructorParams the parameters to pass to the contract constructor
     * @return the ID of the new contract
     * @throws HederaException if the contract could not be created
     */
    @NonNull
    ContractId createContract(@NonNull Path pathToBin, @Nullable ContractParam<?>... constructorParams) throws HederaException;

    /**
     * Call a function on a smart contract.
     * @param contractId the ID of the contract
     * @param functionName the name of the function to call
     * @param params the parameters to pass to the function
     * @return the result of the function call
     * @throws HederaException if the function could not be called
     */
    @NonNull
    default ContractCallResult callContractFunction(@NonNull String contractId, @NonNull String functionName, @Nullable ContractParam<?>... params) throws HederaException {
        return callContractFunction(ContractId.fromString(contractId), functionName, params);
    }

    /**
     * Call a function on a smart contract.
     * @param contractId the ID of the contract
     * @param functionName the name of the function to call
     * @param params the parameters to pass to the function
     * @return the result of the function call
     * @throws HederaException if the function could not be called
     */
    @NonNull
    ContractCallResult callContractFunction(@NonNull ContractId contractId, @NonNull String functionName, @Nullable ContractParam<?>... params) throws HederaException;

}
