package com.openelements.hedera.base.protocol;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.Hbar;
import java.time.Instant;

public interface TransactionRecord extends TransactionResult{

    ByteString transactionHash();

    Instant consensusTimestamp();

    Hbar transactionFee();
}
