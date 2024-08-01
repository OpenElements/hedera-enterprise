module com.openelements.hedera.base {
    exports com.openelements.hedera.base;
    exports com.openelements.hedera.base.protocol;
    exports com.openelements.hedera.base.mirrornode;
    exports com.openelements.hedera.base.implementation.data to com.openelements.hedera.base.test;
    exports com.openelements.hedera.base.implementation to com.openelements.hedera.base.test;

    requires sdk; //Hedera SDK
    requires org.slf4j;
    requires com.google.protobuf; //TODO: We should not have the need to use it
    requires static com.github.spotbugs.annotations;
}