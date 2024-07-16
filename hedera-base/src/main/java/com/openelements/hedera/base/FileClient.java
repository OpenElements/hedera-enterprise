package com.openelements.hedera.base;

import com.hedera.hashgraph.sdk.FileId;
import edu.umd.cs.findbugs.annotations.NonNull;
import java.time.Instant;
import java.util.Objects;

/**
 * A client for interacting with the file service on the Hedera network.
 */
public interface FileClient {

    /**
     * Create a new file with the given contents.
     * @param contents the contents of the file
     * @return the ID of the new file
     * @throws HederaException if the file could not be created
     */
    @NonNull
    FileId createFile(@NonNull byte[] contents) throws HederaException;

    FileId createFile(@NonNull byte[] contents, @NonNull Instant expirationTime) throws HederaException;

    /**
     * Create a new file with the given contents.
     * @param fileId the ID of the file to update
     * @return the ID of the new file
     * @throws HederaException if the file could not be created
     */
    @NonNull
    default byte[] readFile(@NonNull String fileId) throws HederaException {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return readFile(FileId.fromString(fileId));
    }

    /**
     * Read the contents of a file.
     * @param fileId the ID of the file to read
     * @return the contents of the file
     * @throws HederaException if the file could not be read
     */
    @NonNull
    byte[] readFile(@NonNull FileId fileId) throws HederaException;

    /**
     * Delete a file.
     * @param fileId the ID of the file to delete
     * @throws HederaException if the file could not be deleted
     */
    default void deleteFile(@NonNull String fileId) throws HederaException {
        deleteFile(FileId.fromString(fileId));
    }

    /**
     * Delete a file.
     * @param fileId the ID of the file to delete
     * @throws HederaException if the file could not be deleted
     */
    void deleteFile(@NonNull FileId fileId) throws HederaException;

    void updateFile(@NonNull FileId fileId, byte[] content) throws HederaException;

    void updateExpirationTime(@NonNull FileId fileId, @NonNull Instant expirationTime) throws HederaException;

    boolean isDeleted(@NonNull FileId fileId) throws HederaException;

    int getSize(@NonNull FileId fileId) throws HederaException;

    Instant getExpirationTime(@NonNull FileId fileId) throws HederaException;
}
