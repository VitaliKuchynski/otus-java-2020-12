package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppMain {
        public static void main(String[] args) {
            var context = SpringApplication.run(AppMain.class, args);

            context.getBean("actions", Actions.class).action();
        }
    }


