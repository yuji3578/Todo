package com.example.yu.todo.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Event
 */
public class Event extends RealmObject {

    /**
     * ID
     */
    @PrimaryKey
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
