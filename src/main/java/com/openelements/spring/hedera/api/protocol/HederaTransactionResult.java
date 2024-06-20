package com.openelements.spring.hedera.api.protocol;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.ExchangeRate;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.ScheduleId;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import java.util.List;
import java.util.Optional;

/**
 * Represents the result of a transaction.
 * @param accountId The account ID.
 * @param transactionId The transaction ID.
 * @param status The status of the transaction.
 * @param exchangeRate The exchange rate of the transaction.
 * @param fileId The file ID.
 * @param contractId The contract ID.
 * @param topicId The topic ID.
 * @param tokenId The token ID.
 * @param topicSequenceNumber The topic sequence number.
 * @param topicRunningHash The topic running hash.
 * @param totalSupply The total supply.
 * @param scheduleId The schedule ID.
 * @param scheduledTransactionId The scheduled transaction ID.
 * @param serials The serials.
 * @param duplicates The duplicates.
 * @param children The children.
 */
public record HederaTransactionResult(AccountId accountId,
                                      TransactionId transactionId,
                                      Status status,
                                      ExchangeRate exchangeRate,
                                      FileId fileId,
                                      ContractId contractId,
                                      TopicId topicId,
                                      TokenId tokenId,
                                      Long topicSequenceNumber,
                                      ByteString topicRunningHash,
                                      Long totalSupply,
                                      ScheduleId scheduleId,
                                      TransactionId scheduledTransactionId,
                                      List<Long> serials,
                                      List<TransactionReceipt> duplicates,
                                      List<TransactionReceipt> children) {

    public HederaTransactionResult(AccountId accountId, TransactionId transactionId, Status status, ExchangeRate exchangeRate,
             FileId fileId, ContractId contractId, TopicId topicId, TokenId tokenId,
            Long topicSequenceNumber, ByteString topicRunningHash, Long totalSupply, ScheduleId scheduleId,
            TransactionId scheduledTransactionId, List<Long> serials, List<TransactionReceipt> duplicates,
            List<TransactionReceipt> children) {
        this.transactionId = transactionId;
        this.status = status;
        this.exchangeRate = exchangeRate;
        this.accountId = accountId;
        this.fileId = fileId;
        this.contractId = contractId;
        this.topicId = topicId;
        this.tokenId = tokenId;
        this.topicSequenceNumber = topicSequenceNumber;
        this.topicRunningHash = topicRunningHash;
        this.totalSupply = totalSupply;
        this.scheduleId = scheduleId;
        this.scheduledTransactionId = scheduledTransactionId;
        this.serials = Optional.of(serials).map(List::copyOf).orElse(List.of());
        this.duplicates = Optional.of(duplicates).map(List::copyOf).orElse(List.of());
        this.children = Optional.of(children).map(List::copyOf).orElse(List.of());
    }
}
