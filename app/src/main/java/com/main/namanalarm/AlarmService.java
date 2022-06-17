package com.main.namanalarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private boolean inRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Foreground 에서 실행되면 Notification 을 보여줘야 됨
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.0) {
            // Oreo(26) 버전 이후부터는 channel이 필요함
            String channelId = createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    // .setCategory(Notification.CATEGORY_SERVICE)
                    .build();

            startForeground(1, notification);
        }

        String state = intent.getStringExtra("state");

        if (!this.inRunning && state.equals("on")) {
            // 알람음 재생 OFF, 알람음 시작 상태
            this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            this.mediaPlayer.start();

            this.isRunning = true;

            Log.d("AlarmService", "Alarm Start");
        } else if (this.inRunning & state.equals("off")) {
            // 알람음 재생 ON, 알람음 중지 상태
            this.mediaPlayer.stop();
            this.mediaPlayer.reset();
            this.mediaPlayer.release();

            this.inRunning = false;

            Log.d("AlarmService", "Alarm Stop")

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.0) {
                stopForeground(true);
            }
        }

        return START_NOT_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.0)
    private String createNotificationChannel() {
        String chaannelId = "Alarm";
        String channelName = getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
        channel.setSound(null, null);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        return channelId;
    }

}




