public class TransitionChart {
    // transitions
    private String prev_chart_type;
    private String chart_type;
    private boolean in_transition;
    private int transition_start_frame;
    // charts
    private Barchart barchart;
    private Linechart linechart;
    private Piechart piechart;
    // data
    private String[] categories;
    private Table data;
    // constants
    private final float transition_time = 3;
    private final float transition_frames = transition_time * 60.0;

    TransitionChart(Table data, String[] categories) {
        this.barchart = new Barchart(data, categories);
        this.linechart = new Linechart(data, categories);
        this.piechart = new Piechart(data, categories);
        this.data = data;
        this.categories = categories;
        this.transition_start_frame = 0;
        this.in_transition = false;
    }

    boolean setChartType(String chart_type) {
        if (!in_transition && chart_type != "") {
            this.prev_chart_type = this.chart_type;
            this.chart_type = chart_type;
            if (prev_chart_type != "" && prev_chart_type != null) {
                in_transition = true; // should become true
            }
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
            if (transition_start_frame == 0) { // beginning of transition
                transition_start_frame = frameCount;
            }
            int elapsed_frames = frameCount - transition_start_frame;
            float progress = elapsed_frames / transition_frames;

            if (prev_chart_type == "Line Chart" && chart_type == "Bar Chart") {
                if (progress < 0.5) {
                    linechart.draw(1.0 - (progress * 2), Transition.LINETOBAR);
                } else {
                    barchart.draw((progress - 0.5) * 2, Transition.LINETOBAR);
                }
            } else if (prev_chart_type == "Bar Chart" && chart_type == "Line Chart") {
                if (progress < 0.5) {
                    barchart.draw(1.0 - (progress * 2), Transition.LINETOBAR);
                } else {
                    linechart.draw((progress - 0.5) * 2, Transition.LINETOBAR);
                }
            } else if (prev_chart_type == "Bar Chart" && chart_type == "Pie Chart") {
                if (progress < 0.25) {
                    barchart.draw(1.0 - (progress * 4), Transition.BARTOPIE);
                } else {
                    piechart.draw((progress - 0.25) * 4.0/3, Transition.BARTOPIE);
                }
            } else if (prev_chart_type == "Pie Chart" && chart_type == "Bar Chart") {
                if (progress < 0.75) {
                    piechart.draw(1.0 - (progress * 4.0/3), Transition.PIETOBAR);
                } else {
                    barchart.draw((progress - 0.75) * 4.0, Transition.PIETOBAR);
                }
            } else {
                println("Transformation not yet implemented.");
                in_transition = false;
                transition_start_frame = 0;
            }


            if (elapsed_frames >= transition_frames) {
                in_transition = false;
                transition_start_frame = 0;
            }             
        } else {
            if (chart_type == "Line Chart") {
                linechart.draw(1, Transition.NONE);
            } else if (chart_type == "Bar Chart") {
                barchart.draw(1, Transition.NONE);
            } else if (chart_type == "Pie Chart") {
                piechart.draw(1, Transition.NONE);
            }
        }
    }
};