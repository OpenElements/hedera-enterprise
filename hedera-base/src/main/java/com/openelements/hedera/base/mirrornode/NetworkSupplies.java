package com.openelements.hedera.base.mirrornode;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record NetworkSupplies(@NonNull String releasedSupply, @NonNull String totalSupply) {
    public NetworkSupplies {
        Objects.requireNonNull(releasedSupply, "releasedSupply must not be null");
        Objects.requireNonNull(totalSupply, "totalSupply must not be null");
    }
}
