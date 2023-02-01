package com.example.serbUber.request;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedPassengersRequest {
    private List<String> passengersEmail = new LinkedList<>();
    private LocalDateTime started;


    public LinkedPassengersRequest(List<String> passengersEmail, LocalDateTime started) {
        this.passengersEmail = passengersEmail;
        this.started = started;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public LinkedPassengersRequest() {
    }

    public List<String> getPassengersEmail() {
        return passengersEmail;
    }

    public void setPassengersEmail(List<String> passengersEmail) {
        this.passengersEmail = passengersEmail;
    }
}
