import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import controlP5.*; 
import java.awt.event.WindowAdapter; 
import java.awt.event.WindowEvent; 
import java.sql.ResultSet; 

import com.mysql.jdbc.exceptions.*; 
import com.mysql.jdbc.log.*; 
import com.mysql.jdbc.integration.c3p0.*; 
import org.gjt.mm.mysql.*; 
import com.mysql.jdbc.authentication.*; 
import com.mysql.fabric.xmlrpc.base.*; 
import com.mysql.jdbc.integration.jboss.*; 
import com.mysql.jdbc.interceptors.*; 
import com.mysql.jdbc.profiler.*; 
import com.mysql.fabric.*; 
import com.mysql.jdbc.exceptions.jdbc4.*; 
import com.mysql.jdbc.jdbc2.optional.*; 
import com.mysql.jdbc.*; 
import com.mysql.jdbc.util.*; 
import com.mysql.fabric.xmlrpc.*; 
import controlP5.*; 
import com.mysql.fabric.hibernate.*; 
import com.mysql.fabric.jdbc.*; 
import com.mysql.fabric.xmlrpc.exceptions.*; 
import com.mysql.fabric.proto.xmlrpc.*; 
import com.mysql.jdbc.jmx.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lab6 extends PApplet {


/**********************************************************************************************
 ********************************Don't worry about the code in this file***********************
 ********************************Don't worry about the code in this file***********************
 ********************************Don't worry about the code in this file***********************
 **********************************************************************************************/






ControlP5 cp5;
CheckBox checkboxMon;
CheckBox checkboxDay;
Range rangeTemp;
Range rangeHumidity;
Range rangeWind;
controlP5.Button bSubmit;
controlP5.Button bClose;

int monSize = MIN_INT;
int daySize = MIN_INT;
int margin = MIN_INT;
int textSize = 12;
int textSizeSmall = 10;
int mouseOnColor = color(158,202,225);
int mouseOnWarningColor = color(251,106,74);
int notcheckColor = color(210);
int checkedColor = color(54, 144, 192);
int labelColor = color(0);

String[] months = {
    "jan", "feb", "mar", "apr", "may", "jun",
    "jul", "aug", "sep", "oct", "nov", "dec"
};

String[] days = {
    "mon", "tue", "wed", "thu", "fri", "sat", "sun"
};

boolean queryReady = false;
boolean interfaceReady = false;

public void setup() {
    size(PApplet.parseInt(displayWidth * 0.8f), PApplet.parseInt(displayHeight * 0.8f));
    background(245);
    canvasWidth = height;
    monSize = (width - height) / 14;
    daySize = (width - height) / 17;
    margin = PApplet.parseInt((height * 0.0425f));
    
    cp5 = new ControlP5(this);
    rectMode(CORNER);
    ellipseMode(CORNER);
    textAlign(CENTER);
    textSize(textSize);

    //smooth();
    initSetting();

    drawMonth();
    drawDay();

    drawRangeTemp();
    drawRangeHumidity();
    drawRangeWind();
    drawButton();
    drawLegend();

    interfaceReady = true;

    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent we) {
            closeAll();
        }
    });
}

public void clearCanvas() {
    fill(255);
    noStroke();
    rect(0, 0, canvasWidth, canvasWidth);
}

public void drawLegend() {
    textSize(textSize);
    noStroke();
    fill(checkedColor);
    ellipse(height + 3 * margin, margin - 0.015f * height / 2, 0.015f * height, 0.015f * height);
    text("check", height + 4.5f * margin, margin + textSize / 3);

    fill(notcheckColor);
    ellipse(height + 9 * margin, margin - 0.015f * height / 2, 0.015f * height, 0.015f * height);
    text("not check", height + 11 * margin, margin + textSize / 3);
}

