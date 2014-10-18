NodeSystem diagram;

void setup() {
    frame.setResizable(true);
    size(1000, 800);
    frameRate(60);
    diagram = new NodeSystem("small_data.csv");
}

void draw() {
    background(255, 255, 255);
    diagram.draw();
    diagram.update();
}