package com.example.serbUber.repository;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeInfoRepository extends JpaRepository<VehicleTypeInfo, Long> {

     Optional<VehicleTypeInfo> findByVehicleType(VehicleType vehicleType);

}
