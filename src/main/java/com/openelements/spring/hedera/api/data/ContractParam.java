package com.openelements.spring.hedera.api.data;

import com.openelements.spring.hedera.implementation.data.BigIntegerBasedNumericDatatypes;
import com.openelements.spring.hedera.implementation.data.LongBasedNumericDatatypes;
import com.openelements.spring.hedera.implementation.data.ParamSupplier;
import com.openelements.spring.hedera.implementation.data.StringDatatype;
import jakarta.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Objects;

public record ContractParam<T>(@Nonnull T value, @Nonnull ParamSupplier<T> supplier) {

    public ContractParam {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(supplier, "supplier must not be null");
    }

    @Nonnull
    public static ContractParam string(String value) {
        return new ContractParam(value, StringDatatype.STRING);
    }

    @Nonnull
    public static ContractParam int64(long value) {
        return new ContractParam(value, LongBasedNumericDatatypes.INT64);
    }

    @Nonnull
    public static ContractParam int256(@Nonnull BigInteger value) {
        Objects.requireNonNull(value, "value must not be null");
        return new ContractParam(value, BigIntegerBasedNumericDatatypes.INT256);
    }

    @Nonnull
    public static ContractParam int256(long value) {
        final BigInteger valueAsBigInteger = BigInteger.valueOf(value);
        return int256(valueAsBigInteger);
    }

}
