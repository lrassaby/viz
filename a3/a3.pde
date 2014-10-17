void setup () {
  frame.setResizable(true);
  size(1000, 800);
  NodeSystem diagram = new NodeSystem("data.csv");
}

void draw() {
    diagram.draw();
}