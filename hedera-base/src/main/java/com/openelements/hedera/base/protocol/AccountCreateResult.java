package com.openelements.hedera.base.protocol;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record AccountCreateResult(@NonNull TransactionId transactionId, @NonNull Status status, @NonNull ByteString transactionHash, @NonNull Instant consensusTimestamp, @NonNull Hbar transactionFee, @NonNull AccountId accountId, @NonNull PublicKey publicKey, @NonNull PrivateKey privateKey) implements TransactionRecord {

    public AccountCreateResult {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Objects.requireNonNull(transactionHash, "transactionHash must not be null");
        Objects.requireNonNull(consensusTimestamp, "consensusTimestamp must not be null");
        Objects.requireNonNull(transactionFee, "transactionFee must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        Objects.requireNonNull(privateKey, "privateKey must not be null");
    }
}
