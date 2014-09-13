public class Barchart {
    int minY, maxY;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    boolean isect;

    String intersect (int mousex, int mousey) {
        float ratio = float((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8);
        
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = int(datapoints[i].count * ratio) + origin.y;
            
            if (mousex > x - sectionWidth * 0.4 && mousex < x + sectionWidth * 0.4) {
                // within the x range
                if (mousey > y && mousey < origin.y) {
                    // within the y range
                    return "(" + datapoints[i].fruit + ", " + datapoints[i].count + ")";
                }
            }
        }
        return "";
    }

    Barchart(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
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
        float ratio = float((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8);
        strokeCap(SQUARE);
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = int(datapoints[i].count * ratio) + origin.y;
            line(x, origin.y, x, y);
        }
    }
};