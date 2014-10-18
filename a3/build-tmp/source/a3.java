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
    frameRate(60);
    diagram = new NodeSystem("small_data.csv");
}

public void draw() {
    background(255, 255, 255);
    diagram.draw();
    diagram.update();
}
public class Edge {
	public Node a; 
	public Node b;
	public float optimal_length;
	public final float SPRING_MULTIPLE = 1e-3f;

	public Edge(Node a, Node b, float optimal_length) {
		this.a = a;
		this.b = b;
		this.optimal_length = optimal_length;
	}

	public void draw() {
	    strokeWeight(2);
	    stroke(40, 58, 127);
		line(a.x, a.y, b.x, b.y);
	}
	
	public float hookesForce() {
		return SPRING_MULTIPLE * abs(a.distance(b) - optimal_length);
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
    public float energy;
	public final float AREA_MULTIPLE = 150;
	public final float UPDATE_MULTIPLE = 4;
    public final float COULOMB_MULTIPLE = 1e-3f;

	public Node(String id, float mass, float system_mass) {
		this.id = id;
		this.mass = mass;
		this.system_mass = system_mass;
		hover = false;
		x_velocity = 0;
		y_velocity = 0;
		x_acceleration = 0;
		y_acceleration = 0;
        energy = 0;
		edges = new ArrayList<Edge>();
		radius = sqrt(mass / system_mass) * AREA_MULTIPLE;
		x = random(radius, width - radius);
		y = random(radius, height - radius);
	}

	public void draw() {
        fill(154, 175, 255);
        stroke(40, 58, 127);
		ellipse(x, y, radius, radius);
	}

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

	public float coulombForce(Node node) {
        float force = (node.mass * this.mass) / pow(distance(node), 2);
        if (force != force) { // NaN
            force = 0;
        }
        return force * COULOMB_MULTIPLE;
	}

    public float distance(Node node) {
        return sqrt(pow((this.x - node.x), 2) + pow((this.y - node.y), 2));
    }

	public boolean intersect() {
        return sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius;
	}

	public void update() {
        float sum_force_x = 0;
        float sum_force_y = 0;

        // coulomb's law 
        for (Node n : nodes) {
            if (n.id != this.id) {
                float force = coulombForce(n) * UPDATE_MULTIPLE;
                float dir_force_x = (n.x - this.x);
                float dir_force_y = (n.y - this.y);
                sum_force_x += (dir_force_x / (dir_force_x + dir_force_y)) * force;
                sum_force_y += (dir_force_y / (dir_force_x + dir_force_y)) * force;
            }
        }
        // hooke's law
        // for (Edge e : edges) {
        //     sum_of_forces += e.hookesForce() * UPDATE_MULTIPLE;
        //     float dist_a = distance(e.a);
        //     float dist_b = distance(e.b);
        //     float dist = e.a.distance(e.b);

        //     sum_force_x += dist_a > dist_b ? (e.a.x - e.b.x) / dist : (e.b.x - e.a.x) / dist;
        //     sum_force_y += dist_a > dist_b ? (e.a.y - e.b.y) / dist : (e.b.y - e.a.y) / dist;
        // }

        x_acceleration = (sum_force_x / mass);
        y_acceleration = (sum_force_y / mass);
        x_velocity += x_acceleration;
        y_velocity += y_acceleration;
		x += x_velocity * (1 / frameRate);
		y += y_velocity * (1 / frameRate);

        energy += (x_velocity * x_velocity + y_velocity * y_velocity) * mass;
        println("energy: "+energy);
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
