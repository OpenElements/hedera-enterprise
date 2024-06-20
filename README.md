# spring-hedera
Module for Spring that adds support to communicate with the [Hedera network](https://hedera.com). 
This module is based on the [Hedera Java SDK](https://github.com/hashgraph/hedera-sdk-java) and [Spring Boot](https://spring.io/projects/spring-boot).

> [!WARNING]  
> This project has just been started and is not yet ready for production use.

## Getting Started

To use this module, you need to add the following dependency to your project:

```xml 
<dependency>
    <groupId>com.open-elements.spring</groupId>
    <artifactId>spring-hedera</artifactId>
    <version>VERSION</version> 
</dependency>
```

## Configuration

To configure the module, you need to add the following properties to your application.properties file:

```properties
spring.hedera.accountId=0.0.53854625
spring.hedera.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
spring.hedera.network=testnet
```

The account information (accountId, privateKey, publicKey) can all be found at the
[Hedera portal](https://portal.hedera.com/) for a testnet or previewnet account.
Today only the "DER Encoded Private Key" of the "ECDSA" key type is supported for the `spring.hedera.privateKey` property.

## Usage

To use the module, you need to add the `@EnableHedera` annotation to your Spring Boot application class.

```java
import com.open.elements.spring.hedera.EnableHedera;

@SpringBootApplication
@EnableHedera
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
 
Once that is done you can autowire the `HederaClient` class and call the methods to interact with the Hedera network.

```java

@Service
public class HederaAccountService {

    @Autowired
    private HederaClient hederaClient;

    public HBars getBalance(String accountId) {
        AccountBalanceRequest request = AccountBalanceRequest.of("0.0.2237621");
        AccountBalanceResult result = hederaClient.execute(request);
        return result.hbars();
    }
}
```

All APIs of the client are synchronous and return the result of the operation. For asynchronous operations, you can
easily wrap calls by use the [`@Async` annotation of spring](https://spring.io/guides/gs/async-method).

## Built the project

The project is based on [Maven](https://maven.apache.org/). To build the project, you can use the following command:

```shell
./mvnw verify
```

The tests in the project are working against the Hedera testnet.
To run the tests, you need to provide the "DER Encoded Private Key" of the "ECDSA" testnet account in a `.env` file in
the root of the project. The file should look like this:


```
spring.hedera.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
