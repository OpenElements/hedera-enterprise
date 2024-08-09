package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Represents a Hedera account.
 * @param accountId the ID of the account
 * @param publicKey the public key of the account
 * @param privateKey the private key of the account
 */
public record Account(@NonNull AccountId accountId, @NonNull PublicKey publicKey, @NonNull PrivateKey privateKey) {

    public Account {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(publicKey, "publicKey must not be null");
        Objects.requireNonNull(privateKey, "privateKey must not be null");
    }
}
