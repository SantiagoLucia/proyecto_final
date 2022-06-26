package com.example.proyecto_final;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.proyecto_final.db_reminders.FuncionesDB;
import com.example.proyecto_final.db_reminders.Reminders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotifierAlarm extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        FuncionesDB dbHelper = new FuncionesDB(context);
        Reminders reminder = new Reminders();
        reminder.setMessage(intent.getStringExtra("Message"));
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date remint = null;
        try {
            remint = df.parse(intent.getStringExtra("RemindDate"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        reminder.setRemindDate(remint);
        reminder.setId(intent.getIntExtra("idRem",0));
        dbHelper.Delete(intent.getIntExtra("idRem",0));
        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent1 = new Intent(context,DatosControlActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(DatosControlActivity.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"my_channel_01");

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("my_channel_01","hello", NotificationManager.IMPORTANCE_HIGH);
        }

        Log.d("CHANNEL_NOTI", String.valueOf(channel));
        Notification notification = builder.setContentTitle("Mi Auto - Recordatorio!")
                .setContentText(intent.getStringExtra("Message")).setAutoCancel(true)
                .setSound(alarmsound).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(intent2)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notification);
        Activity datosActivity = DatosControlActivity.dca;
        datosActivity.recreate();
    }
}
