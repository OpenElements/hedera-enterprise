package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.Hbar;
import java.math.BigInteger;

public interface ContractCallResult {

    long gasUsed();

    Hbar cost();

    String getString(int index);

    String getAddress(int index);

    boolean getBool(int index);

    byte getInt8(int index);


    int getInt32(int index);

    long getInt64(int index);

    BigInteger getInt256(int index);

    long getUint8(int index);


    long getUint32(int index);

    long getUint64(int index);

    BigInteger getUint256(int index);

}
