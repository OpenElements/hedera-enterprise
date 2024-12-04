package com.openelements.hiero.microprofile;

import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.implementation.AccountClientImpl;
import com.openelements.hiero.base.implementation.FileClientImpl;
import com.openelements.hiero.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hiero.base.implementation.SmartContractClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.verification.ContractVerificationClient;
import com.openelements.hiero.microprofile.implementation.ContractVerificationClientImpl;
import com.openelements.hiero.microprofile.implementation.HieroConfigImpl;
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
    HieroContext createHieroContext(@NonNull final HieroConfig hieroConfig) {
        return hieroConfig.createHieroContext();
    }

    @Produces
    @ApplicationScoped
    ProtocolLayerClient createProtocolLayerClient(@NonNull final HieroContext hieroContext) {
        return new ProtocolLayerClientImpl(hieroContext);
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
