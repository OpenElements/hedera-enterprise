package com.openelements.hedera.base;

import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Represents an exception that occurred while interacting with the Hedera network.
 */
public class HederaException extends Exception {

    /**
     * Constructs a new HederaException with the specified detail message.
     * @param message The detail message.
     */
    public HederaException(@NonNull String message) {
        super(message);
    }

    /**
     * Constructs a new HederaException with the specified detail message and cause.
     * @param message The detail message.
     * @param cause The cause.
     */
    public HederaException(@NonNull String message, @NonNull Throwable cause) {
        super(message, cause);
    }
}
