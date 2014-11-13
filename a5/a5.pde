import controlP5.*;

final int DECIDE_YOURSELF = -1; // This is a placeholder for variables you will replace.
final int NUM_TRIALS = 20;
/**
 * This is a global variable for the dataset in your visualization. You'll be overwriting it each trial.
 */
Data d = null;
Table table = null;
Barchart barchart = null;
Piechart piechart = null;
Tree tree = null;
int chartType = int(random(0.0, 2.99));
//int chartType = 1;
int marked1;
int marked2;

void setup() {
    totalWidth = displayWidth;
    totalHeight = displayHeight;
    chartLeftX = totalWidth / 2.0 - chartSize / 2.0;
    chartLeftY = totalHeight / 2.0 - chartSize / 2.0 - margin_top;

    size((int) totalWidth, (int) totalHeight);
    //if you have a Retina display, use the line below (looks better)
    //size((int) totalWidth, (int) totalHeight, "processing.core.PGraphicsRetina2D");
    
    background(255);
    frame.setTitle("Comp150-07 Visualization, Lab 5, Experiment");

    cp5 = new ControlP5(this);
    pfont = createFont("arial", fontSize, true); 
    textFont(pfont);
    page1 = true;

    /**
     ** Finish this: decide how to generate the dataset you are using (see DataGenerator)
     **/
    marked1 = int(random(0.0, NUM - 0.1));
    marked2 = int(random(0.0, NUM - 0.1));
    if (marked2 == marked1) {
      marked2 = 0;
    }
    if (marked2 == marked1) {
      marked2 = 1;
    }
    d = new Data(marked1, marked2);
    
    
    
    /**
     ** Finish this: how to generate participant IDs
     ** You can write a short alphanumeric ID generator (cool) or modify this for each participant (less cool).
     **/
    partipantID = 5;
}

void draw() {
    textSize(fontSize);
    barchart = new Barchart(d, null,chartLeftX, chartLeftY, chartSize);
    piechart = new Piechart(d, null,chartLeftX, chartLeftY, chartSize);
    tree     = new Tree(d);
    /**
     ** add more: you may need to draw more stuff on your screen
     **/
    if (index < 0 && page1) {
        drawIntro();
        page1 = false;
    } else if (index >= 0 && index < vis.length * NUM_TRIALS) {
        if (index == 0 && page2) {
            clearIntro();
            drawTextField();
            drawInstruction();
            page2 = false;
        }

        /**
         **  Finish this: decide the chart type. You can do this randomly.
         **/
        stroke(0);
        strokeWeight(1);
        fill(150);
        rectMode(CORNER);
                 /*
                  * all your charts must be inside this rectangle
                  */
        rect(chartLeftX, chartLeftY, chartSize, chartSize);
        switch (chartType) {
            case 0:
                barchart.draw(1, Transition.NONE);
                break;
            case 1:
                piechart.draw(1, Transition.NONE);
                break;
            case 2:
                tree.draw();
                break;
            case 3:
                /**
                 ** finish this: 4th visualization
                 **/
                break;
            case 4:
                /**
                 ** finish this: 5th visualization
                 **/
                break;
        }

        drawWarning();

    } else if (index > vis.length*NUM_TRIALS - 1 && pagelast) {
        drawThanks();
        drawClose();
        pagelast = false;
    }
}

/**
 * This method is called when the participant clicked the "NEXT" button.
 */
public void next() {
    String str = cp5.get(Textfield.class, "answer").getText().trim();
    float num = parseFloat(str);
    /*
     * We check their percentage input for you.
     */
    if (!(num >= 0)) {
        warning = "Please input a number!";
        if (num < 0) {
            warning = "Please input a non-negative number!";
        }
    } else if (num > 100) {
        warning = "Please input a number between 0 - 100!";
    } else {
        if (index >= 0 && index < vis.length * NUM_TRIALS) {
            float ans = parseFloat(cp5.get(Textfield.class, "answer").getText());

            /**
             ** Finish this: decide how to compute the right answer
             **/
            truePerc = d.getValue(marked1)/d.getValue(marked2); // hint: from your list of DataPoints, extract the two marked ones to calculate the "true" percentage
            if (truePerc > 1) {truePerc = 1/truePerc;}
            truePerc = truePerc * 100.0;
            
            reportPerc = ans; // this is the participant's response
            
            /**
             ** Finish this: decide how to compute the log error from Cleveland and McGill (see the handout for details)
             **/
            float off = 1.0/8.0;
            float ab = abs(reportPerc - truePerc);
            error = log(ab + off)/log(2);
            
            //saveJudgement(truePerc, reportPerc, error, partipantID, chartType);
            saveJudgement();
        }

        /**
         ** Finish this: decide the dataset (similar to how you did in setup())
         **/
        marked1 = int(random(0.0, NUM - 0.1));
        marked2 = int(random(0.0, NUM - 0.1));
        if (marked2 == marked1) {
          marked2 = 0;
        }
        if (marked2 == marked1) {
          marked2 = 1;
        }
        d = new Data(marked1, marked2);

        cp5.get(Textfield.class, "answer").clear();
        index++;
        if ((index) % vis.length == 0 ) {
          chartType = int(random(0.0, 2.99));
        }
        else {
          chartType = (chartType + 1)%(vis.length);
        }

        if (index == vis.length*NUM_TRIALS - 1) {
            pagelast = true;
        }
    }
}

/**
 * This method is called when the participant clicked "CLOSE" button on the "Thanks" page.
 */
public void close() {
    /**
     ** Change this if you need to do some final processing
     **/
    saveExpData();
    exit();
}

/**
 * Calling this method will set everything to the intro page. Use this if you want to run multiple participants without closing Processing (cool!). Make sure you don't overwrite your data.
 */
public void reset(){
    /**
     ** Finish/Use/Change this method if you need 
     **/
    partipantID = DECIDE_YOURSELF;
    d = null;

    /**
     ** Don't worry about the code below
     **/
    background(255);
    cp5.get("close").remove();
    page1 = true;
    page2 = false;
    pagelast = false;
    index = -1;
}
