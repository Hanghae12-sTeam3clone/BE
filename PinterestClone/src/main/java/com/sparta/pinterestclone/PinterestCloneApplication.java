package com.sparta.pinterestclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
public class PinterestCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(PinterestCloneApplication.class, args);
    }

}
