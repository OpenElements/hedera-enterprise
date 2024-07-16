package com.openelements.hedera.base.protocol;

import com.hedera.hashgraph.sdk.FileId;

public record FileContentsResponse(FileId fileId, byte[] contents) {
}
