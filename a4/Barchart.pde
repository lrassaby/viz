public class Barchart extends AxisChart {
    Barchart(Table data, String[] categories, int[] margins) {
        super(data, categories, margins);
    }

    void draw (float transition_completeness, int transition, int[] selected) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        float c = 0;
        color col = color(c, c, c);
        drawAxes(col);
        drawLabels(col, float(origin.y - topyaxis.y) / maxY);
        drawData(transition_completeness, transition, selected);
    }


    void drawData (float transition_completeness, int transition, int[] selected) {
        float ratio = float(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / categories.length));
        strokeWeight(serp(5, sectionWidth * 0.7, transition_completeness));
        stroke(5, 112, 204, 255);
        strokeCap(SQUARE);

        switch(transition) {
            case 0:
                for (int i = 0; i < categories.length; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(0).getInt(categories[i]) * ratio);
                    selected[i] = selected[i] == 1 ? 1 : int(mouseX >= x - sectionWidth/2 && mouseX <= x + sectionWidth/2 && mouseY >= y && mouseY <= origin.y);
                    if (selected[i] == 1) {stroke(13, 134, 90, 255);}
                    else {stroke(5, 112, 204, 255);}
                    line(x, origin.y, x, y);
                    intersections[2*i] = x;
                    intersections[2*i + 1] = y;
                }
                break;
        }
        
    }   
};