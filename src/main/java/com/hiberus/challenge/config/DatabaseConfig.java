package com.hiberus.challenge.config;

import com.hiberus.challenge.domain.Product;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

  @Bean
  ConcurrentHashMap<String, Product> productDatabase() {
    return new ConcurrentHashMap<>();
  }
}
