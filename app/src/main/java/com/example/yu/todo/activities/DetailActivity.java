package com.example.yu.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yu.todo.R;
import com.example.yu.todo.models.Todo;
import com.example.yu.todo.services.TodoService;
import com.example.yu.todo.utils.CustomDateTimeFormat;

import java.text.SimpleDateFormat;

/**
 * Todoの参照画面
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    /**
     * Todo
     */
    private Todo todo;

    /**
     * Todoのタイトル
     */
    private TextView titleTextView;

    /**
     * Todoの内容
     */
    private TextView contentTextView;

    /**
     * Todoの日時
     */
    private TextView dateTimeTextView;

    /**
     * Todoの通知
     */
    private TextView notifyCheck;

    /**
     * 「削除する」ボタン
     */
    private Button deleteBtn;

    /**
     * Todoが格納されているDBと接続するTodoService
     */
    private TodoService todoService;

    /**
     * 画面が初期表示された際の処理
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 画面の要素を取得する
        findViews();

        // リスナーをセットする
        setListeners();

        // Realmを取得する
        todoService = new TodoService(this);

        // TodoListの一覧画面かTodoらタイトルを受け取る
        Intent intent = getIntent();
        int id = intent.getIntExtra( "id" ,0);

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
        dateTimeTextView = (TextView) findViewById(R.id.dateText);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        notifyCheck = (TextView) findViewById(R.id.notifyCheck);
    }

    /**
     * リスナーをセットする
     */
    protected void setListeners() {
        deleteBtn.setOnClickListener(this);
    }

    /**
     * 画面の要素に値をセットする
     */
    protected void setValueToItems(Todo todo){
        // Todoのタイトルを画面に表示する
        titleTextView.setText(todo.getTitle());

        // Todoの内容を画面に表示する
        contentTextView.setText(todo.getContent());

        // Todoの日時を画面に表示する
        dateTimeTextView.setText(CustomDateTimeFormat.convertToString(todo.getEventDate()));

        // Todoの通知について画面に表示する
        //setNotifyCheck();
    }

    /**
     * ボタンが押下された際の処理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 「削除する」ボタンが押下された場合
            case R.id.deleteBtn:
                // Todoを削除する
                todoService.delete(todo);

                // TodoListの一覧画面に戻る
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
        }
    }

    /**
     * 通知をするかどうかを表示する
     */
    public void setNotifyCheck(){

        if(todo.getNotify()){
            notifyCheck.setText(String.valueOf(todo.getBeforeMinutes()) + "分前に通知をする");
        } else {
            notifyCheck.setText("通知はしない");
        }
    }
}