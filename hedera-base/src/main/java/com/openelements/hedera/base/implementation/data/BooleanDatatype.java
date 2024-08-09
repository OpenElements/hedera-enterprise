package com.openelements.hedera.base.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import org.jspecify.annotations.NonNull;
import java.util.Objects;
import java.util.function.BiConsumer;

public enum BooleanDatatype implements ParamSupplier<Boolean> {

    BOOL;

    private final BiConsumer<Boolean, ContractFunctionParameters> addParam;

    BooleanDatatype() {
        this.addParam = (v, params) -> params.addBool(v);
    }

    public void addParam(@NonNull final Boolean value, @NonNull final ContractFunctionParameters params) {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(params, "params must not be null");
        addParam.accept(value, params);
    }

    public void addParam(final boolean value, @NonNull final ContractFunctionParameters params) {
        Objects.requireNonNull(params, "params must not be null");
        addParam.accept(value, params);
    }

    @Override
    public boolean isValidParam(final Boolean value) {
        return true;
    }

    @Override
    public String getNativeType() {
        return "bool";
    }


}
