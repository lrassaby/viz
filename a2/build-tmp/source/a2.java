import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class a2 extends PApplet {

// TODO: additional requirements


int[] margins = {80, 65, 30, 100}; // left, top, right, bottom
Point origin, 
      topyaxis, 
      rightxaxis;
String hovertext;
Dimensions buttondim;
TransitionChart chart;

// To create a new chart just change the strings in here 
String[] chart_texts = {"Bar Chart", "Line Chart", "Pie Chart"};

Button[] buttons = new Button[chart_texts.length]; 
Point[] buttonpos = new Point[chart_texts.length];
boolean hover;

public void setup () {
  frame.setResizable(true);
  size(700, 700);

  origin = new Point(margins[0], height - margins[3]);
  topyaxis = new Point(margins[0], margins[1]); 
  rightxaxis = new Point(width - margins[2], height - margins[3]);
  buttondim = new Dimensions(100, 30);


  for (int i = 0; i < buttons.length; i++) {
    buttonpos[i] = new Point(width - buttondim.w - margins[2] - (buttons.length - i - 1) * (buttondim.w + 10), margins[1] - 50);
    buttons[i] = new Button(buttonpos[i], buttondim, 7, color(50, 200, 160), chart_texts[i]);
  }

  String file = null;
  try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
    e.printStackTrace();
  } 
  try {
      file = JOptionPane.showInputDialog(frame, "Input file (type csv)", "data.csv");
  } catch (Exception e) {
      println("Process cancelled.");
      exit();
  }
  if (file == null) {
      println("Process cancelled.");
      exit();
  }
  Table data = loadTable(file, "header");

  chart = new TransitionChart(data, origin, topyaxis, rightxaxis);
}


