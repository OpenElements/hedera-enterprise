package com.openelements.hedera.microprofile;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Client;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.ContractVerificationClient;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.SmartContractClient;
import com.openelements.hedera.base.implementation.AccountClientImpl;
import com.openelements.hedera.base.implementation.FileClientImpl;
import com.openelements.hedera.base.implementation.HederaNetwork;
import com.openelements.hedera.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hedera.base.implementation.SmartContractClientImpl;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import com.openelements.hedera.microprofile.implementation.ContractVerificationClientImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.jspecify.annotations.NonNull;

public class ClientProvider {

    @Inject
    @ConfigProperties
    private HieroOperatorConfiguration configuration;

    @Inject
    @ConfigProperties
    private HieroNetworkConfiguration networkConfiguration;


    private AccountId getAccountId() {
        if (configuration == null) {
            throw new IllegalStateException("configuration is null");
        }
        final String accountId = configuration.getAccountId();
        if (accountId == null) {
            throw new IllegalStateException("accountId value is null");
        }
        try {
            return AccountId.fromString(accountId);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Can not parse 'hedera.newAccountId' property: '" + accountId + "'", e);
        }
    }

    private PrivateKey getPrivateKey() {
        if (configuration == null) {
            throw new IllegalStateException("configuration is null");
        }
        final String privateKey = configuration.getPrivateKey();
        if (privateKey == null) {
            throw new IllegalStateException("privateKey value is null");
        }
        try {
            return PrivateKey.fromString(privateKey);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Can not parse 'hedera.privateKey' property: '" + privateKey + "'", e);
        }
    }

    private HederaNetwork getHederaNetwork() {
        if (networkConfiguration == null) {
            throw new IllegalStateException("network value is null");
        }
        return networkConfiguration.getName()
                .map(n -> HederaNetwork.findByName(n).orElse(HederaNetwork.CUSTOM))
                .orElse(HederaNetwork.CUSTOM);
    }

    private Client createClient() {
        final AccountId accountId = getAccountId();
        final PrivateKey privateKey = getPrivateKey();
        final HederaNetwork hederaNetwork = getHederaNetwork();
        if (Objects.equals(HederaNetwork.CUSTOM, hederaNetwork)) {
            final Map<String, AccountId> nodes = networkConfiguration.getNodes()
                    .stream().collect(Collectors.toMap(n -> n.getAddress(), n -> n.getAccountId()));
            Client client = Client.forNetwork(nodes);
            networkConfiguration.getMirrornode()
                    .map(mirrorNode -> List.of(mirrorNode))
                    .ifPresent(mirrorNodes -> {
                        try {
                            client.setMirrorNetwork(mirrorNodes);
                        } catch (InterruptedException e) {
                            throw new RuntimeException("Error setting mirror network", e);
                        }
                    });
            client.setOperator(accountId, privateKey);
            return client;
        } else {
            return Client.forName(hederaNetwork.getName())
                    .setOperator(accountId, privateKey);
        }
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
    SmartContractClient createSmartContractClient(@NonNull final ProtocolLayerClient protocolLayerClient,
            @NonNull final FileClient fileClient) {
        return new SmartContractClientImpl(protocolLayerClient, fileClient);
    }

    @Produces
    @ApplicationScoped
    AccountClient createAccountClient(@NonNull final ProtocolLayerClient protocolLayerClient) {
        return new AccountClientImpl(protocolLayerClient);
    }

    @Produces
    @ApplicationScoped
    ContractVerificationClient createContractVerificationClient(@NonNull final ProtocolLayerClient protocolLayerClient,
            @NonNull final FileClient fileClient) {
        return new ContractVerificationClientImpl(getHederaNetwork());
    }
}
