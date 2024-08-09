package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.ContractParam;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record ContractCreateRequest(Hbar maxTransactionFee,
                                    Duration transactionValidDuration,
                                    @NonNull FileId fileId,
                                    @NonNull List<ContractParam<?>> constructorParams) implements TransactionRequest {

    public ContractCreateRequest {
        Objects.requireNonNull(fileId, "fileId is required");
        Objects.requireNonNull(constructorParams, "constructorParams is required");
    }

    @NonNull
    public static ContractCreateRequest of(@NonNull String fileId, @Nullable ContractParam<?>... constructorParams) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId), constructorParams);
    }

    @NonNull
    public static ContractCreateRequest of(@NonNull FileId fileId, @Nullable ContractParam<?>... constructorParams) {
        if(constructorParams == null) {
            return of(fileId, List.of());
        } else {
            return of(fileId, List.of(constructorParams));
        }
    }

    @NonNull
    public static ContractCreateRequest of(@NonNull String fileId, @NonNull List<ContractParam<?>> constructorParams) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId), constructorParams);
    }
    @NonNull
    public static ContractCreateRequest of(@NonNull FileId fileId, @NonNull List<ContractParam<?>> constructorParams) {
        return new ContractCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, fileId, List.copyOf(constructorParams));
    }
}
