package com.openelements.hedera.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.Nft;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.Optional;

public interface MirrorNodeClient {

    @NonNull
    List<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HederaException;

    @NonNull
    List<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId) throws HederaException;

    @NonNull
    List<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HederaException;

    @NonNull
    Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull TokenId tokenId, long serialNumber) throws HederaException;

    @NonNull
    Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull AccountId accountId, @NonNull TokenId tokenId, long serialNumber) throws HederaException;

    @NonNull
    Optional<TransactionInfo> queryTransaction(@NonNull String transactionId) throws HederaException;
}
