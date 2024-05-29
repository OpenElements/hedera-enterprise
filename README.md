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
spring.hedera.privateKey=3030020100300706052b8104000a04220420c23ff08c429aa5a1d80bb300f436dd89adc5a4aa9a4544d7f3b00b2045c6cc37
spring.hedera.publicKey=302d300706052b8104000a03220003cd09e0aaafe0d4a7c602f581aa00202c5aa4ffbae9b96f479fc1db36f4594a17
spring.hedera.network=testnet
```

The account information (accountId, privateKey, publicKey) can all be found at the
[Hedera portal](https://portal.hedera.com/) for a testnet or previewnet account.

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

import com.open.elements.spring.hedera.HederaClient;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.crypto.TransferTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HederaAccountService {

    @Autowired
    private HederaClient hederaClient;

    public HBars transfer(String accountId) {
        AccountBalanceQuery query = ...
        AccountBalanceResult result =hederaClient.execute(query);
        return result.hbars();
    }
}
```

All APIs of the client are synchronous and return the result of the operation. For asynchronous operations, you can
easily wrap calls by use the [`@Async` annotation of spring](https://spring.io/guides/gs/async-method).

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
