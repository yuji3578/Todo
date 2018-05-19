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
    public Todo find(int id){
        return realm.where(Todo.class).equalTo("id", id).findFirst();
    }

    /**
     * Todoを一件登録する
     */
    public void create(Todo inputTodo){
        realm.beginTransaction();
        Number currentIdNum = realm.where(Todo.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        Todo todo = realm.createObject(Todo.class,nextId);
        todo.setTitle(inputTodo.getTitle());
        todo.setContent(inputTodo.getContent());
        todo.setEventDate(inputTodo.getEventDate());
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
