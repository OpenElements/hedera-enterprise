package com.openelements.hedera.spring.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.NftClient;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class NftClientTests {

    @Autowired
    private NftClient nftClient;

    @Autowired
    private AccountClient accountClient;

    @Test
    void createNftType() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";

        //when
        final TokenId tokenId = nftClient.createNftType(name, symbol);

        //then
        Assertions.assertNotNull(tokenId);
    }

    @Test
    void mintNft() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final String metadata = "https://example.com/metadata";
        final TokenId tokenId = nftClient.createNftType(name, symbol);

        //when
        final long serial = nftClient.mintNft(tokenId, metadata);

        //then
        Assertions.assertTrue(serial > 0);
    }

    @Test
    void mintNfts() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final String metadata1 = "https://example.com/metadata1";
        final String metadata2 = "https://example.com/metadata2";
        final TokenId tokenId = nftClient.createNftType(name, symbol);

        //when
        final List<Long> serial = nftClient.mintNfts(tokenId, List.of(metadata1, metadata2));

        //then
        Assertions.assertEquals(2, serial.size());
    }

    @Test
    void associateNft() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final Account userAccount = accountClient.createAccount(1);

        //then
        Assertions.assertDoesNotThrow(() -> {
            nftClient.associateNft(tokenId, userAccount.accountId(), userAccount.privateKey());
        });
    }

    @Test
    void transferNft() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final Account treasuryAccount = accountClient.createAccount(1);
        final TokenId tokenId = nftClient.createNftType(name, symbol, treasuryAccount.accountId(), treasuryAccount.privateKey());
        final Account userAccount = accountClient.createAccount(1);
        nftClient.associateNft(tokenId, userAccount.accountId(), userAccount.privateKey());
        final String metadata = "https://example.com/metadata";
        final long serial = nftClient.mintNft(tokenId, metadata, treasuryAccount.privateKey());

        //then
        Assertions.assertDoesNotThrow(() -> {
            nftClient.transferNft(tokenId, serial, treasuryAccount.accountId(), treasuryAccount.privateKey(), userAccount.accountId());
        });
    }

    @Test
    void mintNftByNewUserAndTransferByAnotherUser() throws Exception {
        //given
        final String name = "Test NFT";
        final String symbol = "TST";
        final Account treasuryAccount = accountClient.createAccount();
        final Account supplierAccount = accountClient.createAccount();
        final TokenId tokenId = nftClient.createNftType(name, symbol, treasuryAccount, supplierAccount.privateKey());

        final Account userAccount = accountClient.createAccount();
        nftClient.associateNft(tokenId, userAccount);

        final String metadata = "https://example.com/metadata";
        final long serial = nftClient.mintNft(tokenId, metadata, supplierAccount.privateKey());

        //then
        Assertions.assertDoesNotThrow(() -> {
            nftClient.transferNft(tokenId, serial, treasuryAccount, userAccount.accountId());
        });
    }
}
