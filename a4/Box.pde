public class Box {
    public String time; 
    public String port;
    public String op, protocol;
    private ArrayList<Edge> edges;
    private float weight;
    public int index;
    public final float margin_l = 90, margin_b = 40;
    public Boolean selected;
    public float x, y, w, h;

    public void draw() {
        int row = index % 8, col = index / 8;
        w = (width - margin_l)/31;
        h = (200 - margin_b)/8;
        x = col * w + margin_l;
        y = row * h + (height - 200);
        stroke(150);
        if (weight > 0) {
            if (selected) {
                fill(13, 134, 90, 60 + weight);
            } else {
                fill(5, 112, 204, 60 + weight);
            }
        } else {
            fill(255);
        }
        rect(x, y, w, h);
    }

    public Box(TableRow row) {
        selected = false;
        weight = 0;
        this.time = row.getString("Time");
        this.port = row.getString("Destination port");
        this.op = row.getString("Operation");
        this.protocol = row.getString("Protocol");
        edges = new ArrayList();
    }

    public Box(String time, String port) {
        weight = 0;
        this.time = time;
        this.port = port;
        this.op = "";
        this.protocol = "";
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

    public boolean intersect() {
        return ((mouseX > x) && (mouseX < x + w) && (mouseY > y) && (mouseY < y + h));
    }
}
