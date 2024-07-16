package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.FileId;
import java.time.Instant;

public record FileInfoResponse(FileId fileId, int size, boolean deleted, Instant expirationTime){
}