public void drawMonth() {
    stroke(labelColor);
    fill(labelColor);
    line(height + monSize, 2 * margin,
        height + 6 * monSize, 2 * margin);
    line(height + 8.35f * monSize, 2 * margin,
        height + 13.35f * monSize, 2 * margin);
    text("Month", height + 7.15f * monSize, 2 * margin + textSize / 2);

    checkboxMon = cp5.addCheckBox("checkboxMon")
        .setPosition(height + margin, 3 * margin)
        .setColorForeground(mouseOnColor)
        .setColorActive(checkedColor)
        .setColorLabel(labelColor)
        .setColorBackground(notcheckColor)
        .setSize(monSize, monSize)
        .setItemsPerRow(6)
        .setSpacingColumn((int)(1.2f * monSize))
        .setSpacingRow((int)(monSize * 0.9f))
        .addItem(months[0], 1)
        .addItem(months[1], 2)
        .addItem(months[2], 3)
        .addItem(months[3], 4)
        .addItem(months[4], 5)
        .addItem(months[5], 6)
        .addItem(months[6], 7)
        .addItem(months[7], 8)
        .addItem(months[8], 9)
        .addItem(months[9], 10)
        .addItem(months[10], 11)
        .addItem(months[11], 12);
    checkboxMon.activateAll();
}

public void drawDay() {
    stroke(labelColor);
    fill(labelColor);
    line(height + monSize, 8 * margin + margin / 2,
        height + 6 * monSize, 8 * margin + margin / 2);
    line(height + 8.35f * monSize, 8 * margin + margin / 2,
        height + 13.35f * monSize, 8 * margin + margin / 2);
    text("Day", height + 7.25f * monSize, 8 * margin + margin / 2 + textSize / 2);

    checkboxDay = cp5.addCheckBox("checkboxDay")
        .setPosition(height + margin, 9 * margin)
        .setColorForeground(mouseOnColor)
        .setColorActive(checkedColor)
        .setColorLabel(labelColor)
        .setColorBackground(notcheckColor)
        .setSize(daySize, daySize)
        .setItemsPerRow(7)
        .setSpacingColumn(monSize)
        .setSpacingRow(daySize)
        .addItem(days[0], 1)
        .addItem(days[1], 2)
        .addItem(days[2], 3)
        .addItem(days[3], 4)
        .addItem(days[4], 5)
        .addItem(days[5], 6)
        .addItem(days[6], 7);
    checkboxDay.activateAll();
}

public void drawRangeTemp() {
    stroke(labelColor);
    fill(labelColor);
    line(height + 1 * monSize, 12.5f * margin,
        height + 6 * monSize, 12.5f * margin);
    line(height + 8.35f * monSize, 12.5f * margin,
        height + 13.25f * monSize, 12.5f * margin);
    text("Indice", height + 7.35f * monSize, 12.5f * margin + textSize / 2);

    rangeTemp = cp5.addRange("Temp")
        // disable broadcasting since setRange and setRangeValues will trigger an event
        .setBroadcast(false)
        .setPosition(height + margin, 13 * margin + margin / 2)
        .setSize(PApplet.parseInt(((width - height) * 0.8f)), PApplet.parseInt((height * 0.035f)))
        .setHandleSize(5)
        .setRange(rangeTempValue[0], rangeTempValue[1])
        .setRangeValues(rangeTempValue[0], rangeTempValue[1]) // wait for data
        // after the initialization we turn broadcast back on again
        .setBroadcast(true)
        .setColorActive(mouseOnColor)
        .setColorBackground(notcheckColor)
        .setColorForeground(checkedColor)
        .setColorLabel(labelColor);
}

public void drawRangeHumidity() {
    rangeHumidity = cp5.addRange("Humidity")
        // disable broadcasting since setRange and setRangeValues will trigger an event
        .setBroadcast(false)
        .setPosition(height + margin, 16 * margin)
        .setSize((int)((width - height) * 0.8f), (int)(height * 0.035f))
        .setHandleSize(5)
        .setRange(rangeHumidityValue[0], rangeHumidityValue[1])
        .setRangeValues(rangeHumidityValue[0], rangeHumidityValue[1]) // wait for data
        // after the initialization we turn broadcast back on again
        .setBroadcast(true)
        .setColorActive(mouseOnColor)
        .setColorBackground(notcheckColor)
        .setColorForeground(checkedColor)
        .setColorLabel(labelColor);

}

