package com.example.yu.todo.models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Todo
 */
public class Todo extends RealmObject {

    /**
     * タイトル
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 開催日
     */
    private Date eventDate;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Date getEventDate() {
        return eventDate;
    }
}
