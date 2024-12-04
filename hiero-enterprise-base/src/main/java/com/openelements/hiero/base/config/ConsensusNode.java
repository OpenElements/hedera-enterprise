package com.openelements.hiero.base.config;

import com.hedera.hashgraph.sdk.AccountId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record ConsensusNode(@NonNull String ip, @NonNull String port, @NonNull String account) {

    public ConsensusNode {
        Objects.requireNonNull(ip, "ip must not be null");
        Objects.requireNonNull(port, "port must not be null");
        Objects.requireNonNull(account, "account must not be null");
    }

    public String getAddress() {
        return ip + ":" + port;
    }

    public AccountId getAccountId() {
        return AccountId.fromString(account);
    }
}
