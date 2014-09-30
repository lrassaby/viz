void setup() {
    frame.setResizable(true);
    size(700, 700);
	radius = 50;
	center = new Point(CENTER, CENTER);

  	Table data = loadTable("data.csv", "header");
  	datapoints = new FruitCount[data.getRowCount()];       
        for (TableRow row : data.rows()) {
            datapoints[i++] = new FruitCount(row.getString("Name"), parseInt(row.getString("Number")));
        }
	Piechart chart = new Piechart(datapoints, center, radius);
}

void draw() {
	chart.draw();
}