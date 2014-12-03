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
            if (x_start >= width - 140 && y_start <= height - 200) {
                if (mouseX < width - 140) {
                    x_end = width - 140;
                }
                if (mouseY > height - 200) {
                    y_end = height - 200;
                }
            } else if (x_start <= width - 140 && y_start <= height - 200) {
                if (mouseX > width - 140) {
                    x_end = width - 140;
                }
                if (mouseY > height - 200) {
                    y_end = height - 200;
                }
            } else {
                if (mouseY < height - 200) {
                    y_end = height - 200;
                }
            }
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
        hover_mode = false;
    }
    public void enable() {
        selection_mode = true;
        fixed = false;
    }
    public void modifyViews() {
        Boolean b_s = false;
        Boolean te_s = false;
        Boolean tc_s = false;
        Boolean u_s = false;
        Boolean i_s = false;

        Barchart op = categorical.op;
        int sectionWidth = abs(((op.rightxaxis.x - op.origin.x) / op.categories.length));
        float ratio = float(op.origin.y - op.topyaxis.y) / op.maxY;
        int x = op.origin.x + sectionWidth * 0 + sectionWidth / 2 + int(sectionWidth * 0.1);
        int y = op.origin.y - int(op.data.getRow(0).getInt(op.categories[0]) * ratio);
        if (mouseX >= x - sectionWidth/2 && mouseX <= x + sectionWidth/2 && mouseY >= y && mouseY <= categorical.op.origin.y) {hover_mode = true; b_s = true;}
        else { b_s = false;}
        x = op.origin.x + sectionWidth * 1 + sectionWidth / 2 + int(sectionWidth * 0.1);
        y = op.origin.y - int(op.data.getRow(0).getInt(op.categories[1]) * ratio);
        if (mouseX >= x - sectionWidth/2 && mouseX <= x + sectionWidth/2 && mouseY >= y && mouseY <= categorical.op.origin.y) {hover_mode = true; te_s = true;}
        else { te_s = false;}

        Barchart pro = categorical.protocol;
        sectionWidth = abs(((pro.rightxaxis.x - pro.origin.x) / pro.categories.length));
        ratio = float(pro.origin.y - pro.topyaxis.y) / pro.maxY;
        x = pro.origin.x + sectionWidth * 0 + sectionWidth / 2 + int(sectionWidth * 0.1);
        y = pro.origin.y - int(pro.data.getRow(0).getInt(pro.categories[0]) * ratio);
        if (mouseX >= x - sectionWidth/2 && mouseX <= x + sectionWidth/2 && mouseY >= y && mouseY <= pro.origin.y) {hover_mode = true; tc_s = true;}
        else { tc_s = false;}
        x = pro.origin.x + sectionWidth * 1 + sectionWidth / 2 + int(sectionWidth * 0.1);
        y = pro.origin.y - int(pro.data.getRow(0).getInt(pro.categories[1]) * ratio);
        if (mouseX >= x - sectionWidth/2 && mouseX <= x + sectionWidth/2 && mouseY >= y && mouseY <= pro.origin.y) {hover_mode = true; u_s = true;}
        else { u_s = false;}

        Barchart info = categorical.info;
        sectionWidth = abs(((info.rightxaxis.x - info.origin.x) / info.categories.length));
        ratio = float(info.origin.y - info.topyaxis.y) / info.maxY;
        x = info.origin.x + sectionWidth * 0 + sectionWidth / 2 + int(sectionWidth * 0.1);
        y = info.origin.y - int(info.data.getRow(0).getInt(info.categories[0]) * ratio);
        if (mouseX >= x - sectionWidth/2 && mouseX <= x + sectionWidth/2 && mouseY >= y && mouseY <= info.origin.y) {hover_mode = true; i_s = true;}
        else { i_s = false;}




        for (Object key : network.nodes.keySet()) {
            Node n = (Node)(network.nodes.get(key));
            if (n.intersect()) {hover_mode = true;}
        }
        for (Box b : temporal.boxes) {
            if (b.intersect()) {hover_mode = true;}
        }
        if (selection_mode || hover_mode) {

            categorical.built_selected = pointSelected(categorical.op.intersections[0],categorical.op.intersections[1]) || b_s;
            if (pointSelected(categorical.op.intersections[0],categorical.op.intersections[1]) || b_s) {
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

            categorical.tear_selected = pointSelected(categorical.op.intersections[2],categorical.op.intersections[3]) || te_s;
            if (pointSelected(categorical.op.intersections[2],categorical.op.intersections[3]) || te_s) {
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

            categorical.tcp_selected = pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1]) || tc_s;
            if (pointSelected(categorical.protocol.intersections[0],categorical.protocol.intersections[1]) || tc_s) {
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

            categorical.udp_selected = pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3]) || u_s;
            if (pointSelected(categorical.protocol.intersections[2],categorical.protocol.intersections[3]) || u_s) {
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
            categorical.info_selected = pointSelected(categorical.info.intersections[0],categorical.info.intersections[1]) || i_s;
            if (pointSelected(categorical.info.intersections[0],categorical.info.intersections[1]) || i_s) {
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