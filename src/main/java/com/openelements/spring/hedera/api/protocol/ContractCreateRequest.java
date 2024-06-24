package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.spring.hedera.api.data.ContractParam;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record ContractCreateRequest(Hbar maxTransactionFee,
                                    Duration transactionValidDuration,
                                    @Nonnull FileId fileId,
                                    @Nonnull List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public ContractCreateRequest {
        Objects.requireNonNull(fileId, "fileId is required");
        Objects.requireNonNull(constructorParams, "constructorParams is required");
    }

    @Nonnull
    public static ContractCreateRequest of(@Nonnull String fileId, @Nullable ContractParam<?>... constructorParams) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId), constructorParams);
    }

    @Nonnull
    public static ContractCreateRequest of(@Nonnull FileId fileId, @Nullable ContractParam<?>... constructorParams) {
        if(constructorParams == null) {
            return of(fileId, List.of());
        } else {
            return of(fileId, List.of(constructorParams));
        }
    }

    @Nonnull
    public static ContractCreateRequest of(@Nonnull String fileId, @Nonnull List<ContractParam<?>> constructorParams) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId), constructorParams);
    }
    @Nonnull
    public static ContractCreateRequest of(@Nonnull FileId fileId, @Nonnull List<ContractParam<?>> constructorParams) {
        return new ContractCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId, List.copyOf(constructorParams));
    }
}
