package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.openelements.spring.hedera.api.data.ContractParam;
import com.openelements.spring.hedera.api.protocol.AccountBalanceRequest;
import com.openelements.spring.hedera.api.protocol.AccountBalanceResponse;
import com.openelements.spring.hedera.api.protocol.ContractCallRequest;
import com.openelements.spring.hedera.api.protocol.ContractCallResult;
import com.openelements.spring.hedera.api.protocol.ContractCreateRequest;
import com.openelements.spring.hedera.api.protocol.ContractCreateResult;
import com.openelements.spring.hedera.api.protocol.FileAppendRequest;
import com.openelements.spring.hedera.api.protocol.FileAppendResult;
import com.openelements.spring.hedera.api.protocol.FileContentsRequest;
import com.openelements.spring.hedera.api.protocol.FileContentsResponse;
import com.openelements.spring.hedera.api.protocol.FileCreateRequest;
import com.openelements.spring.hedera.api.protocol.FileCreateResult;
import com.openelements.spring.hedera.api.protocol.FileDeleteRequest;
import com.openelements.spring.hedera.api.protocol.FileDeleteResult;
import java.nio.file.Path;


/**
 * Interface for interacting with the Hedera network.
 */
public interface HederaClient {

    FileId uploadFile(byte[] contents) throws HederaException;

    ContractId createContract(FileId fileId, ContractParam<?>... constructorParams) throws HederaException;

    ContractId createContract(byte[] contents, ContractParam<?>... constructorParams) throws HederaException;

    ContractId createContract(Path pathToBin, ContractParam<?>... constructorParams) throws HederaException;

    void deleteFile(FileId fileId) throws HederaException;

    byte[] readFile(FileId fileId) throws HederaException;

    AccountBalanceResponse executeAccountBalanceQuery(AccountBalanceRequest request) throws HederaException;

    FileContentsResponse executeFileContentsQuery(FileContentsRequest request) throws HederaException;

    FileAppendResult executeFileAppendRequestTransaction(FileAppendRequest request) throws HederaException;

    FileDeleteResult executeFileDeleteTransaction(FileDeleteRequest request) throws HederaException;

    FileCreateResult executeFileCreateTransaction(FileCreateRequest request) throws HederaException;

    ContractCreateResult executeContractCreateTransaction(ContractCreateRequest request) throws HederaException;

    ContractCallResult executeContractCallTransaction(ContractCallRequest request) throws HederaException;

    Client getClient();
}
