package com.openelements.spring.hedera.api;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.spring.hedera.api.data.ContractVerificationState;
import org.springframework.stereotype.Service;

@Service
public interface ContractVerificationClient {

    ContractVerificationState checkVerification(String contractId, String contractSource, String contractMetadata);

    ContractVerificationState checkVerification(ContractId contractId, String contractSource, String contractMetadata);

}
