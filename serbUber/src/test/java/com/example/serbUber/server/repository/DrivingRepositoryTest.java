package com.example.serbUber.server.repository;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.model.user.Role;
import com.example.serbUber.repository.DrivingRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class DrivingRepositoryTest {
    @Autowired
    private DrivingRepository drivingRepository;

    @Test
    public void findByDriverId_ReturnDrivingsForPageRequest(){
        Page<Driving> page;
        List<Driving> drivings;
        Pageable pg = PageRequest.of(0, 1);

        page = drivingRepository.findByDriverId(15L, pg);
        drivings = page.getContent();
        assertEquals(1, page.getNumberOfElements());
        assertEquals(2, page.getTotalElements());
        assertEquals(1, drivings.size());
    }

    @Test
    public void findByUserId_ReturnDrivingsForPageRequest(){
        Pageable pg = PageRequest.of(0, 1);

        Page<Driving> page = drivingRepository.findByUserId(6L, pg);
        List<Driving> drivings = page.getContent();
        assertEquals(1, page.getNumberOfElements());
        assertEquals(2, page.getTotalElements());
        assertEquals(1, drivings.size());
    }

    @Test
    public void getDrivingsForUserId_ReturnDrivings(){
        List<Driving> drivings = drivingRepository.getDrivingsForUserId(6L);
        assertEquals(2, drivings.size());
    }
}
