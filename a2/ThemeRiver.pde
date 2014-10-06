public class ThemeRiver extends AxisChart {
    ThemeRiver(Table data, String[] categories) {
        super(data, categories);
    }

    void draw (float transition_completeness, Transition transition) {
        origin.setXY(margins[0], height - margins[3]);
        topyaxis.setXY(margins[0], margins[1]);
        rightxaxis.setXY(width - margins[2], height - margins[3]);
        float c = 0;
        switch(transition) {
            case NONE:
            case LINETOBAR:
            case BARTOLINE:
                c = 0;
                break;
            case LINETOPIE:
            case PIETOLINE:
                c = lerp(255, 0, transition_completeness);
                break;
        }
        color col = color(c, c, c);
        drawAxes(col);

                  drawLabels(col, serp(float(origin.y - topyaxis.y) / maxY, float(origin.y - topyaxis.y) / superMaxY, transition_completeness));

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

                for (int j = 1; j < categories.length; j++) {
                  noFill();
                  beginShape();
                  curveVertex(prev.x, prev.y);
                  curveVertex(prev.x, prev.y);
                  for (int i = 1; i < data.getRowCount(); i++) {
                      int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                      int y = origin.y - int(data.getRow(i).getInt(categories[j]) * ratio);
                      curveVertex(x, y);
                      prev.setXY(x, y);
                  }
                  endShape();
                  if (j < categories.length-1)
                    prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), int(data.getRow(0).getInt(categories[j+1]) * ratio));

                }
                break;
            case LINETORIVER:
                ratio = float(origin.y - topyaxis.y) / serp(maxY, superMaxY, transition_completeness);

                prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), int(data.getRow(0).getInt(categories[1]) * ratio));
                noFill();
                beginShape();
                curveVertex(prev.x, prev.y);
                curveVertex(prev.x, prev.y);
                for (int i = 1; i < data.getRowCount(); i++) {
                      int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                      int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                      curveVertex(x, y);
                      prev.setXY(x, y);
                }
                endShape();
                prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), int(data.getRow(0).getInt(categories[1]) * ratio));
                for (int j = 2; j < categories.length; j++) {
                  noFill();
                  beginShape();
                  curveVertex(prev.x, serp(int(data.getRow(0).getInt(categories[1]) * ratio), int(data.getRow(1).getInt(categories[j]) * ratio), transition_completeness));
                  curveVertex(prev.x, serp(int(data.getRow(0).getInt(categories[1]) * ratio), int(data.getRow(1).getInt(categories[j]) * ratio),transition_completeness));
                  for (int i = 0; i < data.getRowCount(); i++) {
                      int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                      int y = origin.y - int(data.getRow(i).getInt(categories[j]) * ratio);
                      curveVertex(x, serp(int(data.getRow(i).getInt(categories[1]) * ratio), y, 1-transition_completeness));
                      prev.setXY(x, y);
                  }
                  endShape();
                  if (j < categories.length-1)
                    prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), int(data.getRow(0).getInt(categories[j+1]) * ratio));

                }

                break;
        }
    }
};
