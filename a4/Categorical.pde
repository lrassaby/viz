public class Categorical {
  private int tear, built, udp, tcp;
  private Barchart info, op, protocol;
  private ArrayList<Edge> built_edges, tear_edges, tcp_edges, udp_edges;
  private ArrayList<Box> built_boxes, tear_boxes, tcp_boxes, udp_boxes;
  public Boolean built_selected, tear_selected, tcp_selected, udp_selected, info_selected;

  public Categorical() {
    built_edges = new ArrayList<Edge>();
    tear_edges = new ArrayList<Edge>();
    tcp_edges = new ArrayList<Edge>();
    udp_edges = new ArrayList<Edge>();
    built_boxes = new ArrayList<Box>();
    tear_boxes = new ArrayList<Box>();
    tcp_boxes = new ArrayList<Box>();
    udp_boxes = new ArrayList<Box>();
    built_selected = false;
    tear_selected = false;
    tcp_selected = false;
    udp_selected = false;
    info_selected = false;
  }

  public void draw() {
    op.margins[0] = width - 120;
    protocol.margins[0] = width - 120;
    info.margins[0] = width - 120;
    int selected[] = {0, 0};
    if (built_selected) {selected[0] = 1;}
    if (tear_selected) {selected[1] = 1;}
    op.draw(1, 0, selected);
    if (tcp_selected) {selected[0] = 1;} else {selected[0] = 0;}
    if (udp_selected) {selected[1] = 1;} else {selected[1] = 0;}
    protocol.draw(1, 0, selected);
    if (info_selected) {selected[0] = 1;} else {selected[0] = 0;}
    info.draw(1, 0, selected);
  }

  public void set_data(int tear, int built, int tcp, int udp) {
    this.tear = tear;
    this.built = built;
    this.udp = udp;
    this.tcp = tcp;
    Table table = new Table();
    table.addColumn("TCP");
    table.addColumn("UDP");
    TableRow newRow = table.addRow();
    newRow.setInt("TCP", tcp);
    newRow.setInt("UDP", udp);
    String cat[] = {"TCP", "UDP"};
    int[] margins = {width - 120, 50, 20, 650};
    protocol = new Barchart(table,cat, margins);

    Table table2 = new Table();
    table2.addColumn("Built");
    table2.addColumn("Teardown");
    newRow = table2.addRow();
    newRow.setInt("Built", built);
    newRow.setInt("Teardown", tear);
    String cat2[] = {"Built", "Teardown"};
    int[] margins2 = {width - 120, 250, 20, 450};
    op = new Barchart(table2,cat2, margins2);

    Table table3 = new Table();
    table3.addColumn("Info");
    newRow = table3.addRow();
    newRow.setInt("Info", 1);
    String cat3[] = {"Info"};
    int[] margins3 = {width - 120, 450, 20, 250};
    info = new Barchart(table3, cat3, margins3);
  }

  public void set_edges(HashMap edges) {
    for (Object key: edges.keySet()) {
        Edge e = (Edge)edges.get(key);
        if(e.op.equals("Built")) {
            built_edges.add(e);
        } else {
            tear_edges.add(e);
        }
        if (e.protocol.equals("TCP")) {
            tcp_edges.add(e);
        } else {
            udp_edges.add(e);
        }
    }
  }

  public void set_boxes(HashMap boxes) {
    for (Object key: boxes.keySet()) {
        Box b = (Box)boxes.get(key);
        if(b.op.equals("Built")) {
            built_boxes.add(b);
        } else {
            tear_boxes.add(b);
        }
        if (b.protocol.equals("TCP")) {
            tcp_boxes.add(b);
        } else {
            udp_boxes.add(b);
        }
    }
  }
}