public void drawRangeWind() {
    rangeWind = cp5.addRange("Wind")
        // disable broadcasting since setRange and setRangeValues will trigger an event
        .setBroadcast(false)
        .setPosition(height + margin,
            18 * margin + margin / 2)
        .setSize((int)((width - height) * 0.8f), (int)(height * 0.035f))
        .setHandleSize(5)
        .setRange(rangeWindValue[0], rangeWindValue[1])
        .setRangeValues(rangeWindValue[0], rangeWindValue[1]) // wait for data
        // after the initialization we turn broadcast back on again
        .setBroadcast(true)
        .setColorActive(mouseOnColor)
        .setColorBackground(notcheckColor)
        .setColorForeground(checkedColor)
        .setColorLabel(labelColor);
}

public void drawButton() {
    stroke(labelColor);
    line(height + 1 * monSize, 20.5f * margin,
        height + 13.35f * monSize, 20.5f * margin);

    bClose = cp5.addButton("Close")
        .setPosition(height + 5 * margin, 21 * margin + margin / 4)
        .setColorBackground(notcheckColor)
        .setColorActive(checkedColor)
        .setColorForeground(mouseOnWarningColor)
        .setSize(PApplet.parseInt((0.3f * (width - height))), PApplet.parseInt((height * 0.05f)))
        .updateSize();
}

public void destroy() {
    closeAll();
}

public void stop() {
    closeAll();
}
int canvasWidth = MIN_INT; // this would be initialized in setup
ArrayList<float[]> dataSet = new ArrayList<float[]>();

public void draw() {
  clearCanvas();
  float margin = canvasWidth * .2f;
  strokeWeight(1);
  stroke(0, 0, 0);
  line(margin, canvasWidth - margin, canvasWidth - margin, canvasWidth - margin);
  line(margin, margin, margin, canvasWidth - margin);

  for (int i = 0; i < dataSet.size(); i++) {
  	   float[] newPoint = dataSet.get(i);
  	   fill(150);
  	   ellipse((canvasWidth - margin - (newPoint[0]/maxPoint[0]) * (canvasWidth - 2 * margin)), (canvasWidth - margin - (newPoint[1]/maxPoint[1]) * (canvasWidth - 2 * margin)), 7, 7);
  }
}


String table_name = "forestfire";

/**
 * @author: Fumeng Yang
 * @since: 2014
 * we handle the events from the interface for you
 */

public void controlEvent(ControlEvent theEvent) {
    if (interfaceReady) {
        if (theEvent.isFrom("checkboxMon") ||
            theEvent.isFrom("checkboxDay")) {
            submitQuery();
        }
        if (theEvent.isFrom("Temp") ||
            theEvent.isFrom("Humidity") ||
            theEvent.isFrom("Wind")) {
            queryReady = true;
        }

        if (theEvent.isFrom("Close")) {
            closeAll();
        }
    }
}

/**
 * generate and submit a query when mouse is released.
 * don't worry about this method
 */
public void mouseReleased() {
    if (queryReady == true) {
        submitQuery();
        queryReady = false;
    }
}

