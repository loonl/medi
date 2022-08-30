package com.example.medi;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, AlarmReceiver.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingI = PendingIntent.getBroadcast(context, intent.getIntExtra("code", -1),
                notificationIntent, PendingIntent.FLAG_MUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");


        //OREO API 26 이상에서는 채널 필요
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.icon_noti); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            String channelName ="AlarmChannel";
            String description = "Always Rings at certain time";
            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
            channel.setDescription(description);

            if (notificationManager != null) {
                // 노티피케이션 채널을 시스템에 등록
                notificationManager.createNotificationChannel(channel);
            }
        }else builder.setSmallIcon(R.drawable.icon_noti); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남


        builder.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setChannelId("default")
                .setTicker("{Time to watch some cool stuff!}")
                .setContentTitle("약 복용 시간 알림")
                .setContentText("에스리시정 복용 시간 입니다. 잊지 마세요!")
                .setContentInfo("INFO")
                .setContentIntent(pendingI);

        if (notificationManager != null) {

            // 노티피케이션 동작시킴
            notificationManager.notify(intent.getIntExtra("code", -1), builder.build());

            // 내일 같은 시간으로 알람시간 결정
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, intent.getIntExtra("code", -1)
                    , alarmIntent, PendingIntent.FLAG_MUTABLE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.DATE, 1);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }
}