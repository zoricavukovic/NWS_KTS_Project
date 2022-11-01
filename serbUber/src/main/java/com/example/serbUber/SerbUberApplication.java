package com.example.serbUber;

import com.example.serbUber.model.user.Admin;
import com.mongodb.client.MongoClients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@SpringBootApplication
public class SerbUberApplication {

	@Bean
	Jackson2RepositoryPopulatorFactoryBean  repositoryPopulator() {
		Jackson2RepositoryPopulatorFactoryBean factoryBean = new Jackson2RepositoryPopulatorFactoryBean();
		factoryBean.setResources(new Resource[] { new ClassPathResource("data.json")});
		return factoryBean;
	}

	public static void main(String[] args) {
		SpringApplication.run(SerbUberApplication.class, args);
	}
}
