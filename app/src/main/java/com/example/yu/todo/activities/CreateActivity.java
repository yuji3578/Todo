package com.example.yu.todo.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yu.todo.R;
import com.example.yu.todo.flagments.CustomDatePickerDialogFragment;
import com.example.yu.todo.flagments.CustomTimePickerDialogFragment;
import com.example.yu.todo.models.Todo;
import com.example.yu.todo.receivers.AlertReceiver;
import com.example.yu.todo.services.TodoService;
import com.example.yu.todo.utils.CustomDateFormat;
import com.example.yu.todo.utils.CustomDateTimeFormat;
import com.example.yu.todo.utils.CustomTimeFormat;

import java.text.ParseException;
import java.util.Date;

/**
 * Todoの登録画面
 */
public class CreateActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * タイトル
     */
    private TextView titleTextView;

    /**
     * 内容
     */
    private TextView contentTextView;

    /**
     * 「追加する」ボタン
     */
    private Button addBtn;

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
    private Button datePickerBtn;

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
    private Button timePickerBtn;

    /**
     * Todoが格納されているDBと接続するTodoService
     */
    private TodoService todoService;

    /**
     * 画面が初期表示された際の処理
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // 画面の要素を取得する
        findViews();

        // リスナーをセットする
        setListeners();

        // Realmを取得する
        todoService = new TodoService(this);

        // 日時の初期値を設定する
        setDateTime();
    }

    /**
     * 画面の要素を取得する
     */
    protected void findViews() {
        titleTextView = (TextView) findViewById(R.id.titleText);
        contentTextView = (TextView) findViewById(R.id.contentText);
        addBtn = (Button) findViewById(R.id.addBtn);
        dateTextView = (TextView) findViewById(R.id.dateText);
        datePickerBtn = (Button) findViewById(R.id.datePickerBtn);
        timeTextView = (TextView) findViewById(R.id.timeText);
        timePickerBtn = (Button)findViewById(R.id.timePickerBtn);
    }

    /**
     * リスナーをセットする
     */
    protected void setListeners() {
        addBtn.setOnClickListener(this);
        datePickerBtn.setOnClickListener(this);
        timePickerBtn.setOnClickListener(this);
    }

    /**
     * 日時の初期値を設定する
     */
    protected void setDateTime(){
        Date date = new Date();
        dateTextView.setText(CustomDateFormat.convert(date));
        timeTextView.setText(CustomTimeFormat.convert(date));
    }

    /**
     * ボタンが押下された際の処理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 「追加する」ボタンが押下された場合
            case R.id.addBtn:

                // 要素を画面から取得する
                Todo todo = getTextViewValues();

                // エラーチェックを行う
                boolean errorFlag;
                errorFlag = textValidator(todo.getTitle(),titleTextView);
                if(errorFlag) {
                    break;
                }

                // Todoを一件DBに挿入する
                todoService.create(todo);

                // Alertをセットする
                setAlert(todo);

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
        }
    }

    /**
     * 時刻をテキストボックスにセットする
     * @param dateText
     */
    public void setTimeTextView(String dateText){
        TextView timeTextView = (TextView) findViewById(R.id.timeText);
        timeTextView.setText(dateText);
    }

    /**
     * カレンダーの日付をテキストボックスにセットする
     * @param dateText
     */
    public void setDateTextView(String dateText){
        TextView dateTextView = (TextView) findViewById(R.id.dateText);
        dateTextView.setText(dateText);
    }

    /**
     * TextViewの値を取得する
     */
    public Todo getTextViewValues(){
        Todo todo = new Todo();

        // タイトル
        todo.setTitle(titleTextView.getText().toString());

        // 日時
        String dateTime = dateTextView.getText().toString() + " " + timeTextView.getText().toString();
        Date eventDate = null;
        try {
            eventDate = CustomDateTimeFormat.convertToDate(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        todo.setEventDate(eventDate);

        // テキストボックスからTodoの内容を取得する
        todo.setContent(contentTextView.getText().toString());

        return todo;
    }

    /**
     * 文字のエラーチェックを行う
     * @param text
     */
    public boolean textValidator(String text,TextView textView){

        // 空文字の場合
        if(text.isEmpty()){
            textView.setError("文字を入力してください。");
            return true;
        }

        // 空白のみの場合
        String checkText = text.replaceAll(" ", "").replaceAll("　", "");
        if(checkText.isEmpty()){
            textView.setError("空白のみは登録できません。");
            return true;
        }

        return false;
    }

    /**
     * Alertをセットする
     */
    public void setAlert(Todo todo){

        // AlertReceiverを呼び出すインテントを生成する
        Intent eventIntent = new Intent(getApplicationContext(), AlertReceiver.class);

        // Notificationに必要な情報をインテントに追加する
        eventIntent.putExtra("id" , todo.getId());
        eventIntent.putExtra("title",todo.getTitle());

        // PendingIntentを取得する
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0, eventIntent , PendingIntent.FLAG_CANCEL_CURRENT);

        // AlarmManagerを取得する
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,todo.getEventDate().getTime(), pendingIntent);
    }
}
