package com.example.serbUber.request;

import com.example.serbUber.model.ChartType;

import javax.validation.constraints.NotNull;

import java.time.LocalDate;

import static com.example.serbUber.exception.ErrorMessagesConstants.CHART_TYPE_MESSAGE;
import static com.example.serbUber.exception.ErrorMessagesConstants.MISSING_ID;

public class ChartRequest {

    @NotNull(message = MISSING_ID)
    private Long id;

    @NotNull(message = CHART_TYPE_MESSAGE)
    private ChartType chartType;

    @NotNull(message = "Start date must be selected.")
    private LocalDate startDate;

    @NotNull(message = "End date must be selected.")
    private LocalDate endDate;

    public ChartRequest(
            final Long id,
            final ChartType chartType,
            final LocalDate startDate,
            final LocalDate endDate
    ) {
        this.id = id;
        this.chartType = chartType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public void setChartType(ChartType chartType) {
        this.chartType = chartType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

}
