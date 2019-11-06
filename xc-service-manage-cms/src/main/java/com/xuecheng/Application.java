package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@EntityScan("com.xuecheng.framework.domain")
@ComponentScan(basePackages = {"com.xuecheng.api"})
@ComponentScan(basePackages = {"com.xuecheng.manage_cms"})
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
