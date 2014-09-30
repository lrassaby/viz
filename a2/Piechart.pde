public class Piechart {
	float radius;
    Point center;
    Table datapoints;
    float[] angles;
    color[] colors;
    String[] categories;
    int dataHovered;

    boolean intersect (int mousex, int mousey) {
    	return(dist(center.x, center.y, mousex, mousey) <= radius);
    }

    Piechart(Table datapoints, String[] categories, Point center) {
        setData(datapoints, categories, center);
    }

    void setData(Table datapoints, String[] categories, Point center) {
        this.datapoints = datapoints;
        this.categories = categories;
        this.center = center;
        this.radius = 300;
        this.angles = new float[datapoints.getRowCount()];
        int total_magnitude = 0;
        for (int i = 0; i < datapoints.getRowCount(); i++) {
        	total_magnitude += datapoints.getRow(i).getInt(categories[1]);
        }
        for (int i = 0; i < datapoints.getRowCount(); i++) {
        	angles[i] = (float(datapoints.getRow(i).getInt(categories[1])) / total_magnitude) * 360;
        }

        colors = new color[angles.length];
        for (int i = 0; i < colors.length; i++) {
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
};
