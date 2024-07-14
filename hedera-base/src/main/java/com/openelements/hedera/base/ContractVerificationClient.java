package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hedera.base.data.ContractVerificationState;

public interface ContractVerificationClient {

    ContractVerificationState checkVerification(String contractId, String contractSource, String contractMetadata);

    ContractVerificationState checkVerification(ContractId contractId, String contractSource, String contractMetadata);

}
