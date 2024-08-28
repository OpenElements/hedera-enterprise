package com.openelements.hedera.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.Nft;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * A client for querying the Hedera Mirror Node REST API.
 */
public interface MirrorNodeClient {

    @NonNull
    Page<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HederaException;

    @NonNull
    Page<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId)
            throws HederaException;

    @NonNull
    Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HederaException;

    @NonNull
    Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull TokenId tokenId, long serialNumber) throws HederaException;

    @NonNull
    Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull AccountId accountId, @NonNull TokenId tokenId,
            long serialNumber) throws HederaException;

    @NonNull
    Optional<TransactionInfo> queryTransaction(@NonNull String transactionId) throws HederaException;
}
