package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleOutdatedDriving {

    private final DrivingService drivingService;

    private final WebSocketService webSocketService;

    public ScheduleOutdatedDriving(final DrivingService drivingService, final WebSocketService webSocketService){

        this.drivingService = drivingService;
        this.webSocketService = webSocketService;
    }
    @Scheduled(cron = "*/30 * * * * *")
    @Transactional
    public void rejectOutdatedDrivings(){
        List<Driving> drivings = drivingService.getAcceptedNotActiveDrivings();
        for(Driving driving : drivings){
            if(driving.getStarted().plusMinutes(5).isBefore(LocalDateTime.now())){
                driving.setDrivingStatus(DrivingStatus.REJECTED);
                drivingService.save(driving);
                webSocketService.sendRejectedOutdatedDriving(driving.getUsers(), driving.getDriver().getEmail(), driving.getId());
            }
        }
    }
}
