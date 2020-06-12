package kz.spring.workflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    };


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

    //    registry.addResourceHandler("/robots.txt").addResourceLocations("classpath:/static/robots.txt");
     //   registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/favicon.ico");
    }


}