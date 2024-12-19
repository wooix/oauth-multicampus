package com.multi.oauth10client;

import java.util.Arrays;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.multi.oauth10client.servlet.OAuth10CallbackServlet;
import com.multi.oauth10client.servlet.OAuth10RequestServlet;
import com.multi.oauth10client.servlet.OAuth10ResourceServlet;

@Configuration
public class ServletConfig {
    @Bean
    ServletRegistrationBean<OAuth10RequestServlet> requestRegistrationBean(){
        ServletRegistrationBean<OAuth10RequestServlet> bean = new ServletRegistrationBean<OAuth10RequestServlet>();
        bean.setServlet(new OAuth10RequestServlet());
        bean.setUrlMappings(Arrays.asList("/request"));
        return bean;
    }

    @Bean
    ServletRegistrationBean<OAuth10CallbackServlet> callbackRegistrationBean(){
        ServletRegistrationBean<OAuth10CallbackServlet> bean = new ServletRegistrationBean<OAuth10CallbackServlet>();
        bean.setServlet(new OAuth10CallbackServlet());
        bean.setUrlMappings(Arrays.asList("/callback"));
        return bean;
    }
    
    @Bean
    ServletRegistrationBean<OAuth10ResourceServlet> resourceRegistrationBean(){
        ServletRegistrationBean<OAuth10ResourceServlet> bean = new ServletRegistrationBean<OAuth10ResourceServlet>();
        bean.setServlet(new OAuth10ResourceServlet());
        bean.setUrlMappings(Arrays.asList("/resource"));
        return bean;
    }
}
