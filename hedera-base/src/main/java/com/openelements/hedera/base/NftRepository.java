package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.util.List;
import java.util.Optional;

public interface NftRepository {

    @NonNull
    List<Nft> findByOwner(@NonNull AccountId owner) throws HederaException;

    @NonNull
    List<Nft> findByType(@NonNull TokenId tokenId) throws HederaException;

    @NonNull
    Optional<Nft> findByTypeAndSerial(@NonNull TokenId tokenId, long serialNumber) throws HederaException;

    @NonNull
    List<Nft> findByOwnerAndType(@NonNull AccountId owner, @NonNull TokenId tokenId) throws HederaException;

    @NonNull
    Optional<Nft> findByOwnerAndTypeAndSerial(@NonNull AccountId owner, @NonNull TokenId tokenId, long serialNumber) throws HederaException;
}
