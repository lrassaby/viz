public class Box {
    public String time; 
    public String port;
    private ArrayList<Edge> edges;
    private float weight;
    public int index;
    public final int margin_l = 20, margin_b = 10;

    public void draw() {
        int row = index % 8, col = index / 8;
        float x, y, w, h;
        w = (width - margin_l)/31;
        h = (200 - margin_b)/8;
        x = col * w + margin_l;
        y = row * h + (height - 200);
        rect(x, y, w, h);
    }

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
