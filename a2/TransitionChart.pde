public class TransitionChart {
    // transitions
    private String prev_chart_type;
    private String chart_type;
    private boolean in_transition;
    // charts
    private Barchart barchart;
    private Linechart linechart;
    private Piechart piechart;
    // data
    private String[] categories;
    private Table data;

    TransitionChart(Table data, String[] categories, Point center) {
        this.barchart = new Barchart(data, categories);
        this.linechart = new Linechart(data, categories);
        this.piechart = new Piechart(data, categories, center);
        this.data = data;
        this.categories = categories;
    }

    boolean setChartType(String chart_type) {
        if (!in_transition && chart_type != "") {
            this.prev_chart_type = this.chart_type;
            this.chart_type = chart_type;
            in_transition = false; // should become true
            return true;
        }
        return false;
    }

    Boolean inTransition() {
        return in_transition;
    }

    String getChartType() {
        return chart_type;
    }

    void draw() {
        if (in_transition) {

        } else {
            if (chart_type == "Line Chart") {
                linechart.draw();
            } else if (chart_type == "Bar Chart") {
                barchart.draw();
            } else if (chart_type == "Pie Chart") {
                piechart.draw();
            }
        }
    }
};