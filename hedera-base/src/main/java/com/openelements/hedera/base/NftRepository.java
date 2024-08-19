package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hedera network.
 * This interface provides methods for searching for NFTs.
 */
public interface NftRepository {

    /**
     * Return all NFTs that are owned by the given owner.
     *
     * @param ownerId id of the owner account
     * @return list of NFTs
     * @throws HederaException if the search fails
     */
    @NonNull
    List<Nft> findByOwner(@NonNull AccountId ownerId) throws HederaException;

    /**
     * Return all NFTs of a given type.
     *
     * @param tokenId id of the token type
     * @return list of NFTs
     * @throws HederaException if the search fails
     */
    @NonNull
    List<Nft> findByType(@NonNull TokenId tokenId) throws HederaException;

    /**
     * Return the NFTs of a given type with the given serial.
     *
     * @param tokenId id of the token type
     * @param serialNumber serial of the nft instance
     * @return {@link Optional} containing the found NFT or null
     * @throws HederaException if the search fails
     */
    @NonNull
    Optional<Nft> findByTypeAndSerial(@NonNull TokenId tokenId, long serialNumber) throws HederaException;

    /**
     * Return all NFTs of a given type owned by a specific account.
     *
     * @param ownerId id of the owner
     * @param tokenId id of the token type
     * @return list of NFTs
     * @throws HederaException if the search fails
     */
    @NonNull
    List<Nft> findByOwnerAndType(@NonNull AccountId ownerId, @NonNull TokenId tokenId) throws HederaException;

    /**
     * Return the NFT of a given type and serial owned by a specific account.
     *
     * @param owner id of the owner
     * @param tokenId id of the token type
     * @param serialNumber serial of the nft instance
     * @return {@link Optional} containing the found NFT or null
     * @throws HederaException if the search fails
     */
    @NonNull
    Optional<Nft> findByOwnerAndTypeAndSerial(@NonNull AccountId owner, @NonNull TokenId tokenId, long serialNumber) throws HederaException;
}
