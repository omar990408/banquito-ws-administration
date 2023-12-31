package ec.edu.espe.arquitectura.banquito.administration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200","https://banquito-accounts-try-production.up.railway.app/doc/swagger-ui.html","http://0.0.0.0")
                        .exposedHeaders("Content-Disposition")
                        .allowedMethods("*");
            }
        };
    }
}
