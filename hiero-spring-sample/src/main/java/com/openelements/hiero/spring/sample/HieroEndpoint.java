package com.openelements.hiero.spring.sample;

import com.openelements.hiero.base.AccountClient;
import java.util.Objects;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HieroEndpoint {

    private final AccountClient client;

    public HieroEndpoint(final AccountClient client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    @GetMapping("/")
    public String getBalance() {
        try {
            return client.getAccountBalance("0.0.100").toString();
        } catch (final Exception e) {
            throw new RuntimeException("Error in Hiero call", e);
        }
    }
}
