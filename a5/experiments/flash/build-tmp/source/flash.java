import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import java.util.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class flash extends PApplet {



final int DECIDE_YOURSELF = -1; // This is a placeholder for variables you will replace.
final int NUM_TRIALS = 20;
/**
 * This is a global variable for the dataset in your visualization. You'll be overwriting it each trial.
 */
Data d = null;
Table table = null;
Barchart barchart = null;
Piechart piechart = null;
Tree tree = null;
//int chartType = int(random(0.0, 2.99));
int chartType = 0;
int marked1;
int marked2;

public void setup() {
    totalWidth = displayWidth;
    totalHeight = displayHeight;
    chartLeftX = totalWidth / 2.0f - chartSize / 2.0f;
    chartLeftY = totalHeight / 2.0f - chartSize / 2.0f - margin_top;

    size((int) totalWidth, (int) totalHeight);
    //if you have a Retina display, use the line below (looks better)
    //size((int) totalWidth, (int) totalHeight, "processing.core.PGraphicsRetina2D");
    
    background(255);
    frame.setTitle("Comp150-07 Visualization, Lab 5, Experiment");

    cp5 = new ControlP5(this);
    pfont = createFont("arial", fontSize, true); 
    textFont(pfont);
    page1 = true;

    /**
     ** Finish this: decide how to generate the dataset you are using (see DataGenerator)
     **/
    marked1 = PApplet.parseInt(random(0.0f, NUM - 0.1f));
    marked2 = PApplet.parseInt(random(0.0f, NUM - 0.1f));
    if (marked2 == marked1) {
      marked2 = 0;
    }
    if (marked2 == marked1) {
      marked2 = 1;
    }
    d = new Data(marked1, marked2);
    
    
    
    /**
     ** Finish this: how to generate participant IDs
     ** You can write a short alphanumeric ID generator (cool) or modify this for each participant (less cool).
     **/
    partipantID = 5;
}

public void draw() {
    textSize(fontSize);
    barchart = new Barchart(d, null,chartLeftX, chartLeftY, chartSize);
    piechart = new Piechart(d, null,chartLeftX, chartLeftY, chartSize);
    tree     = new Tree(d);
    /**
     ** add more: you may need to draw more stuff on your screen
     **/
    if (index < 0 && page1) {
        drawIntro();
        page1 = false;
    } else if (index >= 0 && index < NUM_TRIALS) {
        if (index == 0 && page2) {
            clearIntro();
            drawTextField();
            drawInstruction();
            page2 = false;
        }

        /**
         **  Finish this: decide the chart type. You can do this randomly.
         **/
        stroke(0);
        strokeWeight(1);
        fill(255);
        rectMode(CORNER);
                 /*
                  * all your charts must be inside this rectangle
                  */
        rect(chartLeftX, chartLeftY, chartSize, chartSize);
        switch (chartType) {
            case 0:
                barchart.draw(1, Transition.NONE);
                break;
            case 1:
                piechart.draw(1, Transition.NONE);
                break;
            case 2:
                tree.draw();
                break;
            case 3:
                /**
                 ** finish this: 4th visualization
                 **/
                break;
            case 4:
                /**
                 ** finish this: 5th visualization
                 **/
                break;
        }

        drawWarning();

    } else if (index >NUM_TRIALS - 1 && pagelast) {
        drawThanks();
        drawClose();
        pagelast = false;
    }
}

/**
 * This method is called when the participant clicked the "NEXT" button.
 */
public void next() {
    String str = cp5.get(Textfield.class, "answer").getText().trim();
    float num = parseFloat(str);
    /*
     * We check their percentage input for you.
     */
    if (!(num >= 0)) {
        warning = "Please input a number!";
        if (num < 0) {
            warning = "Please input a non-negative number!";
        }
    } else if (num > 100) {
        warning = "Please input a number between 0 - 100!";
    } else {
        if (index >= 0 && index < NUM_TRIALS) {
            float ans = parseFloat(cp5.get(Textfield.class, "answer").getText());

            /**
             ** Finish this: decide how to compute the right answer
             **/
            truePerc = d.getValue(marked1)/d.getValue(marked2); // hint: from your list of DataPoints, extract the two marked ones to calculate the "true" percentage
            if (truePerc > 1) {truePerc = 1/truePerc;}
            truePerc = truePerc * 100.0f;
            
            reportPerc = ans; // this is the participant's response
            
            /**
             ** Finish this: decide how to compute the log error from Cleveland and McGill (see the handout for details)
             **/
            float off = 1.0f/8.0f;
            float ab = abs(reportPerc - truePerc);
            error = log(ab + off)/log(2);
            
            //saveJudgement(truePerc, reportPerc, error, partipantID, chartType);
            saveJudgement();
        }

        /**
         ** Finish this: decide the dataset (similar to how you did in setup())
         **/
        marked1 = PApplet.parseInt(random(0.0f, NUM - 0.1f));
        marked2 = PApplet.parseInt(random(0.0f, NUM - 0.1f));
        if (marked2 == marked1) {
          marked2 = 0;
        }
        if (marked2 == marked1) {
          marked2 = 1;
        }
        d = new Data(marked1, marked2);

        cp5.get(Textfield.class, "answer").clear();
        index++;
        //if ((index) % vis.length == 0 ) {
          //chartType = int(random(0.0, 2.99));
        //}
        //else {
          //chartType = (chartType + 1)%(vis.length);
        //}

        if (index == NUM_TRIALS - 1) {
            pagelast = true;
        }
    }
}

/**
 * This method is called when the participant clicked "CLOSE" button on the "Thanks" page.
 */
public void close() {
    /**
     ** Change this if you need to do some final processing
     **/
    saveExpData();
    exit();
}

/**
 * Calling this method will set everything to the intro page. Use this if you want to run multiple participants without closing Processing (cool!). Make sure you don't overwrite your data.
 */
public void reset(){
    /**
     ** Finish/Use/Change this method if you need 
     **/
    partipantID = DECIDE_YOURSELF;
    d = null;

    /**
     ** Don't worry about the code below
     **/
    background(255);
    cp5.get("close").remove();
    page1 = true;
    page2 = false;
    pagelast = false;
    index = -1;
}
public class AxisChart {
    protected Data data;
    protected String[] categories;
    protected float maxY; // for single columned 
    protected float superMaxY; // for multi columned
    public int[] margins = new int[4];
    protected Point origin, topyaxis, rightxaxis;

    AxisChart (Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        this.data = data;
        this.categories = categories;
        maxY = NUM;
        superMaxY = NUM;
        margins[0] = 10 + PApplet.parseInt(chartLeftX);
        margins[1] = PApplet.parseInt(chartLeftY) + 20; 
        margins[2] = width - PApplet.parseInt(chartSize) - PApplet.parseInt(chartLeftX) + 10;
        margins[3] = height - PApplet.parseInt(chartSize) - PApplet.parseInt(chartLeftY) + 10;
        /*for (TableRow row : data.rows()) {
            int elemweight = row.getInt(categories[1]);
            if (elemweight > maxY) {
                maxY = elemweight;
            }
            int rowweight = 0;
            for (int i = 1; i < categories.length; i++) {
                rowweight += row.getInt(categories[i]);
            }
            if (rowweight > superMaxY) {
                superMaxY = rowweight;
            }
        }*/
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]); 
        rightxaxis = new Point(width - margins[2], height - margins[3]);
    }

 
    public void drawAxes(int c) {
        strokeWeight(2);
        stroke(c);
        fill(c);
        line(origin.x, origin.y, topyaxis.x, topyaxis.y - 15);
        line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);
    }

    protected void drawLabels(int c, float ratio) {
        stroke(c);
        fill(c);
        textSize(16); 
        textAlign(RIGHT, CENTER); 
        /* X labels */
        // X-axis label
        /*makeText(categories[0], rightxaxis.x, rightxaxis.y + 70, 0);*/
        // X value labels
        textSize(12); 
        int sectionWidth = abs(((rightxaxis.x - origin.x) / NUM));
        strokeWeight(sectionWidth * 0.8f);
        strokeCap(SQUARE);
        /*for (int i = 0; i < data.getRowCount(); i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = origin.y + 10;
            makeText(data.getRow(i).getString(categories[0]), x, y, -HALF_PI / 2);
        }*/
        /* Y labels */
        // Y-axis label
        textSize(16); 
        //makeText(categories[1], topyaxis.x - 60, topyaxis.y + 50, -HALF_PI);
       
        // Y value labels
        if (ratio < 100) {
            textSize(12);
        
            int increment;
            try {
                increment = PApplet.parseInt(25/ratio);
            } catch (Exception e) {
                increment = 30;
            }
            if (increment < 1) {
                increment = 1;
            }

            float max = PApplet.parseFloat(origin.y - topyaxis.y) / ratio;
            for (int i = 0; i <= max * 1.03f; i+= increment) {
                //makeText(Integer.toString(i), origin.x - 10, int(-i * ratio + origin.y), 0);
            }
        } 
    }
};
public class Barchart extends AxisChart {
    boolean marking;
    Barchart(Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        super(data, categories, chartLeftX, chartLeftY, chartSize);
        marking = true;
    }

