import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lab8 extends PApplet {

ParallelCoordinatesGraph graph;
int initHoverBox_x, initHoverBox_y;
boolean mousePressed = false;

public void setup() {
	frame.setResizable(true);
 	size(900, 700);
	String[] categories, rows;
	Table data = loadTable("iris.csv", "header");
	rows = loadStrings("iris.csv");
	categories = rows[0].split(",");
	graph = new ParallelCoordinatesGraph(categories, data);
	graph.setup();
}

public void draw() {
	background(255, 255, 255);
	graph.draw();
}

public void mousePressed() {
	mousePressed = true;
	initHoverBox_x = mouseX;
	initHoverBox_y = mouseY;
}

public void mouseReleased() {
	mousePressed = false;
}
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
			text(data_min + ((data_max - data_min)/(NUM_MARKS - 1))*i, p1.x - 2, p2.y - 7);
		}

		/* labels axis */
		fill(0);
		textAlign(RIGHT);
		textSize(11);
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
public class ParallelCoordinatesGraph {
	static final int NUM_CLASSES = 3;
	int[] margins = {50, 50, 50, 50}; 
	/* array of axes on graph */
	ArrayList<Axis> axes;
	/* array of array of coordinates for each axis */
	ArrayList<ArrayList<Coordinate>> coordinates;
	/* array of colors needed to represent classification */
	ColorGenerator generator;
	int[] colors;
	/* parallel lines on the graph*/
	ArrayList<Line> lines;
	Point origin, rightxaxis;
	String[] categories;
	Table data;

	ParallelCoordinatesGraph(String[] categories, Table data) {
		axes = new ArrayList<Axis>();
		colors = new int[3];
		generator = new ColorGenerator();
		this.categories = categories;
		this.data = data;
	}

	public void setup() {
		for (int i = 0; i < NUM_CLASSES; i++) {
			colors[i] = generator.generate();
		}
		for (int i = 0; i < categories.length - 1; i++) {
	   		categories[i] = categories[i].trim();
	   		Axis a = new Axis(categories[i], data);
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
			rect(initHoverBox_x, initHoverBox_y, mouseX - initHoverBox_x, mouseY - initHoverBox_y);
		}
	}

	public boolean isHovered(Line l) {
		Point mouse = new Point(mouseX, mouseY);
		if ((distance(l.a, mouse) + distance(l.b, mouse)) - distance(l.a, l.b) < 0.05f) {
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
		return false;
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
public class Interaction {
	public void mousePressed() {
		int init_x = mouseX;
		int init_y = mouseY;
		println("hello!");
	}
}
/* holds two endpoints of the line and its class */
public class Line {
	Point a;
	Point b;
	int classification;

	Line(Point a, Point b, int classification) {
		this.a = a;
		this.b = b;
		this.classification = classification;
	} 

	public void draw(int c, boolean hovered) {
			strokeWeight(1);
			if (hovered) {
				stroke(255, 0, 0);
			}
			else {
				stroke(c, 120);
			}
		line(a.x, a.y, b.x, b.y);
	}
};
/* color generator based on golden ratio */
public class ColorGenerator {
    private float golden_ratio_conjugate = 0.618033988749895f;
    private float h = 0.5f;
    public int generate() {
        h += golden_ratio_conjugate;
        h %= 1;
        return hsvToRGB(h, 0.60f, 0.90f);
    }
    public int hsvToRGB (float h, float s, float v) {
        float h_i = PApplet.parseInt(h*6);
        float f = h*6 - h_i;
        float p = v * (1 - s);
        float q = v * (1 - f*s);
        float t = v * (1 - (1 - f) * s);
        int c = color(0, 0, 0);
        if (h_i == 0) {
            c = color(PApplet.parseInt(v * 256), PApplet.parseInt(t * 256), PApplet.parseInt(p * 256));
        } else if (h_i == 1) {
            c = color(PApplet.parseInt(q * 256), PApplet.parseInt(v * 256), PApplet.parseInt(p * 256));
        } else if (h_i == 2) {
            c = color(PApplet.parseInt(p * 256), PApplet.parseInt(v * 256), PApplet.parseInt(t * 256));
        } else if (h_i == 3) {
            c = color(PApplet.parseInt(p * 256), PApplet.parseInt(q * 256), PApplet.parseInt(v * 256));
        } else if (h_i == 4) {
            c = color(PApplet.parseInt(t * 256), PApplet.parseInt(p * 256), PApplet.parseInt(v * 256));
        } else if (h_i == 5) {
            c = color(PApplet.parseInt(v * 256), PApplet.parseInt(p * 256), PApplet.parseInt(q * 256));
        }
        return c;
    }
};

/* Point class */
public class Point {
    float x, y;
    String disp;
    Point() {
    	this.x = 0;
    	this.y = 0;
    }
    Point(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void setXY(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public void setDisp(String disp) {
        this.disp = disp;
    }
};

/* holds a point and its classification */
public class Coordinate {
	Point p;
	int classification;

	Coordinate(Point p, int classification) {
		this.p = p;
		this.classification = classification;
	}

    public void draw(int c) {
        fill(c);
        stroke(c);
        ellipse(p.x, p.y, 4, 4);
    }
};
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lab8" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
