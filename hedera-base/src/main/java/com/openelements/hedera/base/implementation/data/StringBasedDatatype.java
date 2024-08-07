package com.openelements.hedera.base.implementation.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Objects;
import java.util.function.BiConsumer;

public enum StringBasedDatatype implements ParamSupplier<String> {

    STRING("string", (v, params) -> params.addString(v)),
    ADDRESS("address", (v, params) -> params.addAddress(v));

    private final String nativeType;

    private final BiConsumer<String, ContractFunctionParameters> addParam;

    StringBasedDatatype(final String nativeType, final BiConsumer<String, ContractFunctionParameters> addParam) {
        this.nativeType = nativeType;
        this.addParam = addParam;
    }

    public void addParam(final String value, @NonNull final ContractFunctionParameters params) {
        Objects.requireNonNull(params, "params must not be null");
        addParam.accept(value, params);
    }

    @Override
    public boolean isValidParam(@NonNull final String value) {
        Objects.requireNonNull(value, "value must not be null");
        if(this.equals(ADDRESS)) {
            try {
                AccountId.fromString(value);
            } catch (final Exception e) {
                throw new IllegalArgumentException("Invalid address", e);
            }
        }
        return true;
    }

    @Override
    public String getNativeType() {
        return nativeType;
    }

}
