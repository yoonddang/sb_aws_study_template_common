package com.template.common.configuration;

import com.template.common.configuration.properties.ProjectData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

@Configuration
@ComponentScan(
        basePackages = {
                "com.template.common"
        }
)
public class BasicConfiguration {

    @Bean
    public ProjectData projectData() throws Exception {
        Yaml yaml = new Yaml();


        try(InputStream in = (new ClassPathResource("config/project-data.yml")).getInputStream()) {
            ProjectData projectData = yaml.loadAs(in, ProjectData.class);
            return projectData;
        }
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

}
