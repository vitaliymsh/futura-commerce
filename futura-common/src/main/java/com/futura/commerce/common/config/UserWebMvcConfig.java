package com.futura.commerce.common.config;

import com.futura.commerce.common.interceptor.UserAuthInterceptor;
import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration registering user authentication interceptors
 *
 * @author Vitalii
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class UserWebMvcConfig implements WebMvcConfigurer {

    @Resource
    private UserAuthInterceptor userAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userAuthInterceptor)
                .addPathPatterns("/address/**")
                .addPathPatterns("/user/**")
                .addPathPatterns("/order/**")
                .addPathPatterns("/cart/**");
    }
}
