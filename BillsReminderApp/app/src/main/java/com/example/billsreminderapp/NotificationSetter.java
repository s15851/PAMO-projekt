package com.example.billsreminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.joda.time.DateTime;
import org.joda.time.Interval;

class NotificationSetter {
    private Context context;
    private AlarmManager alarmManager;

    NotificationSetter(Context context, AlarmManager alarmManager) {
        this.context = context;
        this.alarmManager = alarmManager;
    }

    void createRepeatNotification() {
        //create notification and pass to him parameters
        Intent intent = new Intent(context, NotificationReceiver.class);
        //set alarm
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long intervalMinute = 60 * 1000;
        long intervalDay = AlarmManager.INTERVAL_DAY;
        //set when the notification should appear
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  new DateTime().withTime(10, 30, 0, 0).getMillis(),
                intervalMinute, pendingIntent);
    }
}
