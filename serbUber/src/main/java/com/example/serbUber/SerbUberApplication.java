package com.example.serbUber;

import com.graphhopper.GraphHopper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import static com.example.serbUber.util.GraphHopperUtil.createGraphHopperInstance;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SerbUberApplication {

	public static GraphHopper hopper = createGraphHopperInstance("src/main/resources/core/files/serbia-latest.osm.pbf");
	public static void main(String[] args) { SpringApplication.run(SerbUberApplication.class, args);}

}