    public void draw (float transition_completeness, Transition transition) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        float c = 0;
        switch(transition) {
            case NONE:
            case LINETOBAR:
            case BARTOLINE:
                c = 0;
                break;
            case BARTOPIE:
            case PIETOBAR:
                c = serp(255, 0, transition_completeness);
                break;
        }
        int col = color(c, c, c);
        drawAxes(col);
        drawLabels(col, PApplet.parseFloat(origin.y - topyaxis.y) / maxY);
        drawData(transition_completeness, transition);
    }


    public void drawData (float transition_completeness, Transition transition) {
        float ratio = PApplet.parseFloat(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / NUM));
        strokeWeight(serp(5, sectionWidth * 0.8f, transition_completeness));
        stroke(0);
        strokeCap(SQUARE);


        switch(transition) {
            case NONE:
                if (frameCount%120 >60) marking = !marking;
                for (int i = 0; i < NUM; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = origin.y - PApplet.parseInt(data.getValue(i) * ratio);
                    
                    //println(marking);
                    if (marking) {
                        if(data.getMark(i)&&marking) {
                            stroke(0,255,0);
                      //strokeWeight(5);
                      //ellipse(x, origin.y - (int(ratio))/2, 12, 12);
                        }
                        else {
                            stroke(0);
                        }
                    } else {
                        stroke(0);
                    }
                    line(x, origin.y, x, y);
                    
                    strokeWeight(serp(5, sectionWidth * 0.8f, transition_completeness));

                }
                break;
            case LINETOBAR:
            case BARTOLINE:
                /*for (int i = 0; i < NUM; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, serp(y, origin.y, transition_completeness), x, y);
                }*/
                break;
            case BARTOPIE:
            case PIETOBAR:
                /*for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, origin.y, x, y);
                }*/
                break;
        }
        
    }   
};
public class CircleChart {
    protected Data data;
    protected String[] categories;
    protected float[] angles;
    protected int[] colors;
    protected ColorGenerator colorgenerator;
    protected int[] margins = {100, 150, 220, 100};
    protected Point origin, topyaxis, rightxaxis;
    protected float maxY;
    protected float ratio;
    protected float const_angle;
    protected int superMaxY = 0;
    protected float total_magnitude = 0;

