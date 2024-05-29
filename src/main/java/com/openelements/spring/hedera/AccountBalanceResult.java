package com.openelements.spring.hedera;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TokenId;
import java.util.Map;

public record AccountBalanceResult(Hbar hbars) {
}
