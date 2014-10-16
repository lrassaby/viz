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

    String where_clause = "where (";
    boolean selectedMon = false;
    boolean selectedDay = false;
    for (int i = 0; i < 12; i++) {
        if (checkboxMon.getState(i)) {
            selectedMon = true;
            where_clause += "month = '" + checkboxMon.getItem(i).getName() + "' or ";
        }
    }

    where_clause = where_clause.substring(0, where_clause.length() - 4);
    where_clause += ") and (";

    for (int i = 0; i < 7; i++) {
        if (checkboxDay.getState(i)) {
            selectedDay = true;
            where_clause += "day = '" + checkboxDay.getItem(i).getName() + "' or ";
        }
    }

    where_clause = where_clause.substring(0, where_clause.length() - 4);
    where_clause += ");";

    String sql = "select * from forestfire\n";
    if (selectedMon && selectedDay) {
        sql += where_clause;
    }  
    else 
        sql = null; 

    println(sql + "\n");

    println("the " + checkboxMon.getItem(0).getName() + " is " + checkboxMon.getState(0));


    /** use getHighValue() to get the upper value of the current selected interval
     ** use getLowValue() to get the lower value
     **
     ** rangeHumidity and rangeWind are similar with rangeTemp
     **/
    float maxTemp = rangeTemp.getHighValue();
    float minTemp = rangeTemp.getLowValue();

    /** Finish this
     **
     ** finish the sql
     ** do read information from the ResultSet
     **/
    ResultSet rs = null;

    try {
        // submit the sql query and get a ResultSet from the database
       rs  = (ResultSet) DBHandler.exeQuery(sql);

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