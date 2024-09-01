package com.openelements.hedera.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.Nft;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * A client for querying the Hedera Mirror Node REST API.
 */
public interface MirrorNodeClient {

    /**
     * Queries the NFTs owned by an account.
     *
     * @param accountId the account ID
     * @return the NFTs owned by the account
     * @throws HederaException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HederaException;

    /**
     * Queries the NFTs owned by an account.
     *
     * @param accountId the account ID
     * @return the NFTs owned by the account
     * @throws HederaException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByAccount(@NonNull String accountId) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryNftsByAccount(AccountId.fromString(accountId));
    }

    /**
     * Queries the NFTs owned by an account for a specific token ID.
     *
     * @param accountId the account ID
     * @param tokenId   the token ID
     * @return the NFTs owned by the account for the token ID
     * @throws HederaException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId)
            throws HederaException;

    /**
     * Queries the NFTs owned by an account for a specific token ID.
     *
     * @param accountId the account ID
     * @param tokenId   the token ID
     * @return the NFTs owned by the account for the token ID
     * @throws HederaException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByAccountAndTokenId(@NonNull String accountId, @NonNull String tokenId)
            throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByAccountAndTokenId(AccountId.fromString(accountId), TokenId.fromString(tokenId));
    }

    /**
     * Queries the NFTs for a specific token ID.
     *
     * @param tokenId the token ID
     * @return the NFTs for the token ID
     * @throws HederaException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HederaException;

    /**
     * Queries the NFTs for a specific token ID.
     *
     * @param tokenId the token ID
     * @return the NFTs for the token ID
     * @throws HederaException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByTokenId(@NonNull String tokenId) throws HederaException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByTokenId(TokenId.fromString(tokenId));
    }

    /**
     * Queries the NFTs for a specific token ID and serial number.
     *
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs for the token ID and serial number
     * @throws HederaException if an error occurs
     */
    @NonNull
    Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull TokenId tokenId, long serialNumber) throws HederaException;

    /**
     * Queries the NFTs for a specific token ID and serial number.
     *
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs for the token ID and serial number
     * @throws HederaException if an error occurs
     */
    @NonNull
    default Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull String tokenId, long serialNumber)
            throws HederaException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByTokenIdAndSerial(TokenId.fromString(tokenId), serialNumber);
    }

    /**
     * Queries the NFTs owned by an account for a specific token ID and serial number.
     *
     * @param accountId    the account ID
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs owned by the account for the token ID and serial number
     * @throws HederaException if an error occurs
     */
    @NonNull
    Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull AccountId accountId, @NonNull TokenId tokenId,
            long serialNumber) throws HederaException;

    /**
     * Queries the NFTs owned by an account for a specific token ID and serial number.
     *
     * @param accountId    the account ID
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs owned by the account for the token ID and serial number
     * @throws HederaException if an error occurs
     */
    @NonNull
    default Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull String accountId, @NonNull String tokenId,
            long serialNumber) throws HederaException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByAccountAndTokenIdAndSerial(AccountId.fromString(accountId), TokenId.fromString(tokenId),
                serialNumber);
    }

    /**
     * Queries the transaction information for a specific transaction ID.
     *
     * @param transactionId the transaction ID
     * @return the transaction information for the transaction ID
     * @throws HederaException if an error occurs
     */
    @NonNull
    Optional<TransactionInfo> queryTransaction(@NonNull String transactionId) throws HederaException;
}
