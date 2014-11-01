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

public class a4 extends PApplet {


Controller controller;
Network network;
Temporal temporal;
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
    controller = new Controller(filename, network, temporal);
    selection = new Selection(network, temporal);
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
public class Box {
    public String time; 
    public String port;
    private ArrayList<Edge> edges;
    private float weight;

    public Box(TableRow row) {
        weight = 0;
        this.time = row.getString("Time");
        this.port = row.getString("Destination port");
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
public class Controller {
  private Table table;
  private ArrayList<Edge> edges;
  private ArrayList<Box> boxes;
  private HashMap nodes;
  private Network network;
  private Temporal temporal;

  public Controller(String filename, Network network, Temporal temporal) {
    table = loadTable(filename, "header");
    nodes = new HashMap();
    edges = new ArrayList();
    boxes = new ArrayList();
    this.network = network;
    this.temporal = temporal;
    processTable();
    unselect();
  }

  public void draw() {
    network.draw();
    temporal.draw();
    line(0, height - 200, width, height - 200);
    line(width - 300, 0, width - 300, height - 200);
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
  }
  
  private void processTable() {
    ArrayList<String> ips = new ArrayList();
    HashMap edges_map = new HashMap();
    HashMap boxes_map = new HashMap();
    int i;
    //create edges list
    for (i = 0; i < table.getRowCount(); i++) {
      String IP = table.getString(i, "Source IP");
      String IP2 = table.getString(i, "Destination IP");
      if (!edges_map.containsKey(IP+IP2)) {
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
        Edge e = (Edge)(edges_map.get(IP+IP2));
        e.update(table.getRow(i));
      }
      Edge e = (Edge)(edges_map.get(IP+IP2));
      e.add_weight();
    }

    //create box list
    for (i = 0; i < table.getRowCount(); i++) {
      String t = table.getString(i, "Time");
      String port = table.getString(i, "Destination port");
      if (!boxes_map.containsKey(t+port)) {
        boxes_map.put(t+port, new Box(table.getRow(i)));
      }
      Box b = (Box)(boxes_map.get(t+port));
      b.add_weight();  
      b.map_edges(edges_map);
    }

    for (Object key: edges_map.keySet()) {
            Edge e = (Edge)edges_map.get(key);
            edges.add(e);
    } 
    for (Object key: boxes_map.keySet()) {
            Box b = (Box)boxes_map.get(key);
            boxes.add(b);
    } 
    network.add_nodes(nodes);
    network.add_edges(edges);
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
public class Edge {
    public String source; 
    public String dest;    //dest ip
    public HashMap times;
    public ArrayList<Box> boxes;
    public float weight;
    private float x1, y1, x2, y2;
    public Boolean selected;

    public Edge(TableRow row) {
        weight = 0;
        this.source = row.getString("Source IP");
        this.dest = row.getString("Destination IP");
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
        strokeWeight(weight/30);
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
  private final float margin_r = 300;
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

  public void add_edges (ArrayList<Edge> edges) {
  	this.edges = edges;
  }

  public void add_nodes (HashMap nodes) {
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
        radius = 10;
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
    public float x_start, y_start, x_end, y_end;

    Selection(Network network, Temporal temporal) {
        x_start = y_start = x_end = y_end = 0;
        this.network = network;
        this.temporal = temporal;
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
            for (Object key : network.nodes.keySet()) {
                Node n = (Node)(network.nodes.get(key));
                n.selected = pointSelected(n.x, n.y);
                for (Edge e : n.edges) {
                    e.selected = e.selected || n.selected;
                }
            } 
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

  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a4" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
