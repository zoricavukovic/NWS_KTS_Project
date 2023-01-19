
export interface ChartData {
    chartData: ChartItem[];
    chartSumData: ChartSumData;
}

export interface ChartItem {
    date: string;
    value: number;
}

export interface ChartSumData {
    sum: number;
    average: number;
}

export interface ChartRequest {
    id?: number;
    chartType: string;
    startDate: string;
    endDate: string;
}