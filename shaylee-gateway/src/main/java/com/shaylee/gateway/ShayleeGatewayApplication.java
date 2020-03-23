package com.shaylee.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@ComponentScan(basePackages = {"com.shaylee"})
public class ShayleeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShayleeGatewayApplication.class, args);
    }

}
