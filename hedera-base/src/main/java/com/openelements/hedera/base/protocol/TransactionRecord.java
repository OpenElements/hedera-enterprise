package com.openelements.hedera.base.protocol;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;

public interface TransactionRecord extends TransactionResult{

    TransactionId transactionId();

    Status status();

    ByteString transactionHash();

    Instant consensusTimestamp();

    Hbar transactionFee();
}
