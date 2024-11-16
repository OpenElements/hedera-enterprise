module com.openelements.hiero.base {
    exports com.openelements.hiero.base;
    exports com.openelements.hiero.base.protocol;
    exports com.openelements.hiero.base.mirrornode;

    requires sdk; //Hedera SDK
    requires org.slf4j;
    requires com.google.protobuf; //TODO: We should not have the need to use it
    requires static org.jspecify;
}