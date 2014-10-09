import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 

import controlP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lab5 extends PApplet {



final int DECIDE_YOURSELF = -1; // This is a placeholder for variables you will replace.

/**
 * This is a global variable for the dataset in your visualization. You'll be overwriting it each trial.
 */
Data d = null;

public void setup() {
    totalWidth = displayWidth;
    totalHeight = displayHeight;
    chartLeftX = totalWidth / 2.0f - chartSize / 2.0f;
    chartLeftY = totalHeight / 2.0f - chartSize / 2.0f - margin_top;

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
    d = null;

    /**
     ** Finish this: how to generate participant IDs
     ** You can write a short alphanumeric ID generator (cool) or modify this for each participant (less cool).
     **/
    partipantID = DECIDE_YOURSELF;
}

public void draw() {
    textSize(fontSize);
    /**
     ** add more: you may need to draw more stuff on your screen
     **/
    if (index < 0 && page1) {
        drawIntro();
        page1 = false;
    } else if (index >= 0 && index < vis.length) {
        if (index == 0 && page2) {
            clearIntro();
            drawTextField();
            drawInstruction();
            page2 = false;
        }

        /**
         **  Finish this: decide the chart type. You can do this randomly.
         **/
        int chartType = DECIDE_YOURSELF;

        switch (chartType) {
            case -1: // This is a placeholder, you can remove it and use the other cases for the final version
                 stroke(0);
                 strokeWeight(1);
                 fill(255);
                 rectMode(CORNER);
                 /*
                  * all your charts must be inside this rectangle
                  */
                 rect(chartLeftX, chartLeftY, chartSize, chartSize);
                 break;
            case 0:
                /**
                 ** finish this: 1st visualization
                 **/
                break;
            case 1:
                /**
                 ** finish this: 2nd visualization
                 **/
                break;
            case 2:
                /**
                 ** finish this: 3rd visualization
                 **/
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

    } else if (index > vis.length - 1 && pagelast) {
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
        if (index >= 0 && index < vis.length) {
            float ans = parseFloat(cp5.get(Textfield.class, "answer").getText());

            /**
             ** Finish this: decide how to compute the right answer
             **/
            truePerc = DECIDE_YOURSELF; // hint: from your list of DataPoints, extract the two marked ones to calculate the "true" percentage

            reportPerc = ans / 100.0f; // this is the participant's response
            
            /**
             ** Finish this: decide how to compute the log error from Cleveland and McGill (see the handout for details)
             **/
            error = DECIDE_YOURSELF;

            saveJudgement();
        }

        /**
         ** Finish this: decide the dataset (similar to how you did in setup())
         **/
        d = null;

        cp5.get(Textfield.class, "answer").clear();
        index++;

        if (index == vis.length - 1) {
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
class Data {
    class DataPoint {
        private float value = -1;
        private boolean marked = false;

        DataPoint(float f, boolean m) {
            this.value = f;
            this.marked = m;
        }

        public boolean isMarked() {
            return marked;
        }

        public void setMark(boolean b) {
            this.marked = b;
        }

        public float getValue() {
            return this.value;
        }
    }

    private DataPoint[] data = null;
    private final int MARKED1 = 4;
    private final int MARKED2 = 9;

    Data() {
        // NUM is a global varibale in support.pde
        data = new DataPoint[NUM];

        for (int i = 0; i < NUM; i++) {
            data[i].value = random(0, 100.0f);
            if (i == MARKED1 || i == MARKED2)
                data[i].marked = true;
            else
                data[i].marked = false;
        }
    }

    public float getValueAt(int i) {
        return data[i].value;
    }

    public boolean getMarkAt(int i) {
        return data[i].isMarked();
    }

}
/**
 * These five variables are the data you need to collect from participants.
 */
int partipantID = -1;
int index = -1;
float error = -1;
float truePerc = -1;
float reportPerc = -1;

/**
 * The table saves information for each judgement as a row.
 */
Table expData = null;

/**
 * The visualizations you need to plug in.
 * You can change the name, order, and number of elements in this array.
 */

String[] vis = {
    "BarChart", "PieChart", "StackedBarChart", "TreeMap", "LineChart"
};

/**
 * add the data for this judgement from the participant to the table.
 */ 
public void saveJudgement() {
    if (expData == null) {
        expData = new Table();
        expData.addColumn("PartipantID");
        expData.addColumn("Index");
        expData.addColumn("Vis");
        expData.addColumn("VisID");
        expData.addColumn("Error");
        expData.addColumn("TruePerc");
        expData.addColumn("ReportPerc");
    }

    TableRow newRow = expData.addRow();
    newRow.setInt("PartipantID", partipantID);
    newRow.setInt("Index", index);

    /**
     ** finish this: decide the current visualization
     **/
    newRow.setString("Vis", "" + DECIDE_YOURSELF);

    /**
     ** finish this: decide current vis id
     **/
    newRow.setInt("VisID", DECIDE_YOURSELF);
    newRow.setFloat("Error", error);
    newRow.setFloat("TruePerc", truePerc);
    newRow.setFloat("ReportPerc", reportPerc);
}

/**
 * Save the table
 * This method is called when the participant reaches the "Thanks" page and hit the "CLOSE" button.
 */
public void saveExpData() {
    /**
     ** Change this if you need 
     **/
    saveTable(expData, "expData.csv");
}
/********************************************************************************************/
/********************************************************************************************/
/********************************************************************************************/
/************************ Don't worry about the code in this file ***************************/
/********************************************************************************************/
/********************************************************************************************/
/********************************************************************************************/

float margin = 50, margin_small = 20, margin_top = 40, chartSize = 300, answerHeight = 100;
float totalWidth = -1, totalHeight = -1;
float chartLeftX = -1, chartLeftY = -1;
int NUM = 10;

int fontSize = 14, fontSizeBig = 20;
int textFieldWidth = 200, textFieldHeight = 30;
int buttonWidth = 60;
int totalMenuWidth = textFieldWidth + buttonWidth + (int) margin_small;

String warning = null;

ControlP5 cp5 = null;
Textarea myTextarea = null;
PFont pfont = null; 
boolean page1 = false, page2 = false, pagelast = false;

public void drawWarning() {
    fill(255);
    noStroke();
    rectMode(CORNER);
    rect(0, totalHeight / 2.0f + chartSize, totalWidth, fontSize * 3);
    if (warning != null) {
        fill(color(255, 0, 0));
        textSize(fontSize);
        textAlign(LEFT);
        text(warning, totalWidth / 2.0f - chartSize / 2.0f, 
        totalHeight / 2.0f + chartSize + fontSize * 1.5f);
    }
}

public void drawInstruction() {
    fill(0);
    textAlign(CENTER);
    textSize(fontSize);
    text("Two values are marked with dots. \n " 
      + "What percentage is the smaller of the larger? \n" 
      + "Please put your answer below. \n" 
      + "e.g. If you think the smaller is exactly a half of the larger, \n" 
      + "please input \"50\"."
      , totalWidth / 2.0f, totalHeight / 2.0f + chartSize / 2.0f);
}

public void clearInstruction() {
    fill(255);
    noStroke();
    rectMode(CORNER);
    rect(0, chartSize, totalWidth, margin);
}

public void drawTextField() {
    cp5.addTextfield("answer")
        .setPosition(totalWidth / 2.0f - chartSize / 2.0f, totalHeight / 2.0f + chartSize / 2.0f + margin * 2)
        .setSize(textFieldWidth, textFieldHeight)
        .setColorCaptionLabel(color(0, 0, 0))
        .setFont(createFont("arial", 14))
        .setAutoClear(true);

    cp5.addBang("next")
        .setPosition(totalWidth / 2.0f + chartSize / 2.0f - buttonWidth, totalHeight / 2.0f + chartSize / 2.0f + margin * 2)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);
}

public void drawIntro() {
    fill(0);
    textSize(fontSizeBig);
    textAlign(CENTER);
    text("In this experiment, \n" 
          + "you are asked to judge \n" 
          + "ratios between graphical elements " 
          + "in serveral charts. \n\n" 
          + "We won't record any other information from you except your answers.\n" 
          + "Click the \"agree\" button to begin. \n\n" 
          + "Thank you!", totalWidth / 2.0f, chartLeftY + chartSize / 4.0f);

    cp5.addBang("agree")
        .setPosition(totalWidth / 2.0f + margin * 2, totalHeight / 2.0f + chartSize / 2.0f)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);

    cp5.addBang("disagree")
        .setPosition(totalWidth / 2.0f - margin * 3, totalHeight / 2.0f + chartSize / 2.0f)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);
}

public void clearIntro() {
    background(color(255));
    cp5.get("agree").remove();
    cp5.get("disagree").remove();
}

public void agree() {
    index++;
    page2 = true;
}

public void disagree() {
    exit();
}

public void mouseMoved() {
    warning = null;
}

public void drawThanks() {
    background(255, 255, 255);
    fill(0);
    textSize(60);
    cp5.get(Textfield.class, "answer").remove();
    cp5.get("next").remove();
    textAlign(CENTER);
    text("Thanks!", totalWidth / 2.0f, totalHeight / 2.0f);
}

public void drawClose() {
    cp5.addBang("close")
        .setPosition(totalWidth / 2.0f - buttonWidth / 2.0f, totalHeight / 2.0f + margin_top + margin)
        .setSize(buttonWidth, textFieldHeight)
        .getCaptionLabel()
        .align(ControlP5.CENTER, ControlP5.CENTER);
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lab5" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
