package com.openelements.spring.hedera.test;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hedera.base.HederaClient;
import com.openelements.hedera.base.data.ContractParam;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfig.class)
public class DatatypesTest {

    @Autowired
    private HederaClient hederaClient;

    private static ContractId contractId;

    private synchronized ContractId getOrCreateContractId() {
        if(contractId == null) {
                try {
                    final Path path = Path.of(ContractServiceTest.class.getResource("/datatypes.bin").getPath());
                    contractId = hederaClient.createContract(path);
                } catch (Exception e) {
                    throw new RuntimeException("Can not create contract", e);
                }
        }
        return contractId;
    }

    @Test
    public void testString() throws Exception {
        //given
        final ContractId contract = getOrCreateContractId();
        final String expected = "Hello, World!";

        //when
        final ContractFunctionResult result = hederaClient.callContractFunction(contract, "checkString",
                ContractParam.string(expected));

        //then
        Assertions.assertEquals(expected, result.getString(0));
    }

    @Test
    public void testAddress() throws Exception {
        //given
        final ContractId contract = getOrCreateContractId();
        final String expected = contract.toString();

        //when
        final ContractFunctionResult result = hederaClient.callContractFunction(contract, "checkString",
                ContractParam.address(expected));

        //then
        Assertions.assertEquals(expected, result.getAddress(0));
    }

    @ParameterizedTest
    @MethodSource("provideIntArguments")
    public <T> void testLongTypes(String functionName, long value, ContractParam inputParam, Function<ContractFunctionResult, Long> resultExtractor) throws Exception {
        //given
        final ContractId contract = getOrCreateContractId();

        //when
        final ContractFunctionResult result = hederaClient.callContractFunction(contract, functionName, inputParam);

        //then
        Assertions.assertEquals(value, resultExtractor.apply(result));
    }

    @ParameterizedTest
    @MethodSource("provideBigIntegerArguments")
    public <T> void testBigIntegerTypes(String functionName, BigInteger value, ContractParam inputParam, Function<ContractFunctionResult, BigInteger> resultExtractor) throws Exception {
        //given
        final ContractId contract = getOrCreateContractId();

        //when
        final ContractFunctionResult result = hederaClient.callContractFunction(contract, functionName, inputParam);

        //then
        Assertions.assertEquals(value, resultExtractor.apply(result));
    }

    static Stream<Arguments> provideIntArguments() {
        return Stream.of(
                Arguments.of("checkInt8", 7, ContractParam.int8((byte)7), (Function<ContractFunctionResult, Long>) r -> (long) r.getInt8(0)),
                Arguments.of("checkUint8", 7, ContractParam.uint8((byte)7), (Function<ContractFunctionResult, Long>) r -> (long) r.getUint8(0)),
                Arguments.of("checkInt32", 123456789, ContractParam.int32(123456789), (Function<ContractFunctionResult, Long>) r -> (long) r.getInt32(0)),
                Arguments.of("checkUint32", 123456789L, ContractParam.uint32(123456789L), (Function<ContractFunctionResult, Long>) r -> (long) r.getUint32(0)),
                Arguments.of("checkInt64", 1234567890123456789L, ContractParam.int64(1234567890123456789L), (Function<ContractFunctionResult, Long>) r -> (long) r.getInt64(0))
        );
    }

    static Stream<Arguments> provideBigIntegerArguments() {
        return Stream.of(
                Arguments.of("checkInt256", new BigInteger("1234567890123456789012345678901234567890123456789012345678901234567890"), ContractParam.int256(new BigInteger("1234567890123456789012345678901234567890123456789012345678901234567890")), (Function<ContractFunctionResult, BigInteger>) r -> r.getInt256(0)),
                Arguments.of("checkUint256", new BigInteger("1234567890123456789012345678901234567890123456789012345678901234567890"), ContractParam.uint256(new BigInteger("1234567890123456789012345678901234567890123456789012345678901234567890")), (Function<ContractFunctionResult, BigInteger>) r -> r.getUint256(0))
        );
    }
}
