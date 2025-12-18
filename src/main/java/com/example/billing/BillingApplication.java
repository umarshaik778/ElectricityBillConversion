package com.example.billing;

//import com.example.billing.logging.RequestResponseLoggingFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BillingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingApplication.class, args);
    }

//    @Bean
//    public RequestResponseLoggingFilter loggingFilter() {
//        return new RequestResponseLoggingFilter();
//    }
}