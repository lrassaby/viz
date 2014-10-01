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



TransitionChart chart;
ButtonGroup buttons;


public void setup () {
  frame.setResizable(true);
  size(900, 700);
  frameRate(60);

  String filename = null;
  try { 
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  } 
  catch (Exception e) { 
    e.printStackTrace();
  } 
  try {
      filename = JOptionPane.showInputDialog(frame, "Input file (type csv)", "data.csv");
  } catch (Exception e) {
      println("Process cancelled.");
      exit();
  }
  if (filename == null) {
      println("Process cancelled.");
      exit();
  }
  
  Table data = loadTable(filename, "header");
  String[] lines = loadStrings(filename);
  String[] categories = lines[0].split(",");

  String[] chart_texts = {"Bar Chart", "Line Chart", "Pie Chart"};
  buttons = new ButtonGroup(chart_texts);
  chart = new TransitionChart(data, categories);
  chart.setChartType(chart_texts[0]);
  buttons.setSelection(chart_texts[0]);
}


public void draw() {
  background(255, 255, 255);

  chart.draw();
  buttons.draw();
}

public void mouseClicked() {
  String clicked = buttons.getClicked();
  if (clicked != chart.getChartType() && chart.setChartType(clicked)) {
    buttons.setSelection(clicked);
  }
}



public class AxisChart {
    protected Table data;
    protected String[] categories;
    protected float maxY;
    protected int[] margins = {80, 30, 120, 100};
    protected Point origin, topyaxis, rightxaxis;

    AxisChart (Table data, String[] categories) {
        this.data = data;
        maxY = data.getRow(0).getInt(categories[1]);
        for (TableRow row : data.rows()) {
            int rowweight = row.getInt(categories[1]);
            if (rowweight > maxY) {
                maxY = rowweight;
            }
        }
        this.categories = categories;
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]); 
        rightxaxis = new Point(width - margins[2], height - margins[3]);
    }

 
    public void drawAxes(int c) {
        strokeWeight(2);
        stroke(c);
        fill(c);
        line(origin.x, origin.y, topyaxis.x, topyaxis.y - 5);
        line(origin.x, origin.y, rightxaxis.x, rightxaxis.y);
    }

    protected void drawLabels(int c) {
        stroke(c);
        fill(c);
        textSize(16); 
        textAlign(RIGHT, CENTER); 
        /* X labels */
        // X-axis label
        makeText(categories[0], rightxaxis.x, rightxaxis.y + 70, 0);
        // X value labels
        textSize(12); 
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        strokeWeight(sectionWidth * 0.8f);
        strokeCap(SQUARE);
        for (int i = 0; i < data.getRowCount(); i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = origin.y + 10;
            makeText(data.getRow(i).getString(categories[0]), x, y, -HALF_PI / 2);
        }
        /* Y labels */
        // Y-axis label
        textSize(16); 
        makeText(categories[1], topyaxis.x - 60, topyaxis.y + 50, -HALF_PI);

        // Y value labels
        textSize(12);
        float ratio = PApplet.parseFloat((topyaxis.y - origin.y)) / maxY;
        int increment;
        try {
            increment = PApplet.parseInt(25/abs(ratio));
        } catch (Exception e) {
            increment = 30;
        }
        for (int i = 0; i <= maxY * 1.03f; i+= increment) {
            makeText(Integer.toString(i), origin.x - 10, PApplet.parseInt(i * ratio + origin.y), 0);
        }
    }


};
public class Barchart extends AxisChart {
    Barchart(Table data, String[] categories) {
        super(data, categories);
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
                c = lerp(255, 0, transition_completeness);
                break;
        }
        int col = color(c, c, c);
        drawAxes(col);
        drawLabels(col);
        drawData(transition_completeness, transition);
    }


    public void drawData (float transition_completeness, Transition transition) {
        float ratio = PApplet.parseFloat(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        strokeWeight(lerp(5, sectionWidth * 0.8f, transition_completeness));
        stroke(0);
        strokeCap(SQUARE);

        switch(transition) {
            case NONE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = origin.y - PApplet.parseInt(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, origin.y, x, y);
                }
                break;
            case LINETOBAR:
            case BARTOLINE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = origin.y - PApplet.parseInt(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, lerp(y, origin.y, transition_completeness), x, y);
                }
                break;
            case BARTOPIE:
            case PIETOBAR:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = origin.y - PApplet.parseInt(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, origin.y, x, y);
                }
                break;
        }
        
    }   
};
public class Button {
  private boolean isect;
  private Point pos; 
  private Dimensions dim;
  private float roundness;
  private int c;
  private String text;
 

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

