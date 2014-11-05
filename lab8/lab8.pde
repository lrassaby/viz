ParallelCoordinatesGraph graph;
int mouseClickX, mouseClickY;
boolean mousePressed = false;
boolean newClick = false;

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
	mouseClickX = mouseX;
	mouseClickY = mouseY;
}

void mouseReleased() {
	mousePressed = false;
	newClick = true;
}