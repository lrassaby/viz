import java.util.Comparator;
import java.util.Collections;

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
    categorical.built_selected = false;
    categorical.tear_selected = false;
    categorical.tcp_selected = false;
    categorical.udp_selected = false;
    categorical.info_selected = false;
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