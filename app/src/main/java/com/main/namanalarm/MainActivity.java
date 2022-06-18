package com.main.namanalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private TimePicker timePicker;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    this.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    this.timePicker = findViewById(R.id.timePicker);

    findViewById(R.id.btnStart).setOnClickListener(mClickListener);
    findViewById(R.id.btnStop).setOnClickListener(mClickListener);

    }

    // 알람 시작
    private void start() {
        // 시간 설정
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        calendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        // 현재 시간보다 이전이면
        if(calendar.before(Calendar.getInstance())) {
            // 다음 날로 설정
            calendar.add(Calendar.DATE, 1);
        }

        // Receiver 설정
        Intent intent = new Intent(this, AlarmManager.class);
        // state 값이 on 이면 알람 시작, off 이면 중지
        intent.putExtra("state","on");

        this.pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 알람 설정
        this.alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Toast.makeText(this, "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
    }   // start


    // 알람 중지
    private void stop() {
        if(this.pendingIntent == null) {
            return;
        }

        // 알람 취소
        this.alarmManager.cancel(this.pendingIntent);

        // 알람 중지 Broadcast
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("state", "off");

        sendBroadcast(intent);

        this.pendingIntent = null;

    } // stop


    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStart:
                    // 알람 시작
                    start();

                    break;
                case R.id.btnStop:
                    // 알람 중지
                    stop();

                    break;
            }
        }
    };



}