package com.openelements.hiero.microprofile;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hiero.base.Account;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.ContractVerificationClient;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.implementation.AccountClientImpl;
import com.openelements.hiero.base.implementation.FileClientImpl;
import com.openelements.hiero.base.implementation.HederaNetwork;
import com.openelements.hiero.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hiero.base.implementation.SmartContractClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.microprofile.implementation.ContractVerificationClientImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.util.Arrays;
import java.util.Objects;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jspecify.annotations.NonNull;

public class ClientProvider {

    @ConfigProperty(name = "hedera.newAccountId")
    private String accountIdAsString;

    @ConfigProperty(name = "hedera.privateKey")
    private String privateKeyAsString;

    @ConfigProperty(name = "hedera.network")
    private String network;

    private AccountId getAccountId() {
        try {
            return AccountId.fromString(accountIdAsString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'hedera.newAccountId' property", e);
        }
    }

    private PrivateKey getPrivateKey() {
        try {
            return PrivateKey.fromString(privateKeyAsString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not parse 'hedera.privateKey' property", e);
        }
    }

    private HederaNetwork getHederaNetwork() {
        if(Arrays.stream(HederaNetwork.values()).anyMatch(v -> Objects.equals(v.getName(), network))) {
            try {
                return HederaNetwork.valueOf(network.toUpperCase());
            } catch (Exception e) {
                throw new IllegalArgumentException("Can not parse 'hedera.network' property", e);
            }
        } else {
            throw new IllegalArgumentException("'hedera.network' property must be set to a valid value");
        }
    }

    private Client createClient() {
        final AccountId accountId = getAccountId();
        final PrivateKey privateKey = getPrivateKey();
        final HederaNetwork hederaNetwork = getHederaNetwork();
        return Client.forName(hederaNetwork.getName())
            .setOperator(accountId, privateKey);
    }

    @Produces
    @ApplicationScoped
    ProtocolLayerClient createProtocolLayerClient() {
        final Account operator = Account.of(getAccountId(), getPrivateKey());
        return new ProtocolLayerClientImpl(createClient(), operator);
    }

    @Produces
    @ApplicationScoped
    FileClient createFileClient(@NonNull final ProtocolLayerClient protocolLayerClient) {
        return new FileClientImpl(protocolLayerClient);
    }

    @Produces
    @ApplicationScoped
    SmartContractClient createSmartContractClient(@NonNull final ProtocolLayerClient protocolLayerClient, @NonNull final FileClient fileClient) {
        return new SmartContractClientImpl(protocolLayerClient, fileClient);
    }

    @Produces
    @ApplicationScoped
    AccountClient createAccountClient(@NonNull final ProtocolLayerClient protocolLayerClient) {
        return new AccountClientImpl(protocolLayerClient);
    }

    @Produces
    @ApplicationScoped
    ContractVerificationClient createContractVerificationClient(@NonNull final ProtocolLayerClient protocolLayerClient, @NonNull final FileClient fileClient) {
        return new ContractVerificationClientImpl(getHederaNetwork());
    }
}
