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
    public CommandLineRunner runner(FlashcardsController controller) {
        return runner -> {
            System.out.println("Welcome to the dictionary application!");
            boolean running = true;
            while (running) {
                try {
                    int response = controller.showOptions();
                    switch (response) {
                        case 1 -> controller.addWord();
                        case 2 -> controller.showAll(true);
                        case 3 -> controller.test();
                        case 4 -> controller.search(true);
                        case 5 -> controller.delete();
                        case 6 -> controller.modify();
                        case 0 -> running = false; // doesn't work
                    }
                } catch (InputMismatchException e) {
                    controller.flushScanner();
                    System.out.println("INVALID INPUT");
                }
            }
        };
    }

}
