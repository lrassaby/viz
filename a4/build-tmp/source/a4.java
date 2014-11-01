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
    size(1000, 800);
    frameRate(60);
    float [] net_canvas = {0, 0, 800, 600};
    float [] temp_canvas = {0, 700, 800, 200};
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
    network = new Network(net_canvas);
    temporal = new Temporal(temp_canvas);
    controller = new Controller(filename, network, temporal);

}

public void draw() {
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
  private float[] canvas;

  public Network (float[] canvas) {
  	this.canvas = canvas;
  }

  public void draw() {
  	for (Object key: nodes.keySet()) {
  		Node n = (Node)(nodes.get(key));
        n.draw();
    } 
  	for (Edge e : edges) {
  		e.draw();
  	}
  }

  public void add_edges (ArrayList<Edge> edges) {
  	this.edges = edges;
  	for (Edge e : edges) {
  		Node s = (Node)(nodes.get(e.source));
  		Node d = (Node)(nodes.get(e.dest));
  		e.x1 = s.x;
  		e.y1 = s.y;
  		e.x2 = d.x;
  		e.y2 = d.y;
  	}
  }

  public void add_nodes (HashMap nodes) {
  	this.nodes = nodes;
  	ArrayList<Node> r = new ArrayList();
  	for (Object key: nodes.keySet()) {
           r.add((Node)(nodes.get(key)));
    }
  	Node n1 = r.get(0);
  	Node n2 = r.get(1);
  	Node n3 = r.get(2);
  	Node n4 = r.get(3);
  	Node n5 = r.get(4);
  	Node n6 = r.get(5);
  	Node n7 = r.get(6);
  	Node n8 = r.get(7);
  	Node n9 = r.get(8);
  	Node n10 = r.get(9);
  	Node n11 = r.get(10);
  	Node n12 = r.get(11);
  	Node n13 = r.get(12);
  	Node n14 = r.get(13);
  	Node n15 = r.get(14);
  	Node n16 = r.get(15);
  	Node n17 = r.get(16);
  	Node n18 = r.get(17);
  	Node n19 = r.get(18);
  	n1.x = 10;
  	n1.y = 10;
  	n2.x = 10;
  	n2.y = 20;
  	n3.x = 40;
  	n3.y = 70;
  	n4.x = 100;
  	n4.y = 100;
  	n5.x = 200;
  	n5.y = 150;
  	n6.x = 0;
  	n6.y = 340;
  	n7.x = 500;
  	n7.y = 80;
  	n8.x = 100;
  	n8.y = 500;
  	n9.x = 500;
  	n9.y = 200;
  	n10.x = 900; //*********
  	n10.y = 243;
  	n11.x = 162;
  	n11.y = 450;
  	n12.x = 233;
  	n12.y = 459;
  	n13.x = 204;
  	n13.y = 102;
  	n14.x = 589;
  	n14.y = 23;
  	n15.x = 502;
  	n15.y = 430;
  	n16.x = 543;
  	n16.y = 294;
  	n17.x = 750;
  	n17.y = 120;
  	n18.x = 900; //*********
  	n18.y = 432;
  	n19.x = 0;
  	n19.y = 493;
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
  private float[] canvas;

  public Temporal (float[] canvas) {
  	this.canvas = canvas;
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
