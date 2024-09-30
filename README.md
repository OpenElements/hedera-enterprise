# Hedera Enterprise
This project provides Java modules to integrate [Hedera network](https://hedera.com) smoothless in a Spring Boot or Microprofile (like Quarkus) application.
This module is based on the [Hedera Java SDK](https://github.com/hashgraph/hedera-sdk-java).

## Spring Boot support

To use this module, you need to add the following dependency to your project:

```xml 
<dependency>
    <groupId>com.open-elements.hedera</groupId>
    <artifactId>hedera-spring</artifactId>
    <version>VERSION</version> 
</dependency>
```

### Configuration

To configure the module, you need to add the following properties to your application.properties file:

```properties
spring.hedera.accountId=0.0.53854625
spring.hedera.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
spring.hedera.network=testnet
```

The account information (accountId, privateKey, publicKey) can all be found at the
[Hedera portal](https://portal.hedera.com/) for a testnet or previewnet account.
Today only the "DER Encoded Private Key" of the "ECDSA" key type is supported for the `spring.hedera.privateKey` property.

The 2 properties `spring.hedera.accountId` and `spring.hedera.privateKey` define the so called "operation account".
The operational account is used as the account that sends all transactions against the Hedera network.

### Usage

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
 
Once that is done you can for example autowire the `FileClient` class and call the methods to interact with the Hedera network.

```java

@Service
public class HederaAccountService {

    @Autowired
    private FileClient fileClient;

    public String readFile(String id) {
        FileId fileId = FileId.of(id);
        byte[] content = fileClient.readFile(fileId);
        return new String(content);
    }
}
```

All APIs of the client are synchronous and return the result of the operation. For asynchronous operations, you can
easily wrap calls by use the [`@Async` annotation of spring](https://spring.io/guides/gs/async-method).

### Hedera Spring Sample

A sample application that uses the Hedera Spring module can be found in the `hedera-spring-sample` module.
The sample application is a simple Spring Boot application that reads has a REST endpoint at `localhost:8080/` and 
shows the hbar balance of the account `0.0.100`.
To use the application, you need to have created a Hedera testnet account at the [Hedera portal](https://portal.hedera.com/).
The account information can be added to the `application.properties` file in the `hedera-spring-sample` module:
```properties
spring.hedera.accountId=0.0.3447271
spring.hedera.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

Alternatively, you can provide the account information as environment variables:
```shell
export HEDERA_ACCOUNT_ID=0.0.3447271
export HEDERA_PRIVATE_KEY=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

## Microservice support

The support for Microprofile is still in development and can not be used yet.

## Managed services

The module provides a set of managed services that can be used to interact with the Hedera network.
The following services are available in spring and microprofile:

- `com.openelements.hedera.base.AccountClient`: to interact with Hedera accounts
- `com.openelements.hedera.base.FileClient`: to interact with Hedera files
- `com.openelements.hedera.base.SmartContractClient`: to interact with Hedera smart contracts
- `com.openelements.hedera.base.ContractVerificationClient`: to verify smart contracts
- `com.openelements.hedera.base.NftClient`: to interact with Hedera NFTs
- `com.openelements.hedera.base.NftRepository`: to query NFTs

Next to that the following low-level services are available:

- `com.openelements.hedera.base.protocol.ProtocolLayerClient`: to interact with the Hedera protocol layer
- `com.openelements.hedera.base.mirrornode.MirrorNodeClient`: to query the Hedera mirror node 

## Built the project

The project is based on [Maven](https://maven.apache.org/). To build the project, you can use the following command:

```shell
./mvnw verify
```

The tests in the project are working against the Hedera testnet.
To run the tests, you need to provide the account id and the "DER Encoded Private Key" of the "ECDSA" testnet account.
That information can be provided as environemt variables:
 
```shell
export HEDERA_ACCOUNT_ID=0.0.3447271
export HEDERA_PRIVATE_KEY=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
export HEDERA_NETWORK=testnet
```

As an alternative you can define the information in a `.env` file in each sub-module that contains tests.
The file should look like this:

```
spring.hedera.accountId=0.0.3447271
spring.hedera.privateKey=2130020100312346052b8104400304220420c236508c429395a8180b1230f436d389adc5afaa9145456783b57b2045c6cc37
```

### Create a release

The project is using the [JReleaser](https://jreleaser.org) Maven plugin to create and publish releases.
To use the plugin, you need to provide the following environment variables:

```
JRELEASER_GITHUB_TOKEN=your_secret_github_token
JRELEASER_GPG_SECRET_KEY=your_secret_key
JRELEASER_GPG_PASSPHRASE=your_secret_passphrase
JRELEASER_GPG_PUBLIC_KEY=yout_public_key
JRELEASER_NEXUS2_MAVEN_CENTRAL_USERNAME=your_maven_central_username
JRELEASER_NEXUS2_MAVEN_CENTRAL_TOKEN=your_secret_maven_central_token
```

How that environment variables are defined can be found in the
[JReleaser documentation](https://jreleaser.org/guide/latest/examples/maven/maven-central.html) and 
[this blog post](https://foojay.io/today/how-to-release-a-java-module-with-jreleaser-to-maven-central-with-github-actions/).

On a unix based system the environment variables can be defined in the `.env` file in the root of the project.

Once that is done you can use the `release.sh` script in the root folder of the repos to create a release:

```shell
./release 0.1.0 0.2.0-SNAPSHOT
```

This will create a release on GitHub and publish the artifacts to Maven Central.
As you can see 2 params are passed to the `release.sh` script, The first param defines the version for the release and the second param defines the version after the release.
In the given example the project has defined version 0.1.0-SNAPSHOT before the script is executed.
The execution will release the current code under version 0.1.0 and later switch the version to 0.2.0-SNAPSHOT and commit it.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
