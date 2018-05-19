package com.example.yu.todo.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.yu.todo.activities.MainActivity;

/**
 * Todoを通知するクラス
 */
public class AlertReceiver extends BroadcastReceiver {

    private Notification.Builder builder;

    /**
     * 通知を受け取った場合
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // 受信したインテントから識別子を取得する
        int eventId = intent.getIntExtra("eventId" , 0);

        //NotificationManagerオブジェクトを取得する
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // アプリを呼び出すためのインテントを生成する
        Intent eventIntent = new Intent(context, MainActivity.class);

        PendingIntent intentActivity = PendingIntent.getActivity(context
                ,0
                ,eventIntent
                ,PendingIntent.FLAG_CANCEL_CURRENT);

        // Notificationを生成する
        builder = new Notification.Builder(context);

        // Notificationの設定をする
        builder.setContentTitle("時間になりました！")
                // アイコンイメージのセット
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                // インテント受信時のタイムスタンプを表示する
                .setWhen(System.currentTimeMillis())
                // Notificationの重要度を高めに設定する
                .setPriority(Notification.PRIORITY_HIGH)
                // Notificationに触れたときに非表示にする
                .setAutoCancel(true)
                // Notificationがタップされた場合はアプリを呼び出す
                .setContentIntent(intentActivity);

        // Notificationを表示する
        notificationManager.notify(eventId,builder.build());
    }
}
