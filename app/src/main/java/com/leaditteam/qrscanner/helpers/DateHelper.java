package com.leaditteam.qrscanner.helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by leaditteam on 13.04.17.
 */

public class DateHelper {
    private static String[] mSeparated;
    private static String [] hSeparated;
    private static String[] ySeparated;

    public static TimeParser getCurrentTime(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd,HH:mm");
        String formattedDate = df.format(c.getTime());

        return parseTime(formattedDate);
    }
    public static TimeParser parseTime(String s){
try{
        mSeparated = s.split(",");
        hSeparated = mSeparated[1].split(":");
        ySeparated = mSeparated[0].split("\\.");

        String year = (mSeparated[0]);

        int hour =Integer.parseInt( hSeparated[0]) ;
        int min = Integer.parseInt( hSeparated[1]);
        int day = Integer.parseInt(ySeparated[2]);
        int month = Integer.parseInt(ySeparated[1]);

        TimeParser temp_time_parser = new TimeParser(year,day,month,hour,min);

        return temp_time_parser;
    }
catch (Exception e){e.printStackTrace(); return null;}
    }
}
