package br.restaurante.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WebConfigTest {

    private final WebConfig webConfig = new WebConfig();

    @Test
    void deveConfigurarCorsCorretamente() {
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        // Configuração correta dos mocks
        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins(any(String[].class))).thenReturn(registration);
        when(registration.allowedMethods(any(String[].class))).thenReturn(registration);
        when(registration.allowedHeaders(any(String[].class))).thenReturn(registration);
        when(registration.allowCredentials(anyBoolean())).thenReturn(registration);

        // Executa o método alvo
        webConfig.addCorsMappings(registry);

        // Verificações
        verify(registry).addMapping("/**");
        verify(registration).allowedOrigins(
                "http://localhost:5000",
                "http://3.90.155.156",
                "http://localhost:19006"
        );
        verify(registration).allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
        verify(registration).allowedHeaders("*");
        verify(registration).allowCredentials(true);
    }

    @Test
    void deveRegistrarResourceHandlersSemErros() {
        ResourceHandlerRegistry registry = mock(ResourceHandlerRegistry.class);
        ResourceHandlerRegistration registration = mock(ResourceHandlerRegistration.class);

        when(registry.addResourceHandler(any(String[].class))).thenReturn(registration);
        when(registration.addResourceLocations(any(String[].class))).thenReturn(registration);

        webConfig.addResourceHandlers(registry);

        verify(registry, atLeastOnce()).addResourceHandler(any(String[].class));
        verify(registration, atLeastOnce()).addResourceLocations(any(String[].class));
    }
}
