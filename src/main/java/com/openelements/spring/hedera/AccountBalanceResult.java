package com.openelements.spring.hedera;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TokenId;
import java.util.Map;

/**
 * Represents the result of an account balance query.
 *
 * @param hbars The balance of the account in hbars.
 */
public record AccountBalanceResult(Hbar hbars) {
}
