package com.example;

//import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.BeanConfig;
import net.sf.log4jdbc.sql.jdbcapi.DataSourceSpy;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.ws.rs.ApplicationPath;

@Configuration
public class AppConfig {
    @Inject
    DataSourceProperties dataSourceProperties;

    @Named
    @ApplicationPath("/api")
    static class JerseyConfig extends ResourceConfig {
        public JerseyConfig() {

            //this.packages("com.example");
            this.register(HelloEndpoint.class);

            this.register(WadlResource.class);
        }

        @PostConstruct
        public void init() {



            // Available at localhost:port/swagger.json
            this.register(io.swagger.jaxrs.listing.ApiListingResource.class);
            this.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

            BeanConfig config = new BeanConfig();
            // config.setConfigId("springboot-jersey-swagger-docker-example");
            config.setTitle("Spring Boot + Jersey + Swagger + Docker Example");
            config.setVersion("v1");
            config.setContact("Orlando L Otero");
            config.setSchemes(new String[] { "http", "https" });
            config.setBasePath("/api");
            config.setResourcePackage("com.example");
            config.setPrettyPrint(true);
            config.setScan(true);


        }
    }

    @Bean
    DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder
                .create(this.dataSourceProperties.getClassLoader())
                .url(this.dataSourceProperties.getUrl())
                .username(this.dataSourceProperties.getUsername())
                .password(this.dataSourceProperties.getPassword())
                .build();
        return new DataSourceSpy(dataSource);
    }
}