package com.study.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 업로드된 이미지 경로
        registry.addResourceHandler("/uploaded-images/**")
                .addResourceLocations("file:///C:/upload/images/");

        // 정적 리소스 경로
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
