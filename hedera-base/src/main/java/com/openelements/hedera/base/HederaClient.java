package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.Client;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.ContractCallRequest;
import com.openelements.hedera.base.protocol.ContractCallResult;
import com.openelements.hedera.base.protocol.ContractCreateRequest;
import com.openelements.hedera.base.protocol.ContractCreateResult;
import com.openelements.hedera.base.protocol.FileAppendRequest;
import com.openelements.hedera.base.protocol.FileAppendResult;
import com.openelements.hedera.base.protocol.FileContentsRequest;
import com.openelements.hedera.base.protocol.FileContentsResponse;
import com.openelements.hedera.base.protocol.FileCreateRequest;
import com.openelements.hedera.base.protocol.FileCreateResult;
import com.openelements.hedera.base.protocol.FileDeleteRequest;
import com.openelements.hedera.base.protocol.FileDeleteResult;


/**
 * Interface for interacting with the Hedera network.
 */
public interface HederaClient extends FileServiceClient, SmartContractServiceClient {

    AccountBalanceResponse executeAccountBalanceQuery(AccountBalanceRequest request) throws HederaException;

    FileContentsResponse executeFileContentsQuery(FileContentsRequest request) throws HederaException;

    FileAppendResult executeFileAppendRequestTransaction(FileAppendRequest request) throws HederaException;

    FileDeleteResult executeFileDeleteTransaction(FileDeleteRequest request) throws HederaException;

    FileCreateResult executeFileCreateTransaction(FileCreateRequest request) throws HederaException;

    ContractCreateResult executeContractCreateTransaction(ContractCreateRequest request) throws HederaException;

    ContractCallResult executeContractCallTransaction(ContractCallRequest request) throws HederaException;

    Client getClient();
}
