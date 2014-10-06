public class ThemeRiver extends AxisChart {
   private ColorGenerator colorgenerator;
   private color[] colors;
    ThemeRiver(Table data, String[] categories) {
        super(data, categories);
        colorgenerator = new ColorGenerator();
        colors = new color[categories.length-1];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorgenerator.generate();
        }
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
               
                for (int j = categories.length-1; j > 0; j--) {
                  fill(colors[j-1]);
                  prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1),origin.y - int(data.getRow(0).getInt(categories[j]) * ratio));

                  if(j > 1) {
                    for (int k = j-1; k >= 1; k--) {
                        prev.y -= int(data.getRow(0).getInt(categories[k]) * ratio);
                    }
                  }
                  
                  beginShape();
                  curveVertex(prev.x, prev.y);
                  curveVertex(prev.x, prev.y);
                  int x = 0;
                  int y = 0;
                  int origY = 0;
                  for (int i = 0; i < data.getRowCount(); i++) {
                      x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                      y = origin.y - int(data.getRow(i).getInt(categories[j]) * ratio);
                      if(j > 1) {
                        for (int k = j-1; k >= 1; k--) {
                          y -= int(data.getRow(i).getInt(categories[k]) * ratio);
                        }
                      }
                      if (i == 0) {
                        origY = y;
                      }
                      curveVertex(x, y);
                      prev.setXY(x, y);
                  }
                  curveVertex(x,y);
                  strokeWeight(1);
                  vertex(origin.x + sectionWidth * (data.getRowCount()-1) + sectionWidth / 2 + int(sectionWidth * 0.1), origin.y);
                  vertex(origin.x + sectionWidth / 2, origin.y);
                  vertex(origin.x + sectionWidth / 2, origY);
                  //strokeWeight(2);
                  endShape();
                  if (j > 1)
                    prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), origin.y - int(data.getRow(0).getInt(categories[j-1]) * ratio));

                }
                
                break;
            case LINETORIVER:
                ratio = float(origin.y - topyaxis.y) / serp(maxY, superMaxY, transition_completeness);

                prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), origin.y - int(data.getRow(0).getInt(categories[1]) * ratio));
                noFill();
                beginShape();
                curveVertex(prev.x, prev.y);
                curveVertex(prev.x, prev.y);
                int x = 0;
                int y = 0;
                for (int i = 0; i < data.getRowCount(); i++) {
                      x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                      y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                      curveVertex(x, y);
                      prev.setXY(x, y);
                }
                curveVertex(x, y);
                endShape();
                for (int j = 2; j < categories.length; j++) {
                  prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), origin.y - int(data.getRow(0).getInt(categories[j]) * ratio));
                  for (int k = j-1; k >= 1; k--) {
                     prev.y -= int(data.getRow(0).getInt(categories[k]) * ratio);
                  }
                  noFill();
                  beginShape();
                  curveVertex(prev.x, serp(origin.y - int(data.getRow(0).getInt(categories[1]) * ratio), origin.y - int(data.getRow(0).getInt(categories[j]) * ratio), transition_completeness));
                  curveVertex(prev.x, serp(origin.y - int(data.getRow(0).getInt(categories[1]) * ratio), origin.y - int(data.getRow(0).getInt(categories[j]) * ratio),transition_completeness));
                  for (int i = 0; i < data.getRowCount(); i++) {
                      x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                      y = origin.y - int(data.getRow(i).getInt(categories[j]) * ratio);
                      if(j > 1) {
                        for (int k = j-1; k >= 1; k--) {
                          y -= int(data.getRow(i).getInt(categories[k]) * ratio);
                        }
                      }
                      curveVertex(x, serp(int(data.getRow(i).getInt(categories[1]) * ratio), y, transition_completeness));
                      prev.setXY(x, y);
                  }
                  curveVertex(x, y);
                  endShape();
                  if (j < categories.length-1)
                    prev.setXY(origin.x + sectionWidth / 2 + int(sectionWidth * 0.1), int(data.getRow(0).getInt(categories[j+1]) * ratio));

                }

                break;
        }
    }
};
