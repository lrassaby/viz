public class Linechart extends AxisChart {
    private int radius = 5;

    Linechart(Table data, String[] categories) {
        super(data, categories);
    }

    void draw () {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        drawAxes();
        drawLabels();
        drawData();
    }


    void drawData () {
        strokeWeight(2);
        float ratio = float((topyaxis.y - origin.y)) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        Point prev = new Point(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), int(data.getRow(0).getInt(categories[1]) * ratio) + origin.y);
        stroke(0);
        drawCircle(prev.x, prev.y, 10);
        for (int i = 1; i < data.getRowCount(); i++) {
            int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
            int y = int(data.getRow(i).getInt(categories[1]) * ratio) + origin.y;
            line(prev.x, prev.y, x, y);
            prev.setXY(x, y);
            drawCircle(prev.x, prev.y, radius * 2);
        }
    }
};