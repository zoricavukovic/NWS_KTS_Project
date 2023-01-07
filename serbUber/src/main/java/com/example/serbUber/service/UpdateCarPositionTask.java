package com.example.serbUber.service;

import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import com.jayway.restassured.response.Response;

import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.put;

public class UpdateCarPositionTask  extends AbstractTask {


    @Override
    public int getWeight() {
        return 20;
    }


    @Override
    public String getName() {
        return "Update cars position on map task";
    }

    public UpdateCarPositionTask(){
    }

    @Override
    public void execute() {
//        try {
//
//            Response response = put("/vehicles/update-current-location");
//            System.out.println(response.print());
//            assert response.getStatusCode() == 200;
//            Locust.getInstance().recordSuccess("http", getName(), response.getTime(), 1);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        long startTime = System.currentTimeMillis();
        System.out.println("POCETNO VREME");
        System.out.println(startTime);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("KRAJNJE VREME");
        System.out.println(elapsed);
        Locust.getInstance().recordSuccess("http", "success", elapsed, 1);
    }

}
