package com.arista.nestnavigator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class NestNavigatorApplication extends SpringBootServletInitializer{

    @GetMapping
    public String home() {
        return "Welcome to Nest Navigate";
    }
    public static void main(String[] args){
        SpringApplication.run(NestNavigatorApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(NestNavigatorApplication.class);
    }
}
