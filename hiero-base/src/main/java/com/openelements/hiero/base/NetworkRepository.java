package com.openelements.hiero.base;

import com.openelements.hiero.base.mirrornode.ExchangeRates;
import com.openelements.hiero.base.mirrornode.NetworkFee;
import com.openelements.hiero.base.mirrornode.NetworkStake;
import com.openelements.hiero.base.mirrornode.NetworkSupplies;
import java.util.List;
import java.util.Optional;

/**
 * Interface for interacting with a Hedera network. This interface provides methods to get information related to
 * Network.
 */
public interface NetworkRepository {
    /**
     * Return the ExchangeRates for network.
     *
     * @return {@link Optional} containing the ExchangeRates or null
     * @throws HederaException if the search fails
     */
    Optional<ExchangeRates> exchangeRates() throws HederaException;

    /**
     * Return the List of NetworkFee for network.
     *
     * @return {@link List} containing NetworkFee or empty list
     * @throws HederaException if the search fails
     */
    List<NetworkFee> fees() throws HederaException;

    /**
     * Return the NetworkStake for network.
     *
     * @return {@link Optional} containing NetworkStake or null
     * @throws HederaException if the search fails
     */
    Optional<NetworkStake> stake() throws HederaException;

    /**
     * Return the NetworkSupplies for network.
     *
     * @return {@link Optional} containing NetworkSupplies or null
     * @throws HederaException if the search fails
     */
    Optional<NetworkSupplies> supplies() throws HederaException;
}
