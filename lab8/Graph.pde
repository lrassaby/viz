public class ParallelCoordinatesGraph {
	static final int NUM_CLASSES = 3;
	int[] margins = {50, 50, 50, 50}; 
	/* array of axes on graph */
	ArrayList<Axis> axes;
	/* array of array of coordinates for each axis */
	ArrayList<ArrayList<Coordinate>> coordinates;
	/* array of colors needed to represent classification */
	ColorGenerator generator;
	color[] colors;
	/* parallel lines on the graph*/
	ArrayList<Line> lines;
	Point origin, rightxaxis;
	String[] categories;
	Table data;

	ParallelCoordinatesGraph(String[] categories, Table data) {
		axes = new ArrayList<Axis>();
		colors = new color[3];
		generator = new ColorGenerator();
		this.categories = categories;
		this.data = data;
	}

	public void setup() {
		for (int i = 0; i < NUM_CLASSES; i++) {
			colors[i] = generator.generate();
		}
		boolean lastAxis = false;
		for (int i = 0; i < categories.length - 1; i++) {
	   		categories[i] = categories[i].trim();
	   		if (i == categories.length - 2) {
	   			lastAxis = true;
	   		}
	   		Axis a = new Axis(categories[i], data, lastAxis);
	   		axes.add(a);
		}
	}

	public void draw() {
		/* draws bottom line */
		strokeWeight(5);
		stroke(0);
		origin = new Point(margins[0], height - margins[3]);
		rightxaxis = new Point(width - margins[2], height - margins[3]);
		line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);

		/* draws axes based on current width/height */
		for (int i = 0; i < axes.size(); i++) {
			axes.get(i).setEndpoints(margins, origin, axes.size() - 1, i);
			axes.get(i).draw();
		}
		/* draws lines */
		createLines();
		for (Line l : lines) {
			l.draw(colors[l.classification - 1], isHovered(l));
		}

		if (mousePressed) {
			stroke(160);
			fill(200, 100);
			rect(mouseClickX, mouseClickY, mouseX - mouseClickX, mouseY - mouseClickY);
		}
	}

	public boolean isHovered(Line l) {
		Point mouse = new Point(mouseX, mouseY);
		if ((distance(l.a, mouse) + distance(l.b, mouse)) - distance(l.a, l.b) < 0.05) {
			return true;
		}
		else if (mousePressed && isBoxHovered(l)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isBoxHovered(Line l) {
		float x = min(mouseClickX, mouseX);
		float y = min(mouseClickY, mouseY);
		float w = abs(mouseX - mouseClickX);
		float h = abs(mouseY - mouseClickY);
		return lineRectangleIntersect(l.a.x, l.a.y, l.b.x, l.b.y, 
									  x, y, w, h);
	}

	public float distance(Point a, Point b) {
        return sqrt(pow((a.x - b.x), 2) + pow((a.y - b.y), 2));
    }

	public void createLines() {
		getAllCoordinates();
		lines = new ArrayList<Line>();
		for (int i = 0; i < coordinates.size() - 1; i++) {
			for (int j = 0; j < coordinates.get(i).size(); j++) {
				/* two points on line */
				Point a = coordinates.get(i).get(j).p;
				Point b = coordinates.get(i + 1).get(j).p;
				/* classification of line*/
				int classification = coordinates.get(i).get(j).classification;
				Line l = new Line(a, b, classification);
				lines.add(l);
			}
		}
	}

	private void getAllCoordinates() {
		coordinates = new ArrayList<ArrayList<Coordinate>>();
		for (Axis a : axes) {
			coordinates.add(a.getPointsOnAxis());
		}
	}

};