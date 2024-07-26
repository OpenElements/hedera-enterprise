package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hedera.base.ContractParam;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record ContractDeleteRequest(Hbar maxTransactionFee,
                                    Duration transactionValidDuration,
                                    @NonNull ContractId contractId,
                                    @Nullable ContractId transferFeeToContractId,
                                    @Nullable AccountId transferFeeToAccountId) implements TransactionRequest {

    public ContractDeleteRequest {
        Objects.requireNonNull(contractId, "contractId is required");
    }

    @NonNull
    public static ContractDeleteRequest of(@NonNull String contractId) {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return of(ContractId.fromString(contractId));
    }

    @NonNull
    public static ContractDeleteRequest of(@NonNull ContractId contractId) {
        return new ContractDeleteRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contractId, null, null);
    }
}
