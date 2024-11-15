package com.openelements.hiero.base.mirrornode;

import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.Objects;

public record ExchangeRate(int centEquivalent, int hbarEquivalent, @NonNull Instant expirationTime) {
    public ExchangeRate {
        Objects.requireNonNull(expirationTime, "expirationTime must not be null");
    }
}
