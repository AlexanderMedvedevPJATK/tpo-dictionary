package com.s28572.tpo02;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

import java.util.InputMismatchException;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FlashcardsApp {

    public static void main(String[] args) {
        SpringApplication.run(FlashcardsApp.class, args);
    }

    @Bean
    public CommandLineRunner runner(FlashcardsController controller, FileService fileService) {
        return runner -> {
            System.out.println("Welcome to the dictionary application!");
            boolean running = true;
            while (running) {
                int response = controller.showOptions();
                try {
                    switch (response) {
                        case 1 -> controller.addWord();
                        case 2 -> controller.showAll();
                        case 3 -> controller.test();
                        case 0 -> running = false; // doesn't work
                    }
                } catch (InputMismatchException e) {
                    System.out.println("INVALID INPUT");
                }
            }
        };
    }

}
