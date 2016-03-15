package com.omar.exapmle.raneen;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Omar on 12/03/2016.
 */
public class RaneenService extends IntentService {

    // create array of all 5 lecs
    private final ArrayList<Calendar> lecs = new ArrayList<>(5);
    private final static long lecDuration = 5400000;
    // Current calender:
    private Calendar currentCalender = GregorianCalendar.getInstance();
    private long currentTimeMillis = currentCalender.getTimeInMillis();
    // Audio:
    private AudioManager audioManager = (AudioManager) Raneen.getContext().getSystemService(Context.AUDIO_SERVICE);

    LayoutInflater inflater = (LayoutInflater) Raneen.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

    public RaneenService() {
        super("RaneenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        setLecsCalenders();
        String alarmStatus = intent.getStringExtra("alarm");

        switch (alarmStatus){
            case "nextLec":{
                if (setAlarmIfGreen("lec1",0)) break;
                if (setAlarmIfGreen("lec2",1)) break;
                if (setAlarmIfGreen("lec3",2)) break;
                if (setAlarmIfGreen("lec4",3)) break;
                if (setAlarmIfGreen("lec5",4)) break;
                // if no lecs, open ringer:
                setRingerModeNormal();
                break;
            }
            case "lecDuration":{
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = new Toast(Raneen.getContext());
                        toast.setView(inflater.inflate(R.layout.toast_ring_mute, null));
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                // run hour and half
                // silent phone
                setAlarm("nextLec", currentTimeMillis + 30000);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                break;
            }
        }

    }

    private boolean setAlarmIfGreen(String lec, int i){
        if((lecs.get(i).getTimeInMillis() >= currentTimeMillis) && (!Raneen.sharedPref.getString(lec,"null").equals("null"))) {
            setAlarm("lecDuration", lecs.get(i).getTimeInMillis());
            setRingerModeNormal();
            return true;
        }
        else {

            return false;
        }
    }

    private void setLecsCalenders(){

        // create array of all 5 lecs
        for(int i=0; i<5; i++)
            lecs.add(i, GregorianCalendar.getInstance());

        // change current time to specific hour, minute, second:
        lecs.get(0).set(Calendar.HOUR_OF_DAY, 0);
        lecs.get(0).set(Calendar.MINUTE, 40);
        lecs.get(0).set(Calendar.SECOND, 0);

        lecs.get(1).set(Calendar.HOUR_OF_DAY, 0);
        lecs.get(1).set(Calendar.MINUTE, 41);
        lecs.get(1).set(Calendar.SECOND, 0);

        lecs.get(2).set(Calendar.HOUR_OF_DAY, 0);
        lecs.get(2).set(Calendar.MINUTE, 42);
        lecs.get(2).set(Calendar.SECOND, 0);

        lecs.get(3).set(Calendar.HOUR_OF_DAY, 0);
        lecs.get(3).set(Calendar.MINUTE, 43);
        lecs.get(3).set(Calendar.SECOND, 0);

        lecs.get(4).set(Calendar.HOUR_OF_DAY, 0);
        lecs.get(4).set(Calendar.MINUTE, 44);
        lecs.get(4).set(Calendar.SECOND, 0);
    }

    private void setAlarm(String msg, long time){

        // 1. Intent to the BCR
        Intent alarmBCRIntent = new Intent(Raneen.getContext(), AlarmBCReceiver.class);
        alarmBCRIntent.putExtra("alarm", msg);
        // 2. Create PendingIntent to put the above intent inside it.
        // PendingIntent used to describe an Intent, and encapsulate info
        // This code returns the pending intent but suitable to use to broadcast intent
        Raneen.pendingIntent = PendingIntent.getBroadcast(Raneen.getContext(),0,alarmBCRIntent,PendingIntent.FLAG_ONE_SHOT);
        // 3. Get Alarm manager
        // AlarmManager alarmManager = (AlarmManager) Raneen.getContext().getSystemService(Context.ALARM_SERVICE);
        // 4. Pass the PendingIntent to the AlarmManager, the pending intent tells the alarm what to do after counting down
        Raneen.alarmManager.set(AlarmManager.RTC, time, Raneen.pendingIntent);
        // when the alarm finishes counting, it will send back a broadcast as our AlarmBCR class will receive it
    }

    private void setRingerModeNormal(){

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = new Toast(Raneen.getContext());
                toast.setView(inflater.inflate(R.layout.toast_ring_normal, null));
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
}
