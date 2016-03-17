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

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Omar on 12/03/2016.
 */
public class RaneenService extends IntentService {


    private final static long LEC_DURATION = 5400000; // hour and half hour = 90 min
    // Current calender:
    private final Calendar currentCalender = GregorianCalendar.getInstance();
    private final long currentTimeMillis = currentCalender.getTimeInMillis();
    // Audio:
    private AudioManager audioManager = (AudioManager) Raneen.getContext().getSystemService(Context.AUDIO_SERVICE);

    LayoutInflater inflater = (LayoutInflater) Raneen.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);

    int currentLec;

    public RaneenService() {
        super("RaneenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String alarmStatus = intent.getStringExtra("alarm");
        currentLec = intent.getIntExtra("currentLec",-1);

        switch (alarmStatus){
            case "trigger":
                //Raneen.mNotifyMgr.notify(Raneen.notificationID, Raneen.mBuilder.build());
                // continue other cases
            case "nextLec":
            case "firstTime":
            {
                // if a lec finished or last lec, open ringer:
                if(alarmStatus.equals("nextLec")) setRingerModeNormal();
                if (setAlarmIfGreen("lec1",0)) break;
                if (setAlarmIfGreen("lec2",1)) break;
                if (setAlarmIfGreen("lec3", 2)) break;
                if (setAlarmIfGreen("lec4",3)) break;
                if (setAlarmIfGreen("lec5", 4)) break;
                //if (!alarmStatus.equals("trigger")) Raneen.mNotifyMgr.cancel(Raneen.notificationID);
                break;
            }
            case "lecDuration":{
                // silent phone
                // run hour and half
                setRingerModeVibrate();
                setAlarm("nextLec", Raneen.lecs.get(currentLec).getTimeInMillis() + LEC_DURATION, currentLec);
                break;
            }
        }

    }

    private boolean setAlarmIfGreen(String lec, int i){
        if( (!Raneen.sharedPref.getString(lec,"null").equals("null")) &&
                ((Raneen.lecs.get(i).getTimeInMillis() >= currentTimeMillis) || ((Raneen.lecs.get(i).getTimeInMillis() < currentTimeMillis)&&((Raneen.lecs.get(i).getTimeInMillis()+LEC_DURATION) > currentTimeMillis)))) {
            setAlarm("lecDuration", Raneen.lecs.get(i).getTimeInMillis(), i);
            return true;
        }
        else {

            return false;
        }
    }



    private void setAlarm(String msg, long time, int i){

        // 1. Intent to the BCR
        Intent alarmBCRIntent = new Intent(Raneen.getContext(), AlarmBCReceiver.class);
        alarmBCRIntent.putExtra("alarm", msg);
        alarmBCRIntent.putExtra("currentLec",i);
        // 2. Create PendingIntent to put the above intent inside it.
        // PendingIntent used to describe an Intent, and encapsulate info
        // This code returns the pending intent but suitable to use to broadcast intent
        Raneen.lecsPendingIntent = PendingIntent.getBroadcast(Raneen.getContext(),0,alarmBCRIntent,PendingIntent.FLAG_ONE_SHOT);
        // 3. Get Alarm manager
        // AlarmManager alarmManager = (AlarmManager) Raneen.getContext().getSystemService(Context.ALARM_SERVICE);
        // 4. Pass the PendingIntent to the AlarmManager, the pending intent tells the alarm what to do after counting down
        Raneen.alarmManager.set(AlarmManager.RTC, time, Raneen.lecsPendingIntent);
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

    private void setRingerModeVibrate(){

        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
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
    }
}
