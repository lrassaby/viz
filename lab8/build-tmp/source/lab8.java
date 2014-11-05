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
int mouseClickX, mouseClickY;
boolean mousePressed = false;
boolean newClick = false;

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
	mouseClickX = mouseX;
	mouseClickY = mouseY;
}

public void mouseReleased() {
	mousePressed = false;
	newClick = true;
}
/* represents a single axis/category */
public class Axis {
	Point a, b; // a is bottom, b is top
	float data_max, data_min;
	boolean flipped = false;
	boolean lastAxis;
	Button flip;
	static final int NUM_MARKS = 10;
	String category;
    Table data;

	Axis(String category, Table data, boolean lastAxis) {
		this.category = category;
		this.data = data;
		data_max = data.getRow(0).getFloat(category);
		data_min = data_max;
		this.lastAxis = lastAxis;
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
		Point button_center = new Point(b.x, b.y - 30);

		for (int i = 0; i < NUM_MARKS; i++) {
			Point p1 = new Point(a.x, a.y - (((a.y - b.y)/(NUM_MARKS - 1))*i));
			Point p2 = new Point(a.x - 20, a.y - (((a.y - b.y)/(NUM_MARKS - 1))*i));
			line(p1.x, p1.y, p2.x, p2.y);
			stroke(0);
			fill(0);

			if (newClick) {
				isFlipped();
			}
			/* draws flip button */
			flip = new Button(button_center, new Dimensions(30, 15), .5f, 160, "flip");
			flip.draw();

			if (flipped) {
				text(data_max - ((data_max - data_min)/(NUM_MARKS - 1))*i, p2.x, p2.y - 9);
			}
			else {
				text(data_min + ((data_max - data_min)/(NUM_MARKS - 1))*i, p2.x, p2.y - 9);
			}
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
		if (!flipped) {
			for (int i = 0; i < data.getRowCount(); i++) {
				float value = data.getRow(i).getFloat(category);
				float proportion = (value - data_min) / (data_max - data_min);
				Point p = new Point(a.x, (b.y - a.y) * proportion + a.y);
				/* sets classification */
				int classification = data.getRow(i).getInt("class");
				Coordinate coord = new Coordinate(p, classification);
				coords.add(coord);
			}
		}
		else {
			for (int i = 0; i < data.getRowCount(); i++) {
				float value = data.getRow(i).getFloat(category);
				float proportion = (value - data_min) / (data_max - data_min);
				Point p = new Point(a.x, b.y - (b.y - a.y) * proportion);
				/* sets classification */
				int classification = data.getRow(i).getInt("class");
				Coordinate coord = new Coordinate(p, classification);
				coords.add(coord);
			}
		}
		return coords;
	}

	public void isFlipped() {
		flip.intersect(mouseClickX, mouseClickY);
		if (flip.isect) {
			newClick = false;
			flipped = !flipped;
		}
		else if (lastAxis) {
			newClick = false;
		}
	}
};
public class Button {
  boolean isect;
  Point pos; 
  Dimensions dim;
  float roundness;
  int c;
  String text;
 

  public void draw() {
      strokeWeight(2);
      fill(getColor()); 
      rect(pos.x, pos.y, dim.w, dim.h, roundness); 

      fill(0);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      text(text, pos.x + dim.w / 2, pos.y + dim.h / 2);
  }

  
  Button(Point pos, Dimensions dim, float roundness, int c, String text) { /* height and width of button */
     this.isect = false;
     this.pos = pos;
     this.dim = dim;
     this.roundness = roundness;
     this.c = c;
     this.text = text;
  }

  
  public void intersect (int mousex, int mousey) {
    /* if it's within x + width and y + height */
    if (mousex <= (pos.x + dim.w) && mousex >= pos.x && mousey <= (pos.y + dim.h) && mousey >= pos.y) {
      isect = true;
    }
    else {
      isect = false;
    }
  }
  
  public void setSelected (boolean s) {
    isect = s;
  }

  public void setPos(Point pos) {
    this.pos = pos;
  }
  
  public void setSize (Dimensions dim) {
    this.dim = dim;
  }
  
  public void setColor (int r, int g, int b) {
    c = color (r, g, b);
  }

  public void setColorObject (int c) {
    this.c = c;
  }
  
  
  public void setColor (int c) {
    this.c = c;
  }
  
  public void setText (String text) {
     this.text = text; 
  }

  public boolean getIsect() {return isect;}
  public Point getPos() {return pos;}
  public Dimensions getDim() {return dim;}
  public int getColor() {return c;}
  public float getRoundness() {return roundness;}
  public String getText() {return text;}
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
	ArrayList<ArrayList<Line>> lineMap;
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
		for (int i = 0; i < lineMap.size(); i++) {
			for (int j = 0; j < lineMap.get(i).size(); j++) {
				lineMap.get(i).get(j).draw(colors[lineMap.get(i).get(j).classification - 1], isHovered(lineMap.get(i).get(j)), lineMap.get(i));
			}
		}

		if (mousePressed) {
			stroke(160);
			fill(200, 100);
			rect(mouseClickX, mouseClickY, mouseX - mouseClickX, mouseY - mouseClickY);
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

};
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

	public void draw(int c, boolean hovered, ArrayList<Line> lines) {
			strokeWeight(1);
			if (hovered) {
				stroke(255, 0, 0);
				for (Line l : lines) {
					line(l.a.x, l.a.y, l.b.x, l.b.y);
				}
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


// http://sebleedelisle.com/2009/05/super-fast-trianglerectangle-intersection-test/
public boolean lineRectangleIntersect(float x1, float y1, float x2, float y2,
                               float rx, float ry, float rw, float rh) {
                                  
  float topIntersection;
  float bottomIntersection;
  float topPoint;
  float bottomPoint;
 
  // Calculate m and c for the equation for the line (y = mx+c)
  float m = (y2-y1) / (x2-x1);
  float c = y1 -(m*x1);
 
  // If the line is going up from right to left then the top intersect point is on the left
  if(m > 0) {
    topIntersection = (m*rx  + c);
    bottomIntersection = (m*(rx+rw)  + c);
  }
  // Otherwise it's on the right
  else {
    topIntersection = (m*(rx+rw)  + c);
    bottomIntersection = (m*rx  + c);
  }
 
  // Work out the top and bottom extents for the triangle
  if(y1 < y2) {
    topPoint = y1;
    bottomPoint = y2;
  } else {
    topPoint = y2;
    bottomPoint = y1;
  }
 
  float topOverlap;
  float botOverlap;
 
  // Calculate the overlap between those two bounds
  topOverlap = topIntersection > topPoint ? topIntersection : topPoint;
  botOverlap = bottomIntersection < bottomPoint ? bottomIntersection : bottomPoint;
 
  return (topOverlap<botOverlap) && (!((botOverlap<ry) || (topOverlap>ry+rh)));
 
}


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

public class Dimensions {
    int w, h;
    Dimensions(int w, int h) {
        this.w = w;
        this.h = h;
    }
    public void setWH(int w, int h) {
        this.w = w;
        this.h = h;
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
