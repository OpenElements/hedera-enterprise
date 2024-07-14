package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hedera.base.data.ContractParam;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.file.Path;

public interface SmartContractClient {

    @NonNull
    default ContractId createContract(@NonNull String fileId, @Nullable ContractParam<?>... constructorParams) throws HederaException {
        return createContract(FileId.fromString(fileId), constructorParams);
    }

    @NonNull
    ContractId createContract(@NonNull FileId fileId, @Nullable ContractParam<?>... constructorParams) throws HederaException;

    @NonNull
    ContractId createContract(@NonNull byte[] contents, @Nullable ContractParam<?>... constructorParams) throws HederaException;

    @NonNull
    ContractId createContract(@NonNull Path pathToBin, @Nullable ContractParam<?>... constructorParams) throws HederaException;

    @NonNull
    default ContractFunctionResult callContractFunction(@NonNull String contractId, @NonNull String functionName, @Nullable ContractParam<?>... params) throws HederaException {
        return callContractFunction(ContractId.fromString(contractId), functionName, params);
    }

    @NonNull
    ContractFunctionResult callContractFunction(@NonNull ContractId contractId, @NonNull String functionName, @Nullable ContractParam<?>... params) throws HederaException;

}