    CircleChart (Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        setData(data, categories, chartLeftX, chartLeftY, chartSize);
    }

    public void setData(Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        colorgenerator = new ColorGenerator();
        this.data = data;
        this.categories = categories;
        maxY = data.getValue(0);
        for (int i = 0; i < NUM; i++) {
            total_magnitude += data.getValue(i);
            if (data.getValue(i) > maxY) {
              maxY = data.getValue(i);
            }
        }

        const_angle = (360 / (float)(NUM));
        int colorcount = NUM;
        colors = new int[colorcount];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorgenerator.generate();
        }
        margins[0] = 10 + PApplet.parseInt(chartLeftX);
        margins[1] = PApplet.parseInt(chartLeftY) + 20; 
        margins[2] = width - PApplet.parseInt(chartSize) - PApplet.parseInt(chartLeftX) + 10;
        margins[3] = height - PApplet.parseInt(chartSize) - PApplet.parseInt(chartLeftY) + 10;
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]);
        rightxaxis = new Point(width - margins[2], height - margins[3]);

        
        /*for (TableRow row : data.rows()) {
            int rowweight = row.getInt(categories[1]);
            if (rowweight > maxY) {
                maxY = rowweight;
            }
        }*/

        this.angles = new float[NUM];
        for (int i = 0; i < NUM; i++) {
            angles[i] = ((data.getValue(i) / total_magnitude) * 360.0f);
        }

        /*for (TableRow row : data.rows()) {
            int elemweight = row.getInt(categories[1]);
            if (elemweight > maxY) {
                maxY = elemweight;
            }
            int rowweight = 0;
            for (int i = 1; i < categories.length; i++) {
                rowweight += row.getInt(categories[i]);
            }
            if (rowweight > superMaxY) {
                superMaxY = rowweight;
            }
        }*/

        float temp = height / 2  - 40;
        ratio = ((maxY) / temp);
    }
};
class Data {
    class DataPoint {
        private float value = -1;
        private boolean marked = false;

        DataPoint(float f, boolean m) {
            this.value = f;
            this.marked = m;
        }

        public boolean isMarked() {
            return marked;
        }

        public void setMark(boolean b) {
            this.marked = b;
        }

        public float getValue() {
            return this.value;
        }
    }

    public DataPoint[] data = null;

    Data(int marked1, int marked2) {
        // NUM is a global varibale in support.pde
        data = new DataPoint[NUM];

        for (int i = 0; i < NUM; i++) {
            data[i] = new DataPoint(random(2.0f, 10.0f), false);
            if (i == marked1 || i == marked2) {
                data[i].setMark(true);
            }
        }
    }

    public float getValue(int i) {
        return data[i].value;
    }

    public boolean getMark(int i) {
        return data[i].isMarked();
    }

}
public class Point {
    int x, y;
    String disp;
    Point() {
        this.x = 0;
        this.y = 0;
    }
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

public class Color {
    int r, g, b;
    Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public void setRGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
};

public class Rect {
    float d_short, d_long;
    String name;

