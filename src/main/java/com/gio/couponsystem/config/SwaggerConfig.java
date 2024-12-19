package com.gio.couponsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:8080", description = "dev server url"), // 개발 서버
                @Server(url = "https://prod.example.com", description = "production server url")  // 운영 서버
        },
        info = @Info(
                title = "선착순 쿠폰 시스템 API", // API 제목
                version = "v1.0.0",        // API 버전
                description = "선착순 쿠폰 API", // API 설명
                termsOfService = "https://example.com/terms", // 서비스 약관
                contact = @Contact(name = "Support Team", email = "rldh9037@naver.com"), // 연락처 정보
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0") // 라이선스 정보
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI();
                // 보안 구성 요소 추가: Bearer Token 인증 설정
//                .components(new Components()
//                        .addSecuritySchemes("Bearer Token(JWT)", new SecurityScheme() // 보안 스키마 추가
//                                .type(SecurityScheme.Type.HTTP)  // HTTP 기반 인증
//                                .scheme("bearer")              // 인증 방식: Bearer Token
//                                .bearerFormat("JWT")           // 토큰 포맷: JWT
//                                .description("JWT를 이용한 Bearer 인증 방식") // 인증 방식에 대한 설명 추가
//                        )
//                )
                // 보안 요구 사항 추가: 정의된 보안 스키마를 사용하도록 설정
//                .addSecurityItem(new SecurityRequirement().addList("Bearer Token(JWT)"));
    }
}
