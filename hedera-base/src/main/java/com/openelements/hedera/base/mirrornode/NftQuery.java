package com.openelements.hedera.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.protocol.QueryRequest;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public record NftQuery(@NonNull AccountId accountId, @Nullable TokenId tokenId, @Nullable Long serial, @Nullable Long rangeStart, @Nullable Long rangeEnd, Hbar queryPayment, Hbar maxQueryPayment) implements
        QueryRequest {

    public NftQuery {
    }

    public static NftQuery of(AccountId owner) {
        return new NftQuery(owner, null, null, null, null, Hbar.from(10), Hbar.from(10));
    }

    public static NftQuery of(TokenId tokenId) {
        return new NftQuery(null, tokenId, null, null, null, Hbar.from(10), Hbar.from(10));
    }

    public static NftQuery of(TokenId tokenId, long serialNumber) {
        return new NftQuery(null, tokenId, serialNumber, null, null, Hbar.from(10), Hbar.from(10));
    }

    public static NftQuery of(AccountId owner, TokenId tokenId) {
        return new NftQuery(owner, tokenId, null, null, null, Hbar.from(10), Hbar.from(10));
    }

    public static NftQuery of(AccountId owner, TokenId tokenId, long serialNumber) {
        return new NftQuery(owner, tokenId, serialNumber, null, null, Hbar.from(10), Hbar.from(10));
    }

    public static NftQuery of(AccountId owner, long startSerial, long endSerial) {
        return new NftQuery(owner, null, null, startSerial, endSerial, Hbar.from(10), Hbar.from(10));
    }

    public static NftQuery of(TokenId tokenId, long startSerial, long endSerial) {
        return new NftQuery(null, tokenId, null, startSerial, endSerial, Hbar.from(10), Hbar.from(10));
    }

    public static NftQuery of(AccountId owner, TokenId tokenId, long startSerial, long endSerial) {
        return new NftQuery(owner, tokenId, null, startSerial, endSerial, Hbar.from(10), Hbar.from(10));
    }

    @Override
    public Hbar queryPayment() {
        return queryPayment;
    }

    @Override
    public Hbar maxQueryPayment() {
        return maxQueryPayment;
    }
}
