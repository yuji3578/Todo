package com.example.yu.todo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日付をString型に変換するDateFormat
 */
public class CustomDateFormat {

    /**
     * 日付のSimpleDateFormat
     */
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 日付をString型に変換する
     * @param date
     * @return
     */
    public static String convert(Date date){
        return  simpleDateFormat.format(date);
    }
}
