package com.example.serbUber.dto.chart;

public class ChartItemDTO {

    private String date;

    private double value;

    public ChartItemDTO() {
    }

    public ChartItemDTO(final String date, final double value) {
        this.date = date;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public double getValue() {
        return value;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
