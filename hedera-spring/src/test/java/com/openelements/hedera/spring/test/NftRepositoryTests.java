package com.openelements.hedera.spring.test;

import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.HederaException;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class NftRepositoryTests {

    @Autowired
    private NftClient nftClient;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private HederaTestUtils hederaTestUtils;

    @Test
    void findByTokenId() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final String metadata1 = "https://example.com/metadata1";
        final String metadata2 = "https://example.com/metadata2";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final List<Long> serial = nftClient.mintNfts(tokenId, List.of(metadata1, metadata2));
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final List<Nft> result = nftRepository.findByType(tokenId);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(0)));
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(1)));
    }
}
