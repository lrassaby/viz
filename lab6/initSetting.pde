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
void initSetting() {

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