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
	ArrayList<ArrayList<Line>> lineMap;
	Point origin, rightxaxis;
	String[] categories;
	Table data;
	boolean dimensionSelected = false;
	int dimension;
	boolean drawDense = true;

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

		if (newMouseClick) {
			for (int i = 0; i < axes.size(); i++) {
				if (axes.get(i).isDimensionSelected()) {
					newMouseClick = false;
					dimensionSelected = !dimensionSelected;
					dimension = i;
				}
			}
		}
		/* draws lines */
		createLines();
		for (int i = 0; i < lineMap.size(); i++) {
			if (splines[lineMap.get(i).get(0).classification - 1]) {
				noFill();
				beginShape();
			}
			if (densityReduction) {
					drawDense = isDifferent(i);
			} 
			for (int j = 0; j < lineMap.get(i).size(); j++) {
				if (!dimensionSelected && drawDense) {
					lineMap.get(i).get(j).draw(colors[lineMap.get(i).get(j).classification - 1], isHovered(lineMap.get(i).get(j), lineMap.get(i)), j);
				}
				else if (drawDense) {
					int ratio1 = 105 + (int)((data.getRow(i).getInt(dimension)/axes.get(dimension).data_max)*150);
					int ratio2 = 20 + (int)((data.getRow(i).getInt(dimension)/axes.get(dimension).data_max)*235);
					color toSend = color(150, ratio1, ratio2);
					lineMap.get(i).get(j).draw(toSend, isHovered(lineMap.get(i).get(j), lineMap.get(i)), j);
				}
			}
			if (splines[lineMap.get(i).get(0).classification - 1]) {
				endShape();
			}
		}
		drawDense = true;
		if (mousePressed) {
			stroke(160);
			fill(200, 100);
			rect(mouseClickX, mouseClickY, mouseX - mouseClickX, mouseY - mouseClickY);
		}
	}

	public boolean isHovered(Line l, ArrayList<Line> lines) {
		Point mouse = new Point(mouseX, mouseY);
		if ((distance(l.a, mouse) + distance(l.b, mouse)) - distance(l.a, l.b) < 0.05) {
			return true;
		}
		if (lines != null) {
			for (Line line : lines) {
				if (isHovered(line, null)) {
					return true;
				}
			}
		}
			
		if (mousePressed && isBoxHovered(l)) {
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
		lineMap = new ArrayList<ArrayList<Line>>();
		for (int i = 0; i < coordinates.size() - 1; i++) {
			for (int j = 0; j < coordinates.get(i).size(); j++) {
				/* two points on line */
				Point a = coordinates.get(i).get(j).p;
				Point b = coordinates.get(i + 1).get(j).p;
				/* classification of line*/
				int classification = coordinates.get(i).get(j).classification;
				Line l = new Line(a, b, classification);
				if (i == 0) {
					lineMap.add(new ArrayList<Line>());
				}
				lineMap.get(j).add(l);
			}
		}
	}

	private void getAllCoordinates() {
		coordinates = new ArrayList<ArrayList<Coordinate>>();
		for (Axis a : axes) {
			coordinates.add(a.getPointsOnAxis());
		}
	}

	private boolean isDifferent(int k) {
		float difference = 0;
		for (int i = 0; i < k; i++)	{
			difference = 0;
			for (int j = 0; j < 3; j++) {
				difference += abs(lineMap.get(k).get(j).a.y - lineMap.get(i).get(j).a.y);
			}
		}
		if (difference < 200) {
			return false;
		}	
		else {
			return true;
		}

	}

};