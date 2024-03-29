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

public class lab9 extends PApplet {


NodeSystem diagram;

public void setup() {
    frame.setResizable(true);
    size(1000, 800);
    frameRate(60);

    String filename = null;
    try { 
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception e) { 
        e.printStackTrace();
    } 

    try {
      filename = JOptionPane.showInputDialog(frame, "Input file", "data3.csv");
    } catch (Exception e) {
      println("Process cancelled.");
      exit();
    }
    if (filename == null) {
      println("Process cancelled.");
      exit();
    }
    diagram = new NodeSystem(filename);
}

public void draw() {
    background(255, 255, 255);
    diagram.draw();
    diagram.update();
}

/*void mousePressed() {
    diagram.mousePressed();
}

void mouseReleased() {
    diagram.mouseReleased();
}*/
public class Big_Node {
	private ArrayList<Big_Node> big_nodes;
    public ArrayList<Node> nodes;
    public ArrayList<D_Edge> edges;

	public String id;
	private float x, y, mass, system_mass;
    private int num_nodes;
    private float dx, dy;
    private boolean selected;
    private float x_velocity, y_velocity; 
	private float x_acceleration, y_acceleration;
	private float radius;
    private float energy;
	private final float AREA_MULTIPLE = 60;
	private final float UPDATE_MULTIPLE = 10;
    private final float COULOMB_MULTIPLE = 2e4f;
    private final float CENTER_COERSION_MULTIPLE = 1e-2f;
    private final float DAMPING = 0.95f;
    private final float BOX_SIZE = 20;

	public Big_Node(String id, int num_nodes, float system_mass) {
		this.id = id;
        this.num_nodes = num_nodes;
        this.mass = 1;
        this.system_mass = system_mass;
        nodes = new ArrayList<Node>();
        edges = new ArrayList<D_Edge>();

        big_nodes = new ArrayList<Big_Node>();
		x_velocity = 0;
		y_velocity = 0;
		x_acceleration = 0;
		y_acceleration = 0;
        energy = 0;
        radius = sqrt(mass / system_mass) * AREA_MULTIPLE;

		x = random(BOX_SIZE, width - num_nodes*BOX_SIZE);
		y = random(BOX_SIZE, height - num_nodes*BOX_SIZE);
	}

    public void addName(Node n) {
        nodes.add(n);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.dx = x - mouseX;
            this.dy = y - mouseY;
        }
    }

    public boolean isSelected() {
        return selected;
    }

	public void draw(boolean colored) {
        strokeWeight(2);
        if (colored) {
            fill(200, 60, 60);
            stroke(100, 30, 30);
        } else {
            fill(154, 175, 255);
            stroke(40, 58, 127);
        }
        text("id: " + id, x - 10, y - radius - 10, 80, 50); 

        for (int j = 0; j < num_nodes; j++) {
            for (int i = 0; i < num_nodes; i++) {
                int o = getWeight(i, j);
                fill(200, 0, 0, o);
    	        rect(x+ i*BOX_SIZE, y+ j*BOX_SIZE, BOX_SIZE, BOX_SIZE);
            }
        }
        for (int i = 0; i < num_nodes; i++) {
                fill(255);
                rect(x+ i*BOX_SIZE, y-BOX_SIZE, BOX_SIZE, BOX_SIZE);
                Node n = nodes.get(i);
                String l = String.valueOf(n.id.charAt(0));
                fill(0);
                text(l, x+ i*BOX_SIZE + 2, y-BOX_SIZE + 2, BOX_SIZE, BOX_SIZE);
        }
        for (int i = 0; i < num_nodes; i++) {
                fill(255);
                rect(x-BOX_SIZE, y+ i*BOX_SIZE, BOX_SIZE, BOX_SIZE);
                Node n = nodes.get(i);
                String l = String.valueOf(n.id.charAt(0));
                fill(0);
                text(l, x - BOX_SIZE + 2, y+ i*BOX_SIZE + 2, BOX_SIZE, BOX_SIZE);
        }

        for (Node n : nodes) {
            for (Edge e : n.edges) {
                e.draw();
            }
        }

	}

