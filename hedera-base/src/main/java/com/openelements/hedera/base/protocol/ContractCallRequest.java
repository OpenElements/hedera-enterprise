package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.data.ContractParam;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record ContractCallRequest(Hbar maxTransactionFee,
                                  Duration transactionValidDuration,
                                  @NonNull ContractId contractId,
                                  @NonNull String functionName,
                                  @NonNull List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public ContractCallRequest {
        Objects.requireNonNull(contractId, "contractId is required");
        Objects.requireNonNull(functionName, "functionName is required");
        Objects.requireNonNull(constructorParams, "constructorParams is required");
    }

    @NonNull
    public static ContractCallRequest of(@NonNull String contractId, @NonNull String functionName, @Nullable ContractParam<?>... constructorParams) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId), functionName, constructorParams);
    }

    @NonNull
    public static ContractCallRequest of(@NonNull ContractId contractId, @NonNull String functionName, @Nullable ContractParam<?>... constructorParams) {
        if(constructorParams == null) {
            return of(contractId, functionName, List.of());
        } else {
            return of(contractId, functionName, List.of(constructorParams));
        }
    }

    @NonNull
    public static ContractCallRequest of(@NonNull String contractId, @NonNull String functionName, @NonNull List<ContractParam<?>> constructorParams) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId), functionName, constructorParams);
    }

    @NonNull
    public static ContractCallRequest of(@NonNull ContractId contractId, @NonNull String functionName, @NonNull List<ContractParam<?>> constructorParams) {
        return new ContractCallRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contractId, functionName, List.copyOf(constructorParams));
    }
}
