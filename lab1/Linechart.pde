public class Linechart {
    int minY = 1000000, maxY = -1000000;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    Linechart(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        setData(datapoints, origin, topyaxis, rightxaxis);
    }
    void setData(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        this.datapoints = datapoints;
        this.origin = origin;
        this.topyaxis = topyaxis;
        for (int i = 0; i < datapoints.length; i++) {
            if (datapoints[i].count < minY) minY = datapoints[i].count;
            if (datapoints[i].count > maxY) maxY = datapoints[i].count;
        }
    }
    void draw () {
        // Draw function
        // Circles w/ ellipse function
        for (int i = 0; i < datapoints.length; i++) {
            println(datapoints[i].fruit + " " + datapoints[i].count);
        }
    }
};