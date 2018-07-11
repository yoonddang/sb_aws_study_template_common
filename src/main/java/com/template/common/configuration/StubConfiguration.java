package com.template.common.configuration;

import com.template.common.configuration.properties.ProjectData;
import com.template.common.restfull.bean.RestForSpringAutoScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StubConfiguration {

    @Bean
    public RestForSpringAutoScanner restForSpringAutoScanner(RestTemplate restTemplate, ProjectData projectDat){

        RestForSpringAutoScanner restForSpringAutoScanner = new RestForSpringAutoScanner();
        restForSpringAutoScanner.setPackage(
                Stream.of("com.template.common.reststub").collect(Collectors.toList())
        );
        restForSpringAutoScanner.setRestTemplate(restTemplate);
        restForSpringAutoScanner.setServers(projectDat.getServers());

        return restForSpringAutoScanner;
    }
}
