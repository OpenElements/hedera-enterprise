package com.openelements.hiero.microprofile;

import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.NftClient;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.config.HieroConfig;
import com.openelements.hiero.base.implementation.AccountClientImpl;
import com.openelements.hiero.base.implementation.AccountRepositoryImpl;
import com.openelements.hiero.base.implementation.FileClientImpl;
import com.openelements.hiero.base.implementation.NetworkRepositoryImpl;
import com.openelements.hiero.base.implementation.NftRepositoryImpl;
import com.openelements.hiero.base.implementation.NftClientImpl;
import com.openelements.hiero.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hiero.base.implementation.SmartContractClientImpl;
import com.openelements.hiero.base.implementation.TransactionRepositoryImpl;
import com.openelements.hiero.base.mirrornode.AccountRepository;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.mirrornode.NetworkRepository;
import com.openelements.hiero.base.mirrornode.NftRepository;
import com.openelements.hiero.base.mirrornode.TransactionRepository;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.verification.ContractVerificationClient;
import com.openelements.hiero.microprofile.implementation.ContractVerificationClientImpl;
import com.openelements.hiero.microprofile.implementation.HieroConfigImpl;
import com.openelements.hiero.microprofile.implementation.MirrorNodeClientImpl;
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
    NftClient createNftClient(@NonNull final ProtocolLayerClient protocolLayerClient,
            @NonNull final HieroContext hieroContext) {
        return new NftClientImpl(protocolLayerClient, hieroContext.getOperatorAccount());
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

    @Produces
    @ApplicationScoped
    MirrorNodeClient createMirrorNodeClient() {
        return new MirrorNodeClientImpl();
    }

    @Produces
    @ApplicationScoped
    AccountRepository createAccountRepository(@NonNull final MirrorNodeClient mirrorNodeClient) {
        return new AccountRepositoryImpl(mirrorNodeClient);
    }

    @Produces
    @ApplicationScoped
    NetworkRepository createNetworkRepository(@NonNull final MirrorNodeClient mirrorNodeClient) {
        return new NetworkRepositoryImpl(mirrorNodeClient);
    }

    @Produces
    @ApplicationScoped
    NftRepository createNftRepository(@NonNull final MirrorNodeClient mirrorNodeClient) {
        return new NftRepositoryImpl(mirrorNodeClient);
    }

    @Produces
    @ApplicationScoped
    TransactionRepository createTransactionRepository(@NonNull final MirrorNodeClient mirrorNodeClient) {
        return new TransactionRepositoryImpl(mirrorNodeClient);
    }
}
