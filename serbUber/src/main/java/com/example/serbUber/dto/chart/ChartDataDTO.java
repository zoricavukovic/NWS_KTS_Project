package com.example.serbUber.dto.chart;

import java.util.ArrayList;
import java.util.List;

public class ChartDataDTO {

    private List<ChartItemDTO> chartData = new ArrayList<>();

    private ChartSumDataDTO chartSumData;

    public ChartDataDTO() {
    }

    public ChartDataDTO(final List<ChartItemDTO> chartData, final ChartSumDataDTO chartSumData) {
        this.chartData = chartData;
        this.chartSumData = chartSumData;
    }

    public List<ChartItemDTO> getChartData() {
        return chartData;
    }

    public void setChartData(List<ChartItemDTO> chartData) {
        this.chartData = chartData;
    }

    public ChartSumDataDTO getChartSumData() {
        return chartSumData;
    }

    public void setChartSumData(ChartSumDataDTO chartSumData) {
        this.chartSumData = chartSumData;
    }
}
