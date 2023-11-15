package com.koleff.stockserver.stocks.utils.dateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

    /**
     * Transform ISO 8601 string to Calendar.
     * ISO8601 Date example: "2023-09-27T00:00:00+00:00"
     */
    public static Calendar toCalendar(String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar toCalendar(LocalDateTime localDateTime){
        try {
            return toCalendar(localDateTime.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date toISO8601(LocalDateTime localDateTime){
        // Create a formatter for ISO 8601 date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        // Format the current date and time
        String formattedDateTime = localDateTime.format(formatter);

        try {
            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(formattedDateTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
