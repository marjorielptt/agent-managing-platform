package com.hotline;

import com.framework.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hotline", "com.framework"})
public class HotlineApp {
    public static void main(String[] args) {
        SpringApplication.run(HotlineApp.class, args);
    }

    @Bean
    public CommandLineRunner init(ActorSystem system) {
        return args -> {
            system.registerActor(new HotlineActor("hotline-1", system));
        };
    }
}