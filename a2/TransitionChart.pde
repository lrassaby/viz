public class TransitionChart {
    private String prev_chart_type;
    private String chart_type;
    private boolean in_transition;
    private AxisChart chart;
    TransitionChart(Table data, Point origin, Point topyaxis, Point rightxaxis) {
        this.chart = new AxisChart(data, origin, topyaxis, rightxaxis);
        this.in_transition = false;
    }
    void setChartType(String chart_type) {
        if (!in_transition) {
            this.prev_chart_type = this.chart_type;
            this.chart_type = chart_type;
            in_transition = true;
        }
    }
    Boolean inTransition() {
        return in_transition;
    }
    String getChartType() {
        return chart_type;
    }
    void setAxes(Point origin, Point topyaxis, Point rightxaxis) {
        chart.setAxes(origin, topyaxis, rightxaxis);
    }
    void draw() {
        chart.draw();
    }
};