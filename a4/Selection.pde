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
            stroke(0, 120, 180, 180);
            fill(0, 120, 180, 80);
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
        
    }
    private Boolean checkIntersection() {
        return true;
    }
};