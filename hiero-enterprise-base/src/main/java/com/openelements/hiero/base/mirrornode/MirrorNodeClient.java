package com.openelements.hiero.base.mirrornode;

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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * A client for querying the Hiero Mirror Node REST API.
 */
public interface MirrorNodeClient {

    /**
     * Queries the NFTs owned by an account.
     *
     * @param accountId the account ID
     * @return the NFTs owned by the account
     * @throws HieroException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HieroException;

    /**
     * Queries the NFTs owned by an account.
     *
     * @param accountId the account ID
     * @return the NFTs owned by the account
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByAccount(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryNftsByAccount(AccountId.fromString(accountId));
    }

    /**
     * Queries the NFTs owned by an account for a specific token ID.
     *
     * @param accountId the account ID
     * @param tokenId   the token ID
     * @return the NFTs owned by the account for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId)
            throws HieroException;

    /**
     * Queries the NFTs owned by an account for a specific token ID.
     *
     * @param accountId the account ID
     * @param tokenId   the token ID
     * @return the NFTs owned by the account for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByAccountAndTokenId(@NonNull String accountId, @NonNull String tokenId)
            throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByAccountAndTokenId(AccountId.fromString(accountId), TokenId.fromString(tokenId));
    }

    /**
     * Queries the NFTs for a specific token ID.
     *
     * @param tokenId the token ID
     * @return the NFTs for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HieroException;

    /**
     * Queries the NFTs for a specific token ID.
     *
     * @param tokenId the token ID
     * @return the NFTs for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByTokenId(@NonNull String tokenId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByTokenId(TokenId.fromString(tokenId));
    }

    /**
     * Queries the NFTs for a specific token ID and serial number.
     *
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs for the token ID and serial number
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull TokenId tokenId, long serialNumber) throws HieroException;

    /**
     * Queries the NFTs for a specific token ID and serial number.
     *
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs for the token ID and serial number
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull String tokenId, long serialNumber)
            throws HieroException {
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
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull AccountId accountId, @NonNull TokenId tokenId,
            long serialNumber) throws HieroException;

    /**
     * Queries the NFTs owned by an account for a specific token ID and serial number.
     *
     * @param accountId    the account ID
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs owned by the account for the token ID and serial number
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull String accountId, @NonNull String tokenId,
            long serialNumber) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByAccountAndTokenIdAndSerial(AccountId.fromString(accountId), TokenId.fromString(tokenId),
                serialNumber);
    }

    /**
     * Queries all transactions for a specific account.
     *
     * @param accountId the account ID to query transactions for
     * @return a page of transaction information
     * @throws HieroException if an error occurs during the query
     */
    @NonNull
    Page<TransactionInfo> queryTransactionsByAccount(@NonNull AccountId accountId) throws HieroException;


    /**
     * Queries the transaction information for a specific transaction ID.
     *
     * @param transactionId the transaction ID
     * @return the transaction information for the transaction ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<TransactionInfo> queryTransaction(@NonNull String transactionId) throws HieroException;

    /**
     * Queries the account information for a specific account ID.
     *
     * @param accountId the account ID
     * @return the account information for the account ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<AccountInfo> queryAccount(@NonNull AccountId accountId) throws HieroException;

    /**
     * Queries the account information for a specific account ID.
     *
     * @param accountId the account ID
     * @return the account information for the account ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Optional<AccountInfo> queryAccount(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryAccount(AccountId.fromString(accountId));
    }

    /**
     * Queries the ExchangeRates for the network.
     *
     * @return the Optional of ExchangeRates for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<ExchangeRates> queryExchangeRates() throws HieroException;

    /**
     * Queries the NetworkFee for the network.
     *
     * @return the List of NetworkFee for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    List<NetworkFee> queryNetworkFees() throws HieroException;

    /**
     * Queries the NetworkStake for the network.
     *
     * @return the Optional of NetworkStake for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<NetworkStake> queryNetworkStake() throws HieroException;

    /**
     * Queries the NetworkSupplies for the network.
     *
     * @return the Optional of NetworkSupplies for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<NetworkSupplies> queryNetworkSupplies() throws HieroException;

    @NonNull
    NftMetadata getNftMetadata(TokenId tokenId) throws HieroException;

    @NonNull
    Page<NftMetadata> findNftTypesByOwner(AccountId ownerId);

    @NonNull
    Page<NftMetadata> findAllNftTypes();
}
