package com.omar.exapmle.raneen;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Omar on 13/03/2016.
 */
public class Raneen extends Application {

    private static Context context;
    private static final String SHARED_PREF_FILE = "com.omar.exapmle.raneen.SharedPreferences";
    public static SharedPreferences sharedPref;

    // create array of all 5 lecs
    public static final ArrayList<Calendar> lecs = new ArrayList<>(5);

    public static PendingIntent lecsPendingIntent;
    public static PendingIntent triggerPendingIntent;
    public static AlarmManager alarmManager;

    // Gets an instance of the NotificationManager service
    public static NotificationManager mNotifyMgr;
    // Sets an ID for the notification
    public final static int notificationID = 1;
    // builder
    public static NotificationCompat.Builder mBuilder;





    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        sharedPref = getContext().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) Raneen.getContext().getSystemService(Context.ALARM_SERVICE);
        mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        setLecsCalenders();
        initiateNotification();





    }

    public static Context getContext(){
        return context;
    }

    private void setLecsCalenders(){

        // create array of all 5 lecs
        for(int i=0; i<5; i++)
            lecs.add(i, GregorianCalendar.getInstance());

        // change current time to specific hour, minute, second:
        lecs.get(0).set(Calendar.HOUR_OF_DAY, 8);
        lecs.get(0).set(Calendar.MINUTE, 30);
        lecs.get(0).set(Calendar.SECOND, 0);

        lecs.get(1).set(Calendar.HOUR_OF_DAY, 10);
        lecs.get(1).set(Calendar.MINUTE, 15);
        lecs.get(1).set(Calendar.SECOND, 0);

        lecs.get(2).set(Calendar.HOUR_OF_DAY, 12);
        lecs.get(2).set(Calendar.MINUTE, 15);
        lecs.get(2).set(Calendar.SECOND, 0);

        lecs.get(3).set(Calendar.HOUR_OF_DAY, 14);
        lecs.get(3).set(Calendar.MINUTE, 0);
        lecs.get(3).set(Calendar.SECOND, 0);

        lecs.get(4).set(Calendar.HOUR_OF_DAY, 15);
        lecs.get(4).set(Calendar.MINUTE, 45);
        lecs.get(4).set(Calendar.SECOND, 0);
    }

    private void initiateNotification(){
        // notification:
        mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.mipmap.bell96)
                        .setContentTitle("Raneen schedule is running")
                        .setTicker("Raneen schedule service is running now")
                        .setAutoCancel(false)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setOngoing(true)
                        .setContentText("Touch to open the app");
        Intent resultIntent = new Intent(getContext(), MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
    }

}
