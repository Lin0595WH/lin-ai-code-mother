package com.lin.linaicodemother;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lin.linaicodemother.mapper")
public class LinAiCodeMotherApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinAiCodeMotherApplication.class, args);
    }

}