  public void setPos(int x, int y) {
    this.pos.setXY(x, y);
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

class ButtonGroup {
    private Button[] buttons;
    private String selection;
    
    ButtonGroup (String[] chart_texts) {
        selection = chart_texts[0];
        buttons = new Button[chart_texts.length]; 
        Dimensions buttondim = new Dimensions(90, (height - 80) / buttons.length);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button(new Point(), buttondim, 7, color(255, 153, 51), chart_texts[i]);
        }
    }

    public void draw() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].dim.setWH(90, (height - 80) / buttons.length);
            buttons[i].pos.setXY(width - buttons[0].dim.w - 20, 30 + i * (buttons[i].dim.h + 10));
            if (buttons[i].getText() == selection) {
                buttons[i].setColor(40, 190, 100);
            } else {
                buttons[i].setColor(50, 200, 160);
            }
            buttons[i].draw();
        }
    }
    public String getSelection () {
        return selection;
    }

    public void setSelection (String selection) {
        this.selection = selection;
    }

    public String getClicked() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].intersect(mouseX, mouseY);
            boolean selected = buttons[i].getIsect();
            if (selected) { 
                buttons[i].setSelected(false);
                return buttons[i].getText();
            }
        }
        return "";
    }
};
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
public class Linechart extends AxisChart {
    Linechart(Table data, String[] categories) {
        super(data, categories);
    }

    public void draw (float transition_completeness, Transition transition) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        float c = 0;
        int col = color(c, c, c);
        drawAxes(col);
        drawLabels(col);
        drawData(transition_completeness, transition);
    }

    public void drawData (float transition_completeness, Transition transition) {
        strokeWeight(2);
        float ratio = PApplet.parseFloat(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        Point prev = new Point(origin.x + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f), origin.y - PApplet.parseInt(data.getRow(0).getInt(categories[1]) * ratio));
        stroke(0);
        switch (transition) {
            case NONE:
                drawCircle(prev.x, prev.y, 12);
                for (int i = 1; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = origin.y - PApplet.parseInt(data.getRow(i).getInt(categories[1]) * ratio);
                    line(prev.x, prev.y, x, y);
                    prev.setXY(x, y);
                    drawCircle(prev.x, prev.y, 10);
                }
                break;
            case LINETOBAR:
            case BARTOLINE:
                drawCircle(prev.x, prev.y, lerp(4, 12, transition_completeness));
                for (int i = 1; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = origin.y - PApplet.parseInt(data.getRow(i).getInt(categories[1]) * ratio);
                    line(prev.x, prev.y, lerp(prev.x, x, transition_completeness), lerp(prev.y, y, transition_completeness));
                    prev.setXY(x, y);
                    drawCircle(prev.x, prev.y, lerp(2, 10, transition_completeness));
                }
                break;
        }
    }
};
public class Piechart {
    private Table data;
    private String[] categories;
    private float[] angles;
    private int[] colors;
    private ColorGenerator colorgenerator;
    private int[] margins = {80, 30, 120, 100};
    private Point origin, topyaxis, rightxaxis;
    private float maxY;

    Piechart(Table data, String[] categories) {
        setData(data, categories);
    }

    public void setData(Table data, String[] categories) {
        colorgenerator = new ColorGenerator();
        this.data = data;
        this.categories = categories;
        this.angles = new float[data.getRowCount()];
        int total_magnitude = 0;
        for (int i = 0; i < data.getRowCount(); i++) {
        	total_magnitude += data.getRow(i).getInt(categories[1]);
        }
        for (int i = 0; i < data.getRowCount(); i++) {
        	angles[i] = (PApplet.parseFloat(data.getRow(i).getInt(categories[1])) / total_magnitude) * 360;
        }

        colors = new int[angles.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorgenerator.generate();
        }
        origin = new Point(margins[0], height - margins[3]);
        topyaxis = new Point(margins[0], margins[1]); 
        rightxaxis = new Point(width - margins[2], height - margins[3]);

        maxY = data.getRow(0).getInt(categories[1]);
        for (TableRow row : data.rows()) {
            int rowweight = row.getInt(categories[1]);
            if (rowweight > maxY) {
                maxY = rowweight;
            }
        }
    }

    public void draw (float transition_completeness, Transition transition) {
        strokeWeight(1);
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);

        float angle = 0;
        float ratio = PApplet.parseFloat(topyaxis.y - origin.y) / maxY;
        int sectionWidth = abs((rightxaxis.x - origin.x) / data.getRowCount());

