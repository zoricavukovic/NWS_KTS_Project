package com.example.serbUber.server.controller;


import com.example.serbUber.exception.PassengerNotHaveTokensException;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.repository.user.DriverRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.serbUber.server.controller.helper.ControllerConstants.DRIVER_ID_FOR_ACTIVE_DRIVING;
import static com.example.serbUber.server.controller.helper.ControllerConstants.EXIST_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class OptimisticLockTest {

    @Autowired
    private DriverRepository driverRepository;

    @Test
    @Rollback(true)
    public void shouldThrowOptimisticLockingException() throws Throwable {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<?> future1 = executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 1");
                Driver driver = driverRepository.getDriverById(DRIVER_ID_FOR_ACTIVE_DRIVING).orElseGet(null);
                if (driver != null) {
                    driver.setLocked(true);
                    try { Thread.sleep(3000); } catch (InterruptedException e) {}
                    assertThrows(OptimisticLockingFailureException.class, () ->
                            driverRepository.save(driver)
                    );
                }
            }
        });
        executor.submit(new Runnable() {

            @Override
            public void run() {
                System.out.println("Startovan Thread 2");
                Driver secondDriver = driverRepository.getDriverById(DRIVER_ID_FOR_ACTIVE_DRIVING).orElseGet(null);
                if (secondDriver != null) {
                    secondDriver.setLocked(true);
                    driverRepository.save(secondDriver);
                }

            }
        });
        try {
            future1.get();
        } catch (ExecutionException e) {
            System.out.println("Exception from thread " + e.getCause().getClass());
            throw e.getCause();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
    }

}
