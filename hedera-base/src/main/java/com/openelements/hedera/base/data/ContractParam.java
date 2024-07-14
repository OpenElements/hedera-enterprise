package com.openelements.hedera.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hedera.base.implementation.data.BigIntegerBasedNumericDatatypes;
import com.openelements.hedera.base.implementation.data.BooleanDatatype;
import com.openelements.hedera.base.implementation.data.LongBasedNumericDatatypes;
import com.openelements.hedera.base.implementation.data.ParamSupplier;
import com.openelements.hedera.base.implementation.data.StringBasedDatatype;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.math.BigInteger;
import java.util.Objects;

public record ContractParam<T>(@NonNull T value, @NonNull String nativeType, @NonNull ParamSupplier<T> supplier) {

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

    @NonNull
    public static ContractParam<String> string(String value) {
        return of(value, StringBasedDatatype.STRING);
    }

    @NonNull
    public static ContractParam<String> address(String value) {
        return of(value, StringBasedDatatype.ADDRESS);
    }

    @NonNull
    public static ContractParam<String> address(@NonNull AccountId value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value.toString(), StringBasedDatatype.ADDRESS);
    }

    @NonNull
    public static ContractParam<String> address(@NonNull ContractId value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value.toString(), StringBasedDatatype.ADDRESS);
    }

    @NonNull
    public static ContractParam<Boolean> bool(boolean value) {
        return of(value, BooleanDatatype.BOOL);
    }

    @NonNull
    public static ContractParam<Long> int8(byte value) {
        return of((long) value, LongBasedNumericDatatypes.INT8);
    }

    @NonNull
    public static ContractParam<Long> uint8(short value) {
        return of((long) value, LongBasedNumericDatatypes.UINT8);
    }

    @NonNull
    public static ContractParam<Long> int16(short value) {
        return of((long) value, LongBasedNumericDatatypes.INT16);
    }

    @NonNull
    public static ContractParam<Long> uint16(int value) {
        return of((long) value, LongBasedNumericDatatypes.UINT16);
    }

    @NonNull
    public static ContractParam<Long> int32(int value) {
        return of((long) value, LongBasedNumericDatatypes.INT32);
    }

    @NonNull
    public static ContractParam<Long> uint32(long value) {
        return of(value, LongBasedNumericDatatypes.UINT32);
    }

    @NonNull
    public static ContractParam<Long> int40(long value) {
        return of(value, LongBasedNumericDatatypes.INT40);
    }

    @NonNull
    public static ContractParam<Long> uint40(long value) {
        return of(value, LongBasedNumericDatatypes.UINT40);
    }

    @NonNull
    public static ContractParam<Long> int48(long value) {
        return of(value, LongBasedNumericDatatypes.INT48);
    }

    @NonNull
    public static ContractParam<Long> uint48(long value) {
        return of(value, LongBasedNumericDatatypes.UINT48);
    }

    @NonNull
    public static ContractParam<Long> int56(long value) {
        return of(value, LongBasedNumericDatatypes.INT56);
    }

    @NonNull
    public static ContractParam<Long> uint56(long value) {
        return of(value, LongBasedNumericDatatypes.UINT56);
    }

    @NonNull
    public static ContractParam<Long> int64(long value) {
        return of(value, LongBasedNumericDatatypes.INT64);
    }

    @NonNull
    public static ContractParam<BigInteger> int72(long value) {
        return int72(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int72(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT72);
    }

    public static ContractParam<BigInteger> uint72(long value) {
        return uint72(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint72(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT72);
    }

    @NonNull
    public static ContractParam<BigInteger> int80(long value) {
        return int80(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int80(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT80);
    }

    @NonNull
    public static ContractParam<BigInteger> uint80(long value) {
        return uint80(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint80(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT80);
    }

    @NonNull
    public static ContractParam<BigInteger> int88(long value) {
        return int88(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int88(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT88);
    }

    @NonNull
    public static ContractParam<BigInteger> uint88(long value) {
        return uint88(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint88(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT88);
    }


    @NonNull
    public static ContractParam<BigInteger> int96(long value) {
        return int96(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int96(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT96);
    }

    @NonNull
    public static ContractParam<BigInteger> uint96(long value) {
        return uint96(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint96(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT96);
    }

    @NonNull
    public static ContractParam<BigInteger> int104(long value) {
        return int104(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int104(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT104);
    }

    @NonNull
    public static ContractParam<BigInteger> uint104(long value) {
        return uint104(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint104(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT104);
    }

    @NonNull
    public static ContractParam<BigInteger> int112(long value) {
        return int112(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int112(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT112);
    }

    @NonNull
    public static ContractParam<BigInteger> uint112(long value) {
        return uint112(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint112(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT112);
    }

    @NonNull
    public static ContractParam<BigInteger> int120(long value) {
        return int120(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int120(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT120);
    }

    @NonNull
    public static ContractParam<BigInteger> uint120(long value) {
        return uint120(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint120(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT120);
    }

    @NonNull
    public static ContractParam<BigInteger> int128(long value) {
        return int128(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int128(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT128);
    }

    @NonNull
    public static ContractParam<BigInteger> uint128(long value) {
        return uint128(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint128(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT128);
    }

    @NonNull
    public static ContractParam<BigInteger> int136(long value) {
        return int136(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int136(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT136);
    }

    @NonNull
    public static ContractParam<BigInteger> uint136(long value) {
        return uint136(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint136(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT136);
    }

    @NonNull
    public static ContractParam<BigInteger> int144(long value) {
        return int144(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int144(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT144);
    }

    @NonNull
    public static ContractParam<BigInteger> uint144(long value) {
        return uint144(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint144(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT144);
    }

    @NonNull
    public static ContractParam<BigInteger> int152(long value) {
        return int152(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int152(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT152);
    }

    @NonNull
    public static ContractParam<BigInteger> uint152(long value) {
        return uint152(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint152(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT152);
    }

    @NonNull
    public static ContractParam<BigInteger> int160(long value) {
        return int160(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int160(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT160);
    }

    @NonNull
    public static ContractParam<BigInteger> uint160(long value) {
        return uint160(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint160(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT160);
    }

    @NonNull
    public static ContractParam<BigInteger> int168(long value) {
        return int168(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int168(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT168);
    }

    @NonNull
    public static ContractParam<BigInteger> uint168(long value) {
        return uint168(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint168(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT168);
    }

    @NonNull
    public static ContractParam<BigInteger> int176(long value) {
        return int176(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int176(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT176);
    }

    @NonNull
    public static ContractParam<BigInteger> uint176(long value) {
        return uint176(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint176(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT176);
    }

    @NonNull
    public static ContractParam<BigInteger> int184(long value) {
        return int184(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int184(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT184);
    }

    @NonNull
    public static ContractParam<BigInteger> uint184(long value) {
        return uint184(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint184(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT184);
    }

    @NonNull
    public static ContractParam<BigInteger> int192(long value) {
        return int192(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int192(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT192);
    }

    @NonNull
    public static ContractParam<BigInteger> uint192(long value) {
        return uint192(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint192(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT192);
    }

    @NonNull
    public static ContractParam<BigInteger> int200(long value) {
        return int200(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int200(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT200);
    }

    @NonNull
    public static ContractParam<BigInteger> uint200(long value) {
        return uint200(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint200(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT200);
    }

    @NonNull
    public static ContractParam<BigInteger> int208(long value) {
        return int208(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int208(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT208);
    }

    @NonNull
    public static ContractParam<BigInteger> uint208(long value) {
        return uint208(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint208(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT208);
    }

    @NonNull
    public static ContractParam<BigInteger> int216(long value) {
        return int216(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int216(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT216);
    }

    @NonNull
    public static ContractParam<BigInteger> uint216(long value) {
        return uint216(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint216(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT216);
    }

    @NonNull
    public static ContractParam<BigInteger> int224(long value) {
        return int224(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int224(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT224);
    }

    @NonNull
    public static ContractParam<BigInteger> uint224(long value) {
        return uint224(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint224(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT224);
    }

    @NonNull
    public static ContractParam<BigInteger> int232(long value) {
        return int232(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int232(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT232);
    }

    @NonNull
    public static ContractParam<BigInteger> uint232(long value) {
        return uint232(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint232(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT232);
    }

    @NonNull
    public static ContractParam<BigInteger> int240(long value) {
        return int240(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int240(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT240);
    }

    @NonNull
    public static ContractParam<BigInteger> uint240(long value) {
        return uint240(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint240(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT240);
    }

    @NonNull
    public static ContractParam<BigInteger> int248(long value) {
        return int248(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> int248(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT248);
    }

    @NonNull
    public static ContractParam<BigInteger> uint248(long value) {
        return uint248(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint248(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT248);
    }

    @NonNull
    public static ContractParam<BigInteger> int256(@NonNull BigInteger value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value, BigIntegerBasedNumericDatatypes.INT256);
    }

    @NonNull
    public static ContractParam<BigInteger> int256(long value) {
        return int256(BigInteger.valueOf(value));
    }

    @NonNull
    public static ContractParam<BigInteger> uint256(@NonNull BigInteger value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value, BigIntegerBasedNumericDatatypes.UINT256);
    }

    @NonNull
    public static ContractParam<BigInteger> uint256(long value) {
        return uint256(BigInteger.valueOf(value));
    }
}
