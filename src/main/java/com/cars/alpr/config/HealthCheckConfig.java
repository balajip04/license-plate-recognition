package com.cars.alpr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.cars.framework.healthcheck.Dependency;
import com.cars.framework.healthcheck.dependencies.SimpleRestDependency;
import com.cars.framework.healthcheck.service.HealthCheckService;
import com.cars.framework.healthcheck.service.HealthCheckServiceImpl;

@Configuration
@ComponentScan("com.cars.framework.healthcheck")
public class HealthCheckConfig {

	@Value("${config-api.url}")
	private String configApiUrl;

	@Bean
	public Dependency configApiDependency() {
		return new SimpleRestDependency("Config Api", this.configApiUrl);
	}

	@Bean
	public HealthCheckService healthCheckService() {
		HealthCheckService service = new HealthCheckServiceImpl();
		service.addDependency(configApiDependency());
		return service;
	}

}
