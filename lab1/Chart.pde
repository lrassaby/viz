public class Chart {
    Linechart linechart;
    Barchart barchart;
    boolean barchartSelected = true, linechartSelected = false;
    Point origin, topyaxis, rightxaxis;
    String x_label = "Fruits", y_label = "Count";
    FruitCount[] datapoints;

    void draw() {
        drawAxes();
        drawLabels();
        if (barchartSelected) {
            barchart.setData(datapoints, origin, topyaxis, rightxaxis);
            barchart.draw();
        } else if (linechartSelected) {
            linechart.setData(datapoints, origin, topyaxis, rightxaxis);
            linechart.draw();
        }
    }
    void setAxes(Point origin, Point topyaxis, Point rightxaxis) {
        this.origin = origin;
        this.topyaxis = topyaxis;
        this.rightxaxis = rightxaxis;
    }
    void drawAxes() {
        strokeWeight(2);
        line(origin.x, origin.y, topyaxis.x, topyaxis.y);
        line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);
    }
    void drawLabels() {
        fill(0);
        textSize(12); 
        textAlign(CENTER, CENTER); 
    }
    void setup(Table data, Point origin, Point topyaxis, Point rightxaxis) {
        /* get data */
        int i = 0;
        datapoints = new FruitCount[data.getRowCount()];       
        for (TableRow row : data.rows()) {
            datapoints[i++] = new FruitCount(row.getString("Name"), parseInt(row.getString("Number")));
        }
        setAxes(origin, topyaxis, rightxaxis);
        /* initialize */
        barchart = new Barchart(datapoints, origin, topyaxis, rightxaxis);
        linechart = new Linechart(datapoints, origin, topyaxis, rightxaxis);
    }
};