    Rect(float d_short, float d_long, String name) {
        this.d_short = d_short;
        this.d_long = d_long;
        this.name = name;
    }
    // value between 0 and 1, where closer to 1 is more square
    public float getAspectRatio() {
        float aspect_ratio = d_short / d_long;
        if (aspect_ratio > 1) {
            aspect_ratio = 1 / aspect_ratio;
        }
        return aspect_ratio;
    }

    public float size() {
        return d_short * d_long;
    }

};

public class Canvas {
  float x, y, w, h;
  Canvas(float x, float y, float w, float h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  public float size() {
    return w * h;
  }
  public float getShortSide() {
    return w <= h ? w : h;
  }
  public float getLongSide() {
    return w <= h ? h : w;
  }
};
public class Node {
  public SquarifiedChart sqchart;
  public String name = null;
  public String displaystring = null; // if display string should be different from name
  public String hovertext = null;
  public Node parent = null;
  public int size;
  public boolean intersect = false;
  public Color c;
  public String category;
  public Data data = null;
  public boolean marked = false;
  boolean marking = true;

  private static final int XMIN = 0, XMAX = 1, YMIN = 2, YMAX = 3;
  public ArrayList<Node> children = new ArrayList<Node>();
  boolean isLeaf;
  public float x, y, d_short, d_long;


  public Node() {
  }
  public Node(Data data, ArrayList<Node> nodes) {
    children = nodes;
    this.data = data;
    isLeaf = false;
  }
  public Node(String nm, String ds, int sz, boolean lf, SquarifiedChart sqc, Data data) {
    name = nm;
    displaystring = ds;
    size = sz;
    isLeaf = true;
    marked = lf;
    sqchart = sqc;
    this.data = data;
  }
  public float spacing = 4;
  

  // draw function for first element
  public void draw (Canvas canvas) { // xmin, xmax, ymin, ymax
    ArrayList<Rect> root_arraylist = new ArrayList<Rect>();
    root_arraylist.add(new Rect(canvas.getShortSide(), canvas.getLongSide(), name));
    drawSide(root_arraylist, canvas);
    Canvas newcanvas = new Canvas(canvas.x + spacing, canvas.y + spacing, canvas.w - 2 * spacing, canvas.h - 2 * spacing);
    drawChildren(newcanvas);
  }


  private void drawChildren(Canvas canvas) {
    if (!isLeaf && children.size() > 0) {
      int mag = 0;
      for (int i = 0; i < NUM; i++) {
        mag += data.getValue(i);
      }
      drawElements(children, canvas, mag, 0);
    }
  }

  public void drawElements(ArrayList<Node> elements, Canvas canvas, int total_magnitude,
                            int index) {
    ArrayList<Node> side = new ArrayList<Node>();
    ArrayList<Rect> oldSide = null;
    ArrayList<Rect> newSide = null;
    boolean worse = false;
    float used_magnitude = 0;


    do {
      used_magnitude += elements.get(index).size; 
      side.add(elements.get(index++));
      oldSide = newSide;
      newSide = assembleSide(canvas.size()/(float)total_magnitude, canvas.getShortSide(), side, used_magnitude);
      if (oldSide != null) {
        /* does the aspect ratio get worse? */
        Rect new_rectangle = newSide.get(newSide.size() - 1); // changed from newSide.size - 2, because we're actually comparing c2's aspect ratio to c1's according to remco
        Rect old_rectangle = oldSide.get(oldSide.size() - 1); 

        if (new_rectangle.getAspectRatio() > old_rectangle.getAspectRatio()) {
          // keep the change
        } else {
          index--;
          used_magnitude -= elements.get(index).size;
          worse = true;
          drawSide(oldSide, canvas);

          // TODO: condense
          float x = canvas.x;
          float y = canvas.y;
          for (int i = 0; i < side.size() - 1; i++) {
            float w = canvas.w <= canvas.h ? oldSide.get(i).d_short : oldSide.get(i).d_long;
            float h = canvas.w <= canvas.h ? oldSide.get(i).d_long : oldSide.get(i).d_short;
            side.get(i).draw(new Canvas(x, y, w, h));
            if (canvas.w <= canvas.h) {
              x += w;
            } else {
              y += h;
            }
          }
        } 
      }

      if (index >= elements.size()) { // if the last child improves the aspect ratio
        drawSide(newSide, canvas);

        // TODO: condense
        float x = canvas.x;
        float y = canvas.y;
        for (int i = 0; i < side.size(); i++) {
          float w = canvas.w <= canvas.h ? newSide.get(i).d_short : newSide.get(i).d_long;
          float h = canvas.w <= canvas.h ? newSide.get(i).d_long : newSide.get(i).d_short;
          side.get(i).draw(new Canvas(x, y, w, h));
          if (canvas.w <= canvas.h) {
            x += w;
          } else {
            y += h;
          }
        }

        return;
      }
    }
    while (!worse);

    float x_offset = canvas.w <= canvas.h ? 0 : oldSide.get(0).d_long;
    float y_offset = canvas.w <= canvas.h ? oldSide.get(0).d_long : 0;

    Canvas newcanvas = new Canvas(canvas.x + x_offset,
                               canvas.y + y_offset,
                               canvas.w - x_offset,
                               canvas.h - y_offset);
    // println("new canvas starting at (" + newcanvas.x + ", " + newcanvas.y + "), with dimensions (" + newcanvas.w + ", " + newcanvas.h + ")");
    drawElements(elements, newcanvas, (int) total_magnitude - (int) used_magnitude, index);

  }

