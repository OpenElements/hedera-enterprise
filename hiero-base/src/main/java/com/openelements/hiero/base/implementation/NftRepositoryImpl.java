package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HederaException;
import com.openelements.hiero.base.Nft;
import com.openelements.hiero.base.NftRepository;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.mirrornode.Page;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class NftRepositoryImpl implements NftRepository {

    private final MirrorNodeClient mirrorNodeClient;

    public NftRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
    }

    @NonNull
    @Override
    public Page<Nft> findByOwner(@NonNull final AccountId owner) throws HederaException {
        return mirrorNodeClient.queryNftsByAccount(owner);
    }

    @NonNull
    @Override
    public Page<Nft> findByType(@NonNull final TokenId tokenId) throws HederaException {
        return mirrorNodeClient.queryNftsByTokenId(tokenId);
    }

    @NonNull
    @Override
    public Optional<Nft> findByTypeAndSerial(@NonNull final TokenId tokenId, final long serialNumber)
            throws HederaException {
        return mirrorNodeClient.queryNftsByTokenIdAndSerial(tokenId, serialNumber);
    }

    @NonNull
    @Override
    public Page<Nft> findByOwnerAndType(@NonNull final AccountId owner, @NonNull final TokenId tokenId)
            throws HederaException {
        return mirrorNodeClient.queryNftsByAccountAndTokenId(owner, tokenId);
    }

    @NonNull
    @Override
    public Optional<Nft> findByOwnerAndTypeAndSerial(@NonNull final AccountId owner, @NonNull final TokenId tokenId,
            final long serialNumber) throws HederaException {
        return mirrorNodeClient.queryNftsByAccountAndTokenIdAndSerial(owner, tokenId, serialNumber);
    }
}
