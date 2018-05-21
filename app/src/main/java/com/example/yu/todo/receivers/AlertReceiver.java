package com.example.yu.todo.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.yu.todo.activities.MainActivity;

/**
 * Todoを通知するクラス
 */
public class AlertReceiver extends BroadcastReceiver {

    private Notification.Builder builder;

    /**
     * 通知を受け取った場合
     *
     * @param context
     * @param intent
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        // 受信したインテントから情報を取得する
        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        int beforeMinutes = intent.getIntExtra("beforeMinutes", 0);

        // 通知チャンネルのIDにする任意の文字列
        String channelId = "updates";
        // 通知チャンネル名
        String name = "更新情報";
        // デフォルトの重要度
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);

        // 通知チャンネルのデフォルト値を設定する
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setShowBadge(false);

        // NotificationManagerCompatにcreateNotificationChannel()は無い。
        NotificationManager nm = context.getSystemService(NotificationManager.class);
        nm.createNotificationChannel(channel);

        // 通知チャンネルのIDを指定する
        NotificationCompat.Builder builder
                = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("もうすぐイベントが始まります！")
                .setContentText(title)
                .setSubText( beforeMinutes +"分後に開始です！")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true);

        // 通知を実行する
        NotificationManagerCompat.from(context).notify(id, builder.build());
    }
}
