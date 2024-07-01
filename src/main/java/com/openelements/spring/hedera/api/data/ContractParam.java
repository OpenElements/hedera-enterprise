package com.openelements.spring.hedera.api.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.spring.hedera.implementation.data.BigIntegerBasedNumericDatatypes;
import com.openelements.spring.hedera.implementation.data.BooleanDatatype;
import com.openelements.spring.hedera.implementation.data.LongBasedNumericDatatypes;
import com.openelements.spring.hedera.implementation.data.ParamSupplier;
import com.openelements.spring.hedera.implementation.data.StringBasedDatatype;
import jakarta.annotation.Nonnull;
import java.math.BigInteger;
import java.util.Objects;

public record ContractParam<T>(@Nonnull T value, @Nonnull String nativeType, @Nonnull ParamSupplier<T> supplier) {

    public ContractParam {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(nativeType, "nativeType must not be null");
        Objects.requireNonNull(supplier, "supplier must not be null");
        if(!supplier.isValidParam(value)) {
            throw new IllegalArgumentException("value '" + value + "' is not valid for native type '" + nativeType + "'");
        }
    }

    private static <T> ContractParam<T> of(T value, ParamSupplier<T> paramSupplier) {
        Objects.requireNonNull(paramSupplier, "paramSupplier must not be null");
        return new ContractParam<>(value, paramSupplier.getNativeType(), paramSupplier);
    }

    @Nonnull
    public static ContractParam<String> string(String value) {
        return of(value, StringBasedDatatype.STRING);
    }

    @Nonnull
    public static ContractParam<String> address(String value) {
        return of(value, StringBasedDatatype.ADDRESS);
    }

