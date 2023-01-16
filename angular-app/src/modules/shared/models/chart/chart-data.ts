
export interface ChartData {
    chartData: ChartItem[];
    chartSum: ChartSumData;
}

export interface ChartItem {
    date: string;
    value: number;
}

export interface ChartSumData {
    sum: number;
    average: number;
}