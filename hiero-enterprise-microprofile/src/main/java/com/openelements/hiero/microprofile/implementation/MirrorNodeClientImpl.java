package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.NftMetadata;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class MirrorNodeClientImpl implements MirrorNodeClient {
    @Override
    public @NonNull Page<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Page<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId)
            throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull TokenId tokenId, long serialNumber)
            throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull AccountId accountId,
            @NonNull TokenId tokenId, long serialNumber) throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Page<TransactionInfo> queryTransactionsByAccount(@NonNull AccountId accountId)
            throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Optional<TransactionInfo> queryTransaction(@NonNull String transactionId) throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Optional<AccountInfo> queryAccount(@NonNull AccountId accountId) throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Optional<ExchangeRates> queryExchangeRates() throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull List<NetworkFee> queryNetworkFees() throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Optional<NetworkStake> queryNetworkStake() throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Optional<NetworkSupplies> queryNetworkSupplies() throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull NftMetadata getNftMetadata(TokenId tokenId) throws HieroException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Page<NftMetadata> findNftTypesByOwner(AccountId ownerId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Page<NftMetadata> findAllNftTypes() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