  //ratio is the proportion of the canvas that the side should take up
  private ArrayList<Rect> assembleSide(float ratio, float short_side, ArrayList<Node> nodes, float magnitude) {
    ArrayList<Rect> side = new ArrayList<Rect>();
    for (Node n : nodes) {
      float n_short_side = (n.size/ magnitude) * short_side;
      float n_long_side = n.size * ratio/n_short_side;

      side.add(new Rect(n_short_side, n_long_side, n.name));
    }
    return side;
  }

  private void drawSide(ArrayList<Rect> rectangles, Canvas canvas) {
    if (frameCount % 250 > 125) marking = !marking;
    float x = canvas.x;
    float y = canvas.y;
    for (Rect r : rectangles) {
      float w = canvas.w <= canvas.h ? r.d_short : r.d_long;
      float h = canvas.w <= canvas.h ? r.d_long : r.d_short;

      /*Node n = sqchart.getNode(r.name);
      if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h) {
        if (c != null) {
          fill(c.r, c.g, c.b);
        } else {
          fill(200, 200, 255);
        }
        n.intersect = true;
        if (hovertext != null) {
          tree.setHoverText(hovertext);
        }
      } else {
        fill(230);
        n.intersect = false;
      }*/


      stroke(0);
      strokeWeight(1);
      fill(255);
      
      //println(r.name);
      if(r.name != null) {
        if(data.getMark(PApplet.parseInt(r.name))&&marking) {
          fill(0, 255, 0);
          //println(r.name+": " + data.getValue(int(r.name)));
        }
      }
      rect(x, y, w, h);
      /*fill(0);
      stroke(0);
      textSize(12); 
      textAlign(CENTER, CENTER); 

      if (n.displaystring != null) {
        text(n.displaystring, x + w / 2, y + h / 2);
      } else {
        text(n.name, x + w / 2, y + h / 2);
      }*/

      if (canvas.w <= canvas.h) {
        x += w;
      } else {
        y += h;
      }
    }
  }
}
public class Piechart extends CircleChart {
    Boolean marking;
    Piechart(Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        super(data, categories, chartLeftX, chartLeftY, chartSize);
        marking = true;
    }

    public void draw (float transition_completeness, Transition transition) {
        strokeWeight(1);
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);

        float angle = 0;
        float ratio = PApplet.parseFloat(origin.y - topyaxis.y) / maxY;
        float superRatio = PApplet.parseFloat(origin.y - topyaxis.y) / superMaxY;
        int sectionWidth = abs((rightxaxis.x - origin.x) / NUM);
        float default_diam = (min(height - margins[1], width - margins[3] - margins[0]))-250.0f;

