package com.example.yu.todo.utils;

/**
 * 日付と数字が1桁だった場合に0を付ける
 */
public class DateTimeToString {

    /**
     * 日付と数字が1桁だった場合に0を付ける
     * @param num
     * @return
     */
    public static String convert(int num){
        String text = String.valueOf(num);
        String result;

        if(text.length() == 1){
            result = "0" + text;
        } else {
            result = text;
        }

        return result;
    }
}
