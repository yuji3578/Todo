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
        String title = intent.getStringExtra( "title" );

        // DBからTodoを受け取る
        todo = todoService.find(title);

        // Todoのタイトルを画面に表示する
        TextView titleTextView = (TextView) findViewById(R.id.titleText);
        titleTextView.setText(title);

        // Todoの内容を画面に表示する
        TextView contentTextView = (TextView) findViewById(R.id.contentText);
        contentTextView.setText(todo.getContent());

        // Todoの日付を画面に表示する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        TextView dateTextView = findViewById(R.id.dateText);
        dateTextView.setText(sdf.format(todo.getEventDate()));
    }

    /**
     * 画面の要素を取得する
     */
    protected void findViews() {
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
    }

    /**
     * リスナーをセットする
     */
    protected void setListeners() {
        deleteBtn.setOnClickListener(this);
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
}