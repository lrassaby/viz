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
	public final float UPDATE_MULTIPLE = 20;
    public final float COULOMB_MULTIPLE = 1e2;
    public final float DAMPING = 0.95;

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

        x_acceleration = (sum_force_x / mass);
        y_acceleration = (sum_force_y / mass);
        x_velocity += x_acceleration - (1 - DAMPING) * x_velocity;
        y_velocity += y_acceleration - (1 - DAMPING) * y_velocity;
		x += x_velocity * (1 / frameRate);
		y += y_velocity * (1 / frameRate);

        energy = (x_velocity * x_velocity + y_velocity * y_velocity) * mass;
	}
}

