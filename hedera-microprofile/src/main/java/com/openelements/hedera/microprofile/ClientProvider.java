package com.openelements.hedera.microprofile;

import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.ContractVerificationClient;
import com.openelements.hedera.base.FileClient;
import com.openelements.hedera.base.SmartContractClient;
import com.openelements.hedera.base.config.HieroConfig;
import com.openelements.hedera.base.implementation.AccountClientImpl;
import com.openelements.hedera.base.implementation.FileClientImpl;
import com.openelements.hedera.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hedera.base.implementation.SmartContractClientImpl;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import com.openelements.hedera.microprofile.implementation.ContractVerificationClientImpl;
import com.openelements.hedera.microprofile.implementation.HieroConfigImpl;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.jspecify.annotations.NonNull;

public class ClientProvider {

    @Inject
    @ConfigProperties
    private HieroOperatorConfiguration configuration;

    @Inject
    @ConfigProperties
    private HieroNetworkConfiguration networkConfiguration;

    @Produces
    @ApplicationScoped
    HieroConfig createHieroConfig() {
        return new HieroConfigImpl(configuration, networkConfiguration);
    }

    @Produces
    @ApplicationScoped
    ProtocolLayerClient createProtocolLayerClient(@NonNull final HieroConfig hieroConfig) {
        return new ProtocolLayerClientImpl(hieroConfig.createClient(), hieroConfig.getOperatorAccount());
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
    ContractVerificationClient createContractVerificationClient(@NonNull final HieroConfig hieroConfig) {
        return new ContractVerificationClientImpl(hieroConfig.getNetwork());
    }
}
