package com.openelements.hedera.base.protocol;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TransactionId;
import java.time.Instant;

public record AccountCreateResult(TransactionId transactionId, Status status, ByteString transactionHash, Instant consensusTimestamp, Hbar transactionFee, AccountId accountId, PublicKey publicKey, PrivateKey privateKey) implements TransactionRecord {

}
