package com.openelements.spring.hedera.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hedera.base.data.ContractVerificationState;
import com.openelements.hedera.base.implementation.HederaNetwork;
import com.openelements.spring.hedera.api.ContractVerificationClient;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;

public class ContractVerificationClientImplementation implements ContractVerificationClient {

    private final static String CONTRACT_VERIFICATION_URL = "https://server-verify.hashscan.io";

    private record VerifyRequest(String address, String chain, String creatorTxHash, String chosenContract, Map<String, String> files) {}

    private final HederaNetwork hederaNetwork;

    private final ObjectMapper objectMapper;

    private final RestClient restClient;

    public ContractVerificationClientImplementation(@NonNull HederaNetwork hederaNetwork) {
        this.hederaNetwork = Objects.requireNonNull(hederaNetwork, "hederaNetwork must not be null");
        objectMapper = new ObjectMapper();
        restClient = RestClient.create();
    }

    private String getChainId() {
        if(hederaNetwork == HederaNetwork.CUSTOM) {
            throw new IllegalArgumentException("Custom network is not supported");
        }
        return hederaNetwork.getChainId() + "";
    }

    private void handleError(HttpRequest request, ClientHttpResponse response) throws IOException {
        final String error;
        try {
            final String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
                final JsonNode rootNode = objectMapper.readTree(body);
                final JsonNode errorNode = rootNode.get("error");
                if (errorNode != null) {
                    error = errorNode.asText();
                } else {
                    final JsonNode messageNode = rootNode.get("message");
                    if (messageNode != null) {
                        error = messageNode.asText();
                    } else {
                        error = body;
                    }
                }
            } catch (Exception e) {
                throw new IOException("Error parsing body as JSON: " + body, e);
            }
        } catch (Exception e) {
            throw new IOException("Error (" + response.getStatusCode() + "): " + response.getStatusText());
        }
        throw new IOException("Error (" + response.getStatusCode() + "): " + error);
    }

    @Override
    public ContractVerificationState verify(ContractId contractId, String contractName, Map<String, String> files) {
        checkSupportedNetwork();

        final ContractVerificationState state = checkVerification(contractId);
        if(state != ContractVerificationState.NONE) {
           throw new IllegalStateException("Contract is already verified");
        }

        final VerifyRequest verifyRequest = new VerifyRequest(
                contractId.toSolidityAddress(),
                getChainId(),
                "",
                "",
                files
                );

        try {
            final String valueAsString = objectMapper.writerFor(VerifyRequest.class).writeValueAsString(verifyRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        final String resultBody = restClient.post()
                .uri(CONTRACT_VERIFICATION_URL + "/verify")
                .header("Content-Type", "application/json")
                .header("accept", "application/json")
                .body(verifyRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    handleError(request, response);
                }).body(String.class);
        try {
            final JsonNode rootNode = objectMapper.readTree(resultBody);
            final JsonNode resultNode = rootNode.get("result");
            if(resultNode != null) {
                if (resultNode.isArray()) {
                    final List<JsonNode> results =  StreamSupport
                            .stream(resultNode.spliterator(), false)
                            .toList();
                    if(results.size() != 1) {
                        throw new RuntimeException("Expected exactly one result, got " + results.size());
                    }
                    final JsonNode result = results.get(0);
                    final JsonNode statusNode = result.get("status");
                    if(statusNode != null) {
                        if(statusNode.asText().equals("perfect")) {
                            return ContractVerificationState.FULL;
                        } else if(statusNode.asText().equals("false")) {
                            return ContractVerificationState.NONE;
                        }  else {
                            throw new RuntimeException("Status is not success: " + statusNode.asText());
                        }
                    } else {
                        throw new RuntimeException("No status in response");
                    }
                } else {
                    throw new RuntimeException("Result is not an array");
                }
            } else {
                throw new RuntimeException("No result in response");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in ready response", e);
        }
    }

    @Override
    public ContractVerificationState checkVerification(ContractId contractId) {
        checkSupportedNetwork();

        final String uri = CONTRACT_VERIFICATION_URL + "/check-by-addresses" + "?addresses=" + contractId.toSolidityAddress() + "&chainIds=" + getChainId();

        final String resultBody = restClient.get()
                .uri(uri)
                .header("accept", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    handleError(request, response);
                }).body(String.class);
        try {
            final JsonNode rootNode = objectMapper.readTree(resultBody);
            if (rootNode.isArray()) {
                final List<JsonNode> results = StreamSupport
                        .stream(rootNode.spliterator(), false)
                        .toList();
                if (results.size() != 1) {
                    throw new RuntimeException("Expected exactly one result, got " + results.size());
                }
                final JsonNode result = results.get(0);
                final JsonNode statusNode = result.get("status");
                if (statusNode != null) {
                    if (statusNode.asText().equals("perfect")) {
                        return ContractVerificationState.FULL;
                    } else if(statusNode.asText().equals("false")) {
                        return ContractVerificationState.NONE;
                    } else {
                        throw new RuntimeException("Status is not success: " + statusNode.asText());
                    }
                } else {
                    throw new RuntimeException("No status in response");
                }
            } else {
                throw new RuntimeException("Result is not an array");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in ready response", e);
        }
    }

    @Override
    public boolean checkVerification(ContractId contractId, String fileName, String fileContent) {
        checkSupportedNetwork();

        final ContractVerificationState state = checkVerification(contractId);
        if(state != ContractVerificationState.FULL) {
            throw new IllegalStateException("Contract is not verified");
        }

        final String uri = CONTRACT_VERIFICATION_URL + "/files/" + getChainId() + "/" + contractId.toSolidityAddress();

        final String resultBody = restClient.get()
                .uri(uri)
                .header("accept", "application/json")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    handleError(request, response);
                }).body(String.class);
        try {
            final JsonNode rootNode = objectMapper.readTree(resultBody);
            if (rootNode.isArray()) {
                final List<JsonNode> results = StreamSupport
                        .stream(rootNode.spliterator(), false)
                        .filter(node -> node.get("name").asText().equals(fileName))
                        .toList();
                if (results.size() != 1) {
                    throw new RuntimeException("Expected exactly one result, got " + results.size());
                }
                final JsonNode result = results.get(0);
                final JsonNode contentNode = result.get("content");
                if (contentNode != null) {
                    return contentNode.asText().equals(fileContent);
                } else {
                    throw new RuntimeException("No content in response");
                }
            } else {
                throw new RuntimeException("Result is not an array");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error in ready response", e);
        }
    }
}
