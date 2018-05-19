package com.example.yu.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yu.todo.R;
import com.example.yu.todo.models.Todo;
import com.example.yu.todo.services.TodoService;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * Todoの一覧画面
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * Todoを格納するリスト
     */
    private static List<String> dataList = new ArrayList<String>();

    /**
     * TodoをListViewに格納するためのArrayAdapter
     */
    private static ArrayAdapter<String> adapter;

    /**
     * 一覧に表示されるListViewクラス
     */
    private ListView listView;

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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateActivity();
            }
        });

        // 画面の要素を取得する
        findViews();

        // リスナーをセットする
        setListeners();

        // ArrayAdapterをListViewにセットする
        setAdapters();

        // Realmを取得する
        todoService = new TodoService(this);

        // ListViewを再読み込みする
        refreshListView();
    }

    /**
     * 登録画面に遷移する
     */
    public void goToCreateActivity(){
        Intent intent = new Intent(this, CreateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 画面の要素を取得する
     */
    protected void findViews() {
        listView = (ListView) findViewById(R.id.todoList);
    }

    /**
     * リスナーをセットする
     */
    protected void setListeners() {
        listView.setOnItemClickListener(this);
    }

    /**
     * ArrayAdapterをListViewにセットする
     */
    protected void setAdapters(){
        // ArrayAdapterを初期設定する
        adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dataList);

        // ArrayAdapterをListViewにセットする
        listView.setAdapter(adapter);
    }

    /**
     * ListViewを再読み込みする
     */
    protected void refreshListView(){
        // DBから格納されているTodoをすべて取得する
        RealmResults<Todo> todoList = todoService.findAll();

        // dataListを初期化する
        dataList.clear();

        // 取得したTodoをdataListにセットする
        for(Todo todo : todoList){
            dataList.add(todo.getTitle());
        }

        // ListViewを変更したのでArrayAdapterに通知する
        adapter.notifyDataSetChanged();
    }

    /**
     * ListViewの一つが押下された場合
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // 選択アイテムを取得
        List<Todo> todoList = todoService.findAll();
        int id = todoList.get(i).getId();

        // Todo参照画面に遷移する
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
