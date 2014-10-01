public class Linechart extends AxisChart {
    Linechart(Table data, String[] categories) {
        super(data, categories);
    }

    void draw (float transition_completeness, Transition transition) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        drawAxes();
        drawLabels();
        drawData(transition_completeness, transition);
    }

    void drawData (float transition_completeness, Transition transition) {
        strokeWeight(2);
        float ratio = float(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        Point prev = new Point(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), origin.y - int(data.getRow(0).getInt(categories[1]) * ratio));
        stroke(0);
        switch (transition) {
            case NONE:
                drawCircle(prev.x, prev.y, 10);
                for (int i = 1; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(prev.x, prev.y, x, y);
                    prev.setXY(x, y);
                    drawCircle(prev.x, prev.y, 10);
                }
                break;
            case LINETOBAR:
            case BARTOLINE:
                drawCircle(prev.x, prev.y, lerp(0, 10, transition_completeness));
                for (int i = 1; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(prev.x, prev.y, lerp(prev.x, x, transition_completeness), lerp(prev.y, y, transition_completeness));
                    prev.setXY(x, y);
                    drawCircle(prev.x, prev.y, lerp(2, 10, transition_completeness));
                }
                break;
        }
    }
};