        switch(transition) {
            case NONE:
                for (int i = 0; i < angles.length; i++) {
                    fill(colors[i]);
                    arc(width/2 - 50, height/2, (min(height, width - 120) - 40), (min(height, width - 120) - 40), angle, angle+radians(angles[i]), PIE);
                    angle += radians(angles[i]);
                }
                break;
            case BARTOPIE:
            case PIETOBAR:
                for (int i = 0; i < angles.length; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = PApplet.parseInt(data.getRow(i).getInt(categories[1]) * ratio) + origin.y;
                    if (transition_completeness < 0.5f) {
                        fill(lerpColor(color(0, 0, 0), colors[i], transition_completeness * 2));
                    } else {
                        fill(colors[i]);
                    }
                    int arcx, arcy, diam;
                    float startr, endr;
                    if (transition_completeness > 0.5f) {
                        arcx = PApplet.parseInt(lerp(x, width/2 - 50, (transition_completeness - 0.5f) * 2));
                        arcy = PApplet.parseInt(lerp(origin.y, height/2, (transition_completeness - 0.5f) * 2));
                    } else {
                        arcx = x;
                        arcy = origin.y;
                    }
                   
                    if (transition_completeness < 0.5f) {
                        diam = PApplet.parseInt(lerp(origin.y - y, (min(height, width - 120) - 40) / 2, transition_completeness * 2)) * 2;
                    } else {
                        diam = (min(height, width - 120) - 40);
                    }

                    if (transition_completeness < 0.5f) {
                        startr = lerp(3 * HALF_PI - 0.01f, 3 * HALF_PI - radians(angles[i]) / 2, transition_completeness * 2);
                        endr = lerp(3 * HALF_PI + 0.01f, 3 * HALF_PI + radians(angles[i]) / 2, transition_completeness * 2);
                    } else {
                        startr = lerp(3 * HALF_PI - radians(angles[i]) / 2, angle, (transition_completeness - 0.5f) * 2);
                        endr = lerp(3 * HALF_PI + radians(angles[i]) / 2, angle+radians(angles[i]), (transition_completeness - 0.5f) * 2);
                    }
                    arc(arcx, arcy, diam, diam, startr, endr, PIE);
                    angle += radians(angles[i]);
                }
                break;
        }
    }
};
public class TransitionChart {
    // transitions
    private String prev_chart_type;
    private String chart_type;
    private boolean in_transition;
    private int transition_start_frame;
    // charts
    private Barchart barchart;
    private Linechart linechart;
    private Piechart piechart;
    // data
    private String[] categories;
    private Table data;
    // constants
    private final float transition_time = 3;
    private final float transition_frames = transition_time * 60.0f;

    TransitionChart(Table data, String[] categories) {
        this.barchart = new Barchart(data, categories);
        this.linechart = new Linechart(data, categories);
        this.piechart = new Piechart(data, categories);
        this.data = data;
        this.categories = categories;
        this.transition_start_frame = 0;
        this.in_transition = false;
    }

    public boolean setChartType(String chart_type) {
        if (!in_transition && chart_type != "") {
            this.prev_chart_type = this.chart_type;
            this.chart_type = chart_type;
            if (prev_chart_type != "" && prev_chart_type != null) {
                in_transition = true; // should become true
            }
            return true;
        }
        return false;
    }

    public Boolean inTransition() {
        return in_transition;
    }

    public String getChartType() {
        return chart_type;
    }

    public void draw() {
        if (in_transition) {
            if (transition_start_frame == 0) { // beginning of transition
                transition_start_frame = frameCount;
            }
            int elapsed_frames = frameCount - transition_start_frame;
            float progress = elapsed_frames / transition_frames;

            if (prev_chart_type == "Line Chart" && chart_type == "Bar Chart") {
                if (progress < 0.5f) {
                    linechart.draw(1.0f - (progress * 2), Transition.LINETOBAR);
                } else {
                    barchart.draw((progress - 0.5f) * 2, Transition.LINETOBAR);
                }
            } else if (prev_chart_type == "Bar Chart" && chart_type == "Line Chart") {
                if (progress < 0.5f) {
                    barchart.draw(1.0f - (progress * 2), Transition.LINETOBAR);
                } else {
                    linechart.draw((progress - 0.5f) * 2, Transition.LINETOBAR);
                }
            } else if (prev_chart_type == "Bar Chart" && chart_type == "Pie Chart") {
                if (progress < 0.25f) {
                    barchart.draw(1.0f - (progress * 4), Transition.BARTOPIE);
                } else {
                    piechart.draw((progress - 0.25f) * 4.0f/3, Transition.BARTOPIE);
                }
            } else if (prev_chart_type == "Pie Chart" && chart_type == "Bar Chart") {
                if (progress < 0.75f) {
                    piechart.draw(1.0f - (progress * 4.0f/3), Transition.PIETOBAR);
                } else {
                    barchart.draw((progress - 0.75f) * 4.0f, Transition.PIETOBAR);
                }
            } else {
                println("Transformation not yet implemented.");
                in_transition = false;
                transition_start_frame = 0;
            }


            if (elapsed_frames >= transition_frames) {
                in_transition = false;
                transition_start_frame = 0;
            }             
        } else {
            if (chart_type == "Line Chart") {
                linechart.draw(1, Transition.NONE);
            } else if (chart_type == "Bar Chart") {
                barchart.draw(1, Transition.NONE);
            } else if (chart_type == "Pie Chart") {
                piechart.draw(1, Transition.NONE);
            }
        }
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
