class Selection {
    private Boolean selection_mode;
    private Boolean hover_mode;
    private Boolean fixed;
    private Network network;
    private Temporal temporal;
    private Categorical categorical;
    public float x_start, y_start, x_end, y_end;

    Selection(Network network, Temporal temporal, Categorical categorical) {
        x_start = y_start = x_end = y_end = 0;
        this.network = network;
        this.temporal = temporal;
        this.categorical = categorical;
        disable();
        fixed = true;
        hover_mode = false;
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
        for (Object key : network.nodes.keySet()) {
                Node n = (Node)(network.nodes.get(key));
                if (n.intersect()) {hover_mode = true;}
        }
        for (Box b : temporal.boxes) {
            if (b.intersect()) {hover_mode = true;}
        }
        if (selection_mode || hover_mode) {
            categorical.built_selected = pointSelected(categorical.op.intersections[0],categorical.op.intersections[1]);
            if (pointSelected(categorical.op.intersections[0],categorical.op.intersections[1])) {
                for (Edge e : network.edges) {
                        if (e.op.equals("Built")) {
                            e.selected = categorical.built_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.op.equals("Built")) {
                            b.selected = categorical.built_selected;
                        }
                }
            }

            categorical.tear_selected = pointSelected(categorical.op.intersections[2],categorical.op.intersections[3]);
            if (pointSelected(categorical.op.intersections[2],categorical.op.intersections[3])) {
                for (Edge e : network.edges) {
                        if (e.op.equals("Teardown")) {
                            e.selected = categorical.tear_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.op.equals("Teardown")) {
                            b.selected = categorical.tear_selected;
                        }
                }
            }

            categorical.tcp_selected = pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1]);
            if (pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1])) {
                for (Edge e : network.edges) {
                        if (e.protocol.equals("TCP")) {
                            e.selected = categorical.tcp_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.protocol.equals("TCP")) {
                            b.selected = categorical.tcp_selected;
                        }
                }
            }

            categorical.udp_selected = pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3]);
            if (pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3])) {
                for (Edge e : network.edges) {
                        if (e.protocol.equals("UDP")) {
                            e.selected = categorical.udp_selected;
                        }
                }
                for (Box b : temporal.boxes) {
                        if (b.protocol.equals("UDP")) {
                            b.selected = categorical.udp_selected;
                        }
                }
            }
            categorical.info_selected = pointSelected(categorical.info.intersections[0],categorical.info.intersections[1]);
            if (pointSelected(categorical.info.intersections[0],categorical.info.intersections[1])) {
                for (Edge e : network.edges) {
                        e.selected = true;
                }
                for (Box b : temporal.boxes) {
                        b.selected = true;
                }
            }

            for (Object key : network.nodes.keySet()) {
                Node n = (Node)(network.nodes.get(key));
                n.selected =(selection_mode && pointSelected(n.x, n.y)) || n.intersect();
                
                if ((selection_mode && pointSelected(n.x, n.y)) || n.intersect()) {
                    for (Edge e : n.edges) {
                        e.selected = e.selected || n.selected;
                        for (Box b : e.boxes) {
                            b.selected = e.selected;
                        }
                    }
                    for (Edge e : n.edges) {
                        if (e.op.equals("Built")) {
                            categorical.built_selected = e.selected;
                        } else {
                            categorical.tear_selected = e.selected;
                        }
                        if (e.protocol.equals("TCP")) {
                            categorical.tcp_selected = e.selected;
                        } else {
                            categorical.udp_selected = e.selected;
                        }
                        categorical.info_selected = e.selected;
                    }
                } /*else {
                    
                }*/
            } 
            for (Box b : temporal.boxes) {
                b.selected = b.selected || (selection_mode && pointSelected(b.x + b.w/2, b.y + b.h/2)) || b.intersect();

                if ((selection_mode && pointSelected(b.x + b.w/2, b.y + b.h/2)) || b.intersect()) {
                    for (Edge e : b.edges) {
                        e.selected = e.selected || b.selected;
                    }
                    if (!b.op.equals("")) {
                        if (b.op.equals("Built")) {
                                categorical.built_selected = b.selected || categorical.built_selected;
                        } else {
                                categorical.tear_selected = b.selected || categorical.tear_selected;
                        } 
                        if (b.protocol.equals("TCP")) {
                                categorical.tcp_selected = b.selected || categorical.tcp_selected;
                        } else {
                                categorical.udp_selected = b.selected || categorical.udp_selected;
                        }
                        categorical.info_selected = b.selected ||categorical.info_selected;
                    }
                }
            }
            /*categorical.built_selected = pointSelected(categorical.op.intersections[0],categorical.op.intersections[1]) || categorical.built_selected;
            categorical.tear_selected = pointSelected(categorical.op.intersections[2],categorical.op.intersections[3]) || categorical.tear_selected ;
            categorical.tcp_selected = pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1]) || categorical.tcp_selected ;
            categorical.udp_selected = pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3]) || categorical.udp_selected ;
            categorical.info_selected = pointSelected(categorical.info.intersections[0],categorical.info.intersections[1]) || categorical.info_selected ;*/
        }
        hover_mode = false;
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