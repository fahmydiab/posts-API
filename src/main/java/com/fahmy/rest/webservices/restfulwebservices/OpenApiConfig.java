package com.fahmy.rest.webservices.restfulwebservices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class OpenApiConfig {



private Contact contact = new Contact();

@Bean
public OpenAPI customOpenAPI() {
    
    
	return new OpenAPI()
            .components(new Components())
            .info(new Info().title("API Documentation").description(
                    "This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")
            		.contact(contact.name("fahmy") ).contact(contact.email("fahmy1.diab@gmail.com"))
            		);
}
}
