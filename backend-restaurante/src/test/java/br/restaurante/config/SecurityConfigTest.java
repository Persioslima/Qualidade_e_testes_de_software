package br.restaurante.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    @Test
    void deveCarregarBeansDeSecurityConfig() throws Exception {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(SecurityConfig.class)) {

            SecurityFilterChain chain = context.getBean(SecurityFilterChain.class);
            assertThat(chain).isNotNull();

            BCryptPasswordEncoder encoder = context.getBean(BCryptPasswordEncoder.class);
            assertThat(encoder).isNotNull();

            CorsConfigurationSource corsSource = context.getBean(CorsConfigurationSource.class);
            assertThat(corsSource).isNotNull();
        }
    }
}
