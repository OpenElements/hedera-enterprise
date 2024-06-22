package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.spring.hedera.api.data.ContractParam;
import java.time.Duration;
import java.util.List;

public record ContractCreateRequest(Hbar maxTransactionFee,
                                    Duration transactionValidDuration,
                                    FileId fileId,
                                    List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public static ContractCreateRequest of(FileId fileId) {
        return of(fileId, List.of());
    }

    public static ContractCreateRequest of(FileId fileId, List<ContractParam<?>> constructorParams) {
        return new ContractCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId, List.copyOf(constructorParams));
    }
}
