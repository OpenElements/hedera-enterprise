package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.spring.hedera.api.data.ContractParam;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record ContractCallRequest(Hbar maxTransactionFee,
                                  Duration transactionValidDuration,
                                  @Nonnull ContractId contractId,
                                  @Nonnull String functionName,
                                  @Nonnull List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public ContractCallRequest {
        Objects.requireNonNull(contractId, "contractId is required");
        Objects.requireNonNull(functionName, "functionName is required");
        Objects.requireNonNull(constructorParams, "constructorParams is required");
    }

    @Nonnull
    public static ContractCallRequest of(@Nonnull String contractId, @Nonnull String functionName, @Nullable ContractParam<?>... constructorParams) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId), functionName, constructorParams);
    }

    @Nonnull
    public static ContractCallRequest of(@Nonnull ContractId contractId, @Nonnull String functionName, @Nullable ContractParam<?>... constructorParams) {
        if(constructorParams == null) {
            return of(contractId, functionName, List.of());
        } else {
            return of(contractId, functionName, List.of(constructorParams));
        }
    }

    @Nonnull
    public static ContractCallRequest of(@Nonnull String contractId, @Nonnull String functionName, @Nonnull List<ContractParam<?>> constructorParams) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId), functionName, constructorParams);
    }

    @Nonnull
    public static ContractCallRequest of(@Nonnull ContractId contractId, @Nonnull String functionName, @Nonnull List<ContractParam<?>> constructorParams) {
        return new ContractCallRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contractId, functionName, List.copyOf(constructorParams));
    }
}
