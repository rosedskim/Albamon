package javaproject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CurrentTime {

   public static String getCurrentTime()
   {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
   }
}