class Selection {
    private Boolean selection_mode;
    private Boolean fixed;
    private Network network;
    private Temporal temporal;
    public float x_start, y_start, x_end, y_end;

    Selection(Network network, Temporal temporal) {
        x_start = y_start = x_end = y_end = 0;
        this.network = network;
        this.temporal = temporal;
        disable();
        fixed = true;
    }
    public void setFixed() {
        fixed = true;
    }
    public void setMutable() {
        fixed = false;
    }
    public Boolean active() {
        return selection_mode;
    }
    public void update() {
        if (!selection_mode) {
            enable();
            x_start = mouseX;
            y_start = mouseY;
        }
        if (!fixed) {
            x_end = mouseX;
            y_end = mouseY;
        }
    }
    public void draw() {
        if (selection_mode) {
            strokeWeight(1);
            stroke(1, 57, 37, 180);
            fill(96, 185, 153, 50);
            rect(x_start, y_start, x_end - x_start, y_end - y_start);
        }
    }
    public void disable() {
        selection_mode = false;
    }
    public void enable() {
        selection_mode = true;
        fixed = false;
    }
    public void modifyViews() {
        if (selection_mode) {
            for (Object key : network.nodes.keySet()) {
                Node n = (Node)(network.nodes.get(key));
                n.selected = pointSelected(n.x, n.y);
                for (Edge e : n.edges) {
                    e.selected = e.selected || n.selected;
                    for (Box b : e.boxes) {
                        b.selected = e.selected;
                    }
                }
            } 
            for (Box b : temporal.boxes) {
                b.selected = pointSelected(b.x + b.w/2, b.y + b.h/2);
                for (Edge e : b.edges) {
                    e.selected = e.selected || b.selected;
                }
            }
        }
    }
    private Boolean pointSelected(float x, float y) {
        float topleft_x = min(x_start, x_end);
        float topleft_y = min(y_start, y_end);
        float bottomright_x = max(x_start, x_end);
        float bottomright_y = max(y_start, y_end);
        return x > topleft_x && x < bottomright_x 
            && y > topleft_y && y < bottomright_y;
    }
};