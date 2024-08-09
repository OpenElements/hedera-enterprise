package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record TokenAssociateRequest(Hbar maxTransactionFee,

                                    Duration transactionValidDuration,

                                    @NonNull TokenId tokenId,

                                    @NonNull AccountId accountId,

                                    @NonNull PrivateKey accountPrivateKey) implements TransactionRequest {

    public TokenAssociateRequest {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountPrivateKey, "accountPrivateKey must not be null");
    }

    public static TokenAssociateRequest of(@NonNull final TokenId tokenId, @NonNull final AccountId accountId, @NonNull final PrivateKey accountPrivateKey) {
        return new TokenAssociateRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE, TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenId, accountId, accountPrivateKey);
    }

}
