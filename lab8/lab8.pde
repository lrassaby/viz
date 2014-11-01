ArrayList<Axis> axes;

void setup() {
	String[] categories;
	axes = new ArrayList<Axis>();
	Table data = loadTable(filename, "header");
	lines = loadStrings(filename);
	categories = lines[0].split(",");
	for (int i = 0; i < categories.length; i++) { // trim whitespace
	   categories[i] = categories[i].trim();
	   Axis a = new Axis(categories[i], data);
	}
}

void draw() {
	// draw axes
	for (Axis a : axes) {
		setAxes();
	}

	// draw lines
}