        switch (transition) {
        case NONE:
            if (frameCount%250 > 125) marking = !marking;
            for (int i = 0; i < angles.length; i++) {
                fill(255);
                strokeWeight(1);

                if(data.getMark(i)&&marking) {
                      fill(0, 255, 0);
                }
         
                arc(width / 2, height / 2 - 40 , default_diam, default_diam, angle, angle + radians(angles[i]), PIE);
                angle += radians(angles[i]);
            }
            break;
        case BARTOPIE:
        case PIETOBAR:
            /*for (int i = 0; i < angles.length; i++) {
                int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                if (transition_completeness < 0.5) {
                    fill(lerpColor(color(0, 0, 0), colors[i], transition_completeness * 2));
                } else {
                    fill(colors[i]);
                }
                int arcx, arcy, diam;
                float startr, endr;
                if (transition_completeness > 0.5) {
                    arcx = int(serp(x, width / 2 - 50, (transition_completeness - 0.5) * 2));
                    arcy = int(serp(origin.y, height / 2, (transition_completeness - 0.5) * 2));
                } else {
                    arcx = x;
                    arcy = origin.y;
                }

                if (transition_completeness < 0.5) {
                    diam = int(serp(origin.y - y, default_diam / 2, transition_completeness * 2)) * 2;
                } else {
                    diam = default_diam;
                }

                if (transition_completeness < 0.5) {
                    startr = serp(3 * HALF_PI - 0.01, 3 * HALF_PI - radians(angles[i]) / 2, transition_completeness * 2);
                    endr = serp(3 * HALF_PI + 0.01, 3 * HALF_PI + radians(angles[i]) / 2, transition_completeness * 2);
                } else {
                    startr = serp(3 * HALF_PI - radians(angles[i]) / 2, angle, (transition_completeness - 0.5) * 2);
                    endr = serp(3 * HALF_PI + radians(angles[i]) / 2, angle + radians(angles[i]), (transition_completeness - 0.5) * 2);
                }
                arc(arcx, arcy, diam, diam, startr, endr, PIE);
                angle += radians(angles[i]);
            }*/
            break;
        case LINETOPIE:
        case PIETOLINE:
            /*for (int i = 0; i < angles.length; i++) {
                int yval = data.getRow(i).getInt(categories[1]);
                int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                int y = origin.y - int(yval * ratio);
                if (transition_completeness < 0.5) {
                    fill(lerpColor(color(0, 0, 0), colors[i], transition_completeness * 2));
                } else {
                    fill(colors[i]);
                }
                int arcx, arcy, diam;
                float startr, endr;
                if (transition_completeness > 0.5) {
                    arcx = int(serp(x, width / 2 - 50, (transition_completeness - 0.5) * 2));
                    arcy = int(serp(y, height / 2, (transition_completeness - 0.5) * 2));
                } else {
                    arcx = x;
                    arcy = y;
                }

                // d = sqrt(A/pi) * 2;

                diam = default_diam;
                float midway_diam = default_diam * sqrt(((float(yval) / maxY)) / PI);
                if (transition_completeness < 0.5) {
                    diam = int(serp(12, midway_diam, transition_completeness * 2));
                } else {
                    diam = int(serp(midway_diam, default_diam, (transition_completeness - 0.5) * 2.0));
                }

                if (transition_completeness < 0.5) {
                    startr = serp(angle - PI, angle, transition_completeness * 2);
                    endr = serp(angle + radians(angles[i]) + PI, angle + radians(angles[i]), transition_completeness * 2);
                } else {
                    startr = angle;
                    endr = angle + radians(angles[i]);
                }

                arc(arcx, arcy, diam, diam, startr, endr, PIE);
                angle += radians(angles[i]);
            }*/
            break;
        case PIETOROSE:
        case ROSETOPIE:
            /*for (int i = 0; i < angles.length; i++) {
                float angle_increment = serp(radians(const_angle), radians(angles[i]), transition_completeness);
                fill(lerpColor(colors[0], colors[i], transition_completeness));
                float diam = serp(data.getRow(i).getInt(categories[1]) * ratio, default_diam, transition_completeness);
                arc(width / 2 - 50, height / 2, diam, diam, angle, angle + angle_increment, PIE);
                angle += angle_increment;
            }*/
            break;
        }
    }
};
interface SquarifiedChart {
    public Node getNode(String name);
    public void draw();
    public void setClicked(boolean val);
    public void setHoverText(String hovertext);
    public void levelUp();
};

public class Tree implements SquarifiedChart {
    private ArrayList<Node> nodes = new ArrayList<Node>();
    private HashMap tree;
    private boolean clicked;
    private int[] margins = {20, 20, 20, 20}; // left, top, right, bottom
    private String hovertext;
    private ColorGenerator colors;
    private Node root;

    Tree (Data data) {
        //colors = new ColorGenerator();
        //readInput(filename);
        for (int i = 0; i < NUM; i++) {
          nodes.add(new Node(Integer.toString(i), null, PApplet.parseInt(data.getValue(i)), data.getMark(i), null, data));
        }
        Collections.sort(nodes, new Comparator<Node>() {
          @Override
          public int compare(Node a, Node b) {
            return Integer.compare(b.size, a.size);
          }
        });
        root = new Node(data, nodes);
        
        margins[0] = 10 + PApplet.parseInt(chartLeftX);
        margins[1] = PApplet.parseInt(chartLeftY) + 20; 
        margins[2] = width - PApplet.parseInt(chartSize) - PApplet.parseInt(chartLeftX) + 10;
        margins[3] = height - PApplet.parseInt(chartSize) - PApplet.parseInt(chartLeftY) + 10;
    }

    public void setClicked (boolean val){
        clicked = val;
    }

    public void draw () {
        Canvas canvas = new Canvas(margins[0], margins[1], 
            width - margins[2] - margins[0], height - margins[3] - margins[1]);
        if (clicked) {
            if (mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
                respondToClick();
            } else {
                clicked = false;
            }
        }
        root.draw(canvas);
        if (hovertext != null && mouseX >= canvas.x && mouseX <= canvas.x + canvas.w && 
                mouseY >= canvas.y && mouseY <= canvas.y + canvas.h) {
            drawHoverText();
        } 
    }

