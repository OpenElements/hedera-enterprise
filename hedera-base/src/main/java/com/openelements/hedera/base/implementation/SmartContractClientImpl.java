package com.openelements.hedera.base.implementation;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hedera.base.ContractParam;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.SmartContractClient;
import com.openelements.hedera.base.protocol.ContractCallRequest;
import com.openelements.hedera.base.protocol.ContractCreateRequest;
import com.openelements.hedera.base.protocol.ContractCreateResult;
import com.openelements.hedera.base.protocol.ProtocolLevelClient;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartContractClientImpl implements SmartContractClient {

    private final static Logger log = LoggerFactory.getLogger(SmartContractClientImpl.class);

    private final ProtocolLevelClient protocolLevelClient;

    private final FileClient fileClient;

    public SmartContractClientImpl(@NonNull final ProtocolLevelClient protocolLevelClient, FileClient fileClient) {
        this.protocolLevelClient = Objects.requireNonNull(protocolLevelClient, "protocolLevelClient must not be null");
        this.fileClient = Objects.requireNonNull(fileClient, "fileClient must not be null");
    }

    @NonNull
    @Override
    public ContractId createContract(@NonNull FileId fileId, @Nullable ContractParam<?>... constructorParams)
            throws HederaException {
        try {
            final ContractCreateRequest request;
            if (constructorParams == null) {
                request = ContractCreateRequest.of(fileId);
            } else {
                request = ContractCreateRequest.of(fileId, Arrays.asList(constructorParams));
            }
            final ContractCreateResult result = protocolLevelClient.executeContractCreateTransaction(request);
            return result.contractId();
        } catch (Exception e) {
            throw new HederaException("Failed to create contract with fileId " + fileId, e);
        }
    }

    @NonNull
    @Override
    public ContractId createContract(@NonNull byte[] contents, @Nullable ContractParam<?>... constructorParams)
            throws HederaException {
        try {
            final FileId fileId = fileClient.createFile(contents);
            final ContractId contract = createContract(fileId, constructorParams);
            fileClient.deleteFile(fileId);
            return contract;
        } catch (Exception e) {
            throw new HederaException("Failed to create contract out of byte array", e);
        }
    }

    @NonNull
    @Override
    public ContractId createContract(@NonNull Path pathToBin, @Nullable ContractParam<?>... constructorParams)
            throws HederaException {
        try {
            String content = Files.readString(pathToBin, StandardCharsets.UTF_8);
            final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            return createContract(bytes, constructorParams);
        } catch (Exception e) {
            throw new HederaException("Failed to create contract from path " + pathToBin, e);
        }
    }

    @NonNull
    @Override
    public ContractFunctionResult callContractFunction(@NonNull ContractId contractId, @NonNull String functionName,
            @Nullable ContractParam<?>... params) throws HederaException {
        try {
            final ContractCallRequest request = ContractCallRequest.of(contractId, functionName, params);
            return protocolLevelClient.executeContractCallTransaction(request).contractFunctionResult();
        } catch (Exception e) {
            throw new HederaException("Failed to call function '" + functionName + "' on contract with id " + contractId, e);
        }
    }
}
