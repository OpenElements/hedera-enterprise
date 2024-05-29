package com.openelements.spring.hedera;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.openelements.spring.hedera.implementation.HederaAutoConfiguration;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(TYPE)
@Retention(RUNTIME)
@Import(HederaAutoConfiguration.class)
public @interface EnableHedera {
}
