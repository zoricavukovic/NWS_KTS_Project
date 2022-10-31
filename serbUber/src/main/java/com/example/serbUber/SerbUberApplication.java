package com.example.serbUber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@SpringBootApplication
public class SerbUberApplication {

	@Bean
	Jackson2RepositoryPopulatorFactoryBean  repositoryPopulator() {
		Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
		factoryBean.setResources(new Resource[] { new ClassPathResource("locations.json"), new ClassPathResource("admins.json") });
		return factoryBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(SerbUberApplication.class, args);
	}
}
