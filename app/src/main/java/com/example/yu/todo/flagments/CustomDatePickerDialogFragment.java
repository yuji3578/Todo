package com.example.yu.todo.flagments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import com.example.yu.todo.activities.CreateActivity;
import com.example.yu.todo.utils.DateTimeToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 開催日を選択するダイアログ
 */
public class CustomDatePickerDialogFragment extends DialogFragment {

    /**
     * カレンダーダイアログが表示された場合
     * @param savedInstanceState
     * @return
     */
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // 今日の日付のカレンダーインスタンスを取得
        final Calendar calendar = Calendar.getInstance();

        // ダイアログ生成  DatePickerDialogのBuilderクラスを指定してインスタンス化します
        DatePickerDialog dateBuilder = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 選択された年・月・日を成形 ※月は0-11なので+1している
                        String dateText = year + "/" + DateTimeToString.convert(month + 1) + "/" + DateTimeToString.convert(dayOfMonth);

                        // CreateActivityのインスタンスを取得
                        CreateActivity createActivity = (CreateActivity) getActivity();
                        createActivity.setDateTextView(dateText);
                    }
                },

                calendar.get(Calendar.YEAR), // 初期選択年
                calendar.get(Calendar.MONTH), // 初期選択月
                calendar.get(Calendar.DAY_OF_MONTH) // 初期選択日
        );

        // dateBuilderを返す
        return dateBuilder;
    }
}