public void submitQuery() {
    /**
     ** Finish this
     **/

    /** abstract information from the interface and generate a SQL
     ** use int checkboxMon.getItems().size() to get the number of items in checkboxMon
     ** use boolean checkboxMon.getState(index) to check if an item is checked
     ** use String checkboxMon.getState(index).getName() to get the name of an item
     **
     ** checkboxDay (Mon-Sun) is similar with checkboxMon
     **/
    String where = "Where ";
    for (int i = 0; i < checkboxMon.getItems().size(); i++) {
      String name = checkboxMon.getItem(i).getName();
      if(!checkboxMon.getState(i)) {
        if (where != "Where "){
          where += "AND ";
        }
        where += "month != '" + name + "' ";
      }
    }
    
    for (int i = 0; i < checkboxDay.getItems().size(); i++) {
      String name = checkboxDay.getItem(i).getName();
      if(!checkboxDay.getState(i)) {
        if (where != "Where "){
          where += "AND ";
        }
        where += "day != '" + name + "' ";
      }
    }


    /** use getHighValue() to get the upper value of the current selected interval
     ** use getLowValue() to get the lower value
     **
     ** rangeHumidity and rangeWind are similar with rangeTemp
     **/
    float maxTemp = rangeTemp.getHighValue();
    float minTemp = rangeTemp.getLowValue();
    if (where == "Where "){
      where += "temp > " + minTemp + " AND temp < " + maxTemp + " ";
    } else {
      where += "AND temp > " + minTemp + " AND temp < " + maxTemp + " ";
    }
    
    float maxHumidity = rangeHumidity.getHighValue();
    float minHumidity = rangeHumidity.getLowValue();
    where += "AND humidity > " + minHumidity + " AND humidity < " + maxHumidity + " ";
    
    float maxWind = rangeWind.getHighValue();
    float minWind = rangeWind.getLowValue();
    where += "AND wind > " + minWind + " AND wind < " + maxWind + " ";

    /** Finish this
     **
     ** finish the sql
     ** do read information from the ResultSet
     **/
    String sql = "Select x, y from forestfire";
    
    if (where != "Where "){
      sql += " " + where;
    }
    
    ResultSet rs = null;
    dataSet = new ArrayList<float[]>();
    
    try {
        // submit the sql query and get a ResultSet from the dataSetbase
       rs  = (ResultSet) DBHandler.exeQuery(sql);
       while (rs.next()) {
         float xval = rs.getFloat("x");
         float yval = rs.getFloat("y");
         float[] dataSetpt = {xval, yval};
         dataSet.add(dataSetpt);
       }

    } catch (Exception e) {
        // should be a java.lang.NullPointerException here when rs is empty
        e.printStackTrace();
    } finally {
        closeThisResultSet(rs);
    }
}

public void closeThisResultSet(ResultSet rs) {
    if(rs == null){
        return;
    }
    try {
        rs.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void closeAll() {
    DBHandler.closeConnection();
    frame.dispose();
    exit();
}
/** the first element of is the min value of the column
 ** the second element of is the max value of the column
 **/

float[] rangeTempValue = {0, 1};  // slider of temp 
float[] rangeHumidityValue = {0, 1}; // slider of humidity 
float[] rangeWindValue = {0, 1}; // slider of wind 
float[] maxPoint = {0, 1}; //range of points

/**
 * this function is called before initializing the interface
 */
public void initSetting() {

    ResultSet rs = null;
     
    try {
        // submit the sql query and get a ResultSet from the database
    String sql = "select max(x), max(y), max(temp), max(wind), max(humidity), min(temp), min(wind), min(humidity) from forestfire";
    rs = (ResultSet) DBHandler.exeQuery(sql);
    while(rs.next()) {
        float minTemp = rs.getFloat("min(temp)");
        float maxTemp = rs.getFloat("max(temp)");
        float minHumidity = rs.getFloat("min(humidity)");
        float maxHumidity = rs.getFloat("max(humidity)");
        float maxX = rs.getFloat("max(x)");
        float maxY = rs.getFloat("max(y)");
        float minWind = rs.getFloat("min(wind)");
        float maxWind =  rs.getFloat("max(wind)");

        rangeTempValue[0] = minTemp;
        rangeTempValue[1] = maxTemp;
        rangeHumidityValue[0] = minHumidity;
        rangeHumidityValue[1] = maxHumidity;
        maxPoint[0] = maxX;
        maxPoint[1] = maxY;
        rangeWindValue[0] = minWind;
        rangeWindValue[1] = maxWind;
    }

    } catch (Exception e) {
        // should be a java.lang.NullPointerException here when rs is empty
        e.printStackTrace();
    } finally {
        closeThisResultSet(rs);
    }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lab6" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