    public int getWeight(int i, int j) {
        Node n = nodes.get(i);
        Node b = nodes.get(j);
        if (n.inner_edges.containsKey(b.id)) {
            int w = (Integer) n.inner_edges.get(b.id);
            return PApplet.parseInt((PApplet.parseFloat(w)/5) * 255);
        }
        else {
            return 0;
        }
    }

    public void drawHoverBox() {
        fill(180, 180, 180, 128);
        strokeWeight(0);
        rect(x - 20, y - radius - 60, 90, 50, 15);
        fill(200, 60, 60);
        text("id: " + id, x - 10, y - radius - 50, 80, 50); 
        text("mass: " + mass, x - 10, y - radius - 40, 80, 50); 
    }

    public void addEdge(Node b, Node s, int weight) {
        b.addInner(s, weight);
        s.addInner(b, weight);
    }

    public void addDummyEdge(D_Edge e) {
        edges.add(e);
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

	public float coulombForce(Big_Node node) {
        float force = COULOMB_MULTIPLE / pow(distance(node), 2);
        if (force != force) { // NaN
            force = 0;
        }
        return force;
	}

    public float distance(Big_Node node) {
        return max(sqrt(pow((this.x - node.x), 2) + pow((this.y - node.y), 2)), 1);
    }

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}

	public void update() {
        float sum_force_x = 0;
        float sum_force_y = 0;

        // coulomb's law 
        for (Big_Node n : big_nodes) {
            if (n.id != this.id) {
                float force = coulombForce(n) * UPDATE_MULTIPLE;
                float dist = distance(n);
                float dir_force_x = (this.x - n.x)/dist;
                float dir_force_y = (this.y - n.y)/dist;
                sum_force_x += dir_force_x * force;
                sum_force_y += dir_force_y * force;
            }
        }

        for (int i = 0; i < num_nodes; i++) {
            Node n = nodes.get(i);
            n.x = x + 0.5f * BOX_SIZE + BOX_SIZE * i;
            n.y = y - BOX_SIZE;
        }

        /* coerce the system towards the center */
        sum_force_x += (width/2 - this.x) * CENTER_COERSION_MULTIPLE;
        sum_force_y += (height/2 - this.y) * CENTER_COERSION_MULTIPLE;

        x_acceleration = (sum_force_x / mass);
        y_acceleration = (sum_force_y / mass);
        x_velocity += x_acceleration - (1 - DAMPING) * x_velocity;
        y_velocity += y_acceleration - (1 - DAMPING) * y_velocity;
		x += x_velocity * (1 / frameRate);
		y += y_velocity * (1 / frameRate);

        if (selected) {
            x = mouseX + dx;
            y = mouseY + dy;
        }

        if (x > width - BOX_SIZE*(num_nodes+1) - 1) {
            x = width - BOX_SIZE*(num_nodes+1);
        } else if (x < radius) {
            x = radius;
        }
        if (y > height - BOX_SIZE*(num_nodes+1) - 1) {
            y = height - BOX_SIZE*(num_nodes+1);
        } else if (y < radius) {
            y = radius;
        }

        energy = (x_velocity * x_velocity + y_velocity * y_velocity) * mass;
	}
}

public class D_Edge {
	public Big_Node a; 
	public Big_Node b;
	public float optimal_length;
	public final float SPRING_MULTIPLE = 0.5e-1f;

	public D_Edge(Big_Node a, Big_Node b, float optimal_length) {
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
		return SPRING_MULTIPLE * (a.distance(b) - optimal_length);
	}
}
public class Edge {
	public Node a; 
	public Node b;
	public float optimal_length;
	public final float SPRING_MULTIPLE = 0.5e-1f;

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
		return SPRING_MULTIPLE * (a.distance(b) - optimal_length);
	}
}
public class Node {
	private ArrayList<Edge> edges;
	private ArrayList<Node> nodes;
    private HashMap inner_edges;
	private String id;
	private float x, y, mass, system_mass;
    private float dx, dy;
    private boolean selected;
    private float x_velocity, y_velocity; 
	private float x_acceleration, y_acceleration;
	private float radius;
    private float energy;
	private final float AREA_MULTIPLE = 60;
	private final float UPDATE_MULTIPLE = 10;
    private final float COULOMB_MULTIPLE = 2e3f;
    private final float CENTER_COERSION_MULTIPLE = 1e-2f;
    private final float DAMPING = 0.95f;

