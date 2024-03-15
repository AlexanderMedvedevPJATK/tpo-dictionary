package com.s28572.tpo02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class Config {

    @Bean
    public Scanner scanner() {
        return new Scanner(System.in);
    }
}
