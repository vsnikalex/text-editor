package com.epam.texteditor.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ImportResource("classpath:app-config.xml")
public class XmlConfiguration {

}
