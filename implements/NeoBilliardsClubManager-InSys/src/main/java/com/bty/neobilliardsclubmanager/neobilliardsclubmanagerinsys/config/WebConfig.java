package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image.billiard-table.dir}")
    private String BILLIARD_TABLE_IMAGE_DIR;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/billiard-table-image/**")
                .addResourceLocations("file:" + BILLIARD_TABLE_IMAGE_DIR);
    }
}