    private void drawHoverText() {
      fill(0, 150, 150);
      textSize(20);
      if (mouseX < (width/2)) {
        textAlign(LEFT, CENTER);
        text(hovertext, mouseX, mouseY - 10);
      } else {
        textAlign(RIGHT, CENTER);
        text(hovertext, mouseX, mouseY - 10);
      }
    }

    public void setHoverText(String hovertext) {
      this.hovertext = hovertext;
    }

    public void levelUp() {
        if (root.parent != null) {
            root = root.parent;
        }
    }

    public void setRoot(String rootname) {
        Node tryroot = (Node) tree.get(rootname);
        if (tryroot != null) {
            root = tryroot;
        }
    }

    public Node getNode(String name) {
        return (Node)tree.get(name);
    }

    private void respondToClick() {
        if (root.isLeaf) {
            clicked = false;
        } else {
            for (Node n : root.children) {
                // println(n.name);
                if (n.intersect) {
                    root = n;
                    clicked = false;
                }
            }
        }
    }

    /*private void readInput(String filename) {
      tree = new HashMap();
      String lines[] = loadStrings(filename);

      /* takes in leaves 
      int num_leaves = parseInt(lines[0]);
      for (int i = 1; i <= num_leaves; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the node
        // temp[1] is its size
        tree.put(temp[0], new Node(temp[0], parseInt(temp[1]), true, this));
      }

      /* take in relationships 
      int num_rels = parseInt(lines[num_leaves+1]);
      for (int i = num_leaves + 2; i < num_rels + num_leaves + 2; i++) {
        String[] temp = split(lines[i], ' ');
        // temp[0] is the name of the parent
        // temp[1] is the name of the child
        if (!(tree.containsKey(temp[0]))) { // tree doesn't have the parent
          tree.put(temp[0], new Node(temp[0], 0, false, this)); // add the parent
        }
        if (!(tree.containsKey(temp[1]))) { // tree doesn't have the child
          tree.put(temp[1], new Node(temp[1], 0, false, this)); // add the child (size 0)
        }

        /* add the child to the parent and the parent to the child 
        Node par = (Node)tree.get(temp[0]);
        Node chi = (Node)tree.get(temp[1]);
        //par.c = colors.generate();
        
        par.children.add(chi);
        chi.parent = par;
      }
    }*/

    private Node getRoot(HashMap tree) {
      Node root = null;
      for (Object value : tree.values()) {
          Node v = (Node)value;
          Node p = (Node)v.parent;
          if (p != null) {
          } else {
            root = v;
          }
      }
      return root;
    }

