# Hedera Enterprise
This project provides Java modules to integrate [Hedera network](https://hedera.com) smoothless in a Spring Boot or Microprofile (like Quarkus) application.
This module is based on the [Hedera Java SDK](https://github.com/hashgraph/hedera-sdk-java).

> [!WARNING]  
> This project has just been started and is not yet ready for production use.

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

## Managed services

The module provides a set of managed services that can be used to interact with the Hedera network.
The following services are available in spring and microprofile:

- `com.openelements.hedera.base.AccountClient`: to interact with Hedera accounts
- `com.openelements.hedera.base.FileClient`: to interact with Hedera files
- `com.openelements.hedera.base.SmartContractClient`: to interact with Hedera smart contracts
- `com.openelements.hedera.base.ContractVerificationClient`: to verify smart contracts
- `com.openelements.hedera.base.protocol.ProtocolLayerClient`: to interact with the Hedera protocol layer

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
That file is added to the `.gitignore` file and can not be committed to the repository.
If you have created the `.env` file, you can simply load the environment variables with the following command:

```shell
export $(grep -v '^#' .env | xargs)
```

To create a release, you can use the following commands:

```shell
./mvnw versions:set -DnewVersion=0.1.0
./mvnw clean verify
git commit -am "Version 0.1.0"
git push
./mvnw -Ppublication deploy
./mvnw -Ppublication jreleaser:full-release
./mvnw versions:set -DnewVersion=0.2.0-SNAPSHOT
git commit -am "Version 0.2.0-SNAPSHOT"
git push
```

This will create a release on GitHub and publish the artifacts to Maven Central.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
