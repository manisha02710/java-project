package com.rbac.config;

import com.rbac.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration that registers the authentication/authorization interceptor.
 * // OOP: Abstraction – implements WebMvcConfigurer.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                // Allow unauthenticated access to login page and static resources
                .excludePathPatterns("/login", "/logout", "/error/**", "/css/**", "/js/**", "/images/**", "/webjars/**");
    }
}
