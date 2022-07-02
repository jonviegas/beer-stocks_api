package com.jonservices.beerstocks.docs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info =
@Info(title = "Beer Stocks API",
        version = "v1",
        description = "This REST API provides a system for managing beer stocks, " +
                "offering the functionality to register a beer by its name, brand, " +
                "quantity in stock and max limit. in addition, it is also possible to " +
                "search an individual beer by its name and list all registered beers."))
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Beer Stocks API")
                        .version("V1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));

    }

}
