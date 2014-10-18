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
	public final float UPDATE_MULTIPLE = 1e-5;
    public final float COULOMB_MULTIPLE = 1e6;

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
        // coulomb's law 
        for (Node n : nodes) {
            if (n.id != this.id) {
                float force = coulombForce(n);
                float dist = distance(n);
                x_acceleration += (force / mass) * (this.x - n.x) / dist * UPDATE_MULTIPLE;
                y_acceleration += (force / mass) * (this.y - n.y) / dist * UPDATE_MULTIPLE;
            }
        }
        // hooke's law
        for (Edge e : edges) {
            float force = e.hookesForce();
            float x_component = e.a.id != this.id ? (e.a.x - e.b.x) : (e.b.x - e.a.x);
            float y_component = e.a.id != this.id ? (e.a.y - e.b.y) : (e.b.y - e.a.y);
            x_acceleration += (force / mass) * x_component;
            y_acceleration += (force / mass) * y_component;  
        }

        x_velocity += (x_acceleration * (1 / frameRate));
        y_velocity += (y_acceleration * (1 / frameRate));
		x += x_velocity;
		y += y_velocity ;
	}
}

