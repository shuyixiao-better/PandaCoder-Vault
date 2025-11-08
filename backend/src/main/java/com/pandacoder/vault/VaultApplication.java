package com.pandacoder.vault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * PandaCoder-Vault 主应用类
 * 程序员个人知识库系统
 *
 * @author PandaCoder Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableMongoAuditing
public class VaultApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultApplication.class, args);
        System.out.println("""
                
                ╔═══════════════════════════════════════════════════════════╗
                ║                                                           ║
                ║   🐼 PandaCoder-Vault Backend Started Successfully! 🐼   ║
                ║                                                           ║
                ║   API Base URL: http://localhost:8080/api                ║
                ║   Swagger UI:   http://localhost:8080/api/swagger-ui     ║
                ║                                                           ║
                ╚═══════════════════════════════════════════════════════════╝
                """);
    }
}

