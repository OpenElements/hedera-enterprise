package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;

public record Nft(TokenId tokenId, long serial, AccountId owner, byte[] metadata) {

}
