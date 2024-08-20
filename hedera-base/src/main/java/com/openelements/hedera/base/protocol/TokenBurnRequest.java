package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import java.time.Duration;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TokenBurnRequest(@NonNull Hbar maxTransactionFee,
                               @NonNull Duration transactionValidDuration,
                               @NonNull TokenId tokenId,
                               @NonNull PrivateKey supplyKey,
                               @Nullable Long amount,
                               @Nullable Set<Long> serials) implements TransactionRequest {

    public static TokenBurnRequest of(TokenId tokenId, long serial, PrivateKey supplyKey) {
        return of(tokenId, Set.of(serial), supplyKey);
    }

    public static TokenBurnRequest of(TokenId tokenId, Set<Long> serials, PrivateKey supplyKey) {
        return new TokenBurnRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenId, supplyKey, null, serials);
    }
}
