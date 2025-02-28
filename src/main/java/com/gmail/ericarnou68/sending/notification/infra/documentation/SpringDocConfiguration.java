package com.gmail.ericarnou68.sending.notification.infra.documentation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SpringDocConfiguration {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Send Notification API")
                .description("API para envio de notificações")
                .version("v1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
