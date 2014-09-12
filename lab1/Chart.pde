public class Chart {
    Point origin, topyaxis, rightxaxis;
    String x_label, y_label;
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
};