package app.utils;

import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.Date;

@Component
public class DateHandler {

    public static Date addMinute(Date date, int min){
        Calendar calAux = Calendar.getInstance();
        calAux.setTime(date);
        calAux.add(Calendar.MINUTE, min);

        return calAux.getTime();
    }
}
