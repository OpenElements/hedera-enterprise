package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.spring.hedera.api.data.ContractParam;
import java.time.Duration;
import java.util.List;

public record ContractCallRequest(Hbar maxTransactionFee,
                                  Duration transactionValidDuration,
                                  ContractId contractId,
                                  String functionName,
                                  List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public static ContractCallRequest of(ContractId contractId, String functionName, ContractParam<?>... constructorParams) {
        if(constructorParams == null) {
            return of(contractId, functionName, List.of());
        } else {
            return of(contractId, functionName, List.of(constructorParams));
        }
    }

    public static ContractCallRequest of(ContractId contractId, String functionName, List<ContractParam<?>> constructorParams) {
        return new ContractCallRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contractId, functionName, List.copyOf(constructorParams));
    }
}
