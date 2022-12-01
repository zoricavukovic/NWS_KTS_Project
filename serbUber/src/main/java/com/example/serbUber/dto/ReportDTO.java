package com.example.serbUber.dto;

import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.model.Report;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

public class ReportDTO {

    private final Long id;
    private final String message;
    private final UserDTO sender;
    private final LocalDateTime timeStamp;

    public ReportDTO(final Report report) {
        this.id = report.getId();
        this.message = report.getMessage();
        this.sender = new UserDTO(report.getSender());
        this.timeStamp = report.getTimeStamp();
    }

    public static List<ReportDTO> fromReports(List<Report> reports) {
        List<ReportDTO> dtos = new LinkedList<>();
        reports.forEach(report -> {
            report.getSender().setProfilePicture(convertPictureToBase64ByName(report.getSender().getProfilePicture()));
            dtos.add(new ReportDTO(report));
        });

        return dtos;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public UserDTO getSender() {
        return sender;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
