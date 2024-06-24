package com.openelements.spring.hedera.api.protocol;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;

public interface TransactionRecord extends TransactionResult{

    ByteString transactionHash();

    Instant consensusTimestamp();

    Hbar transactionFee();
}