	public Node(String id) {
        inner_edges = new HashMap();
		this.id = id;
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

    public void addInner(Node b, int weight) {
        inner_edges.put(b.id, weight);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        if (selected) {
            this.dx = x - mouseX;
            this.dy = y - mouseY;
        }
    }

    public boolean isSelected() {
        return selected;
    }

	public void draw(boolean colored) {
        strokeWeight(2);
        if (colored) {
            fill(200, 60, 60);
            stroke(100, 30, 30);
        } else {
            fill(154, 175, 255);
            stroke(40, 58, 127);
        }
		ellipse(x, y, radius * 2, radius * 2);
	}

    public void drawHoverBox() {
        fill(180, 180, 180, 128);
        strokeWeight(0);
        rect(x - 20, y - radius - 60, 90, 50, 15);
        fill(200, 60, 60);
        text("id: " + id, x - 10, y - radius - 50, 80, 50); 
        text("mass: " + mass, x - 10, y - radius - 40, 80, 50); 
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

	public float coulombForce(Node node) {
        float force = COULOMB_MULTIPLE / pow(distance(node), 2);
        if (force != force) { // NaN
            force = 0;
        }
        return force;
	}

    public float distance(Node node) {
        return max(sqrt(pow((this.x - node.x), 2) + pow((this.y - node.y), 2)), 1);
    }

	public boolean intersect() {
        return (sqrt(pow((mouseX - this.x), 2) + pow((mouseY - this.y), 2)) < radius);
	}

	public void update() {
        float sum_force_x = 0;
        float sum_force_y = 0;

        // coulomb's law 
        for (Node n : nodes) {
            if (n.id != this.id) {
                float force = coulombForce(n) * UPDATE_MULTIPLE;
                float dist = distance(n);
                float dir_force_x = (this.x - n.x)/dist;
                float dir_force_y = (this.y - n.y)/dist;
                sum_force_x += dir_force_x * force;
                sum_force_y += dir_force_y * force;
            }
        }

        // hooke's law
        for (Edge e : edges) {
            float force = e.hookesForce() * UPDATE_MULTIPLE;
            float dist_a = distance(e.a);
            float dist_b = distance(e.b);
            float dist = e.a.distance(e.b);

            float dir_force_x = dist_a > dist_b ? (e.a.x - e.b.x) / dist : (e.b.x - e.a.x) / dist;
            float dir_force_y = dist_a > dist_b ? (e.a.y - e.b.y) / dist : (e.b.y - e.a.y) / dist; 

            sum_force_x += dir_force_x * force;
            sum_force_y += dir_force_y * force;
        }

        /* coerce the system towards the center */
        sum_force_x += (width/2 - this.x) * CENTER_COERSION_MULTIPLE;
        sum_force_y += (height/2 - this.y) * CENTER_COERSION_MULTIPLE;

        x_acceleration = (sum_force_x / mass);
        y_acceleration = (sum_force_y / mass);
        x_velocity += x_acceleration - (1 - DAMPING) * x_velocity;
        y_velocity += y_acceleration - (1 - DAMPING) * y_velocity;
		x += x_velocity * (1 / frameRate);
		y += y_velocity * (1 / frameRate);

        if (selected) {
            x = mouseX + dx;
            y = mouseY + dy;
        }

        if (x > width - radius - 1) {
            x = width - radius;
        } else if (x < radius) {
            x = radius;
        }
        if (y > height - radius - 1) {
            y = height - radius;
        } else if (y < radius) {
            y = radius;
        }

        energy = (x_velocity * x_velocity + y_velocity * y_velocity) * mass;
	}
}

public class NodeSystem {
  private ArrayList<Big_Node> big_nodes;
  private ArrayList<Edge> edges;
  private float total_energy;


  public NodeSystem(String filename) {
    big_nodes = new ArrayList<Big_Node>();
    edges = new ArrayList<Edge>();
    readInput(filename);
    total_energy = 0;
  }

  public void draw() {
    for (Edge e : edges) {
      e.draw();
    }
    float energy = 0;
    Node hovernode = null;
    Node selectednode = null;
    for (Big_Node n : big_nodes) {
      n.draw(false);
      energy += n.energy;
    }
    total_energy = energy;
    fill(40, 58, 127);
    text("Total energy: " + total_energy, 10, 15); 
  }  

  public void update() {
    for (Big_Node n : big_nodes) {
      n.update();
    }
  }

  /*public void mousePressed() {
    for (Node n : nodes) {
      if (mousePressed) {
        if(n.intersect()) {
          n.setSelected(true);
        } else {
          n.setSelected(false);
        }
      } else {
        n.setSelected(false);
      }
    }
  }

  public void mouseReleased() {
    for (Node n : nodes) {
      n.setSelected(false);
    }
  }*/
  


  private void readInput(String filename) {
    HashMap nodes_map = new HashMap();
    String lines[] = loadStrings(filename);
    int num_nodes = parseInt(lines[0]);
    int curr_line = 1;
    
    /* add nodes to arraylist */
    for (int i = 1; i <= num_nodes; i++) {
      String[] temp = split(lines[curr_line], ',');
      curr_line ++;
      // temp[0] is the name of the node
      // temp[1] is the # of names in the cluster
      // temp[2] is the # of inner relationships

      Big_Node n = new Big_Node(temp[0], parseInt(temp[1]), num_nodes);
      for (int j = 0; j < parseInt(temp[1]); j++) {
        Node l = new Node(lines[curr_line + j]);
        n.addName(l);
        nodes_map.put(lines[curr_line + j], l);
      }
      curr_line += parseInt(temp[1]);

      for (int j = 0; j < parseInt(temp[2]); j++) {
        String[] temp2 = split(lines[curr_line + j], ',');
        // temp[0], temp[1] are the names of the nodes
        // temp[2] is the weight of the edge
        Node brother = (Node)nodes_map.get(temp2[0]);
        Node sister = (Node)nodes_map.get(temp2[1]);
        n.addEdge(brother, sister, parseInt(temp2[2]));
      }
      curr_line += parseInt(temp[2]);
      
      nodes_map.put(temp[0], n);
      big_nodes.add(n);
    }

    /* add edges to arraylist */
    for (int i = curr_line; i < lines.length; i++) {
      String[] temp = split(lines[i], ',');
      // temp[0], temp[1] are the names of the nodes
      // temp[2] is the optimal length of the spring
      Node brother = (Node)nodes_map.get(temp[0]);
      Node sister = (Node)nodes_map.get(temp[2]);
      Edge e = new Edge(brother, sister, 1);
      brother.addEdge(e);
      sister.addEdge(e);
      edges.add(e);
    }
    /* add all nodes to each node */
    for (int i = 0; i < num_nodes; i++) {
      Big_Node n = big_nodes.get(i);
      n.big_nodes = big_nodes;
      for (int j = i + 1; j < num_nodes; j++) {
        Big_Node b = big_nodes.get(j);
        D_Edge e = new D_Edge(n, b, 15);
        n.addDummyEdge(e);
        b.addDummyEdge(e);
      }
    }
  }

}


/*

  public void test() {
    for (Big_Node n : big_nodes) {
      println("ID: " + n.id);
      println("Children: ");
      for (Node l : n.nodes) {
        println(" "+l.id+ ", with siblings: ");
        for (Pair p : l.inner_edges) {
          Node t =(Node)(p.first);
          println("  "+t.id+", "+p.second);
        }
      }
      println();
      println();
    }
    println("Outer Edges:");
    for (Edge e : edges) {
      println(e.a.id + ", " + e.b.id);
    }

*/

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lab9" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
