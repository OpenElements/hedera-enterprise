package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record TopicCreateRequest(Hbar maxTransactionFee,

                                 Duration transactionValidDuration) implements TransactionRequest {

    public TopicCreateRequest {

    }

    public static TopicCreateRequest of() {
        return new TopicCreateRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE, TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION);
    }


}
