package com.example.serbUber.repository;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.VehicleTypeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleTypeInfoRepository extends MongoRepository<VehicleTypeInfo, String> {

    @Query(value = "{ 'vehicleType' : ?0 }, fields = { 'vehicleType' : 1, 'startPrice' : 1, 'numOfSeats' : 1 }")
    Optional<VehicleTypeInfo> findByVehicleType(VehicleType vehicleType);

}
