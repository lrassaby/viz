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

public void setup() {
    frame.setResizable(true);
    size(800, 800);
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
}

public void draw() {
  background(255, 255, 255);
  controller.draw();
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
  }

  public void draw() {
    network.draw();
    temporal.draw();
    line(0, height - 200, width, height - 200);
    line(width - 200, 0, width - 200, height - 200);
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
        edges_map.put(IP+IP2, new Edge(table.getRow(i)));
        if (!nodes.containsKey(IP)) {
          nodes.put(IP, new Node(IP));
        }
        if (!nodes.containsKey(IP2)) {
          nodes.put(IP2, new Node(IP2));
        }
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
    }

    public void draw() {
        //strokeWeight(weight);
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

}
public class Network {
  private ArrayList<Edge> edges;
  private HashMap nodes;
  private final float margin_r = 200;
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

	public Node(String id) {
        this.id = id;
        radius = 4;
    }

	public void draw() {
        strokeWeight(2);
        fill(200, 60, 60);
        stroke(100, 30, 30);
		ellipse(x, y, radius * 2, radius * 2);
	}

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}
}
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
