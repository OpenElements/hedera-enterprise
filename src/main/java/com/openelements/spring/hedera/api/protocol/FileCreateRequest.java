package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;

public record FileCreateRequest(Hbar maxTransactionFee,
                                Duration transactionValidDuration,
                                byte[] contents,
                                String fileMemo) implements TransactionRequest {

    private static final String DEFAULT_FILE_MEMO = "";

    public static FileCreateRequest of(byte[] contents) {
        return new FileCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contents, DEFAULT_FILE_MEMO);
    }
}
