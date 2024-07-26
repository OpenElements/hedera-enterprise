package com.openelements.hedera.spring.sample;

import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import java.util.Objects;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HederaEndpoint {

    private final ProtocolLayerClient client;

    public HederaEndpoint(final ProtocolLayerClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @GetMapping("/balance")
    public String getBalance() {
        try {
            final AccountBalanceRequest request = AccountBalanceRequest.of("0.0.100");
            final AccountBalanceResponse response = client.executeAccountBalanceQuery(request);
            return response.hbars().toString();
        } catch (final Exception e) {
            throw new RuntimeException("Error in Hedera call", e);
        }
    }
}
