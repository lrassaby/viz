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

public class a3 extends PApplet {

NodeSystem diagram;

public void setup() {
    frame.setResizable(true);
    size(1000, 800);
    background(255);
    diagram = new NodeSystem("data.csv");
}

public void draw() {
    diagram.draw();
    // diagram.update();
}
public class Edge {
	public Node a; 
	public Node b;
	public float optimal_length;
	public final float SPRING_MULTIPLE = 400;

	public Edge(Node a, Node b, float optimal_length) {
		this.a = a;
		this.b = b;
		this.optimal_length = optimal_length;
	}

	public void draw() {
	    strokeWeight(1);
		line(a.x, a.y, b.x, b.y);
	}
	
	public float hookesForce() {
		return SPRING_MULTIPLE * (a.distance(b) - optimal_length);
	}
}
public class Node {
	public ArrayList<Edge> edges;
	public ArrayList<Node> nodes;
	public String id;
	public float x, y, mass, system_mass;
    public float x_velocity, y_velocity; 
	public float x_acceleration, y_acceleration;
	public float radius;
	public boolean hover;
	public final float AREA_MULTIPLE = 150;
	public final float UPDATE_MULTIPLE = 1;
    public final float COULOMB_MULTIPLE = 1;

	public Node(String id, float mass, float system_mass) {
		this.id = id;
		this.mass = mass;
		this.system_mass = system_mass;
		hover = false;
		x_velocity = 0;
		y_velocity = 0;
		x_acceleration = 0;
		y_acceleration = 0;
		edges = new ArrayList<Edge>();
		radius = sqrt(mass / system_mass) * AREA_MULTIPLE;
		x = random(radius, width - radius);
		y = random(radius, height - radius);
	}

	public void draw() {
		ellipse(x, y, radius, radius);
	}

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

	public float coulombForce(Node node) {
        return (COULOMB_MULTIPLE * (node.mass * this.mass) / system_mass) / pow(distance(node), 2);
	}

    public float distance(Node node) {
        return sqrt(pow((this.x - node.x), 2) + pow((this.y - node.y), 2));
    }

	public boolean intersect() {
        return sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius;
	}

	public void update() {
        // cooloommmmbbb
        for (Node n : nodes) {
            if (n.id != this.id) {
                float force = coulombForce(n);
                x_acceleration = (force / mass) * (n.x - this.x);
                y_acceleration = (force / mass) * (n.y - this.y);
                println("x_accel" + x_acceleration);
                println("y_accel" + y_acceleration);
            }
        }
        // hooookes
        for (Edge e : edges) {
            float force = e.hookesForce();
            float x_component = e.a.id == this.id ? (e.a.x - e.b.x) : (e.b.x - e.a.x);
            float y_component = e.a.id == this.id ? (e.a.y - e.b.y) : (e.b.y - e.a.y);
            x_acceleration = (force / mass) * x_component;
            y_acceleration = (force / mass) * y_component;   
        }

        x_velocity += (x_acceleration * (1 / frameRate));
        y_velocity += (y_acceleration * (1 / frameRate));
		x += (x_velocity * (1 / frameRate)) * UPDATE_MULTIPLE;
		y += (y_velocity * (1 / frameRate)) * UPDATE_MULTIPLE;
	}
}

public class NodeSystem {
  public ArrayList<Node> nodes;
  public ArrayList<Edge> edges;

  public NodeSystem(String filename) {
    nodes = new ArrayList<Node>();
    edges = new ArrayList<Edge>();
    readInput(filename);
  }

  public void draw() {
    for (Edge e : edges) {
      e.draw();
    }
    for (Node n : nodes) {
      n.draw();
    }
  }  

  public void update() {
    for (Node n : nodes) {
      n.update();
    }
  }
  
  private void readInput(String filename) {
    HashMap nodes_map = new HashMap();
    String lines[] = loadStrings(filename);
    int total_mass = 0;
    int num_nodes = parseInt(lines[0]);
    int num_edges = parseInt(lines[num_nodes + 1]);
    
    /* calculate total mass */
    for (int i = 1; i <= num_nodes; i++) {
      String[] temp = split(lines[i], ',');
      total_mass += parseInt(temp[1]);
    }
    
    /* add nodes to arraylist */
    for (int i = 1; i <= num_nodes; i++) {
      String[] temp = split(lines[i], ',');
      // temp[0] is the name of the node
      // temp[1] is its mass
      Node n = new Node(temp[0], parseInt(temp[1]), total_mass);
      nodes_map.put(temp[0], n);
      nodes.add(n);
    }

    /* add edges to arraylist */
    for (int i = num_nodes + 2; i <= num_nodes + num_edges + 1; i++) {
      String[] temp = split(lines[i], ',');
      // temp[0], temp[1] are the names of the nodes
      // temp[2] is the optimal length of the spring
      Node brother = (Node)nodes_map.get(temp[0]);
      Node sister = (Node)nodes_map.get(temp[1]);
      Edge e = new Edge(brother, sister, PApplet.parseFloat(parseInt(temp[2])));
      brother.addEdge(e);
      sister.addEdge(e);
      edges.add(e);
    }
    /* add all nodes to each node */
    for (Node n : nodes) {
      n.nodes = nodes;
    }
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "a3" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}