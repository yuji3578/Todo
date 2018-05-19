package com.example.yu.todo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 時刻をString型に変換するDateFormat
 */
public class CustomTimeFormat {

    /**
     * 時刻のSimpleDateFormat
     */
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

    /**
     * 時刻をString型に変換する
     * @param date
     * @return
     */
    public static String convert(Date date){
        return  simpleDateFormat.format(date);
    }
}
