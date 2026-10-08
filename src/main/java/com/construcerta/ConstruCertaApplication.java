package com.construcerta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ConstruCertaApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConstruCertaApplication.class, args);
    }
}
