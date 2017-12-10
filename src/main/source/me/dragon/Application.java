package me.dragon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by dragon on 11/4/2017.
 */
@ComponentScan
@EnableScheduling
@ServletComponentScan
@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    @Value("${env}")
    private String env;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}