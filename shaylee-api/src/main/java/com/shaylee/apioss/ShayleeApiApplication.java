package com.shaylee.apioss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ShayleeApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShayleeApiApplication.class, args);
    }

}
