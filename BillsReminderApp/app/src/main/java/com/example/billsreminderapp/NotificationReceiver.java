package com.example.billsreminderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;

import java.util.Set;

//class responsible for constructing the appearance of the reminder

public class NotificationReceiver extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        wyswietlNotyfikacje(context, intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void wyswietlNotyfikacje(Context context, Intent intent) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            // Get the pending intent containing the entire back stack

            InvoicesManager invoicesManager = new InvoicesManager(context.getSharedPreferences("zapisaneFaktury", Context.MODE_PRIVATE));
            Set<Invoice> overdueInvoices = invoicesManager.takeOverdue();
            Set<Invoice> incomingInvoices = invoicesManager.takeIncoming();

            //if pay time is up, show notification
            if(overdueInvoices.size() > 0) {
                StringBuilder content = new StringBuilder();
                for(Invoice overdue : overdueInvoices) {
                    content.append(String.format("Odbiorca: %s, kwota: %szł, termin: %s \n",
                            overdue.getReceiver(), overdue.getCharge(), overdue.getTimeToDisplay()));
                }
                // and show how many invoices are overdue and show icon
                Notification.Builder mBuilder = new Notification.Builder(context, getIdCanal(notificationManager));
                Notification n = mBuilder
                        .setContentTitle(String.format("%s zaległych faktur", overdueInvoices.size()))
                        .setSmallIcon(R.drawable.ico2)
                        .setStyle(new Notification.BigTextStyle().bigText(content.toString()))
                        .setContentText(content.toString()).build();

                notificationManager.notify(0, n);
            }

            //if there are incoming invoices, show notification
            if(incomingInvoices.size() > 0) {
                StringBuilder content = new StringBuilder();
                for(Invoice incoming : incomingInvoices) {
                    content.append(String.format("Odbiorca: %s, kwota: %szł, termin: %s \n",
                            incoming.getReceiver(), incoming.getCharge(), incoming.getTimeToDisplay()));
                }
                assert notificationManager != null;
                // and show how many invoices are incoming and show icon
                Notification.Builder mBuilder = new Notification.Builder(context, getIdCanal(notificationManager));
                Notification n = mBuilder
                        .setContentTitle(String.format("%s zbliżających się faktur", incomingInvoices.size()))
                        .setSmallIcon(R.drawable.ico2)
                        .setStyle(new Notification.BigTextStyle().bigText(content.toString()))
                        .setContentText(content.toString()).build();

                notificationManager.notify(1, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getIdCanal(NotificationManager notificationManager) {
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
        String Description = "This is my channel";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        mChannel.setDescription(Description);
        mChannel.enableLights(true);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mChannel.setShowBadge(false);
        notificationManager.createNotificationChannel(mChannel);
        return CHANNEL_ID;
    }
}
