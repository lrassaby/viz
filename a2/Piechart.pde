public class Piechart {
	float radius;
    Table data;
    float[] angles;
    color[] colors;
    String[] categories;
    int dataHovered;

    boolean intersect (int mousex, int mousey) {
    	return(dist(width / 2, height / 2, mousex, mousey) <= radius);
    }

    Piechart(Table data, String[] categories) {
        setData(data, categories);
    }

    void setData(Table data, String[] categories) {
        this.data = data;
        this.categories = categories;
        this.radius = 300;
        this.angles = new float[data.getRowCount()];
        int total_magnitude = 0;
        for (int i = 0; i < data.getRowCount(); i++) {
        	total_magnitude += data.getRow(i).getInt(categories[1]);
        }
        for (int i = 0; i < data.getRowCount(); i++) {
        	angles[i] = (float(data.getRow(i).getInt(categories[1])) / total_magnitude) * 360;
        }

        colors = new color[angles.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = color(random(255), random(255), random(255));
        }
    }

    void draw (float transition_completeness, Transition transition) {
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
