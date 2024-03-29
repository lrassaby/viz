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
    private StackedBar stackedbar;
    private RoseChart rosechart;
    private ThemeRiver themeriver;
    // data
    private String[] categories;
    private Table data;
    // constants
    private final float transition_time = 2;
    private final float transition_frames = transition_time * 60.0;
    private float progress;

    TransitionChart(Table data, String[] categories) {
        this.barchart = new Barchart(data, categories);
        this.linechart = new Linechart(data, categories);
        this.piechart = new Piechart(data, categories);
        this.stackedbar = new StackedBar(data, categories);
        this.rosechart = new RoseChart(data, categories);
        this.themeriver = new ThemeRiver(data, categories);
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
            progress = elapsed_frames / transition_frames;

            /* PRIMARY TRANSFORMATIONS */
            if ((prev_chart_type == "Line Chart" && chart_type == "Bar Chart") || (prev_chart_type == "Bar Chart" && chart_type == "Line Chart")) {
                LineBar();
            } else if ((prev_chart_type == "Bar Chart" && chart_type == "Pie Chart") || (prev_chart_type == "Pie Chart" && chart_type == "Bar Chart")) {
                PieBar();
            } else if ((prev_chart_type == "Line Chart" && chart_type == "Pie Chart") || (prev_chart_type == "Pie Chart" && chart_type == "Line Chart")) {
                LinePie();
            } else if ((prev_chart_type == "Bar Chart" && chart_type == "Stacked Bar") || (prev_chart_type == "Stacked Bar" && chart_type == "Bar Chart")) {
                BarStacked();
            } else if ((prev_chart_type == "Pie Chart" && chart_type == "Rose Chart") || (prev_chart_type == "Rose Chart" && chart_type == "Pie Chart")) {
                RosePie();
            } else if ((prev_chart_type == "Line Chart" && chart_type == "ThemeRiver") || (prev_chart_type == "ThemeRiver" && chart_type == "Line Chart"))  {
                LineThemeRiver();
            /* SECONDARY TRANSFORMATIONS */
            } else if (prev_chart_type == "Stacked Bar") {
                BarStacked();
            } else if (prev_chart_type == "Rose Chart") {
                RosePie();
            } else if (prev_chart_type == "ThemeRiver") {
                LineThemeRiver();
            } else if (prev_chart_type == "Pie Chart") {
                if (chart_type == "Stacked Bar") {
                    PieBar();
                } else if (chart_type == "ThemeRiver") {
                    LinePie();
                }
            } else if (prev_chart_type == "Line Chart") {
                if (chart_type == "Stacked Bar") {
                    LineBar();
                } else if (chart_type == "Rose Chart") {
                    LinePie();
                }
            } else if (prev_chart_type == "Bar Chart") {
                if (chart_type == "Rose Chart") {
                    PieBar();
                } else if (chart_type == "ThemeRiver") {
                    LineBar();
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
            } else if (chart_type == "Stacked Bar") {
                stackedbar.draw(1, Transition.NONE);
            } else if (chart_type == "Rose Chart") {
                rosechart.draw(1, Transition.NONE);
            }  else if (chart_type == "ThemeRiver") {
                themeriver.draw(1, Transition.NONE);
            } 
        }
    }
    public void LineBar () {
        if (prev_chart_type == "Line Chart") {
            if (progress < 0.5) {
                linechart.draw(1.0 - (progress * 2), Transition.LINETOBAR);
            } else {
                barchart.draw((progress - 0.5) * 2, Transition.LINETOBAR);
            }
            continueTransformation("Bar Chart");
        } else if (prev_chart_type == "Bar Chart") {
            if (progress < 0.5) {
                barchart.draw(1.0 - (progress * 2), Transition.LINETOBAR);
            } else {
                linechart.draw((progress - 0.5) * 2, Transition.LINETOBAR);
            }
            continueTransformation("Line Chart");
        }
    }

    public void LinePie () {
        if (prev_chart_type == "Line Chart") {
            if (progress < 0.25) {
                linechart.draw(1.0 - (progress * 4.0), Transition.LINETOPIE);
            } else {
                piechart.draw((progress - 0.25) * 4.0/3, Transition.LINETOPIE);
            }
            continueTransformation("Pie Chart");
        } else if (prev_chart_type == "Pie Chart") {
            if (progress < 0.75) {
                piechart.draw(1.0 - (progress * 4.0/3), Transition.PIETOLINE);
            } else {
                linechart.draw((progress - 0.75) * 4.0, Transition.PIETOLINE);
            }
            continueTransformation("Line Chart");
        }
    }

    public void PieBar () {
        if (prev_chart_type == "Bar Chart") {
            if (progress < 0.25) {
                barchart.draw(1.0 - (progress * 4), Transition.BARTOPIE);
            } else {
                piechart.draw((progress - 0.25) * 4.0/3, Transition.BARTOPIE);
            }
            continueTransformation("Pie Chart");
        } else if (prev_chart_type == "Pie Chart") {
            if (progress < 0.75) {
                piechart.draw(1.0 - (progress * 4.0/3), Transition.PIETOBAR);
            } else {
                barchart.draw((progress - 0.75) * 4.0, Transition.PIETOBAR);
            }
            continueTransformation("Bar Chart");
        }
    }

    public void RosePie () {
        if (prev_chart_type == "Pie Chart") {
            if (progress < 0.25) {
                piechart.draw(1-(progress*4), Transition.PIETOROSE);
            } else {
                rosechart.draw((progress - 0.25) * 4.0/3, Transition.PIETOROSE);
            }   
            continueTransformation("Rose Chart");
        } else if (prev_chart_type == "Rose Chart") {
            if (progress < 0.75) {
                rosechart.draw(1.0 - (progress * 4.0/3), Transition.ROSETOPIE);
            } else {
                piechart.draw((progress - 0.75) * 4.0, Transition.ROSETOPIE);
            }
            continueTransformation("Pie Chart");
        } 
    }

    public void LineThemeRiver () {
        if (prev_chart_type == "Line Chart") {
            if (progress < 0.25) {
                linechart.draw(1-(progress*4), Transition.LINETORIVER);
            } else {
                themeriver.draw((progress - 0.25) * 4.0/3, Transition.LINETORIVER);
            }
            continueTransformation("ThemeRiver");
        } else if (prev_chart_type == "ThemeRiver") {
             if (progress < 0.75) {
                themeriver.draw(1.0 - (progress * 4.0/3), Transition.RIVERTOLINE);
            } else {
                linechart.draw((progress - 0.75) * 4.0, Transition.RIVERTOLINE);
            }
            continueTransformation("Line Chart");
        }
    }

    public void BarStacked () {
        if (prev_chart_type == "Bar Chart") {
            stackedbar.draw(progress, Transition.BARTOSTACKED);
            continueTransformation("Stacked Bar");
        } else if (prev_chart_type == "Stacked Bar") {
            stackedbar.draw(1 - progress, Transition.STACKEDTOBAR);
            continueTransformation("Bar Chart");
        } 
    }

    public void continueTransformation (String type) {
        if (chart_type != type && progress > 0.99) {
            transition_start_frame = 0;
            prev_chart_type = type;
        }
    }
};

