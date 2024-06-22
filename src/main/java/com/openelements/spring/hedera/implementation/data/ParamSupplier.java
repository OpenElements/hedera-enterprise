package com.openelements.spring.hedera.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;

public interface ParamSupplier<T> {

    void addParam(T value, ContractFunctionParameters params);
}
