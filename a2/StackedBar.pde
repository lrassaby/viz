public class StackedBar extends AxisChart {
    private ColorGenerator colorgenerator;
    private color[] colors;

    StackedBar(Table data, String[] categories) {
        super(data, categories);
        colorgenerator = new ColorGenerator();
        colors = new color[categories.length - 1];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorgenerator.generate();
        }
    }

    void draw (float transition_completeness, Transition transition) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        color col = color(0, 0, 0);
        drawAxes(col);
        drawLabels(col, true);
        drawData(transition_completeness, transition);
    }


    void drawData (float transition_completeness, Transition transition) {
        float ratio = float(origin.y - topyaxis.y) / superMaxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        strokeWeight(lerp(5, sectionWidth * 0.8, transition_completeness));
        stroke(0);
        strokeCap(SQUARE);

        switch(transition) {
            case NONE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1), y = origin.y;
                    int prevy = origin.y;
                    for (int j = 1; j < categories.length; j++) {
                        y -= int(data.getRow(i).getInt(categories[j]) * ratio);
                        stroke(colors[j - 1]);
                        line(x, prevy, x, y);
                        prevy = y;
                    }
                }
                break;
            case BARTOSTACKED:
            case STACKEDTOBAR:
                break;
        }
        
    }   
};