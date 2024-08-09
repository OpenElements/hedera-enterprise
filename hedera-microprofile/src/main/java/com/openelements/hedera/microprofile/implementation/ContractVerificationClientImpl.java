package com.openelements.hedera.microprofile.implementation;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hedera.base.ContractVerificationClient;
import com.openelements.hedera.base.ContractVerificationState;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.implementation.HederaNetwork;
import org.jspecify.annotations.NonNull;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParserFactory;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ContractVerificationClientImpl implements ContractVerificationClient {

    private final static String CONTRACT_VERIFICATION_URL = "https://server-verify.hashscan.io";

    private final HederaNetwork hederaNetwork;

    private final JsonParserFactory jsonParserFactory;

    private final Client webClient;

    public ContractVerificationClientImpl(@NonNull final HederaNetwork hederaNetwork) {
        this.hederaNetwork = Objects.requireNonNull(hederaNetwork, "hederaNetwork must not be null");
        jsonParserFactory = Json.createParserFactory(Map.of());
        webClient = ClientBuilder.newBuilder().build();
    }

    private String getChainId() throws HederaException {
        if(hederaNetwork == HederaNetwork.CUSTOM) {
            throw new HederaException("A custom Hedera network is not supported for smart contract verification. Please use MainNet, TestNet or PreviewNet.");
        }
        return hederaNetwork.getChainId() + "";
    }

    @NonNull
    @Override
    public ContractVerificationState checkVerification(@NonNull ContractId contractId) throws HederaException {
        throw new UnsupportedOperationException("Not implemented");
    }

    private void handleError(@NonNull final Response response) throws IOException {
            final String body = response.readEntity(String.class);
            throw new IOException("Error response: " + body);
    }

    @Override
    public boolean checkVerification(@NonNull ContractId contractId, @NonNull String fileName,
            @NonNull String fileContent) throws HederaException {
        final ContractVerificationState state = checkVerification(contractId);
        if(state != ContractVerificationState.FULL) {
            throw new IllegalStateException("Contract is not verified");
        }

        final String uri = CONTRACT_VERIFICATION_URL + "/files/" + getChainId() + "/" + contractId.toSolidityAddress();

        try {
            final Response response = webClient.target(uri).request()
                    .header("accept", "application/json")
                    .get();
            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                handleError(response);
            }
            final String resultBody = response.readEntity(String.class);
            final JsonParser parser = jsonParserFactory.createParser(new StringReader(resultBody));
            final JsonArray root = parser.getArray();
            final List<JsonObject> results = root.stream()
                    .filter(JsonValue.ValueType.OBJECT::equals)
                    .map(JsonValue::asJsonObject)
                    .filter(obj -> obj.getString("name").equals(fileName))
                    .toList();
            if (results.size() != 1) {
                throw new RuntimeException("Expected exactly one result, got " + results.size());
            }
            final JsonObject result = results.get(0);
            final String content = result.getString("content");
            return Objects.equals(content, fileContent);
        } catch (Exception e) {
            throw new HederaException("Error verification step", e);
        }
    }

    @NonNull
    @Override
    public ContractVerificationState verify(@NonNull ContractId contractId, @NonNull String contractName,
            @NonNull Map<String, String> files) throws HederaException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
