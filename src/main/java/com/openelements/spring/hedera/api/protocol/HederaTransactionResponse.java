package com.openelements.spring.hedera.api.protocol;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TransactionId;

/**
 * Represents the response of a transaction.
 * @param nodeId The node ID.
 * @param transactionId The transaction ID.
 * @param transactionHash The transaction hash.
 */
public record HederaTransactionResponse(AccountId nodeId, TransactionId transactionId, byte[] transactionHash) {
}
