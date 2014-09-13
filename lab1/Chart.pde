public class Chart {
    Linechart linechart;
    Barchart barchart;
    boolean barchartSelected = false, linechartSelected = true;
    Point origin, topyaxis, rightxaxis;
    String x_label = "Fruits", y_label = "Count";
    FruitCount[] datapoints;
    int minY, maxY;

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
    void toggleChartSelection() {
        barchartSelected = linechartSelected;
        linechartSelected = !linechartSelected;
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
        textSize(16); 
        textAlign(RIGHT, CENTER); 
        /* X labels */
        // X-axis label
        makeText(x_label, rightxaxis.x, rightxaxis.y + 70, false);
        // X value labels
        textSize(12); 
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8);
        strokeCap(SQUARE);
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = origin.y + 10;
            makeText(datapoints[i].fruit, x, y, true);
        }
        /* Y labels */
        // Y-axis label
        textSize(16); 
        makeText(y_label, topyaxis.x - 60, topyaxis.y + 50, true);

        // Y value labels
        textSize(12);
        int ratio = (topyaxis.y - origin.y) / maxY;
        int increment;
        if (ratio > -8) {
            increment = 8;
        } else if (ratio > -16) {
            increment = 4;
        } else {
            increment = 2;
        }

        for (int i = 0; i <= maxY; i+= increment) {
            makeText(Integer.toString(i), origin.x - 10, i * ratio + origin.y, false);
        }
    }
    void makeText(String str, int x, int y, boolean vert) {      
        if (vert) {
            pushMatrix();
            translate(x, y);
            rotate(-HALF_PI);
            translate(-x, -y);
            text(str, x, y);
            popMatrix();
        } else {
            text(str, x, y);
        }
    }

    String mouseOver(int x, int y) {
        if (barchartSelected) {
            return barchart.intersect(x, y);
        } else {
            return linechart.intersect(x, y);
        }
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
        
        minY = datapoints[0].count;
        maxY = datapoints[0].count;
        for (i = 1; i < datapoints.length; i++) {
            if (datapoints[i].count < minY) minY = datapoints[i].count;
            if (datapoints[i].count > maxY) maxY = datapoints[i].count;
        }
    }
};