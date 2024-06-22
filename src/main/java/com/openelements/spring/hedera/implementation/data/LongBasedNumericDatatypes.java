package com.openelements.spring.hedera.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import java.util.Objects;
import java.util.function.BiConsumer;

public enum LongBasedNumericDatatypes implements ParamSupplier<Long> {

    INT64((v, params) -> params.addInt64(v), Long.MIN_VALUE, Long.MAX_VALUE);

    private final BiConsumer<Long, ContractFunctionParameters> addParam;

    private final long minValue;

    private final long maxValue;

    LongBasedNumericDatatypes(final BiConsumer<Long, ContractFunctionParameters> addParam, long minValue, long maxValue) {
        this.addParam = addParam;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void addParam(Long value, ContractFunctionParameters params) {
        Objects.requireNonNull(value, "value must not be null");
        addParam.accept(value.longValue(), params);
    }

    public void addParam(long value, ContractFunctionParameters params) {
        Objects.requireNonNull(params, "params must not be null");
        if(value < minValue || value > maxValue) {
            throw new IllegalArgumentException("value out of range for type '" + this + "': " + value);
        }
        addParam.accept(value, params);
    }
}
