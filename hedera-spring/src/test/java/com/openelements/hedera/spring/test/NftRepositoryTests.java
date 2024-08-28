package com.openelements.hedera.spring.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hedera.base.Account;
import com.openelements.hedera.base.AccountClient;
import com.openelements.hedera.base.Nft;
import com.openelements.hedera.base.NftClient;
import com.openelements.hedera.base.NftRepository;
import com.openelements.hedera.base.mirrornode.Page;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class NftRepositoryTests {

    @Autowired
    private NftClient nftClient;

    @Autowired
    private NftRepository nftRepository;

    @Autowired
    private HederaTestUtils hederaTestUtils;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private Account adminAccount;

    private <T> List<T> getAll(Page<T> page) {
        if (!page.isFirst()) {
            final Page<T> first = page.first();
            return getFromDataSliceTillEnd(first);
        }
        final List<T> data = new ArrayList<>();
        data.addAll(page.getData());
        if (page.hasNext()) {
            final Page<T> next = page.next();
            data.addAll(getFromDataSliceTillEnd(next));
        }
        return data;
    }

    private <T> List<T> getFromDataSliceTillEnd(Page<T> page) {
        final List<T> data = new ArrayList<>();
        data.addAll(page.getData());
        if (page.hasNext()) {
            final Page<T> next = page.next();
            data.addAll(getFromDataSliceTillEnd(next));
        }
        return data;
    }

    @Test
    void findByTokenId() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final byte[] metadata1 = "https://example.com/metadata1".getBytes(StandardCharsets.UTF_8);
        final byte[] metadata2 = "https://example.com/metadata2".getBytes(StandardCharsets.UTF_8);
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final List<Long> serial = nftClient.mintNfts(tokenId, metadata1, metadata2);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Page<Nft> slice = nftRepository.findByType(tokenId);
        final List<Nft> result = getAll(slice);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(0)));
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(1)));
    }

    @Test
    void findByTokenIdForSomePages() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final List<byte[]> metadata = IntStream.range(0, 40)
                .mapToObj(i -> "metadata" + i)
                .map(s -> s.getBytes(StandardCharsets.UTF_8))
                .toList();
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final int batchSize = 10;
        for (int i = 0; i < metadata.size(); i += batchSize) {
            final int start = i;
            final int end = Math.min(i + batchSize, metadata.size());
            nftClient.mintNfts(tokenId, metadata.subList(start, end).toArray(new byte[0][]));
        }
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Page<Nft> slice = nftRepository.findByType(tokenId);
        final List<Nft> result = getAll(slice);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(metadata.size(), result.size());
    }

    @Test
    void findByTokenIdForManyTokens() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final List<byte[]> metadata = IntStream.range(0, 400)
                .mapToObj(i -> "metadata" + i)
                .map(s -> s.getBytes(StandardCharsets.UTF_8))
                .toList();
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final int batchSize = 10;
        for (int i = 0; i < metadata.size(); i += batchSize) {
            final int start = i;
            final int end = Math.min(i + batchSize, metadata.size());
            nftClient.mintNfts(tokenId, metadata.subList(start, end).toArray(new byte[0][]));
        }
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Page<Nft> slice = nftRepository.findByType(tokenId);
        final List<Nft> result = getAll(slice);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(metadata.size(), result.size());
    }

    @Test
    void findByTokenIdWithZeroResult() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Page<Nft> slice = nftRepository.findByType(tokenId);
        final List<Nft> result = getAll(slice);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void findByAccountId() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final byte[] metadata1 = "https://example.com/metadata1".getBytes(StandardCharsets.UTF_8);
        final byte[] metadata2 = "https://example.com/metadata2".getBytes(StandardCharsets.UTF_8);
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final List<Long> serial = nftClient.mintNfts(tokenId, metadata1, metadata2);
        final AccountId adminAccountId = adminAccount.accountId();
        final PrivateKey adminAccountPrivateKey = adminAccount.privateKey();
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        final PrivateKey newOwnerPrivateKey = account.privateKey();
        nftClient.associateNft(tokenId, newOwner, newOwnerPrivateKey);
        nftClient.transferNft(tokenId, serial.get(0), adminAccountId, adminAccountPrivateKey, newOwner);
        nftClient.transferNft(tokenId, serial.get(1), adminAccountId, adminAccountPrivateKey, newOwner);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final List<Nft> result = nftRepository.findByOwner(newOwner);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(0)));
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(1)));
    }

    @Test
    void findByAccountIdWIthZeroResult() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        final PrivateKey newOwnerPrivateKey = account.privateKey();
        nftClient.associateNft(tokenId, newOwner, newOwnerPrivateKey);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final List<Nft> result = nftRepository.findByOwner(newOwner);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }


    @Test
    void findByTokenIdAndAccountId() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final byte[] metadata1 = "https://example.com/metadata1".getBytes(StandardCharsets.UTF_8);
        final byte[] metadata2 = "https://example.com/metadata2".getBytes(StandardCharsets.UTF_8);
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final List<Long> serial = nftClient.mintNfts(tokenId, metadata1, metadata2);
        final AccountId adminAccountId = adminAccount.accountId();
        final PrivateKey adminAccountPrivateKey = adminAccount.privateKey();
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        final PrivateKey newOwnerPrivateKey = account.privateKey();
        nftClient.associateNft(tokenId, newOwner, newOwnerPrivateKey);
        nftClient.transferNft(tokenId, serial.get(0), adminAccountId, adminAccountPrivateKey, newOwner);
        nftClient.transferNft(tokenId, serial.get(1), adminAccountId, adminAccountPrivateKey, newOwner);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final List<Nft> result = nftRepository.findByOwnerAndType(newOwner, tokenId);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(0)));
        Assertions.assertTrue(result.stream().anyMatch(nft -> nft.serial() == serial.get(1)));
    }

    @Test
    void findByTokenIdAndAccountIdWithZeroResult() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        final PrivateKey newOwnerPrivateKey = account.privateKey();
        nftClient.associateNft(tokenId, newOwner, newOwnerPrivateKey);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final List<Nft> result = nftRepository.findByOwnerAndType(newOwner, tokenId);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void findByTokenIdAndSerial() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final byte[] metadata = "https://example.com/metadata1".getBytes(StandardCharsets.UTF_8);
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final long serial = nftClient.mintNft(tokenId, metadata);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Optional<Nft> result = nftRepository.findByTypeAndSerial(tokenId, serial);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertArrayEquals(metadata, result.get().metadata());
        Assertions.assertEquals(serial, result.get().serial());
        Assertions.assertEquals(tokenId, result.get().tokenId());
    }

    @Test
    void findByTokenIdAndSerialWithZeroResult() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final String metadata = "https://example.com/metadata1";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Optional<Nft> result = nftRepository.findByTypeAndSerial(tokenId, 10);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void findByTokenIdAndAccountIdAndSerial() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final byte[] metadata = "https://example.com/metadata1".getBytes(StandardCharsets.UTF_8);
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final long serial = nftClient.mintNft(tokenId, metadata);
        final AccountId adminAccountId = adminAccount.accountId();
        final PrivateKey adminAccountPrivateKey = adminAccount.privateKey();
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        final PrivateKey newOwnerPrivateKey = account.privateKey();
        nftClient.associateNft(tokenId, newOwner, newOwnerPrivateKey);
        nftClient.transferNft(tokenId, serial, adminAccountId, adminAccountPrivateKey, newOwner);
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Optional<Nft> result = nftRepository.findByOwnerAndTypeAndSerial(newOwner, tokenId, serial);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertArrayEquals(metadata, result.get().metadata());
        Assertions.assertEquals(serial, result.get().serial());
        Assertions.assertEquals(tokenId, result.get().tokenId());
    }

    @Test
    void findByTokenIdAndAccountIdAndSerialWithZeroResult() throws Exception {
        //given
        final String name = "Tokemon cards";
        final String symbol = "TOK";
        final TokenId tokenId = nftClient.createNftType(name, symbol);
        final Account account = accountClient.createAccount();
        final AccountId newOwner = account.accountId();
        hederaTestUtils.waitForMirrorNodeRecords();

        //when
        final Optional<Nft> result = nftRepository.findByOwnerAndTypeAndSerial(newOwner, tokenId, 10);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPresent());
    }
}
