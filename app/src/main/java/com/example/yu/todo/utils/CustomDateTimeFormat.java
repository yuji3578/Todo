package com.example.yu.todo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日時をString型に変換するDateFormat
 */
public class CustomDateTimeFormat {

    /**
     * 日時のSimpleDateFormat
     */
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    /**
     * 日時をString型に変換する
     * @param date
     * @return
     */
    public static String convertToString(Date date){
        return  simpleDateFormat.format(date);
    }

    /**
     * String型を日時に変換する
     * @param dateText
     * @return
     */
    public static Date convertToDate(String dateText) throws ParseException {
        return  simpleDateFormat.parse(dateText);
    }
}
