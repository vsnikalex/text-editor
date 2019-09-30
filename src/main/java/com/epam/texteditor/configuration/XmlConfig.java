package com.epam.texteditor.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ImportResource("classpath:app-config.xml")
public class XmlConfig implements WebMvcConfigurer {

}
