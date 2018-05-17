package com.example.yu.todo.services;

import android.content.Context;

import com.example.yu.todo.models.Todo;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class TodoService {

    /**
     * DBと接続するRealmクラス
     */
    private Realm realm;

    /**
     * コンストラクタ
     * @param context
     */
    public TodoService(Context context){
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    /**
     * Todoを一件検索する
     */
    public Todo find(String title){
        return realm.where(Todo.class).equalTo("title", title).findFirst();
    }

    /**
     * Todoを一件登録する
     */
    public void create(String title,String content,Date eventDate){
        realm.beginTransaction();
        Todo todo = realm.createObject(Todo.class);
        todo.setTitle(title);
        todo.setContent(content);
        todo.setEventDate(eventDate);
        realm.commitTransaction();
    }

    /**
     * Todoを一件削除する
     */
    public void delete(final Todo todoModel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                todoModel.deleteFromRealm();
            }
        });
    }

    /**
     * Todoを全件取得する
     */
    public RealmResults<Todo> findAll(){
        return realm.where(Todo.class).findAll();
    }
}
