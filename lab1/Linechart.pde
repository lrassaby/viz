public class Linechart {
    int minY, maxY;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    boolean isect;
    int radius = 5;
    int dataHovered;

    String intersect (int mousex, int mousey) {
        float ratio = float((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8);
        
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = int(datapoints[i].count * ratio) + origin.y + 4;
            
            if ((mousex - x) * (mousex - x) + (mousey - y) * (mousey - y) < (radius + 1) * (radius + 1)) {
                dataHovered = i;
                return "(" + datapoints[i].fruit + ", " + datapoints[i].count + ")";
            }
        }
        return "";
    }

    Linechart(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        setData(datapoints, origin, topyaxis, rightxaxis);
    }
    void setData(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        this.datapoints = datapoints;
        this.origin = origin;
        this.topyaxis = topyaxis;
        this.rightxaxis = rightxaxis;
        minY = datapoints[0].count;
        maxY = datapoints[0].count;
        for (int i = 1; i < datapoints.length; i++) {
            if (datapoints[i].count < minY) minY = datapoints[i].count;
            if (datapoints[i].count > maxY) maxY = datapoints[i].count;
        }
    }

    void draw () {
        strokeWeight(2);
        float ratio = float((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        Point prev = new Point(origin.x + sectionWidth / 2, int(datapoints[0].count * ratio) + origin.y);
        drawCircle(prev.x, prev.y, 10);
        for (int i = 1; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = int(datapoints[i].count * ratio) + origin.y;
            line(prev.x, prev.y, x, y);
            prev.setXY(x, y);
            drawCircle(prev.x, prev.y, radius * 2);
        }
    }

    void drawCircle(int x, int y, float diameter) {
        ellipse(x, y, diameter, diameter);
    }

    void highlightOnHover() {
        float ratio = float((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        Point prev = new Point(origin.x + sectionWidth / 2, int(datapoints[0].count * ratio) + origin.y);
        int x = origin.x + sectionWidth * dataHovered + sectionWidth / 2 + int(sectionWidth * 0.1);
        int y = int(datapoints[dataHovered].count * ratio) + origin.y;
            
        prev.setXY(x, y);
        fill(255, 255, 0);
        stroke(255, 255, 0);
        drawCircle(prev.x, prev.y, radius * 2);
        stroke(0);
        fill(0);
    }
};