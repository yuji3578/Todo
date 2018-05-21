package com.example.yu.todo.services;

import android.content.Context;

/**
 * EventをやりとりするRealmクラス
 */
import com.example.yu.todo.models.Event;

import io.realm.Realm;

public class EventService {

    /**
     * DBと接続するRealmクラス
     */
    private Realm realm;

    /**
     * コンストラクタ
     * @param context
     */
    public EventService(Context context){
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    /**
     * 次のEventIdを取得する
     * @return
     */
    public int getNextEventId(){
        realm.beginTransaction();
        Number currentIdNum = realm.where(Event.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        realm.createObject(Event.class,nextId);
        realm.commitTransaction();
        return nextId;
    }

}
