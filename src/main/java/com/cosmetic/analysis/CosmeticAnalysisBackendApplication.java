package com.cosmetic.analysis;

import com.cosmetic.analysis.mapper.test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cosmetic.analysis.mapper")
public class CosmeticAnalysisBackendApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CosmeticAnalysisBackendApplication.class, args);
	}
}
