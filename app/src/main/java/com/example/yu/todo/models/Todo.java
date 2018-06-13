package com.example.yu.todo.models;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Todo
 */
public class Todo extends RealmObject {

    /**
     * ID
     */
    @PrimaryKey
    private Integer id;

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

    /**
     * 何分前に通知の分数
     */
    private Integer beforeMinutes;

    /**
     * 通知
     */
    private int notify;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getBeforeMinutes() {
        return beforeMinutes;
    }

    public void setBeforeMinutes(Integer beforeMinutes) {
        this.beforeMinutes = beforeMinutes;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getNotify() {
        return notify;
    }
}
