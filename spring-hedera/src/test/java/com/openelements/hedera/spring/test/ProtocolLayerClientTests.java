package com.openelements.hedera.spring.test;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Status;
import com.openelements.hedera.base.protocol.AccountBalanceRequest;
import com.openelements.hedera.base.protocol.AccountBalanceResponse;
import com.openelements.hedera.base.protocol.ContractCreateRequest;
import com.openelements.hedera.base.protocol.ContractCreateResult;
import com.openelements.hedera.base.protocol.FileAppendRequest;
import com.openelements.hedera.base.protocol.FileContentsRequest;
import com.openelements.hedera.base.protocol.FileContentsResponse;
import com.openelements.hedera.base.protocol.FileCreateRequest;
import com.openelements.hedera.base.protocol.FileCreateResult;
import com.openelements.hedera.base.protocol.FileDeleteRequest;
import com.openelements.hedera.base.protocol.FileDeleteResult;
import com.openelements.hedera.base.protocol.FileInfoRequest;
import com.openelements.hedera.base.protocol.FileInfoResponse;
import com.openelements.hedera.base.protocol.FileUpdateRequest;
import com.openelements.hedera.base.protocol.ProtocolLayerClient;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class ProtocolLayerClientTests {

    @Autowired
    private ProtocolLayerClient protocolLayerClient;

    @Value("${spring.hedera.accountId}")
    private String accountId;

    @Test
    void testGetBalance() throws Exception {
        //given
        final AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of(accountId);

        //when
        final AccountBalanceResponse accountBalanceResult = protocolLayerClient.executeAccountBalanceQuery(
                accountBalanceRequest);

        //then
        Assertions.assertNotNull(accountBalanceResult);
        Assertions.assertNotNull(accountBalanceResult.hbars());
        Assertions.assertTrue(accountBalanceResult.hbars().toTinybars() > 0);
        System.out.println("Balance: " + accountBalanceResult.hbars().toString() + " HBARs");
    }

    @Test
    void testCreateFile() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileCreateRequest request = FileCreateRequest.of(contents);

        //when
        final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.transactionId());
        Assertions.assertEquals(Status.SUCCESS, result.status());
        Assertions.assertNotNull(result.fileId());
    }

    @Test
    void testFileContents() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileCreateRequest request = FileCreateRequest.of(contents);
        final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
        final FileId fileId = result.fileId();
        final FileContentsRequest contentsRequest = FileContentsRequest.of(fileId);

        //when
        final FileContentsResponse fileContentsResponse = protocolLayerClient.executeFileContentsQuery(contentsRequest);

        //then
        Assertions.assertNotNull(fileContentsResponse);
        Assertions.assertArrayEquals(contents, fileContentsResponse.contents());
    }

    @Test
    void testFileInfo() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileCreateRequest request = FileCreateRequest.of(contents);
        final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
        final FileId fileId = result.fileId();
        final FileInfoRequest infoRequest = FileInfoRequest.of(fileId);

        //when
        final FileInfoResponse infoResponse = protocolLayerClient.executeFileInfoQuery(infoRequest);

        //then
        Assertions.assertNotNull(infoResponse);
        Assertions.assertEquals(contents.length, infoResponse.size());
    }

    @Test
    void testFileDelete() throws Exception {
        //given
        final byte[] contents = "Hello, Hedera!".getBytes();
        final FileCreateRequest request = FileCreateRequest.of(contents);
        final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
        final FileId fileId = result.fileId();
        final FileDeleteRequest deleteRequest = FileDeleteRequest.of(fileId);

        //when
        final FileDeleteResult deleteResponse = protocolLayerClient.executeFileDeleteTransaction(deleteRequest);

        //then
        Assertions.assertNotNull(deleteResponse);
        Assertions.assertEquals(Status.SUCCESS, deleteResponse.status());
        Assertions.assertNotNull(deleteResponse.transactionId());
    }

    @Test
    void testContractCreate() throws Exception {
        //given
        final Path path = Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());
        final String content = Files.readString(path, StandardCharsets.UTF_8);
        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        final FileCreateRequest fileCreateRequest = FileCreateRequest.of(bytes);
        final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(fileCreateRequest);
        final FileId fileId = result.fileId();
        final ContractCreateRequest request = ContractCreateRequest.of(fileId);

        //when
        final ContractCreateResult contractCreateResult = protocolLayerClient.executeContractCreateTransaction(request);

        //then
        Assertions.assertNotNull(contractCreateResult);
        Assertions.assertEquals(Status.SUCCESS, contractCreateResult.status());
        Assertions.assertNotNull(contractCreateResult.transactionId());
        Assertions.assertNotNull(contractCreateResult.contractId());
    }

    @Test
    void testTooLargeFile() {
        //given
        final byte[] contents = new byte[FileCreateRequest.FILE_MAX_SIZE + 1];
        final FileId fakeId = FileId.fromString("1.2.3");

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> protocolLayerClient.executeFileCreateTransaction(FileCreateRequest.of(contents)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> protocolLayerClient.executeFileAppendRequestTransaction(FileAppendRequest.of(fakeId, contents)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> protocolLayerClient.executeFileUpdateRequestTransaction(FileUpdateRequest.of(fakeId, contents)));
    }

    @Test
    void testTooLargeFileForOneTransaction() {
        //given
        final byte[] contents = new byte[FileCreateRequest.FILE_CREATE_MAX_SIZE + 1];
        final FileId fakeId = FileId.fromString("1.2.3");

        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> protocolLayerClient.executeFileCreateTransaction(FileCreateRequest.of(contents)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> protocolLayerClient.executeFileAppendRequestTransaction(FileAppendRequest.of(fakeId, contents)));
        Assertions.assertThrows(IllegalArgumentException.class, () -> protocolLayerClient.executeFileUpdateRequestTransaction(FileUpdateRequest.of(fakeId, contents)));
    }
}
