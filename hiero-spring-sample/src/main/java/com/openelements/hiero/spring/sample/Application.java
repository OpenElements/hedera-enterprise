package com.openelements.hiero.spring.sample;

import com.openelements.hiero.spring.EnableHedera;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableHedera
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
