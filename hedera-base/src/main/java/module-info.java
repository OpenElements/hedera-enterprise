module com.openelements.hedera.base {
    exports com.openelements.hedera.base;

    requires sdk; //Hedera SDK
    requires org.slf4j;
    requires com.google.protobuf;
    requires static com.github.spotbugs.annotations;
}