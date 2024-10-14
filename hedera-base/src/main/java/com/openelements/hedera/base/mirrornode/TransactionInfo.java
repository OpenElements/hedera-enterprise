package com.openelements.hedera.base.mirrornode;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.Nullable;

public record  TransactionInfo(    @Nullable Byte bytes,
                                    long chargedTxFee,
                                    String consensusTimestamp,
                                    String entityId,
                                    String maxFee,
                                    String memoBase64,
                                    String name,
                                    List<NftTransfers>  nftTransfers,
                                    String node,
                                    int nonce,
                                    String parentConsensusTimestamp,
                                    String result,
                                    boolean scheduled,
                                    @Nullable List<Object> stakingRewardTransfers,
                                    @Nullable List<Object> tokenTransfers,
                                    String transactionHash,
                                    String transactionId,
                                    List<Transfers> transfers,
                                    String validDurationSeconds,
                                    String validStartTimestamp
                        ) {

    @JsonCreator
    public TransactionInfo(
            @JsonProperty("bytes") @Nullable Byte bytes,
            @JsonProperty("charged_tx_fee") long chargedTxFee,
            @JsonProperty("consensus_timestamp") String consensusTimestamp,
            @JsonProperty("entity_id") String entityId,
            @JsonProperty("max_fee") String maxFee,
            @JsonProperty("memo_base64") String memoBase64,
            @JsonProperty("name") String name,
            @JsonProperty("nft_transfers") List<NftTransfers> nftTransfers,
            @JsonProperty("node") String node,
            @JsonProperty("nonce") int nonce,
            @JsonProperty("parent_consensus_timestamp") String parentConsensusTimestamp,
            @JsonProperty("result") String result,
            @JsonProperty("scheduled") boolean scheduled,
            @JsonProperty("staking_reward_transfers") @Nullable List<Object> stakingRewardTransfers,
            @JsonProperty("token_transfers") @Nullable List<Object> tokenTransfers,
            @JsonProperty("transaction_hash") String transactionHash,
            @JsonProperty("transaction_id") String transactionId,
            @JsonProperty("transfers") List<Transfers> transfers,
            @JsonProperty("valid_duration_seconds") String validDurationSeconds,
            @JsonProperty("valid_start_timestamp") String validStartTimestamp
            ){
                    this.bytes= bytes;
                    this.chargedTxFee = chargedTxFee;
                    this.consensusTimestamp= consensusTimestamp;
                    this.entityId= entityId;
                    this.maxFee= maxFee;
                    this.memoBase64= memoBase64;
                    this.name= name;
                    this.nftTransfers= nftTransfers;
                    this.node= node;
                    this.nonce= nonce;
                    this.parentConsensusTimestamp= parentConsensusTimestamp;
                    this.result= result;
                    this.scheduled= scheduled;
                    this.stakingRewardTransfers= stakingRewardTransfers;
                    this.tokenTransfers= tokenTransfers;
                    this.transactionHash= transactionHash;
                    this.transactionId= transactionId;
                    this.transfers= transfers;
                    this.validDurationSeconds= validDurationSeconds;
                    this.validStartTimestamp= validStartTimestamp;

        Objects.requireNonNull(transactionId, "transactionId must not be null");
    }
}

class NftTransfers{
    private final boolean isApproval;
    private final String receiverAccountId;
    private final String senderAccountId;
    private final int serialNumber;
    private final String tokenId;

    @JsonCreator
    public NftTransfers(    @JsonProperty("is_approval") boolean isApproval,
                            @JsonProperty("receiver_account_id") String receiverAccountId,
                            @JsonProperty("sender_account_id") @Nullable String senderAccountId,
                            @JsonProperty("serial_number") int serialNumber,
                            @JsonProperty("token_id") String tokenId
                        ) {
        this.isApproval = isApproval;
        this.receiverAccountId = receiverAccountId;
        this.senderAccountId = senderAccountId;
        this.serialNumber = serialNumber;
        this.tokenId = tokenId;
    }
}

class Transfers{
    private final String account;
    private final long amount;
    private final boolean isApproved;

    @JsonCreator
    public Transfers(   @JsonProperty("account") String account,
                        @JsonProperty("amount") long amount,
                        @JsonProperty("is_approved") boolean isApproved
                    ) {
        this.account = account;
        this.amount = amount;
        this.isApproved = isApproved;
    }
}
