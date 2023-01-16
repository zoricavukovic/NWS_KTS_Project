package com.example.serbUber.dto.chart;

public class ChartSumDataDTO {

    private double sum;

    private double average;

    public ChartSumDataDTO(final double sum, final double average) {
        this.sum = sum;
        this.average = average;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
