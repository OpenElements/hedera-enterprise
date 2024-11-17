package com.openelements.hiero.base.mirrornode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record TransactionInfo(
        @Nullable Byte bytes,
        long chargedTxFee,
        String consensusTimestamp,
        String entityId,
        String maxFee,
        String memoBase64,
        String name,
        List<NftTransfers> nftTransfers,
        String node,
        int nonce,
        String parentConsensusTimestamp,
        String result,
        boolean scheduled,
        @Nullable List<Object> stakingRewardTransfers,
        @Nullable List<Object> tokenTransfers,
        String transactionHash,
        @NonNull String transactionId,
        List<Transfers> transfers,
        String validDurationSeconds,
        String validStartTimestamp
) {


    public TransactionInfo {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
    }
    public TransactionInfo(@NonNull String transactionId) {
        this(null,
                0L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                null,
                null,
                false,
                null,
                null,
                null,
                transactionId,
                null,
                null,
                null
        );
    }
    public static TransactionInfo fromJson(JSONObject json) throws JSONException {
        Byte bytes = json.has("bytes") ? (byte) json.getInt("bytes") : null;
        long chargedTxFee = json.getLong("charged_tx_fee");
        String consensusTimestamp = json.getString("consensus_timestamp");
        String entityId = json.getString("entity_id");
        String maxFee = json.getString("max_fee");
        String memoBase64 = json.getString("memo_base64");
        String name = json.getString("name");

        List<NftTransfers> nftTransfers = parseNftTransfers(json.getJSONArray("nft_transfers"));
        List<Transfers> transfers = parseTransfers(json.getJSONArray("transfers"));

        String node = json.getString("node");
        int nonce = json.getInt("nonce");
        String parentConsensusTimestamp = json.getString("parent_consensus_timestamp");
        String result = json.getString("result");
        boolean scheduled = json.getBoolean("scheduled");

        // stakingRewardTransfers might be null if "staking_reward_transfers" is absent in the JSON.
        List<Object> stakingRewardTransfers = json.has("staking_reward_transfers")
                ? fromJsonToArray(json.getJSONArray("staking_reward_transfers")) : null;
        // tokenTransfers might be null if "token_transfers" is absent in the JSON.
        List<Object> tokenTransfers = json.has("token_transfers")
                ? fromJsonToArray(json.getJSONArray("token_transfers")) : null;

        String transactionHash = json.getString("transaction_hash");
        // transactionId can never be null
        String transactionId = json.getString("transaction_id");
        String validDurationSeconds = json.getString("valid_duration_seconds");
        String validStartTimestamp = json.getString("valid_start_timestamp");

        return new TransactionInfo(
                bytes, chargedTxFee, consensusTimestamp, entityId, maxFee, memoBase64, name,
                nftTransfers, node, nonce, parentConsensusTimestamp, result, scheduled,
                stakingRewardTransfers, tokenTransfers, transactionHash, transactionId,
                transfers, validDurationSeconds, validStartTimestamp
        );
    }

    private static List<Object> fromJsonToArray(JSONArray jsonArray) throws JSONException {
        List<Object> lists = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            lists.add(jsonArray.getJSONArray(i));
        }
        return lists;
    }

    private static List<NftTransfers> parseNftTransfers(JSONArray jsonArray) throws JSONException {
        List<NftTransfers> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(NftTransfers.fromJson(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    private static List<Transfers> parseTransfers(JSONArray jsonArray) throws JSONException {
        List<Transfers> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(Transfers.fromJson(jsonArray.getJSONObject(i)));
        }
        return list;
    }

   static class NftTransfers {
        private final boolean isApproval;
        private final String receiverAccountId;
        private final String senderAccountId;
        private final int serialNumber;
        private final String tokenId;

        public NftTransfers(boolean isApproval, String receiverAccountId, String senderAccountId, int serialNumber, String tokenId) {
            this.isApproval = isApproval;
            this.receiverAccountId = receiverAccountId;
            this.senderAccountId = senderAccountId;
            this.serialNumber = serialNumber;
            this.tokenId = tokenId;
        }

        public static NftTransfers fromJson(JSONObject json) throws JSONException {
            boolean isApproval = json.getBoolean("is_approval");
            String receiverAccountId = json.getString("receiver_account_id");
            String senderAccountId = json.optString("sender_account_id", null);
            int serialNumber = json.getInt("serial_number");
            String tokenId = json.getString("token_id");
            return new NftTransfers(isApproval, receiverAccountId, senderAccountId, serialNumber, tokenId);
        }
    }


    static class Transfers {
        private final String account;
        private final long amount;
        private final boolean isApproved;

        public Transfers(String account, long amount, boolean isApproved) {
            this.account = account;
            this.amount = amount;
            this.isApproved = isApproved;
        }


        public static Transfers fromJson(JSONObject jsonObject) throws JSONException {
            String account = jsonObject.getString("account");
            long amount = jsonObject.getLong("amount");
            boolean isApproved = jsonObject.getBoolean("is_approved");
            return new Transfers(account, amount, isApproved);
        }


    }

}

