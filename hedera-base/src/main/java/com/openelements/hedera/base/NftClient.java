package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Key;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.stream.Stream;

public interface NftClient {

    @NonNull
    TokenId createNftType(String name, String symbol) throws HederaException;

    @NonNull
    TokenId createNftType(String name, String symbol, PrivateKey supplierKey) throws HederaException;

    @NonNull
    TokenId createNftType(String name, String symbol, AccountId treasuryAccountId, PrivateKey treasuryKey) throws HederaException;

    @NonNull
    TokenId createNftType(String name, String symbol, AccountId treasuryAccountId, PrivateKey treasuryKey, PrivateKey supplierKey) throws HederaException;

    void associateNft(TokenId tokenId, AccountId accountId, PrivateKey accountKey) throws HederaException;

    long mintNft(TokenId tokenId, String metadata) throws HederaException;

    long mintNft(TokenId tokenId, String metadata, PrivateKey supplyKey) throws HederaException;

    @NonNull
    List<Long> mintNfts(TokenId tokenId, List<String> metadata) throws HederaException;

    @NonNull
    List<Long> mintNfts(TokenId tokenId, List<String> metadata, PrivateKey supplyKey) throws HederaException;

    void transferNft(TokenId tokenId, long serialNumber, AccountId fromAccountId, PrivateKey fromAccountKey, AccountId toAccountId) throws HederaException;

    void transferNfts(TokenId tokenId, List<Long> serialNumber, AccountId fromAccountId, PrivateKey fromAccountKey, AccountId toAccountId) throws HederaException;

}
