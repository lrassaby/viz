ParallelCoordinatesGraph graph;
int mouseClickX, mouseClickY;
boolean mousePressed = false;
boolean newClick = false;
boolean newMouseClick = true;
boolean densityReduction = false;
boolean[] splines;
int max_a = 60;
int min_a = 50;

void setup() {
	frame.setResizable(true);
 	size(900, 700);
	String[] categories, rows;
	Table data = loadTable("iris.csv", "header");
	rows = loadStrings("iris.csv");
	categories = rows[0].split(",");
	graph = new ParallelCoordinatesGraph(categories, data);
	graph.setup();
	splines = new boolean[3];
}

void draw() {
	background(255, 255, 255);
	graph.draw();
}

void mousePressed() {
	mousePressed = true;
	mouseClickX = mouseX;
	mouseClickY = mouseY;
}

void mouseReleased() {
	mousePressed = false;
	newClick = true;
}

void mouseClicked() {
	newMouseClick = true;
}

void keyPressed() {
	for (int i = 0; i < 3; i++) {
		if (keyCode == 49 + i) {
			splines[i] = !splines[i];
		}
	}
	/* density reduction */
	if (keyCode == 68) {
		densityReduction = !densityReduction;
	}

	if (keyCode == 37) {
		min_a--;
	}

	if (keyCode == 38) {
		max_a++;
	}

	if (keyCode == 39) {
		min_a++;
	}

	if (keyCode == 40) {
		max_a--;
	}
}