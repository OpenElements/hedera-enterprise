package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;

public record Account(@NonNull AccountId accountId, @NonNull PublicKey publicKey, @NonNull PrivateKey privateKey) {

    public Account {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        Objects.requireNonNull(privateKey, "privateKey must not be null");
    }
}
