public class Piechart {
	float radius;
    Point center;
    FruitCount[] datapoints;
    float[] angles;
    color[] colors;
    int dataHovered;

    boolean intersect (int mousex, int mousey) {
    	return(dist(center.x, center.y, mousex, mousey) <= radius);
    }

    Piechart(FruitCount[] datapoints, Point center, float radius) {
        setData(datapoints, center, radius);
    }

    void setData(FruitCount[] datapoints, Point center, float radius) {
        this.datapoints = datapoints;
        this.center = center;
        this.radius = radius;
        this.angles = new float[datapoints.length];
        int total_magnitude = 0;
        for (int i = 0; i < datapoints.length; i++) {
        	total_magnitude += datapoints[i].count;
        }
        println(total_magnitude);
        for (int i = 0; i < datapoints.length; i++) {
        	angles[i] = (float(datapoints[i].count) / total_magnitude) * 360;
        }

        colors = new color[angles.length];
        for (int i = 0; i < datapoints.length; i++) {
            colors[i] = color(random(255), random(255), random(255));
        }
    }

    void draw () {
        float angle = 0;
        for (int i = 0; i < angles.length; i++) {
            fill(colors[i]);
            arc(width/2, height/2, radius * 2, radius *2, angle, angle+radians(angles[i]));
            angle += radians(angles[i]);
        }
    }

    void drawCircle(int x, int y, float diameter) {
        ellipse(x, y, diameter, diameter);
    }

    // void highlightOnHover() {
    //     float ratio = float((topyaxis.y - origin.y)) / maxY;
    //     int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
    //     Point prev = new Point(origin.x + sectionWidth / 2, int(datapoints[0].count * ratio) + origin.y);
    //     int x = origin.x + sectionWidth * dataHovered + sectionWidth / 2 + int(sectionWidth * 0.1);
    //     int y = int(datapoints[dataHovered].count * ratio) + origin.y;
            
    //     prev.setXY(x, y);
    //     fill(255, 255, 0);
    //     stroke(255, 255, 0);
    //     drawCircle(prev.x, prev.y, radius * 2);
    //     stroke(0);
    //     fill(0);
    // }
};