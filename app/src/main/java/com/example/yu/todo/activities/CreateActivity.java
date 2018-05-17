package com.example.yu.todo.activities;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yu.todo.R;
import com.example.yu.todo.flagments.CustomDatePickerDialogFragment;
import com.example.yu.todo.flagments.CustomTimePickerDialogFragment;
import com.example.yu.todo.services.TodoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Todoの登録画面
 */
public class CreateActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * 「追加する」ボタン
     */
    private Button addBtn;

    /**
     * 日付のダイアログ
     */
    private CustomDatePickerDialogFragment cdpdf;

    /**
     * 「日付を選択する」ボタン
     */
    private Button datePickerBtn;

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

    }

    /**
     * 画面の要素を取得する
     */
    protected void findViews() {
        addBtn = (Button) findViewById(R.id.addBtn);
        datePickerBtn = (Button) findViewById(R.id.datePickerBtn);
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
     * ボタンが押下された際の処理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 「追加する」ボタンが押下された場合
            case R.id.addBtn:

                // エラーチェックを行う
                boolean errorFlag;
                // タイトル
                // テキストボックスからTodoのタイトルを取得する
                TextView titleTextView = (TextView) findViewById(R.id.titleText);
                String title = titleTextView.getText().toString();

                errorFlag = textValidator(title,titleTextView);
                if(errorFlag) {
                    break;
                }
                // 日付
                // テキストボックスからTodoの日付を取得する
                TextView dateTextView = (TextView) findViewById(R.id.dateText);
                String date = dateTextView.getText().toString();
                errorFlag = dateValidator(date,dateTextView);
                if(errorFlag) {
                    break;
                }

                // 時刻
                // テキストボックスからTodoの日付を取得する
                TextView timeTextView = (TextView) findViewById(R.id.timeText);
                String time = timeTextView.getText().toString();
                errorFlag = timeValidator(time,timeTextView);
                if(errorFlag) {
                    break;
                }

                // テキストボックスからTodoの内容を取得する
                TextView contentTextView = (TextView) findViewById(R.id.contentText);
                String content = contentTextView.getText().toString();

                // 日付と時刻を成形する
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                String dateTime = date + " " + time;
                Date eventDate = new Date();
                try {
                    eventDate = sdf.parse(dateTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Todoを一件DBに挿入する
                todoService.create(title,content,eventDate);

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
     * 日付のエラーチェックを行う
     * @param text
     */
    public boolean dateValidator(String text,TextView textView){

        // 空文字の場合
        if(text.isEmpty()){
            textView.setError("日付を入力してください。");
            return true;
        }

        return false;
    }

    /**
     * 時刻のエラーチェックを行う
     * @param text
     */
    public boolean timeValidator(String text,TextView textView){

        // 空文字の場合
        if(text.isEmpty()){
            textView.setError("日付を入力してください。");
            return true;
        }

        return false;
    }
}