public void draw() {

  background(240, 240, 240);

  origin.setXY(margins[0], height - margins[3]);
  topyaxis.setXY(margins[0], margins[1]);
  rightxaxis.setXY(width - margins[2], height - margins[3]);

  chart.setAxes(origin, topyaxis, rightxaxis);
  chart.draw();

  String chart_type = chart.getChartType();

  for (int i = 0; i < buttons.length; i++) {
    buttonpos[i].setXY(width - buttondim.w - margins[2] - (buttons.length - i - 1) * (buttondim.w + 10), margins[1] - 50);
    if (chart_texts[i] == chart_type) {
      buttons[i].setColor(40, 190, 100);
    } else {
      buttons[i].setColor(50, 200, 160);
    }
    buttons[i].draw();
  }

  if (hover) {
    fill(255, 0, 0);
    textSize(20);
    // chart.highlightOnHover();
    fill(255, 0, 0);
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
  for (int i = 0; i < buttons.length; i++) {
    buttons[i].intersect(mouseX, mouseY);
    boolean selected = buttons[i].getIsect();
    if (selected) { 
      buttons[i].setSelected(false);
      if (!chart.inTransition()) {
        chart.setChartType(chart_texts[i]);
      }
    }
  }
}



public class AxisChart {
    Linechart linechart;
    Barchart barchart;
    boolean barchartSelected = false, linechartSelected = true;
    Point origin, topyaxis, rightxaxis;
    String x_label = "Fruits", y_label = "Count";
    FruitCount[] datapoints;
    int minY, maxY;

    AxisChart (Table data, Point origin, Point topyaxis, Point rightxaxis) {
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
            increment = PApplet.parseInt(25/abs(ratio));
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

    public void highlightOnHover() {
        if (barchartSelected) {
            barchart.highlightOnHover();
        } else if (linechartSelected) {
            linechart.highlightOnHover();
        }
    }
};
public class Barchart {
    int minY, maxY;
    Point origin, topyaxis, rightxaxis;
    FruitCount[] datapoints;
    boolean isect;
    int dataHovered; 

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
                    dataHovered = i;
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

    public void highlightOnHover() {
            float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
            int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
            
            int x = origin.x + sectionWidth * dataHovered + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = PApplet.parseInt(datapoints[dataHovered].count * ratio) + origin.y;
            stroke(255, 255, 0);
            strokeWeight(sectionWidth * 0.8f);
            strokeCap(SQUARE);
            line(x, origin.y, x, y);
            stroke(0);
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
      fill(c); 
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
    int dataHovered;

    public String intersect (int mousex, int mousey) {
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        strokeWeight(sectionWidth * 0.8f);
        
        for (int i = 0; i < datapoints.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = PApplet.parseInt(datapoints[i].count * ratio) + origin.y + 4;
            
            if ((mousex - x) * (mousex - x) + (mousey - y) * (mousey - y) < (radius + 1) * (radius + 1)) {
                dataHovered = i;
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
        Point prev = new Point(origin.x + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f), PApplet.parseInt(datapoints[0].count * ratio) + origin.y);
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

    public void highlightOnHover() {
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
        Point prev = new Point(origin.x + sectionWidth / 2, PApplet.parseInt(datapoints[0].count * ratio) + origin.y);
        int x = origin.x + sectionWidth * dataHovered + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
        int y = PApplet.parseInt(datapoints[dataHovered].count * ratio) + origin.y;
            
        prev.setXY(x, y);
        fill(255, 255, 0);
        stroke(255, 255, 0);
        drawCircle(prev.x, prev.y, radius * 2);
        stroke(0);
        fill(0);
    }
};
public class Piechart {
	float radius;
    Point center;
    FruitCount[] datapoints;
    float[] ratios;
    int dataHovered;

    public boolean intersect (int mousex, int mousey) {
    	return(dist(center.x, center.y, mousex, mousey) <= radius);
    }

    Piechart(FruitCount[] datapoints, Point center, float radius) {
        setData(datapoints, center, radius);
    }

    public void setData(FruitCount[] datapoints, Point center, float radius) {
        this.datapoints = datapoints;
        this.center = center;
        this.radius = radius;
        int total_magnitude = 0;
        for (int i = 0; i < datapoints.length; i++) {
        	total_magnitude += datapoints[i].count;
        }
        for (int i = 0; i < datapoints.length; i++) {
        	ratios[i] = (datapoints[i].count/total_magnitude);
        }
    }

    public void draw () {
    	drawCircle(center.x, center.y, radius * 2);
        for (int i = 0; i < datapoints.length; i++) {
            float x = cos(radians(360 * ratios[i])) * radius;
            float y = sin(radians(360 * ratios[i])) * radius;
            line(center.x, center.y, x, y);
        }
    }

    public void drawCircle(int x, int y, float diameter) {
        ellipse(x, y, diameter, diameter);
    }

    // void highlightOnHover() {
    //     float ratio = float((topyaxis.y - origin.y)) / maxY;
    //     int sectionWidth = abs(((rightxaxis.x - origin.x) / datapoints.length));
    //     Point prev = new Point(origin.x + sectionWidth / 2, int(datapoints[0].count * ratio) + origin.y);
    //     int x = origin.x + sectionWidth * dataHovered + sectionWidth / 2 + int(sectionWidth * 0.1);
    //     int y = int(datapoints[dataHovered].count * ratio) + origin.y;
            
    //     prev.setXY(x, y);
    //     fill(255, 255, 0);
    //     stroke(255, 255, 0);
    //     drawCircle(prev.x, prev.y, radius * 2);
    //     stroke(0);
    //     fill(0);
    // }
};
public class TransitionChart {
    private String prev_chart_type;
    private String chart_type;
    private boolean in_transition;
    private AxisChart chart;
    TransitionChart(Table data, Point origin, Point topyaxis, Point rightxaxis) {
        this.chart = new AxisChart(data, origin, topyaxis, rightxaxis);
        this.in_transition = false;
    }
    public void setChartType(String chart_type) {
        if (!in_transition) {
            this.prev_chart_type = this.chart_type;
            this.chart_type = chart_type;
            in_transition = true;
        }
    }
    public Boolean inTransition() {
        return in_transition;
    }
    public String getChartType() {
        return chart_type;
    }
    public void setAxes(Point origin, Point topyaxis, Point rightxaxis) {
        chart.setAxes(origin, topyaxis, rightxaxis);
    }
    public void draw() {
        chart.draw();
    }
};
public void setup() {
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

public void draw() {
	chart.draw();
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
