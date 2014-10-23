import java.sql.ResultSet;

String table_name = "forestfire";

/**
 * @author: Fumeng Yang
 * @since: 2014
 * we handle the events from the interface for you
 */

void controlEvent(ControlEvent theEvent) {
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
void mouseReleased() {
    if (queryReady == true) {
        submitQuery();
        queryReady = false;
    }
}

void submitQuery() {
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

void closeThisResultSet(ResultSet rs) {
    if(rs == null){
        return;
    }
    try {
        rs.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

void closeAll() {
    DBHandler.closeConnection();
    frame.dispose();
    exit();
}