package com.openelements.spring.hedera.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import java.util.Objects;
import java.util.function.BiConsumer;

public enum StringDatatype implements ParamSupplier<String> {

    STRING("string");

    private final String name;

    private final BiConsumer<String, ContractFunctionParameters> addParam;

    StringDatatype(final String name) {
        this.name = name;
        this.addParam = (v, params) -> params.addString(v);
    }

    public void addParam(String value, ContractFunctionParameters params) {
        Objects.requireNonNull(params, "params must not be null");
        addParam.accept(value, params);
    }
}
