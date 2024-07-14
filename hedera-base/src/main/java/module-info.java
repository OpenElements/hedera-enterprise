module com.openelements.hedera.base {
    exports com.openelements.hedera.base;

    requires sdk; //Hedera SDK
    requires org.slf4j;
    requires com.google.protobuf; //TODO: We should not have the need to use it
    requires static com.github.spotbugs.annotations;
}