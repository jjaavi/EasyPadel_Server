package com.easypadel;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class EasyPadelApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyPadelApplication.class, args);
    }

    @Bean
    ApplicationRunner comprovarArrencada(DataSource dataSource) {
        return args -> {
            System.out.println("Aplicacio de part servidor arrencada.");
            try (Connection connexio = dataSource.getConnection()) {
                if (connexio.isValid(2)) {
                    System.out.println("Conexio a Base de dades MySQL de EasyPadel correcta.");
                }
            } catch (Exception e) {
                System.err.println("Error de connexio amb la BD: " + e.getMessage());
                System.out.println("Aplicacio no arrancada.");
            }
        };
    }
}
