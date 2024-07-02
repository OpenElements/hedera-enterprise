package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.openelements.spring.hedera.api.data.ContractParam;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public interface SmartContractServiceClient {

    default ContractId createContract(String fileId, ContractParam<?>... constructorParams) throws HederaException {
        return createContract(FileId.fromString(fileId), constructorParams);
    }

    ContractId createContract(FileId fileId, ContractParam<?>... constructorParams) throws HederaException;

    ContractId createContract(byte[] contents, ContractParam<?>... constructorParams) throws HederaException;

    ContractId createContract(Path pathToBin, ContractParam<?>... constructorParams) throws HederaException;

    default ContractFunctionResult callContractFunction(String contractId, String functionName, ContractParam<?>... params) throws HederaException {
        return callContractFunction(ContractId.fromString(contractId), functionName, params);
    }

    ContractFunctionResult callContractFunction(ContractId contractId, String functionName, ContractParam<?>... params) throws HederaException;

}
