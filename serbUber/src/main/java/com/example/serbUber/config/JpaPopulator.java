package com.example.serbUber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaPopulator {

//    @Bean
//    public void getRespositoryPopulator() throws Exception {
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("USLO");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
//        factory.setResources(new Resource[] { new ClassPathResource("locations.json") });
//        //return factory;
//    }
//
//    @Bean
//    public void repositoryPopulator() {
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("USLO2");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        System.out.println("--------------------------------------\n");
//        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
//        unmarshaller.setClassesToBeBound(Location.class);
//
//        UnmarshallerRepositoryPopulatorFactoryBean factory = new UnmarshallerRepositoryPopulatorFactoryBean();
//        factory.setUnmarshaller(unmarshaller);
//        factory.setResources(new Resource[] { new ClassPathResource("locations.xml") });
////        return factory;
//    }

    @Bean
    public void nekiBean(){
        System.out.println("ja te o");
    }
}