package com.example.yu.todo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.example.yu.todo.R;
import com.example.yu.todo.flagments.CustomDatePickerDialogFragment;
import com.example.yu.todo.flagments.CustomTimePickerDialogFragment;
import com.example.yu.todo.models.Todo;
import com.example.yu.todo.receivers.AlertReceiver;
import com.example.yu.todo.services.TodoService;
import com.example.yu.todo.utils.CustomDateFormat;
import com.example.yu.todo.utils.CustomDateTimeFormat;
import com.example.yu.todo.utils.CustomTimeFormat;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Todoの編集画面
 */
public class EditActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    /**
     * タイトル
     */
    private TextView titleTextView;

    /**
     * 内容
     */
    private TextView contentTextView;

    /**
     * 「更新」ボタン
     */
    private BootstrapButton updateBtn;

    /**
     * 日付
     */
    private TextView dateTextView;

    /**
     * 日付のダイアログ
     */
    private CustomDatePickerDialogFragment cdpdf;

    /**
     * 「日付を選択する」ボタン
     */
    private BootstrapButton datePickerBtn;

    /**
     * 時刻
     */
    private TextView timeTextView;

    /**
     * 時刻のダイアログ
     */
    private CustomTimePickerDialogFragment ctpdf;

    /**
     * 「時刻を選択する」ボタン
     */
    private BootstrapButton timePickerBtn;

    /**
     * 通知をするかどうかのチェックボックス
     */
    private CheckBox notifyCheck;

    /**
     * チェックボックスによる値
     */
    private int checkBox;

    /**
     * 何分前に通知のスピナー
     */
    private Spinner minutesSpinner;

    /**
     * 選択された何分前か
     */
    private Integer beforeMinutes;

    /**
     * Todo
     */
    private Todo todo;

    /**
     * Todoが格納されているDBと接続するTodoService
     */
    private TodoService todoService;

    /**
     * 初期処理
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // 画面の要素を取得する
        findViews();

        // リスナーをセットする
        setListeners();

        // Realmを取得する
        todoService = new TodoService(this);

        // TodoListの一覧画面かTodoらタイトルを受け取る
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        // DBからTodoを受け取る
        todo = todoService.find(id);

        // 画面の要素に値をセットする
        setValueToItems(todo);
    }

    /**
     * 画面の要素を取得する
     */
    protected void findViews() {
        titleTextView = (TextView) findViewById(R.id.titleText);
        contentTextView = (TextView) findViewById(R.id.contentText);
        updateBtn = (BootstrapButton) findViewById(R.id.updateBtn);
        dateTextView = (TextView) findViewById(R.id.dateText);
        datePickerBtn = (BootstrapButton) findViewById(R.id.datePickerBtn);
        timeTextView = (TextView) findViewById(R.id.timeText);
        timePickerBtn = (BootstrapButton) findViewById(R.id.timePickerBtn);
        notifyCheck = (CheckBox) findViewById(R.id.notifyCheck);
        minutesSpinner = (Spinner) findViewById(R.id.minutes_spinner);
    }

    /**
     * 画面の要素に値をセットする
     */
    protected void setValueToItems(Todo todo) {
        // Todoのタイトルを画面に表示する
        titleTextView.setText(todo.getTitle());


        // Todoの内容を画面に表示する
        contentTextView.setText(todo.getContent());

        // Todoの日時を画面に表示する
        dateTextView.setText(CustomDateTimeFormat.convertToDateString(todo.getEventDate()));
        timeTextView.setText(CustomDateTimeFormat.convertToTimeString(todo.getEventDate()));

        // Todoの通知について画面に表示する
        ArrayAdapter minutesSpinnerAdapter = (ArrayAdapter) minutesSpinner.getAdapter();
        int defaultValue = minutesSpinnerAdapter.getPosition(String.valueOf(todo.getBeforeMinutes()));
        minutesSpinner.setSelection(defaultValue);
        if (todo.getNotify() == 1) {
            notifyCheck.setChecked(true);
        } else {
            notifyCheck.setChecked(false);
        }
    }

    /**
     * リスナーをセットする
     */
    protected void setListeners() {
        updateBtn.setOnClickListener(this);
        datePickerBtn.setOnClickListener(this);
        timePickerBtn.setOnClickListener(this);
        minutesSpinner.setOnItemSelectedListener(this);
        notifyCheck.setOnClickListener(this);
    }

    /**
     * 日時の初期値を設定する
     */
    protected void setDateTime() {
        Date date = new Date();
        dateTextView.setText(CustomDateFormat.convert(date));
        timeTextView.setText(CustomTimeFormat.convert(date));
    }

    /**
     * ボタンが押下された際の処理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        // NotifyCheckを取得する
        boolean checked = notifyCheck.isChecked();

        switch (v.getId()) {

            // 「更新」ボタンが押下された場合
            case R.id.updateBtn:

                // 要素を画面から取得する
                todo = getTextViewValues();

                // エラーチェックを行う
                boolean errorFlag;
                errorFlag = textValidator(todo.getTitle(), titleTextView);
                if (errorFlag) {
                    break;
                }

                // Todoを一件更新する
                todoService.update(todo);

                // Alertをセットする
                if (checked) {
                    setAlert(todo);
                }

                // 一覧画面に戻る
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;

            // 「日付を選択する」ボタンが押下された場合
            case R.id.datePickerBtn:

                // ダイアログを表示する
                cdpdf = new CustomDatePickerDialogFragment();
                cdpdf.show(getFragmentManager(), "sampleDate");

                break;

            // 「時刻を選択する」ボタンが押下された場合
            case R.id.timePickerBtn:

                // ダイアログを表示する
                ctpdf = new CustomTimePickerDialogFragment();
                ctpdf.show(getFragmentManager(), "sampleTime");

                break;

            // チェックボックスが押下された場合
            case R.id.notifyCheck:
                if (notifyCheck.isChecked()) {
                    checkBox = 1;
                } else {
                    checkBox = 0;
                }

                break;
        }
    }

    /**
     * 時刻をテキストボックスにセットする
     *
     * @param dateText
     */
    public void setTimeTextView(String dateText) {
        TextView timeTextView = (TextView) findViewById(R.id.timeText);
        timeTextView.setText(dateText);
    }

    /**
     * カレンダーの日付をテキストボックスにセットする
     *
     * @param dateText
     */
    public void setDateTextView(String dateText) {
        TextView dateTextView = (TextView) findViewById(R.id.dateText);
        dateTextView.setText(dateText);
    }

    /**
     * TextViewの値を取得する
     */
    public Todo getTextViewValues() {

        // タイトル
        todo.setTitle(titleTextView.getText().toString());

        // 内容
        todo.setContent(contentTextView.getText().toString());

        // 日時
        String dateTime = dateTextView.getText().toString() + " " + timeTextView.getText().toString();
        Date eventDate = null;
        try {
            eventDate = CustomDateTimeFormat.convertToDate(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        todo.setEventDate(eventDate);

        // 通知をするかどうか
        todo.setNotify(checkBox);

        // 何分前に通知をするかどうか
        todo.setBeforeMinutes(beforeMinutes);

        return todo;
    }

    /**
     * 文字のエラーチェックを行う
     *
     * @param text
     */
    public boolean textValidator(String text, TextView textView) {

        // 空文字の場合
        if (text.isEmpty()) {
            textView.setError("文字を入力してください。");
            return true;
        }

        // 空白のみの場合
        String checkText = text.replaceAll(" ", "").replaceAll("　", "");
        if (checkText.isEmpty()) {
            textView.setError("空白のみは登録できません。");
            return true;
        }

        return false;
    }

    /**
     * Alertをセットする
     */
    public void setAlert(Todo todo) {

        // AlertReceiverを呼び出すインテントを生成する
        Intent eventIntent = new Intent(getApplicationContext(), AlertReceiver.class);

        // Notificationに必要な情報をインテントに追加する
        eventIntent.putExtra("id", todo.getId());
        eventIntent.putExtra("title", todo.getTitle());
        eventIntent.putExtra("beforeMinutes", todo.getBeforeMinutes());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todo.getEventDate());
        calendar.add(Calendar.MINUTE, -(beforeMinutes));

        // PendingIntentを取得する
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, eventIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // AlarmManagerを取得する
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * スピナーが選択されている場合
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        beforeMinutes = Integer.valueOf((String) minutesSpinner.getSelectedItem());
    }

    /**
     * スピナーが選択されていない場合
     *
     * @param adapterView
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        beforeMinutes = 0;
    }
}
