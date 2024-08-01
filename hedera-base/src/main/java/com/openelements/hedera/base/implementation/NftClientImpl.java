package com.openelements.hedera.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Key;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import com.openelements.hedera.base.protocol.TokenAssociateRequest;
import com.openelements.hedera.base.protocol.TokenCreateRequest;
import com.openelements.hedera.base.protocol.TokenCreateResult;
import com.openelements.hedera.base.protocol.TokenMintRequest;
import com.openelements.hedera.base.protocol.TokenMintResult;
import com.openelements.hedera.base.protocol.TokenTransferRequest;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NftClientImpl implements NftClient {

    private final ProtocolLayerClient client;

    private final AccountId adminAccount;

    private final PrivateKey adminSupplyKey;

    public NftClientImpl(@NonNull final ProtocolLayerClient client, @NonNull final AccountId adminAccount, @NonNull final PrivateKey adminSupplyKey) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.adminAccount = Objects.requireNonNull(adminAccount, "adminAccount must not be null");
        this.adminSupplyKey = Objects.requireNonNull(adminSupplyKey, "adminSupplyKey must not be null");
    }

    @Override
    public TokenId createNftType(@NonNull final String name, @NonNull final String symbol) throws HederaException{
        return createNftType(name, symbol, adminAccount, adminSupplyKey);
    }


    @Override
    public TokenId createNftType(@NonNull final String name, @NonNull final String symbol, @NonNull final PrivateKey supplierKey) throws HederaException {
        return createNftType(name, symbol, adminAccount, adminSupplyKey, supplierKey);
    }

    @Override
    public TokenId createNftType(@NonNull final String name, @NonNull final String symbol, @NonNull final AccountId treasuryAccountId, @NonNull final PrivateKey treasuryKey) throws HederaException {
        return createNftType(name, symbol, treasuryAccountId, treasuryKey, adminSupplyKey);
    }

    @Override
    public TokenId createNftType(@NonNull final String name, @NonNull final String symbol, @NonNull final AccountId treasuryAccountId, @NonNull final PrivateKey treasuryKey,
            @NonNull final PrivateKey supplierKey) throws HederaException {
        final TokenCreateRequest request = TokenCreateRequest.of(name, symbol, treasuryAccountId, treasuryKey, TokenType.NON_FUNGIBLE_UNIQUE, supplierKey);
        final TokenCreateResult tokenCreateResult = client.executeTokenCreateTransaction(request);
        return tokenCreateResult.tokenId();
    }

    @Override
    public void associateNft(@NonNull final TokenId tokenId, @NonNull final AccountId accountId, @NonNull final PrivateKey accountKey) throws HederaException {
        final TokenAssociateRequest request = TokenAssociateRequest.of(tokenId, accountId, accountKey);
        client.executeTokenAssociateTransaction(request);
    }

    @Override
    public long mintNft(@NonNull final TokenId tokenId, @NonNull final String metadata) throws HederaException {
        return mintNft(tokenId, metadata, adminSupplyKey);
    }

    @Override
    public List<Long> mintNfts(@NonNull final TokenId tokenId, @NonNull final List<String> metadata) throws HederaException {
        return mintNfts(tokenId, metadata, adminSupplyKey);
    }

    @Override
    public long mintNft(@NonNull final TokenId tokenId, @NonNull final String metadata, @NonNull final PrivateKey supplyKey) throws HederaException {
        final TokenMintRequest request = TokenMintRequest.of(tokenId, supplyKey, metadata);
        final TokenMintResult tokenMintResult = client.executeMintTokenTransaction(request);
        final List<Long> serials = tokenMintResult.serials();
        if(serials.size() != 1) {
            throw new HederaException("Expected 1 serial number, but got " + serials.size());
        }
        return serials.get(0);
    }

    @Override
    public List<Long> mintNfts(@NonNull final TokenId tokenId, @NonNull final List<String> metadata, @NonNull final PrivateKey supplyKey) throws HederaException {
        final TokenMintRequest request = TokenMintRequest.of(tokenId, supplyKey, metadata);
        final TokenMintResult result = client.executeMintTokenTransaction(request);
        return Collections.unmodifiableList(result.serials());
    }

    @Override
    public void transferNft(@NonNull final TokenId tokenId, final long serialNumber, @NonNull final AccountId fromAccountId, @NonNull final PrivateKey fromAccountKey,
            @NonNull final AccountId toAccountId) throws HederaException {
        transferNfts(tokenId, List.of(serialNumber), fromAccountId, fromAccountKey, toAccountId);
    }

    @Override
    public void transferNfts(@NonNull final TokenId tokenId, @NonNull final List<Long> serialNumber, @NonNull final AccountId fromAccountId,
            @NonNull final PrivateKey fromAccountKey, @NonNull final AccountId toAccountId) throws HederaException {
        final TokenTransferRequest request = TokenTransferRequest.of(tokenId, serialNumber, fromAccountId, toAccountId, fromAccountKey);
        client.executeTransferTransactionForNft(request);
    }


}
