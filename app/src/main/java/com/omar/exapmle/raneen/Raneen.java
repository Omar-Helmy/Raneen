package com.omar.exapmle.raneen;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Omar on 13/03/2016.
 */
public class Raneen extends Application {

    private static Context context;
    private static final String SHARED_PREF_FILE = "com.omar.exapmle.raneen.SharedPreferences";
    public static SharedPreferences sharedPref;

    public static PendingIntent pendingIntent;
    public static AlarmManager alarmManager;





    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        sharedPref = getContext().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) Raneen.getContext().getSystemService(Context.ALARM_SERVICE);





    }

    public static Context getContext(){
        return context;
    }

}
