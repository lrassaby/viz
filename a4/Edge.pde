public class Edge {
    public String source; 
    public String dest;    //dest ip
    public HashMap times;
    public ArrayList<Box> boxes;
    public float weight;
    private float x1, y1, x2, y2;
    public Boolean selected;

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
        selected = false;
    }

    public void draw() {
        strokeWeight(weight/30);
        if (selected) {
            stroke(5, 185, 119, 150);
        } else {
            stroke(83, 176, 255, 150);
        }
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

    // intersect function
}
