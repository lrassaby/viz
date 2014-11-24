public class Barchart extends AxisChart {
    boolean marking;
    Barchart(Data data, String[] categories, float chartLeftX, float chartLeftY, float chartSize) {
        super(data, categories, chartLeftX, chartLeftY, chartSize);
        marking = true;
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
            case BARTOPIE:
            case PIETOBAR:
                c = serp(255, 0, transition_completeness);
                break;
        }
        color col = color(c, c, c);
        drawAxes(col);
        drawLabels(col, float(origin.y - topyaxis.y) / maxY);
        drawData(transition_completeness, transition);
    }


    void drawData (float transition_completeness, Transition transition) {
        float ratio = float(origin.y - topyaxis.y) / maxY;
        int sectionWidth = abs(((rightxaxis.x - origin.x) / NUM));
        strokeWeight(serp(5, sectionWidth * 0.8, transition_completeness));
        stroke(0);
        strokeCap(SQUARE);


        switch(transition) {
            case NONE:
                if (frameCount%120 >60) marking = !marking;
                for (int i = 0; i < NUM; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getValue(i) * ratio);
                    
                    //println(marking);
                    if (marking) {
                        if(data.getMark(i)&&marking) {
                            stroke(0,255,0);
                      //strokeWeight(5);
                      //ellipse(x, origin.y - (int(ratio))/2, 12, 12);
                        }
                        else {
                            stroke(0);
                        }
                    } else {
                        stroke(0);
                    }
                    line(x, origin.y, x, y);
                    
                    strokeWeight(serp(5, sectionWidth * 0.8, transition_completeness));

                }
                break;
            case LINETOBAR:
            case BARTOLINE:
                /*for (int i = 0; i < NUM; i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, serp(y, origin.y, transition_completeness), x, y);
                }*/
                break;
            case BARTOPIE:
            case PIETOBAR:
                /*for (int i = 0; i < data.getRowCount(); i++) {
                    int x = origin.x + sectionWidth * i + sectionWidth / 2 + int(sectionWidth * 0.1);
                    int y = origin.y - int(data.getRow(i).getInt(categories[1]) * ratio);
                    line(x, origin.y, x, y);
                }*/
                break;
        }
        
    }   
};
