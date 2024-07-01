package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.openelements.spring.hedera.implementation.data.LongBasedNumericDatatypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class LongBasedNumericDatatypesTest {

    @ParameterizedTest
    @EnumSource(LongBasedNumericDatatypes.class)
    void checkMethodsForNonNullResult(final LongBasedNumericDatatypes type) {
        //given
        final ContractFunctionParameters parameters = new ContractFunctionParameters();

        //then
        Assertions.assertThrows(NullPointerException.class, () -> type.addParam(1L, null));
        Assertions.assertThrows(NullPointerException.class, () -> type.addParam(null, parameters));
        Assertions.assertThrows(NullPointerException.class, () -> type.addParam(Long.valueOf(1L), null));
        Assertions.assertThrows(NullPointerException.class, () -> type.addParam(null, null));
    }

    @Test
    void checkIsValidParamInt8() {

        //given
        final LongBasedNumericDatatypes type = LongBasedNumericDatatypes.INT8;

        //then
        Assertions.assertTrue(type.isValidParam(1L));
        Assertions.assertTrue(type.isValidParam(0L));
        Assertions.assertTrue(type.isValidParam(-1L));
        Assertions.assertFalse(type.isValidParam(128L));
        Assertions.assertFalse(type.isValidParam(-129L));
    }

    @Test
    void checkIsValidParamUint8() {

        //given
        final LongBasedNumericDatatypes type = LongBasedNumericDatatypes.UINT8;

        //then
        Assertions.assertTrue(type.isValidParam(1L));
        Assertions.assertTrue(type.isValidParam(0L));
        Assertions.assertTrue(type.isValidParam(255L));
        Assertions.assertFalse(type.isValidParam(256L));
        Assertions.assertFalse(type.isValidParam(-1L));
    }

}
