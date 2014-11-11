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
    private final float COULOMB_MULTIPLE = 2e4;
    private final float CENTER_COERSION_MULTIPLE = 1e-2;
    private final float DAMPING = 0.95;
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
            return int((float(w)/5) * 255);
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
            n.x = x + 0.5 * BOX_SIZE + BOX_SIZE * i;
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

