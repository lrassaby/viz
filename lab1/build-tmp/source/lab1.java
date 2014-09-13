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

int[] margins = {80, 30, 30, 100}; // left, top, right, bottom
Point origin, 
      topyaxis, 
      rightxaxis,
      buttonpos;
String hovertext;
Dimensions buttondim;
Chart chart;
Button button;
boolean hover;

public void setup () {
  frame.setResizable(true);
  size(700, 700);
  chart = new Chart();

  origin = new Point(margins[0], height - margins[3]);
  topyaxis = new Point(margins[0], margins[1]); 
  rightxaxis = new Point(width - margins[2], height - margins[3]);
  buttondim = new Dimensions(70, 30);
  buttonpos = new Point(width - buttondim.w - margins[2], margins[1]);

  button = new Button(buttonpos, buttondim, 7, color(255, 153, 51), "Bar Chart");

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

  buttonpos.setXY(width - buttondim.w - margins[2], margins[1]);
  button.draw();

  if (hover) {
    fill(255, 0, 0);
    textSize(20);
    if (mouseX < (width/2)) {
      textAlign(LEFT, CENTER);
      text(hovertext, mouseX, mouseY - 20);
    } else {
      textAlign(RIGHT, CENTER);
      text(hovertext, mouseX, mouseY - 20);
    }
  }
}

public void mouseClicked() {
  button.intersect(mouseX, mouseY);
  boolean selected = button.getIsect();
  if (selected) { 
    // click!
    chart.toggleChartSelection();
    if (chart.barchartSelected) {
      button.setText("Line Chart");
    } else {
      button.setText("Bar Chart");
    }
    button.setSelected(false);
  } 
}

public void mouseMoved() {
  String str = chart.mouseOver(mouseX, mouseY);
  if (str != "") { // BAD BAD BAD!
    hover = true;
    hovertext = str;
  } else {
    hover = false;
  }
}





public class Barchart {
    int minY, maxY;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    boolean isect;

    public String intersect (int mousex, int mousey) {
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8f);
        
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = PApplet.parseInt(datapoints[i].count * ratio) + origin.y;
            
            if (mousex > x - sectionWidth * 0.4f && mousex < x + sectionWidth * 0.4f) {
                // within the x range
                if (mousey > y && mousey < origin.y) {
                    // within the y range
                    return "(" + datapoints[i].fruit + ", " + datapoints[i].count + ")";
                }
            }
        }
        return "";
    }

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
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8f);
        strokeCap(SQUARE);
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = PApplet.parseInt(datapoints[i].count * ratio) + origin.y;
            line(x, origin.y, x, y);
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
      fill(button.getColor()); 
      rect(button.pos.x, button.pos.y, button.dim.w, button.dim.h, button.roundness); 

      fill(0);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      text(button.text, button.pos.x + dim.w / 2, button.pos.y + dim.h / 2);
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

public class Chart {
    Linechart linechart;
    Barchart barchart;
    boolean barchartSelected = false, linechartSelected = true;
    Point origin, topyaxis, rightxaxis;
    String x_label = "Fruits", y_label = "Count";
    FruitCount[] datapoints;
    int minY, maxY;

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
    public void toggleChartSelection() {
        barchartSelected = linechartSelected;
        linechartSelected = !linechartSelected;
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
        textSize(16); 
        textAlign(RIGHT, CENTER); 
        /* X labels */
        // X-axis label
        makeText(x_label, rightxaxis.x, rightxaxis.y + 70, false);
        // X value labels
        textSize(12); 
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8f);
        strokeCap(SQUARE);
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = origin.y + 10;
            makeText(datapoints[i].fruit, x, y, true);
        }
        /* Y labels */
        // Y-axis label
        textSize(16); 
        makeText(y_label, topyaxis.x - 60, topyaxis.y + 50, true);

        // Y value labels
        textSize(12);
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int increment;
        try {
            increment = PApplet.parseInt(35/abs(ratio));
        } catch (Exception e) {
            increment = 30;
        }
        for (int i = 0; i <= maxY; i+= increment) {
            makeText(Integer.toString(i), origin.x - 10, PApplet.parseInt(i * ratio + origin.y), false);
        }
    }
    public void makeText(String str, int x, int y, boolean vert) {      
        if (vert) {
            pushMatrix();
            translate(x, y);
            rotate(-HALF_PI);
            translate(-x, -y);
            text(str, x, y);
            popMatrix();
        } else {
            text(str, x, y);
        }
    }

    public String mouseOver(int x, int y) {
        if (barchartSelected) {
            return barchart.intersect(x, y);
        } else {
            return linechart.intersect(x, y);
        }
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
        
        minY = datapoints[0].count;
        maxY = datapoints[0].count;
        for (i = 1; i < datapoints.length; i++) {
            if (datapoints[i].count < minY) minY = datapoints[i].count;
            if (datapoints[i].count > maxY) maxY = datapoints[i].count;
        }
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
    int minY, maxY;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    boolean isect;
    int radius = 5;

    public String intersect (int mousex, int mousey) {
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8f);
        
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = PApplet.parseInt(datapoints[i].count * ratio) + origin.y + 4;
            
            if ((mousex - x) * (mousex - x) + (mousey - y) * (mousey - y) < (radius + 1) * (radius + 1)) {
                return "(" + datapoints[i].fruit + ", " + datapoints[i].count + ")";
            }
        }
        return "";
    }

    Linechart(FruitCount[] datapoints, Point origin, Point topyaxis, Point rightxaxis) {
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
        strokeWeight(2);
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        Point prev = new Point(origin.x + sectionWidth / 2, PApplet.parseInt(datapoints[0].count * ratio) + origin.y);
        drawCircle(prev.x, prev.y, 10);
        for (int i = 1; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = PApplet.parseInt(datapoints[i].count * ratio) + origin.y;
            line(prev.x, prev.y, x, y);
            prev.setXY(x, y);
            drawCircle(prev.x, prev.y, radius * 2);
        }
    }

    public void drawCircle(int x, int y, float diameter) {
        ellipse(x, y, diameter, diameter);
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
