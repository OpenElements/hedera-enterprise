package com.openelements.spring.hedera.implementation.data;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import java.math.BigInteger;
import java.util.function.BiConsumer;

public enum BigIntegerBasedNumericDatatypes implements ParamSupplier<BigInteger> {

    INT256((v, params) -> params.addInt256(v), new BigInteger("-2").pow(255), new BigInteger("2").pow(255).subtract(BigInteger.ONE)),
    UINT256((v, params) -> params.addUint256(v), BigInteger.ZERO, new BigInteger("2").pow(256).subtract(BigInteger.ONE));

    private final BiConsumer<BigInteger, ContractFunctionParameters> addParam;

    private final BigInteger minValue;

    private final BigInteger maxValue;

    BigIntegerBasedNumericDatatypes(BiConsumer<BigInteger, ContractFunctionParameters> addParam, BigInteger minValue, BigInteger maxValue) {
        this.addParam = addParam;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public void addParam(BigInteger value, ContractFunctionParameters params) {
        if(value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
            throw new IllegalArgumentException("value out of range for type '" + this + "': " + value);
        }
        addParam.accept(value, params);
    }
}
