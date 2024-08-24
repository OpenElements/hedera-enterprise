package com.openelements.hedera.spring.implementation;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openelements.hedera.base.mirrornode.Page;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

public class RestBasedPage<T> implements Page<T> {

    private final static Logger log = LoggerFactory.getLogger(RestBasedPage.class);


    private final ObjectMapper objectMapper;

    private final RestClient restClient;

    private final Function<JsonNode, List<T>> dataExtractionFunction;

    private final Function<JsonNode, URI> nextUriExtractionFunction;

    private final int number;

    private final List<T> data;

    private final URI nextUri;

    private final URI firstUri;

    private final URI currentUri;

    public RestBasedPage(final @NonNull ObjectMapper objectMapper, final @NonNull RestClient restClient,
            final @NonNull URI uri,
            final @NonNull Function<JsonNode, List<T>> dataExtractionFunction,
            final @NonNull Function<JsonNode, URI> nextUriExtractionFunction) {
        this(objectMapper, restClient, uri, 0, dataExtractionFunction, nextUriExtractionFunction, uri);
    }

    public RestBasedPage(final @NonNull ObjectMapper objectMapper, final @NonNull RestClient restClient,
            final @NonNull URI uri, int number,
            final @NonNull Function<JsonNode, List<T>> dataExtractionFunction,
            final @NonNull Function<JsonNode, URI> nextUriExtractionFunction,
            final @NonNull URI firstUri) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
        this.restClient = Objects.requireNonNull(restClient, "restClient must not be null");
        this.dataExtractionFunction = Objects.requireNonNull(dataExtractionFunction,
                "dataExtractionFunction must not be null");
        this.nextUriExtractionFunction = Objects.requireNonNull(nextUriExtractionFunction,
                "nextUriExtractionFunction must not be null");
        this.firstUri = Objects.requireNonNull(firstUri, "firstUri must not be null");
        this.currentUri = Objects.requireNonNull(uri, "uri must not be null");
        this.number = number;
        if (number < 0) {
            throw new IllegalArgumentException("number must be non-negative");
        }
        log.debug("Fetching data from URI: {}", uri);
        final ResponseEntity<String> response = restClient.get()
                .uri(uri).accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(String.class);
        final HttpStatusCode statusCode = response.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            throw new IllegalStateException("HTTP status code: " + statusCode);
        }
        final String body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("Response body is null");
        }
        try {
            final JsonNode jsonNode = objectMapper.readTree(body);
            data = Collections.unmodifiableList(dataExtractionFunction.apply(jsonNode));
            nextUri = nextUriExtractionFunction.apply(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public boolean hasNext() {
        return nextUri != null;
    }

    @Override
    public Page<T> next() {
        if (nextUri == null) {
            throw new IllegalStateException("No next URI");
        }
        return new RestBasedPage<>(objectMapper, restClient, nextUri, number + 1, dataExtractionFunction,
                nextUriExtractionFunction, firstUri);
    }

    @Override
    public Page<T> first() {
        return new RestBasedPage<>(objectMapper, restClient, firstUri, dataExtractionFunction,
                nextUriExtractionFunction);
    }

    @Override
    public boolean isFirst() {
        return Objects.equals(firstUri, currentUri);
    }
}
