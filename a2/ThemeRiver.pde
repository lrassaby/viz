public class ThemeRiver extends AxisChart {
    private ColorGenerator colorgenerator;
    private color[] colors;
    ThemeRiver(Table data, String[] categories) {
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
        float c = 0;
        switch (transition) {
        case NONE:
        case LINETOBAR:
        case BARTOLINE:
            c = 0;
            break;
        case LINETOPIE:
        case PIETOLINE:
            c = serp(255, 0, transition_completeness);
            break;
        }
        color col = color(c, c, c);
        drawAxes(col);
        drawLabels(col, serp(float(origin.y - topyaxis.y) / maxY, 150, transition_completeness));
        drawData(transition_completeness, transition);
    }

    void drawData (float transition_completeness, Transition transition) {
        strokeWeight(2);
        float ratio = float(origin.y - topyaxis.y) / superMaxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / data.getRowCount()));
        Point prev = new Point(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), origin.y - int(data.getRow(0).getInt(categories[1]) * ratio));
        stroke(0);
        fill(0);

        switch (transition) {
        case NONE:
            int middle = (origin.y + topyaxis.y) / 2;
            int[] startYs = new int[data.getRowCount()];
            int colTotal = 0;
            //bottom line
            noFill();
            beginShape();
            for (int i = 0; i < data.getRowCount(); i++) {
                colTotal = 0;
                prev.x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                for (int j = 1; j < categories.length; j++) {
                    colTotal += int(data.getRow(i).getInt(categories[j]) * ratio);
                }
                startYs[i] = middle + colTotal / 2; 
                curveVertex(prev.x, middle + colTotal / 2);
                if (i == 0) {
                    curveVertex(prev.x, middle + colTotal / 2);
                }
            }
            curveVertex(prev.x, middle + colTotal / 2);
            endShape();

            //rest of lines
            for (int j = 1; j < categories.length; j++) {
                beginShape();
                int newY = 0;
                for (int i = 0; i < data.getRowCount(); i++) {
                    newY = startYs[i];
                    prev.x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    newY -= int(data.getRow(i).getInt(categories[j]) * ratio);
                    startYs[i] -= int(data.getRow(i).getInt(categories[j]) * ratio);
                   
                    curveVertex(prev.x, newY);
                    if (i == 0) {
                        curveVertex(prev.x, newY);
                    }
                }
                curveVertex(prev.x, newY);
                endShape();
            }

            break;
        case LINETORIVER:
        case RIVERTOLINE:
            middle = (origin.y + topyaxis.y) / 2;
            startYs = new int[data.getRowCount()];
            colTotal = 0;
            int y = 0;
            //bottom line
            noFill();
            beginShape();
            for (int i = 0; i < data.getRowCount(); i++) {
                y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                colTotal = 0;
                prev.x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                for (int j = 1; j < categories.length; j++) {
                    colTotal += int(data.getRow(i).getInt(categories[j]) * ratio);
                }
                startYs[i] = middle + colTotal / 2; 
                curveVertex(prev.x, serp(y, middle + colTotal / 2, transition_completeness));
                if (i == 0) {
                    curveVertex(prev.x, serp(y, middle + colTotal / 2, transition_completeness));
                }
            }
            curveVertex(prev.x, serp(y, middle + colTotal / 2, transition_completeness));
            endShape();

            //rest of lines
            for (int j = 1; j < categories.length; j++) {
                beginShape();
                int newY = 0;
                y = 0;
                for (int i = 0; i < data.getRowCount(); i++) {
                    y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    newY = startYs[i];
                    prev.x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    newY -= int(data.getRow(i).getInt(categories[j]) * ratio);
                    startYs[i] -= int(data.getRow(i).getInt(categories[j]) * ratio);
                   
                    curveVertex(prev.x, serp(y, newY, transition_completeness));
                    if (i == 0) {
                        curveVertex(prev.x, serp(y, newY, transition_completeness));
                    }
                }
                curveVertex(prev.x, serp(y, newY, transition_completeness));
                endShape();
            }


            break;
        }
    }
};
