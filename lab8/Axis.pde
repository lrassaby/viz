public class Point {
    float x, y;
    String disp;
    Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }
    void setDisp(String disp) {
        this.disp = disp;
    }
};

public class Axis {
	Point a, b; // a is bottom, b is top
	float data_max, data_min;
	String category;
    Table data;

	Axis(String category, Table data) {
		this.category = category;
		this.data = data;
		data_max = data.getRow(0).getFloat(category);
		data_min = data_max;
		for (TableRow row : data) {
			if (row.getFloat(category) > data_max) {
				data_max = row.getFloat(category);
			} else if (row.getFloat(category) < data_min) {
				data_min = row.getFloat(category);
			}
		}
	}

	public void setAxes(Point a, Point b) {
		this.a = a;
		this.b = b;
	}

	public void draw() {
		line(a.x, a.y, b.x, b.y);
	}

	public ArrayList<Point> getPointsOnAxis() {
		ArrayList<Point> points = new ArrayList<Point>();
		for (TableRow row : data) {
			float value = row.getFloat(category);
			float proportion = (value - data_min) / (data_max - data_min);
			Point p = new Point(a.x, (b.y - a.y) * proportion + a.y);
			points.add(p);
		}
		return points;
	}

}