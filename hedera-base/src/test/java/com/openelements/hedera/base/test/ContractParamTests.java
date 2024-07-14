package com.openelements.hedera.base.test;

import com.openelements.hedera.base.ContractParam;
import java.math.BigInteger;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ContractParamTests {

    @ParameterizedTest
    @MethodSource("provideInt8Arguments")
    public void testInt8Range(byte value) {
        Assertions.assertDoesNotThrow(() -> ContractParam.int8(value));

    }

    static Stream<Arguments> provideInt8Arguments() {
        return Stream.of(
                Arguments.of(Byte.MIN_VALUE),
                Arguments.of(Byte.MAX_VALUE),
                Arguments.of((byte) 0, true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUint8Arguments")
    public void testUint8Range(short value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint8(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint8(value));
        }
    }

    static Stream<Arguments> provideUint8Arguments() {
        return Stream.of(
                Arguments.of( (short) 0, true),
                Arguments.of((short) 255, true),
                Arguments.of((short) -1, false),
                Arguments.of((short) 256, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInt16Arguments")
    public void testInt16Range(short value) {
        Assertions.assertDoesNotThrow(() -> ContractParam.int16(value));
    }

    static Stream<Arguments> provideInt16Arguments() {
        return Stream.of(
                Arguments.of(Short.MIN_VALUE),
                Arguments.of(Short.MAX_VALUE),
                Arguments.of((short) 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUint16Arguments")
    public void testUint16Range(int value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint16(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint16(value));
        }
    }

    static Stream<Arguments> provideUint16Arguments() {
        return Stream.of(
                Arguments.of(0, true),
                Arguments.of(65535, true),
                Arguments.of(-1, false),
                Arguments.of(65536, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInt32Arguments")
    public void testInt32Range(int value) {
        Assertions.assertDoesNotThrow(() -> ContractParam.int32(value));
    }

    static Stream<Arguments> provideInt32Arguments() {
        return Stream.of(
                Arguments.of(Integer.MIN_VALUE),
                Arguments.of(Integer.MAX_VALUE),
                Arguments.of(0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUint32Arguments")
    public void testUint32Range(long value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint32(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint32(value));
        }
    }

    static Stream<Arguments> provideUint32Arguments() {
        return Stream.of(
                Arguments.of(0L, true),
                Arguments.of(4294967295L, true),
                Arguments.of(-1L, false),
                Arguments.of(4294967296L, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInt64Arguments")
    public void testInt64Range(long value) {
        Assertions.assertDoesNotThrow(() -> ContractParam.int64(value));
    }

    static Stream<Arguments> provideInt64Arguments() {
        return Stream.of(
                Arguments.of(Long.MIN_VALUE),
                Arguments.of(Long.MAX_VALUE),
                Arguments.of(0L)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInt128Arguments")
    public void testInt128Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int128(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int128(value));
        }
    }

    static Stream<Arguments> provideInt128Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(127).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(127).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(127).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(127), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideUint128Arguments")
    public void testUint128Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint128(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint128(value));
        }
    }

    static Stream<Arguments> provideUint128Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(128).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(128), false)
        );
    }
}
