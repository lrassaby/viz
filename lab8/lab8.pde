ParallelCoordinatesGraph graph;
int initHoverBox_x, initHoverBox_y;
boolean mousePressed = false;

void setup() {
	frame.setResizable(true);
 	size(900, 700);
	String[] categories, rows;
	Table data = loadTable("iris.csv", "header");
	rows = loadStrings("iris.csv");
	categories = rows[0].split(",");
	graph = new ParallelCoordinatesGraph(categories, data);
	graph.setup();
}

void draw() {
	background(255, 255, 255);
	graph.draw();
}

void mousePressed() {
	mousePressed = true;
	initHoverBox_x = mouseX;
	initHoverBox_y = mouseY;
}

void mouseReleased() {
	mousePressed = false;
}