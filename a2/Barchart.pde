public class Barchart extends AxisChart {
    Barchart(Table data, String[] categories) {
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
        float ratio = float(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        strokeWeight(lerp(5, sectionWidth * 0.8, transition_completeness));
        stroke(0);
        strokeCap(SQUARE);

        switch(transition) {
            case NONE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, origin.y, x, y);
                }
                break;
            case LINETOBAR:
            case BARTOLINE:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, lerp(y, origin.y, transition_completeness), x, y);
                }
                break;
            case BARTOPIE:
            case PIETOBAR:
                for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    if (i == 0) {
                        println(y);
                    }
                    line(x, origin.y, x, y);
                }
                break;
        }
        
    }   
};