    private int preprocessTree(Node root) {

      // base case: we're at a leaf
      if (root.isLeaf) {
        return root.size;
      } 

      // recursive case: we have children
      int tree_size = 0;
      for (Object childobj : root.children) {
        Node child = (Node)childobj;
        tree_size += preprocessTree(child);
      }
      root.size = tree_size;
      // sort children by size
      Collections.sort(root.children, new Comparator<Node>() {
        @Override
        public int compare(Node a, Node b) {
          return Integer.compare(b.size, a.size);
        }
      });

      return tree_size;
    }
};
public void makeText(String str, int x, int y, float rotation) {      
    if (rotation != 0) {
        pushMatrix();
        translate(x, y);
        rotate(rotation);
        translate(-x, -y);
        text(str, x, y);
        popMatrix();
    } else {
        text(str, x, y);
    }
}

public void drawCircle(int x, int y, float diameter) {
    ellipse(x, y, diameter, diameter);
}

public final float serp(float x, float y, float completeness) {
    float sinvalue = sin(completeness * HALF_PI);
    return y * sinvalue + x * (1-sinvalue);
}

// see http://martin.ankerl.com/2009/12/09/how-to-create-random-colors-programmatically/
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
/**
 * These five variables are the data you need to collect from participants.
 */
int partipantID = -1;
int index = -1;
float error = -1;
float truePerc = -1;
float reportPerc = -1;

/**
 * The table saves information for each judgement as a row.
 */
Table expData = null;

/**
 * The visualizations you need to plug in.
 * You can change the name, order, and number of elements in this array.
 */

String[] vis = {
    "BarChart", "PieChart", "TreeMap"
};

/**
 * add the data for this judgement from the participant to the table.
 */ 
//void saveJudgement(float truePerc, float reportPerc, float error, int partipantID, String v) {
public void saveJudgement() {
    if (expData == null) {
        expData = new Table();
        expData.addColumn("PartipantID");
        expData.addColumn("Index");
        expData.addColumn("Vis");
        expData.addColumn("VisID");
        expData.addColumn("Error");
        expData.addColumn("TruePerc");
        expData.addColumn("ReportPerc");
    }

    TableRow newRow = expData.addRow();
    newRow.setInt("PartipantID", partipantID);
    newRow.setInt("Index", index);

    /**
     ** finish this: decide the current visualization
     **/
    newRow.setString("Vis", vis[chartType]);

    /**
     ** finish this: decide current vis id
     **/
    newRow.setInt("VisID", chartType);
    newRow.setFloat("Error", error);
    newRow.setFloat("TruePerc", truePerc);
    newRow.setFloat("ReportPerc", reportPerc);
}

/**
 * Save the table
 * This method is called when the participant reaches the "Thanks" page and hit the "CLOSE" button.
 */
public void saveExpData() {
    /**
     ** Change this if you need 
     **/
    saveTable(expData, "expData.csv");
}
/********************************************************************************************/
/********************************************************************************************/
/********************************************************************************************/
/************************ Don't worry about the code in this file ***************************/
/********************************************************************************************/
/********************************************************************************************/
/********************************************************************************************/

float margin = 50, margin_small = 20, margin_top = 40, chartSize = 300, answerHeight = 100;
float totalWidth = -1, totalHeight = -1;
float chartLeftX = -1, chartLeftY = -1;
int NUM = 10;

int fontSize = 14, fontSizeBig = 20;
int textFieldWidth = 200, textFieldHeight = 30;
int buttonWidth = 60;
int totalMenuWidth = textFieldWidth + buttonWidth + (int) margin_small;

String warning = null;

ControlP5 cp5 = null;
Textarea myTextarea = null;
PFont pfont = null; 
boolean page1 = false, page2 = false, pagelast = false;

public void drawWarning() {
    fill(255);
    noStroke();
    rectMode(CORNER);
    rect(0, totalHeight / 2.0f + chartSize, totalWidth, fontSize * 3);
    if (warning != null) {
        fill(color(255, 0, 0));
        textSize(fontSize);
        textAlign(LEFT);
        text(warning, totalWidth / 2.0f - chartSize / 2.0f, 
        totalHeight / 2.0f + chartSize + fontSize * 1.5f);
    }
}

public void drawInstruction() {
    fill(0);
    textAlign(CENTER);
    textSize(fontSize);
    text("Two values are marked with dots. \n " 
      + "What percentage is the smaller of the larger? \n" 
      + "Please put your answer below. \n" 
      + "e.g. If you think the smaller is exactly a half of the larger, \n" 
      + "please input \"50\"."
      , totalWidth / 2.0f, totalHeight / 2.0f + chartSize / 2.0f);
}

public void clearInstruction() {
    fill(255);
    noStroke();
    rectMode(CORNER);
    rect(0, chartSize, totalWidth, margin);
}

public void drawTextField() {
    cp5.addTextfield("answer")
        .setPosition(totalWidth / 2.0f - chartSize / 2.0f, totalHeight / 2.0f + chartSize / 2.0f + margin * 2)
        .setSize(textFieldWidth, textFieldHeight)
        .setColorCaptionLabel(color(0, 0, 0))
        .setFont(createFont("arial", 14))
        .setAutoClear(true);

    cp5.addBang("next")
        .setPosition(totalWidth / 2.0f + chartSize / 2.0f - buttonWidth, totalHeight / 2.0f + chartSize / 2.0f + margin * 2)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);
}

public void drawIntro() {
    fill(0);
    textSize(fontSizeBig);
    textAlign(CENTER);
    text("In this experiment, \n" 
          + "you are asked to judge \n" 
          + "ratios between graphical elements " 
          + "in serveral charts. \n\n" 
          + "We won't record any other information from you except your answers.\n" 
          + "Click the \"agree\" button to begin. \n\n" 
          + "Thank you!", totalWidth / 2.0f, chartLeftY + chartSize / 4.0f);

    cp5.addBang("agree")
        .setPosition(totalWidth / 2.0f + margin * 2, totalHeight / 2.0f + chartSize / 2.0f)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);

    cp5.addBang("disagree")
        .setPosition(totalWidth / 2.0f - margin * 3, totalHeight / 2.0f + chartSize / 2.0f)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);
}

public void clearIntro() {
    background(color(255));
    cp5.get("agree").remove();
    cp5.get("disagree").remove();
}

public void agree() {
    index++;
    page2 = true;
}

public void disagree() {
    exit();
}

public void mouseMoved() {
    warning = null;
}

public void drawThanks() {
    background(255, 255, 255);
    fill(0);
    textSize(60);
    cp5.get(Textfield.class, "answer").remove();
    cp5.get("next").remove();
    textAlign(CENTER);
    text("Thanks!", totalWidth / 2.0f, totalHeight / 2.0f);
}

public void drawClose() {
    cp5.addBang("close")
        .setPosition(totalWidth / 2.0f - buttonWidth / 2.0f, totalHeight / 2.0f + margin_top + margin)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "flash" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
