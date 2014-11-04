import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.*; 
import java.util.Comparator; 
import java.util.Collections; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class a4 extends PApplet {


Controller controller;
Network network;
Temporal temporal;
Categorical categorical;
Selection selection;


public void setup() {
    frame.setResizable(true);
    size(900, 800);
    frameRate(60);
    String filename = null;
    try { 
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { 
      e.printStackTrace();
    } 

    try {
      filename = JOptionPane.showInputDialog(frame, "Input file", "data_aggregate.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }
    if (filename == null) {
      println("Process cancelled.");
      exit();
    }
    network = new Network();
    temporal = new Temporal();
    categorical = new Categorical();
    controller = new Controller(filename, network, temporal, categorical);
    selection = new Selection(network, temporal, categorical);
}

public void draw() {
  background(255, 255, 255);
  if (mousePressed) {
    selection.update();
  }
  selection.modifyViews();
  controller.draw();
  selection.draw();
}

public void mouseClicked() {
  if (selection.active()) {
    selection.disable();
  }
}

public void mouseReleased() {
  selection.setFixed();
}
public class AxisChart {
    protected Table data;
    protected String[] categories;
    protected float maxY; // for single columned 
    protected float superMaxY; // for multi columned
    public int[] margins = {100, 150, 220, 100};
    protected Point origin, topyaxis, rightxaxis;
    public float[] intersections = {100000, 100000, 100000, 100000};

    AxisChart (Table data, String[] categories, int[] margins) {
        this.data = data;
        this.categories = categories;
        maxY = 0;
        superMaxY = 0;
        for (int i = 0; i < categories.length; i ++) {
            int elemweight = data.getRow(0).getInt(categories[i]);
            if (elemweight > maxY) {
                maxY = elemweight;
            }
            /*int rowweight = 0;
            for (int i = 1; i < categories.length; i++) {
                rowweight += row.getInt(categories[i]);
            }
            if (rowweight > superMaxY) {
                superMaxY = rowweight;
            }*/
        }
        this.margins = margins;
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
        //makeText(categories[0], rightxaxis.x, rightxaxis.y + 70, 0);
        // X value labels
        textSize(12); 
        int sectionWidth = abs(((rightxaxis.x - origin.x) / categories.length));
        strokeWeight(sectionWidth * 0.8f);
        strokeCap(SQUARE);
        for (int i = 0; i < categories.length; i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
            int y = origin.y + 10;
            makeText(categories[i], x, y, -HALF_PI / 2);
        }
        /* Y labels */
        // Y-axis label
        /*textSize(16); 
        makeText(categories[1], topyaxis.x - 60, topyaxis.y + 50, -HALF_PI);

        // Y value labels
        if (ratio < 100) {
            textSize(12);
        
            int increment;
            try {
                increment = int(25/ratio);
            } catch (Exception e) {
                increment = 30;
            }
            if (increment < 1) {
                increment = 1;
            }

            float max = float(origin.y - topyaxis.y) / ratio;
            for (int i = 0; i <= max * 1.03; i+= increment) {
                makeText(Integer.toString(i), origin.x - 10, int(-i * ratio + origin.y), 0);
            }
        }*/
    }
};
public class Barchart extends AxisChart {
    Barchart(Table data, String[] categories, int[] margins) {
        super(data, categories, margins);
    }

    public void draw (float transition_completeness, int transition, int[] selected) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        float c = 0;
        /*switch(transition) {
            case 0:
            case LINETOBAR:
            case BARTOLINE:
                c = 0;
                break;
            case BARTOPIE:
            case PIETOBAR:
                c = serp(255, 0, transition_completeness);
                break;
        }*/
        int col = color(c, c, c);
        drawAxes(col);
        drawLabels(col, PApplet.parseFloat(origin.y - topyaxis.y) / maxY);
        drawData(transition_completeness, transition, selected);
    }


    public void drawData (float transition_completeness, int transition, int[] selected) {
        float ratio = PApplet.parseFloat(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / categories.length));
        strokeWeight(serp(5, sectionWidth * 0.7f, transition_completeness));
        stroke(5, 112, 204, 255);
        strokeCap(SQUARE);

        switch(transition) {
            case 0:
                for (int i = 0; i < categories.length; i++) {
                    if (selected[i] == 1) {stroke(13, 134, 90, 255);}
                    else {stroke(5, 112, 204, 255);}
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + PApplet.parseInt(sectionWidth * 0.1f);
                    int y = origin.y - PApplet.parseInt(data.getRow(0).getInt(categories[i]) * ratio);
                    line(x, origin.y, x, y);
                    intersections[2*i] = x;
                    intersections[2*i + 1] = y;
                }
                break;
            /*case LINETOBAR:
            case BARTOLINE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, serp(y, origin.y, transition_completeness), x, y);
                }
                break;
            case BARTOPIE:
            case PIETOBAR:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, origin.y, x, y);
                }
                break;*/
        }
        
    }   
};
public class Box {
    public String time; 
    public String port;
    public String op, protocol;
    private ArrayList<Edge> edges;
    private float weight;
    public int index;
    public final float margin_l = 50, margin_b = 20;
    public Boolean selected;
    public float x, y, w, h;

    public void draw() {
        int row = index % 8, col = index / 8;
        w = (width - margin_l)/31;
        h = (200 - margin_b)/8;
        x = col * w + margin_l;
        y = row * h + (height - 200);
        stroke(150);
        if (weight > 0) {
            if (selected) {
                fill(13, 134, 90, 60 + weight);
            } else {
                fill(5, 112, 204, 60 + weight);
            }
        } else {
            fill(255);
        }
        rect(x, y, w, h);
    }

    public Box(TableRow row) {
        selected = false;
        weight = 0;
        this.time = row.getString("Time");
        this.port = row.getString("Destination port");
        this.op = row.getString("Operation");
        this.protocol = row.getString("Protocol");
        edges = new ArrayList();
    }

    public Box(String time, String port) {
        weight = 0;
        this.time = time;
        this.port = port;
        this.op = "";
        this.protocol = "";
        edges = new ArrayList();
    }

    public void add_weight() {
        weight++;
    }

    public void map_edges(HashMap edges_map) {
        for (Object key: edges_map.keySet()) {
            Edge e = (Edge)edges_map.get(key);
            if(e.times.containsKey(time)) {
                HashMap times = (HashMap)(e.times.get(time));
                if (times.containsKey(port)) {
                    if (!edges.contains(e)) {
                        edges.add(e);
                        e.map_box(this);
                    }  
                }
            }
        }
    }
}
public class Categorical {
  private int tear, built, udp, tcp;
  private Barchart info, op, protocol;
  private ArrayList<Edge> built_edges, tear_edges, tcp_edges, udp_edges;
  private ArrayList<Box> built_boxes, tear_boxes, tcp_boxes, udp_boxes;
  public Boolean built_selected, tear_selected, tcp_selected, udp_selected, info_selected;

  public Categorical() {
    built_edges = new ArrayList<Edge>();
    tear_edges = new ArrayList<Edge>();
    tcp_edges = new ArrayList<Edge>();
    udp_edges = new ArrayList<Edge>();
    built_boxes = new ArrayList<Box>();
    tear_boxes = new ArrayList<Box>();
    tcp_boxes = new ArrayList<Box>();
    udp_boxes = new ArrayList<Box>();
    built_selected = false;
    tear_selected = false;
    tcp_selected = false;
    udp_selected = false;
    info_selected = false;
  }

  public void draw() {
    op.margins[0] = width - 120;
    protocol.margins[0] = width - 120;
    info.margins[0] = width - 120;
    int selected[] = {0, 0};
    if (built_selected) {selected[0] = 1;}
    if (tear_selected) {selected[1] = 1;}
    op.draw(1, 0, selected);
    if (tcp_selected) {selected[0] = 1;} else {selected[0] = 0;}
    if (udp_selected) {selected[1] = 1;} else {selected[1] = 0;}
    protocol.draw(1, 0, selected);
    if (info_selected) {selected[0] = 1;} else {selected[0] = 0;}
    info.draw(1, 0, selected);
  }

  public void set_data(int tear, int built, int tcp, int udp) {
    this.tear = tear;
    this.built = built;
    this.udp = udp;
    this.tcp = tcp;
    Table table = new Table();
    table.addColumn("TCP");
    table.addColumn("UDP");
    TableRow newRow = table.addRow();
    newRow.setInt("TCP", tcp);
    newRow.setInt("UDP", udp);
    String cat[] = {"TCP", "UDP"};
    int[] margins = {width - 120, 50, 20, 650};
    protocol = new Barchart(table,cat, margins);

    Table table2 = new Table();
    table2.addColumn("Built");
    table2.addColumn("Teardown");
    newRow = table2.addRow();
    newRow.setInt("Built", built);
    newRow.setInt("Teardown", tear);
    String cat2[] = {"Built", "Teardown"};
    int[] margins2 = {width - 120, 250, 20, 450};
    op = new Barchart(table2,cat2, margins2);

    Table table3 = new Table();
    table3.addColumn("Info");
    newRow = table3.addRow();
    newRow.setInt("Info", 1);
    String cat3[] = {"Info"};
    int[] margins3 = {width - 120, 450, 20, 250};
    info = new Barchart(table3, cat3, margins3);
  }

  public void set_edges(HashMap edges) {
    for (Object key: edges.keySet()) {
        Edge e = (Edge)edges.get(key);
        if(e.op.equals("Built")) {
            built_edges.add(e);
        } else {
            tear_edges.add(e);
        }
        if (e.protocol.equals("TCP")) {
            tcp_edges.add(e);
        } else {
            udp_edges.add(e);
        }
    }
  }

  public void set_boxes(HashMap boxes) {
    for (Object key: boxes.keySet()) {
        Box b = (Box)boxes.get(key);
        if(b.op.equals("Built")) {
            built_boxes.add(b);
        } else {
            tear_boxes.add(b);
        }
        if (b.protocol.equals("TCP")) {
            tcp_boxes.add(b);
        } else {
            udp_boxes.add(b);
        }
    }
  }
}



public class BoxComparator implements Comparator<Box> {
    @Override
    public int compare(Box a, Box b) {
        int time_comparator = a.time.compareTo(b.time);
        if (time_comparator != 0) {
          return time_comparator;
        } else {
          return a.port.compareTo(b.port);
        }
    }
}


public class Controller {
  private Table table;
  private ArrayList<Edge> edges;
  private ArrayList<Box> boxes;
  private int built, tear, tcp, udp = 0;
  private HashMap nodes;
  private Network network;
  private Temporal temporal;
  private Categorical categorical;

  public Controller(String filename, Network network, Temporal temporal, Categorical categorical) {
    table = loadTable(filename, "header");
    nodes = new HashMap();
    edges = new ArrayList();
    boxes = new ArrayList();
    this.network = network;
    this.temporal = temporal;
    this.categorical = categorical;
    processTable();
    unselect();
  }

  public void draw() {
    network.draw();
    temporal.draw();
    categorical.draw();
    strokeWeight(1);
    stroke(100);
    line(0, height - 200, width, height - 200);
    line(width - 140, 0, width - 140, height - 200);
    unselect();
  }

  public void unselect() {
    for (Edge e : edges) {
      e.selected = false; 
    }
    for (Object key: nodes.keySet()) {
      Node n = (Node)(nodes.get(key));
      n.selected = false;
    } 
    for (Box b : boxes) {
      b.selected = false;
    }
  }
  
  private void processTable() {
    ArrayList<String> ips = new ArrayList();
    HashMap edges_map = new HashMap();
    HashMap boxes_map = new HashMap();
    ArrayList<String> ts = new ArrayList();
    ArrayList<String> ps = new ArrayList();
    int i;
    //create edges list
    for (i = 0; i < table.getRowCount(); i++) {
      String IP = table.getString(i, "Source IP");
      String IP2 = table.getString(i, "Destination IP");
      if (!(edges_map.containsKey(IP+IP2) || edges_map.containsKey(IP2+IP))) {
        Edge ed = new Edge(table.getRow(i));
        edges_map.put(IP+IP2, ed);
        if (!nodes.containsKey(IP)) {
          nodes.put(IP, new Node(IP));
        }
        if (!nodes.containsKey(IP2)) {
          nodes.put(IP2, new Node(IP2));
        }
        Node n = (Node)(nodes.get(IP));
        n.add_edge(ed);
        n = (Node)(nodes.get(IP2));
        n.add_edge(ed);
      }
      else {
        Edge e = null;
        if (edges_map.containsKey(IP+IP2)) {
          e = (Edge)(edges_map.get(IP+IP2));
        } else {
          e = (Edge)(edges_map.get(IP2+IP));
        }
        e.update(table.getRow(i));
      }
      Edge e = null;
      if (edges_map.containsKey(IP+IP2)) {
          e = (Edge)(edges_map.get(IP+IP2));
        } else {
          e = (Edge)(edges_map.get(IP2+IP));
      }
      e.add_weight();
    }

    //create box list
    for (i = 0; i < table.getRowCount(); i++) {
      String t = table.getString(i, "Time");
      String port = table.getString(i, "Destination port");
      if (!boxes_map.containsKey(t+port)) {
        boxes_map.put(t+port, new Box(table.getRow(i)));
      }
      if (!ts.contains(t)) {
        ts.add(t);
      }
      if (!ps.contains(port)) {
        ps.add(port);
      }
      Box b = (Box)(boxes_map.get(t+port));
      b.add_weight();  
      b.map_edges(edges_map);
    }

    //count categorical stuff
    for (i = 0; i < table.getRowCount(); i++) {
      String op = table.getString(i, "Operation");
      String protocol = table.getString(i, "Protocol");
      if (op.equals("Built")) {
        built ++;
      } else {
        tear ++;
      }
      if (protocol.equals("TCP")) {
        tcp ++;
      } else {
        udp ++;
      }
    }

    categorical.set_data(tear, built, tcp, udp);
    categorical.set_edges(edges_map);
    categorical.set_boxes(boxes_map);

    for (String t: ts) {
        for (String p: ps) {
          if (!boxes_map.containsKey(t+p)) {
            boxes_map.put(t+p, new Box(t, p));
          }
        }
    } 

    for (Object key: edges_map.keySet()) {
            Edge e = (Edge)edges_map.get(key);
            edges.add(e);
    } 
    for (Object key: boxes_map.keySet()) {
            Box b = (Box)boxes_map.get(key);
            boxes.add(b);
    } 



    Collections.sort(boxes, new BoxComparator());
    int index = 0;
    for (Box b : boxes) {
      b.index = index++;
    }
    network.set_nodes(nodes);
    network.set_edges(edges);
    temporal.set_boxes(boxes);
  }
}






/*println("EDGES: ");
    for (Object key: edges_map.keySet()) {
            Edge e = (Edge)edges_map.get(key);
            println(e.source + ": " + e.dest + ", weight: " + e.weight);
            println("Corresponding boxes: ");
            for (Box b: e.boxes) {
              println(b.time + ": " + b.port); 
            }
            println();
    }

    println("BOXES: ");
    for (Object key: boxes_map.keySet()) {
            Box b = (Box)boxes_map.get(key);
            println(b.time + ": " + b.port + ", weight: " + b.weight);
            println("Corresponding edges: ");
            for (Edge e: b.edges) {
              println(e.source + ": " + e.dest);
            }
            println();
    }*/


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
public class Edge {
    public String source; 
    public String dest;    //dest ip
    public HashMap times;
    public ArrayList<Box> boxes;
    public String op, protocol;
    public float weight;
    private float x1, y1, x2, y2;
    public Boolean selected;

    public Edge(TableRow row) {
        weight = 0;
        this.source = row.getString("Source IP");
        this.dest = row.getString("Destination IP");
        this.op = row.getString("Operation");
        this.protocol = row.getString("Protocol");
        String t = row.getString("Time");
        String p = row.getString("Destination port");
        HashMap t_map = new HashMap();
        t_map.put(p, 1);
        times = new HashMap();
        boxes = new ArrayList();
        times.put(t, t_map);
        selected = false;
    }

    public void draw() {
        strokeWeight(weight/20);
        if (selected) {
            stroke(5, 185, 119, 150);
        } else {
            stroke(83, 176, 255, 150);
        }
        line(x1, y1, x2, y2);
    }

    public void add_weight() {
        weight++;
    }

    public void map_box(Box b) {
        boxes.add(b);
    }

    public void update(TableRow row) {
        String t = row.getString("Time");
        String p = row.getString("Destination port");
        if (times.containsKey(t)) {
            HashMap t_map = (HashMap)(times.get(t));
            if (!t_map.containsKey(p)) {
                t_map.put(p, 1);
            }
        }
        else { 
            HashMap t_map = new HashMap();
            t_map.put(p, 1);
            times.put(t, t_map);
        }
    }

    // intersect function
}
public class Network {
  public ArrayList<Edge> edges;
  public HashMap nodes;
  private final float margin_r = 140;
  private final float margin_b = 200;
  private final float margin = 20;

  public Network () {
  }

  public void draw() {
    resize();
    for (Edge e : edges) {
      e.draw();
    }
    for (Object key: nodes.keySet()) {
      Node n = (Node)(nodes.get(key));
      n.draw();
    } 
  }

  public void set_edges (ArrayList<Edge> edges) {
  	this.edges = edges;
  }

  public void set_nodes (HashMap nodes) {
    this.nodes = nodes;
  }

  private void resize () {
    float center_x = (width - margin_r)/2;
    float center_y = (height - margin_b)/2;
    float radius_x = (width - margin_r)/2 - margin;
    float radius_y = (height - margin_b)/2 - margin;

    float nodeangle = 0, serverangle = 0;
    int servercount = 2;
    int nodecount = nodes.size() - servercount;
    int count = 0;
    for (Object key : nodes.keySet()) {
      Node n = (Node)(nodes.get(key));
      if (count != 9 && count != 17) {
        n.x = center_x + cos(nodeangle) * radius_x;
        n.y = center_y + sin(nodeangle) * radius_y;
        nodeangle += (2 * PI) / nodecount;
      } else {
        n.x = center_x + cos(nodeangle) * min(radius_x, radius_y)/2;
        n.y = center_y + sin(nodeangle) * min(radius_x, radius_y)/2;
        serverangle += (2 * PI) / servercount;
      }
      count += 1;
    }
    for (Edge e : edges) {
      Node s = (Node)(nodes.get(e.source));
      Node d = (Node)(nodes.get(e.dest));
      e.x1 = s.x;
      e.y1 = s.y;
      e.x2 = d.x;
      e.y2 = d.y;
    }
  }
}
public class Node {
	private String id;
	public float x, y;
    private float radius;
    public ArrayList<Edge> edges;
    public Boolean selected; 

	public Node(String id) {
        this.id = id;
        radius = 15;
        edges = new ArrayList();
        selected = false;
    }

	public void draw() {
        strokeWeight(1);
        if (selected) {
            fill(13, 134, 90, 255);
        } else {
            fill(5, 112, 204, 255);
        }
        stroke(0);
		ellipse(x, y, radius * 2, radius * 2);
	}

	public void add_edge(Edge edge) {
		edges.add(edge);
	}

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}
}
class Selection {
    private Boolean selection_mode;
    private Boolean fixed;
    private Network network;
    private Temporal temporal;
    private Categorical categorical;
    public float x_start, y_start, x_end, y_end;

    Selection(Network network, Temporal temporal, Categorical categorical) {
        x_start = y_start = x_end = y_end = 0;
        this.network = network;
        this.temporal = temporal;
        this.categorical = categorical;
        disable();
        fixed = true;
    }
    public void setFixed() {
        fixed = true;
    }
    public void setMutable() {
        fixed = false;
    }
    public Boolean active() {
        return selection_mode;
    }
    public void update() {
        if (!selection_mode) {
            enable();
            x_start = mouseX;
            y_start = mouseY;
        }
        if (!fixed) {
            x_end = mouseX;
            y_end = mouseY;
        }
    }
    public void draw() {
        if (selection_mode) {
            strokeWeight(1);
            stroke(1, 57, 37, 180);
            fill(96, 185, 153, 50);
            rect(x_start, y_start, x_end - x_start, y_end - y_start);
        }
    }
    public void disable() {
        selection_mode = false;
    }
    public void enable() {
        selection_mode = true;
        fixed = false;
    }
    public void modifyViews() {
        if (selection_mode) {
            categorical.built_selected = pointSelected(categorical.op.intersections[0],categorical.op.intersections[1]);
            if (pointSelected(categorical.op.intersections[0],categorical.op.intersections[1])) {
                for (Edge e : network.edges) {
                        if (e.op.equals("Built")) {
                            e.selected = categorical.built_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.op.equals("Built")) {
                            b.selected = categorical.built_selected;
                        }
                }
            }

            categorical.tear_selected = pointSelected(categorical.op.intersections[2],categorical.op.intersections[3]);
            if (pointSelected(categorical.op.intersections[2],categorical.op.intersections[3])) {
                for (Edge e : network.edges) {
                        if (e.op.equals("Teardown")) {
                            e.selected = categorical.tear_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.op.equals("Teardown")) {
                            b.selected = categorical.tear_selected;
                        }
                }
            }

            categorical.tcp_selected = pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1]);
            if (pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1])) {
                for (Edge e : network.edges) {
                        if (e.protocol.equals("TCP")) {
                            e.selected = categorical.tcp_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.protocol.equals("TCP")) {
                            b.selected = categorical.tcp_selected;
                        }
                }
            }

            categorical.udp_selected = pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3]);
            if (pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3])) {
                for (Edge e : network.edges) {
                        if (e.protocol.equals("UDP")) {
                            e.selected = categorical.udp_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.protocol.equals("UDP")) {
                            b.selected = categorical.udp_selected;
                        }
                }
            }
            categorical.info_selected = pointSelected(categorical.info.intersections[0],categorical.info.intersections[1]);
            if (pointSelected(categorical.info.intersections[0],categorical.info.intersections[1])) {
                for (Edge e : network.edges) {
                        e.selected = true;
                }
                for (Box b : temporal.boxes) {
                        b.selected = true;
                }
            }

            for (Object key : network.nodes.keySet()) {
                Node n = (Node)(network.nodes.get(key));
                n.selected = pointSelected(n.x, n.y);
                
                if (pointSelected(n.x, n.y)) {
                    for (Edge e : n.edges) {
                        e.selected = e.selected || n.selected;
                        for (Box b : e.boxes) {
                            b.selected = e.selected;
                        }
                    }
                    for (Edge e : n.edges) {
                        if (e.op.equals("Built")) {
                            categorical.built_selected = e.selected;
                        } else {
                            categorical.tear_selected = e.selected;
                        }
                        if (e.protocol.equals("TCP")) {
                            categorical.tcp_selected = e.selected;
                        } else {
                            categorical.udp_selected = e.selected;
                        }
                        categorical.info_selected = e.selected;
                    }
                } /*else {
                    
                }*/
            } 
            for (Box b : temporal.boxes) {
                b.selected = b.selected || pointSelected(b.x + b.w/2, b.y + b.h/2);

                
                if (pointSelected(b.x + b.w/2, b.y + b.h/2)) {
                    for (Edge e : b.edges) {
                        e.selected = e.selected || b.selected;
                    }
                    if (!b.op.equals("")) {
                        if (b.op.equals("Built")) {
                                categorical.built_selected = b.selected || categorical.built_selected;
                        } else {
                                categorical.tear_selected = b.selected || categorical.tear_selected;
                        } 
                        if (b.protocol.equals("TCP")) {
                                categorical.tcp_selected = b.selected || categorical.tcp_selected;
                        } else {
                                categorical.udp_selected = b.selected || categorical.udp_selected;
                        }
                        categorical.info_selected = b.selected ||categorical.info_selected;
                    }
                }
            }
            /*categorical.built_selected = pointSelected(categorical.op.intersections[0],categorical.op.intersections[1]) || categorical.built_selected;
            categorical.tear_selected = pointSelected(categorical.op.intersections[2],categorical.op.intersections[3]) || categorical.tear_selected ;
            categorical.tcp_selected = pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1]) || categorical.tcp_selected ;
            categorical.udp_selected = pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3]) || categorical.udp_selected ;
            categorical.info_selected = pointSelected(categorical.info.intersections[0],categorical.info.intersections[1]) || categorical.info_selected ;*/
        }
    }
    private Boolean pointSelected(float x, float y) {
        float topleft_x = min(x_start, x_end);
        float topleft_y = min(y_start, y_end);
        float bottomright_x = max(x_start, x_end);
        float bottomright_y = max(y_start, y_end);
        return x > topleft_x && x < bottomright_x 
            && y > topleft_y && y < bottomright_y;
    }
};
public class Temporal {
  private ArrayList<Box> boxes;

  public Temporal () {
  }
  public void draw() {
    // labels
    for (Box b : boxes) {
        b.draw();
    }
  }

  public void set_boxes(ArrayList<Box> boxes) {
    this.boxes = boxes;
  }
}
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a4" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
