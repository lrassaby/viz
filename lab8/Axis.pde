/* represents a single axis/category */
public class Axis {
	Point a, b; // a is bottom, b is top
	float data_max, data_min;
	static final int NUM_MARKS = 10;
	String category;
    Table data;

	Axis(String category, Table data) {
		this.category = category;
		this.data = data;
		data_max = data.getRow(0).getFloat(category);
		data_min = data_max;
		a = new Point();
		b = new Point();
		/* find data max and min */
		for (TableRow row : data.rows()) {
			if (row.getFloat(category) > data_max) {
				data_max = row.getFloat(category);
			} else if (row.getFloat(category) < data_min) {
				data_min = row.getFloat(category);
			}
		}
	}

	/* sets the endpoints of the axis based on current data and canvas*/
	public void setEndpoints(int[] margins, Point origin, int sections, int i) {
			a.x = origin.x + ((width - (margins[0] * 2))/sections) * i;
			b.x = a.x;
			a.y = height - margins[3];
			b.y = margins[2];
	}

	public void draw() {
		/* draws axis */
		strokeWeight(5);
		stroke(0);
		line(a.x, a.y, b.x, b.y);

		for (int i = 0; i < NUM_MARKS; i++) {
			Point p1 = new Point(a.x, a.y - (((a.y - b.y)/(NUM_MARKS - 1))*i));
			Point p2 = new Point(a.x - 20, a.y - (((a.y - b.y)/(NUM_MARKS - 1))*i));
			line(p1.x, p1.y, p2.x, p2.y);
			stroke(0);
			fill(0);
			text(data_min + ((data_max - data_min)/(NUM_MARKS - 1))*i, p1.x - 2, p2.y - 7);
		}

		/* labels axis */
		fill(0);
		textAlign(RIGHT);
		textSize(11);
		stroke(0);
		text(category, a.x + 20, a.y + 20); 
	}

	/* calculate coordinates on axis based on current endpoints */
	public ArrayList<Coordinate> getPointsOnAxis() {
		ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
		for (int i = 0; i < data.getRowCount(); i++) {
			float value = data.getRow(i).getFloat(category);
			float proportion = (value - data_min) / (data_max - data_min);
			Point p = new Point(a.x, (b.y - a.y) * proportion + a.y);
			/* sets classification */
			int classification = data.getRow(i).getInt("class");
			Coordinate coord = new Coordinate(p, classification);
			coords.add(coord);
		}
		return coords;
	}
};
