package edu.northeastern.a321habits;

import android.annotation.SuppressLint;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DateUtil {
    public static LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @SuppressLint("DefaultLocale")
    public static String formatDateTime(Date dateTime) {

        Map<Integer, String> monthNames = new HashMap<>();
        monthNames.put(0, "Jan");
        monthNames.put(1, "Feb");
        monthNames.put(2, "Mar");
        monthNames.put(3, "Apr");
        monthNames.put(4, "May");
        monthNames.put(5, "Jun");
        monthNames.put(6, "Jul");
        monthNames.put(7, "Aug");
        monthNames.put(8, "Sep");
        monthNames.put(9, "Oct");
        monthNames.put(10, "Nov");
        monthNames.put(11, "Dec");

        return String.format("%s %02d %02d:%02d", monthNames.get(dateTime.getMonth()),
                dateTime.getDate(), dateTime.getHours(), dateTime.getMinutes());

    }

    public static int getDaysBetween(Date startDate, Date endDate) {
        long duration  = endDate.getTime() - startDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        return (int)days + 1;
    }
}
