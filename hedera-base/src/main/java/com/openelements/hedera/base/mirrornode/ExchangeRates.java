package com.openelements.hedera.base.mirrornode;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record ExchangeRates(@NonNull  ExchangeRate currentRate, @NonNull  ExchangeRate nextRate) {
    public ExchangeRates {
        Objects.requireNonNull(currentRate, "currentRate must not be null");
        Objects.requireNonNull(nextRate, "nextRate must not be null");
    }
}
