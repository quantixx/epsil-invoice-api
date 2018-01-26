package io.epsil.invoice.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "io.epsil.invoice")
public class FeignConfiguration {

}