    @Nonnull
    public static ContractParam<String> address(@Nonnull AccountId value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value.toString(), StringBasedDatatype.ADDRESS);
    }

    @Nonnull
    public static ContractParam<String> address(@Nonnull ContractId value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value.toString(), StringBasedDatatype.ADDRESS);
    }

    @Nonnull
    public static ContractParam<Boolean> bool(boolean value) {
        return of(value, BooleanDatatype.BOOL);
    }

    @Nonnull
    public static ContractParam<Long> int8(byte value) {
        return of((long) value, LongBasedNumericDatatypes.INT8);
    }

    @Nonnull
    public static ContractParam<Long> uint8(short value) {
        return of((long) value, LongBasedNumericDatatypes.UINT8);
    }

    @Nonnull
    public static ContractParam<Long> int16(short value) {
        return of((long) value, LongBasedNumericDatatypes.INT8);
    }

    @Nonnull
    public static ContractParam<Long> uint16(int value) {
        return of((long) value, LongBasedNumericDatatypes.UINT8);
    }

    @Nonnull
    public static ContractParam<Long> int32(int value) {
        return of((long) value, LongBasedNumericDatatypes.INT32);
    }

    @Nonnull
    public static ContractParam<Long> uint32(long value) {
        return of(value, LongBasedNumericDatatypes.UINT32);
    }

    @Nonnull
    public static ContractParam<Long> int40(long value) {
        return of(value, LongBasedNumericDatatypes.INT40);
    }

    @Nonnull
    public static ContractParam<Long> uint40(long value) {
        return of(value, LongBasedNumericDatatypes.UINT40);
    }

    @Nonnull
    public static ContractParam<Long> int48(long value) {
        return of(value, LongBasedNumericDatatypes.INT48);
    }

    @Nonnull
    public static ContractParam<Long> uint48(long value) {
        return of(value, LongBasedNumericDatatypes.UINT48);
    }

    @Nonnull
    public static ContractParam<Long> int56(long value) {
        return of(value, LongBasedNumericDatatypes.INT56);
    }

    @Nonnull
    public static ContractParam<Long> uint56(long value) {
        return of(value, LongBasedNumericDatatypes.UINT56);
    }

    @Nonnull
    public static ContractParam<Long> int64(long value) {
        return of(value, LongBasedNumericDatatypes.INT64);
    }

    @Nonnull
    public static ContractParam<BigInteger> int72(long value) {
        return int72(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int72(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT72);
    }

    public static ContractParam<BigInteger> uint72(long value) {
        return uint72(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint72(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT72);
    }

    @Nonnull
    public static ContractParam<BigInteger> int80(long value) {
        return int80(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int80(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT80);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint80(long value) {
        return uint80(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint80(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT80);
    }

    @Nonnull
    public static ContractParam<BigInteger> int88(long value) {
        return int88(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int88(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT88);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint88(long value) {
        return uint88(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint88(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT88);
    }


    @Nonnull
    public static ContractParam<BigInteger> int96(long value) {
        return int96(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int96(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT96);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint96(long value) {
        return uint96(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint96(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT96);
    }

    @Nonnull
    public static ContractParam<BigInteger> int104(long value) {
        return int104(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int104(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT104);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint104(long value) {
        return uint104(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint104(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT104);
    }

    @Nonnull
    public static ContractParam<BigInteger> int112(long value) {
        return int112(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int112(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT112);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint112(long value) {
        return uint112(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint112(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT112);
    }

    @Nonnull
    public static ContractParam<BigInteger> int120(long value) {
        return int120(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int120(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT120);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint120(long value) {
        return uint120(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint120(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT120);
    }

    @Nonnull
    public static ContractParam<BigInteger> int128(long value) {
        return int128(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int128(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT128);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint128(long value) {
        return uint128(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint128(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT128);
    }

    @Nonnull
    public static ContractParam<BigInteger> int136(long value) {
        return int136(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int136(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT136);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint136(long value) {
        return uint136(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint136(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT136);
    }

    @Nonnull
    public static ContractParam<BigInteger> int144(long value) {
        return int144(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int144(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT144);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint144(long value) {
        return uint144(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint144(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT144);
    }

    @Nonnull
    public static ContractParam<BigInteger> int152(long value) {
        return int152(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int152(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT152);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint152(long value) {
        return uint152(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint152(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT152);
    }

    @Nonnull
    public static ContractParam<BigInteger> int160(long value) {
        return int160(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int160(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT160);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint160(long value) {
        return uint160(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint160(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT160);
    }

    @Nonnull
    public static ContractParam<BigInteger> int168(long value) {
        return int168(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int168(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT168);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint168(long value) {
        return uint168(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint168(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT168);
    }

    @Nonnull
    public static ContractParam<BigInteger> int176(long value) {
        return int176(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int176(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT176);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint176(long value) {
        return uint176(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint176(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT176);
    }

    @Nonnull
    public static ContractParam<BigInteger> int184(long value) {
        return int184(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int184(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT184);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint184(long value) {
        return uint184(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint184(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT184);
    }

    @Nonnull
    public static ContractParam<BigInteger> int192(long value) {
        return int192(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int192(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT192);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint192(long value) {
        return uint192(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint192(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT192);
    }

    @Nonnull
    public static ContractParam<BigInteger> int200(long value) {
        return int200(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int200(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT200);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint200(long value) {
        return uint200(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint200(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT200);
    }

    @Nonnull
    public static ContractParam<BigInteger> int208(long value) {
        return int208(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int208(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT208);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint208(long value) {
        return uint208(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint208(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT208);
    }

    @Nonnull
    public static ContractParam<BigInteger> int216(long value) {
        return int216(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int216(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT216);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint216(long value) {
        return uint216(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint216(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT216);
    }

    @Nonnull
    public static ContractParam<BigInteger> int224(long value) {
        return int224(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int224(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT224);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint224(long value) {
        return uint224(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint224(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT224);
    }

    @Nonnull
    public static ContractParam<BigInteger> int232(long value) {
        return int232(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int232(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT232);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint232(long value) {
        return uint232(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint232(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT232);
    }

    @Nonnull
    public static ContractParam<BigInteger> int240(long value) {
        return int240(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int240(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT240);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint240(long value) {
        return uint240(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint240(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT240);
    }

    @Nonnull
    public static ContractParam<BigInteger> int248(long value) {
        return int248(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> int248(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT248);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint248(long value) {
        return uint248(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint248(@Nonnull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT248);
    }

    @Nonnull
    public static ContractParam<BigInteger> int256(@Nonnull BigInteger value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value, BigIntegerBasedNumericDatatypes.INT256);
    }

    @Nonnull
    public static ContractParam<BigInteger> int256(long value) {
        return int256(BigInteger.valueOf(value));
    }

    @Nonnull
    public static ContractParam<BigInteger> uint256(@Nonnull BigInteger value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value, BigIntegerBasedNumericDatatypes.UINT256);
    }

    @Nonnull
    public static ContractParam<BigInteger> uint256(long value) {
        return uint256(BigInteger.valueOf(value));
    }
}
