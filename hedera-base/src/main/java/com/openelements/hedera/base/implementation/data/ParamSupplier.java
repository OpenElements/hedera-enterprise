package com.openelements.hedera.base.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;

public interface ParamSupplier<T> {

    void addParam(T value, ContractFunctionParameters params);

    boolean isValidParam(T value);

    String getNativeType();
}
