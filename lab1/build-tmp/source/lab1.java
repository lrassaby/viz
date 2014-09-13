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

public class lab1 extends PApplet {

int[] margins = {120, 30, 30, 120}; // left, top, right, bottom
Button button = new Button(new Point(width - 50, height + 20), new Dimensions(70, 30), 7, color(255, 153, 51), "Bar Chart");
Chart chart = new Chart();
Point origin, 
      topyaxis, 
      rightxaxis;

public void setup () {
  frame.setResizable(true);
  size(700, 700);

  origin = new Point(margins[0], height - margins[3]);
  topyaxis = new Point(margins[0], margins[1]); 
  rightxaxis = new Point(width - margins[2], height - margins[3]);

  Table data = loadTable("data.csv", "header");
  chart.setup(data, origin, topyaxis, rightxaxis);
}


public void draw() {

  background(200, 200, 200);

  origin.setXY(margins[0], height - margins[3]);
  topyaxis.setXY(margins[0], margins[1]);
  rightxaxis.setXY(width - margins[2], height - margins[3]);

  chart.setAxes(origin, topyaxis, rightxaxis);
  chart.draw();
}

public void mouseClicked() {
  button.intersect(mouseX, mouseY);
}
public void mouseMoved() {
  button.intersect(mouseX, mouseY);
}





public class Barchart {
    int minY, maxY;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    Barchart(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        setData(datapoints, origin, topyaxis, rightxaxis);
    }
    public void setData(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        this.datapoints = datapoints;
        this.origin = origin;
        this.topyaxis = topyaxis;
        this.rightxaxis = rightxaxis;
        minY = datapoints[0].count;
        maxY = datapoints[0].count;
        for (int i = 1; i < datapoints.length; i++) {
            if (datapoints[i].count < minY) minY = datapoints[i].count;
            if (datapoints[i].count > maxY) maxY = datapoints[i].count;
        }
    }
    public void draw () {
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8f);
        strokeCap(SQUARE);
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + (sectionWidth * (i + 1)) / 2;
            int ratio = (topyaxis.y - origin.y) / maxY;
            int y = datapoints[i].count * ratio + origin.y;
            line(x, origin.y, x, y);
        }
    }
};
public class Button {
  boolean isect;
  Point center, pos; 
  Dimensions dim;
  float roundness;
  int c;
  String text;
 

  public void draw() {
      boolean selected = button.getIsect();
      if (selected) { 
         // DO SOMETHING
         button.setSelected(false);
      } 

      fill(button.getColor()); 

      fill(255);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      text(button.getText(), button.center.x, button.center.y);
  }

  
  Button(Point pos, Dimensions dim, float roundness, int c, String text) { /* height and width of button */
     this.isect = false;
     this.pos = pos;
     this.dim = dim;
     this.roundness = roundness;
     this.c = c;
     this.center = center;
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
  public Point getCenter() {return center;}
  public Point getPos() {return pos;}
  public Dimensions getDim() {return dim;}
  public int getColor() {return c;}
  public float getRoundness() {return roundness;}
  public String getText() {return text;}
};

public class Chart {
    Linechart linechart;
    Barchart barchart;
    boolean barchartSelected = true, linechartSelected = false;
    Point origin, topyaxis, rightxaxis;
    String x_label = "Fruits", y_label = "Count";
    FruitCount[] datapoints;

    public void draw() {
        drawAxes();
        drawLabels();
        if (barchartSelected) {
            barchart.setData(datapoints, origin, topyaxis, rightxaxis);
            barchart.draw();
        } else if (linechartSelected) {
            linechart.setData(datapoints, origin, topyaxis, rightxaxis);
            linechart.draw();
        }
    }
    public void setAxes(Point origin, Point topyaxis, Point rightxaxis) {
        this.origin = origin;
        this.topyaxis = topyaxis;
        this.rightxaxis = rightxaxis;
    }
    public void drawAxes() {
        strokeWeight(2);
        line(origin.x, origin.y, topyaxis.x, topyaxis.y);
        line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);
    }
    public void drawLabels() {
        fill(0);
        textSize(12); 
        textAlign(CENTER, CENTER); 
    }
    public void setup(Table data, Point origin, Point topyaxis, Point rightxaxis) {
        /* get data */
        int i = 0;
        datapoints = new FruitCount[data.getRowCount()];       
        for (TableRow row : data.rows()) {
            datapoints[i++] = new FruitCount(row.getString("Name"), parseInt(row.getString("Number")));
        }
        setAxes(origin, topyaxis, rightxaxis);
        /* initialize */
        barchart = new Barchart(datapoints, origin, topyaxis, rightxaxis);
        linechart = new Linechart(datapoints, origin, topyaxis, rightxaxis);
    }
};
public class Point {
    int x, y;
    String disp;
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setDisp(String disp) {
        this.disp = disp;
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

public class Line {
    Point start, end;
    String disp;
    Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
    public void draw() {
        
    }
    public void setPoints(Point start, Point end) {
        this.start = start;
        this.end = end;
    }
};


public class FruitCount {
    String fruit;
    int count;
    FruitCount(String fruit, int count) {
        this.fruit = fruit;
        this.count = count;
    }
    public void setFruitCount(String fruit, int count) {
        this.fruit = fruit;
        this.count = count;
    }
};
public class Linechart {
    int minY = 1000000, maxY = -1000000;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    Linechart(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        setData(datapoints, origin, topyaxis, rightxaxis);
    }
    public void setData(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
        this.datapoints = datapoints;
        this.origin = origin;
        this.topyaxis = topyaxis;
        for (int i = 0; i < datapoints.length; i++) {
            if (datapoints[i].count < minY) minY = datapoints[i].count;
            if (datapoints[i].count > maxY) maxY = datapoints[i].count;
        }
    }
    public void draw () {
        // Draw function
        // Circles w/ ellipse function
        for (int i = 0; i < datapoints.length; i++) {
            println(datapoints[i].fruit + " " + datapoints[i].count);
        }
    }
};
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lab1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
