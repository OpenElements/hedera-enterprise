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
    @MethodSource("providedIntInt72Arguments")
    public void testInt72Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int72(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int72(value));
        }
    }
    static Stream<Arguments> providedIntInt72Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(71).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(71).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(71).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(71), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt72Arguments")
    public void testUint72Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint72(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint72(value));
        }
    }
    static Stream<Arguments> providedUintInt72Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(72).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(72), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt80Arguments")
    public void testInt80Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int80(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int80(value));
        }
    }
    static Stream<Arguments> providedIntInt80Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(79).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(79).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(79).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(79), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt80Arguments")
    public void testUint80Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint80(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint80(value));
        }
    }
    static Stream<Arguments> providedUintInt80Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(80).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(80), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt88Arguments")
    public void testInt88Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int88(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int88(value));
        }
    }
    static Stream<Arguments> providedIntInt88Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(87).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(87).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(87).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(87), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt88Arguments")
    public void testUint88Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint88(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint88(value));
        }
    }
    static Stream<Arguments> providedUintInt88Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(88).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(88), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt96Arguments")
    public void testInt96Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int96(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int96(value));
        }
    }
    static Stream<Arguments> providedIntInt96Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(95).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(95).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(95).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(95), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt96Arguments")
    public void testUint96Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint96(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint96(value));
        }
    }
    static Stream<Arguments> providedUintInt96Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(96).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(96), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt104Arguments")
    public void testInt104Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int104(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int104(value));
        }
    }
    static Stream<Arguments> providedIntInt104Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(103).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(103).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(103).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(103), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt104Arguments")
    public void testUint104Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint104(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint104(value));
        }
    }
    static Stream<Arguments> providedUintInt104Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(104).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(104), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt112Arguments")
    public void testInt112Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int112(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int112(value));
        }
    }
    static Stream<Arguments> providedIntInt112Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(111).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(111).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(111).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(111), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt112Arguments")
    public void testUint112Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint112(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint112(value));
        }
    }
    static Stream<Arguments> providedUintInt112Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(112).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(112), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt120Arguments")
    public void testInt120Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int120(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int120(value));
        }
    }
    static Stream<Arguments> providedIntInt120Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(119).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(119).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(119).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(119), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt120Arguments")
    public void testUint120Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint120(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint120(value));
        }
    }
    static Stream<Arguments> providedUintInt120Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(120).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(120), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt128Arguments")
    public void testInt128Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int128(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int128(value));
        }
    }
    static Stream<Arguments> providedIntInt128Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(127).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(127).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(127).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(127), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt128Arguments")
    public void testUint128Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint128(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint128(value));
        }
    }
    static Stream<Arguments> providedUintInt128Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(128).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(128), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt136Arguments")
    public void testInt136Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int136(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int136(value));
        }
    }
    static Stream<Arguments> providedIntInt136Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(135).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(135).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(135).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(135), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt136Arguments")
    public void testUint136Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint136(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint136(value));
        }
    }
    static Stream<Arguments> providedUintInt136Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(136).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(136), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt144Arguments")
    public void testInt144Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int144(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int144(value));
        }
    }
    static Stream<Arguments> providedIntInt144Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(143).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(143).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(143).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(143), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt144Arguments")
    public void testUint144Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint144(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint144(value));
        }
    }
    static Stream<Arguments> providedUintInt144Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(144).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(144), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt152Arguments")
    public void testInt152Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int152(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int152(value));
        }
    }
    static Stream<Arguments> providedIntInt152Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(151).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(151).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(151).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(151), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt152Arguments")
    public void testUint152Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint152(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint152(value));
        }
    }
    static Stream<Arguments> providedUintInt152Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(152).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(152), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt160Arguments")
    public void testInt160Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int160(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int160(value));
        }
    }
    static Stream<Arguments> providedIntInt160Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(159).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(159).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(159).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(159), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt160Arguments")
    public void testUint160Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint160(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint160(value));
        }
    }
    static Stream<Arguments> providedUintInt160Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(160).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(160), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt168Arguments")
    public void testInt168Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int168(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int168(value));
        }
    }
    static Stream<Arguments> providedIntInt168Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(167).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(167).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(167).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(167), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt168Arguments")
    public void testUint168Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint168(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint168(value));
        }
    }
    static Stream<Arguments> providedUintInt168Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(168).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(168), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt176Arguments")
    public void testInt176Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int176(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int176(value));
        }
    }
    static Stream<Arguments> providedIntInt176Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(175).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(175).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(175).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(175), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt176Arguments")
    public void testUint176Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint176(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint176(value));
        }
    }
    static Stream<Arguments> providedUintInt176Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(176).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(176), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt184Arguments")
    public void testInt184Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int184(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int184(value));
        }
    }
    static Stream<Arguments> providedIntInt184Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(183).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(183).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(183).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(183), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt184Arguments")
    public void testUint184Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint184(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint184(value));
        }
    }
    static Stream<Arguments> providedUintInt184Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(184).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(184), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt192Arguments")
    public void testInt192Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int192(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int192(value));
        }
    }
    static Stream<Arguments> providedIntInt192Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(191).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(191).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(191).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(191), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt192Arguments")
    public void testUint192Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint192(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint192(value));
        }
    }
    static Stream<Arguments> providedUintInt192Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(192).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(192), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt200Arguments")
    public void testInt200Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int200(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int200(value));
        }
    }
    static Stream<Arguments> providedIntInt200Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(199).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(199).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(199).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(199), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt200Arguments")
    public void testUint200Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint200(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint200(value));
        }
    }
    static Stream<Arguments> providedUintInt200Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(200).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(200), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt208Arguments")
    public void testInt208Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int208(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int208(value));
        }
    }
    static Stream<Arguments> providedIntInt208Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(207).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(207).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(207).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(207), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt208Arguments")
    public void testUint208Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint208(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint208(value));
        }
    }
    static Stream<Arguments> providedUintInt208Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(208).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(208), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt216Arguments")
    public void testInt216Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int216(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int216(value));
        }
    }
    static Stream<Arguments> providedIntInt216Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(215).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(215).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(215).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(215), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt216Arguments")
    public void testUint216Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint216(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint216(value));
        }
    }
    static Stream<Arguments> providedUintInt216Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(216).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(216), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt224Arguments")
    public void testInt224Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int224(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int224(value));
        }
    }
    static Stream<Arguments> providedIntInt224Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(223).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(223).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(223).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(223), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt224Arguments")
    public void testUint224Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint224(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint224(value));
        }
    }
    static Stream<Arguments> providedUintInt224Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(224).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(224), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt232Arguments")
    public void testInt232Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int232(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int232(value));
        }
    }
    static Stream<Arguments> providedIntInt232Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(231).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(231).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(231).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(231), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt232Arguments")
    public void testUint232Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint232(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint232(value));
        }
    }
    static Stream<Arguments> providedUintInt232Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(232).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(232), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt240Arguments")
    public void testInt240Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int240(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int240(value));
        }
    }
    static Stream<Arguments> providedIntInt240Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(239).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(239).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(239).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(239), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt240Arguments")
    public void testUint240Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint240(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint240(value));
        }
    }
    static Stream<Arguments> providedUintInt240Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(240).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(240), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt248Arguments")
    public void testInt248Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int248(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int248(value));
        }
    }
    static Stream<Arguments> providedIntInt248Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(247).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(247).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(247).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(247), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt248Arguments")
    public void testUint248Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint248(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint248(value));
        }
    }
    static Stream<Arguments> providedUintInt248Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(248).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(248), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedIntInt256Arguments")
    public void testInt256Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.int256(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.int256(value));
        }
    }
    static Stream<Arguments> providedIntInt256Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.valueOf(2).pow(255).negate(), true),
                Arguments.of(BigInteger.valueOf(2).pow(255).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(2).pow(255).negate().subtract(BigInteger.ONE), false),
                Arguments.of(BigInteger.valueOf(2).pow(255), false)
        );
    }

    @ParameterizedTest
    @MethodSource("providedUintInt256Arguments")
    public void testUint256Range(BigInteger value, boolean shouldPass) {
        if (shouldPass) {
            Assertions.assertDoesNotThrow(() -> ContractParam.uint256(value));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> ContractParam.uint256(value));
        }
    }
    static Stream<Arguments> providedUintInt256Arguments() {
        return Stream.of(
                Arguments.of(BigInteger.ZERO, true),
                Arguments.of(BigInteger.valueOf(2).pow(256).subtract(BigInteger.ONE), true),
                Arguments.of(BigInteger.valueOf(-1), false),
                Arguments.of(BigInteger.valueOf(2).pow(256), false)
        );
    }
}
