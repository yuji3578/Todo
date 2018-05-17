package com.example.yu.todo.flagments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.yu.todo.activities.CreateActivity;
import com.example.yu.todo.utils.DateTimeToString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 時刻を選択するダイアログ
 */
public class CustomTimePickerDialogFragment extends DialogFragment {

    /**
     * 時刻ダイアログが表示された場合
     * @param savedInstanceState
     * @return
     */
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // 時刻インスタンスを取得
        final Calendar calendar = Calendar.getInstance();

        // ダイアログ生成  DatePickerDialogのBuilderクラスを指定してインスタンス化します
        TimePickerDialog timeBuilder = new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        // 選択された時刻を成形 ※月は0-11なので+1している
                        String timeText = DateTimeToString.convert(hour) + ":" + DateTimeToString.convert(minute);

                        // CreateActivityのインスタンスを取得
                        CreateActivity createActivity = (CreateActivity) getActivity();
                        createActivity.setTimeTextView(timeText);
                    }
                },

                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),false);

        // timeBuilderを返す
        return timeBuilder;
    }
}