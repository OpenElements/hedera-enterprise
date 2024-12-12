package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.NftMetadata;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.implementation.AbstractMirrorNodeClient;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import com.openelements.hiero.base.implementation.MirrorNodeRestClient;
import jakarta.json.JsonObject;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class MirrorNodeClientImpl extends AbstractMirrorNodeClient<JsonObject> {

    private final MirrorNodeRestClient<JsonObject> restClient;

    private final MirrorNodeJsonConverter<JsonObject> jsonConverter;

    public MirrorNodeClientImpl(MirrorNodeRestClient<JsonObject> restClient,
            MirrorNodeJsonConverter<JsonObject> jsonConverter) {
        this.restClient = Objects.requireNonNull(restClient, "restClient must not be null");
        this.jsonConverter = Objects.requireNonNull(jsonConverter, "jsonConverter must not be null");
    }

    @Override
    protected @NonNull MirrorNodeRestClient<JsonObject> getRestClient() {
        return restClient;
    }

    @Override
    protected @NonNull MirrorNodeJsonConverter<JsonObject> getJsonConverter() {
        return jsonConverter;
    }

    @Override
    public @NonNull Page<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId)
            throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<TransactionInfo> queryTransactionsByAccount(@NonNull AccountId accountId)
            throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<TransactionInfo> queryTransaction(@NonNull String transactionId) throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull NftMetadata getNftMetadata(TokenId tokenId) throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<NftMetadata> findNftTypesByOwner(AccountId ownerId) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<NftMetadata> findAllNftTypes() {
        throw new RuntimeException("Not implemented");
    }
}
