package com.omar.exapmle.raneen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button btn1, btn2, btn3, btn4, btn5;
    private FloatingActionButton fabSave, fabCancel;
    private SharedPreferences.Editor toggleEditor = Raneen.sharedPref.edit(); // request editing shared pref file;
    SharedPreferences.Editor restoreEditor = Raneen.sharedPref.edit(); // request editing shared pref file
    private boolean[] btns = new boolean[5];





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // tool bar:
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // buttons:
        btn1 = (Button) findViewById(R.id.lec1);
        btn2 = (Button) findViewById(R.id.lec2);
        btn3 = (Button) findViewById(R.id.lec3);
        btn4 = (Button) findViewById(R.id.lec4);
        btn5 = (Button) findViewById(R.id.lec5);

        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        fabCancel = (FloatingActionButton) findViewById(R.id.fab_cancel);


        // check previous state of each button (red or green):
        restoreBtnPref("lec1",btn1,0);
        restoreBtnPref("lec2",btn2,1);
        restoreBtnPref("lec3",btn3,2);
        restoreBtnPref("lec4",btn4,3);
        restoreBtnPref("lec5", btn5, 4);


        fabSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (boolean btn : btns)
                    if (btn) { // at least one schedule
                        //fabCancel.setVisibility(View.VISIBLE);
                        Raneen.alarmManager.cancel(Raneen.lecsPendingIntent);
                        toggleEditor.apply();
                        // start service here
                        Toast.makeText(MainActivity.this, "Done! check notification area", Toast.LENGTH_SHORT).show();
                        Intent serviceIntent = new Intent(Raneen.getContext(), RaneenService.class);
                        serviceIntent.putExtra("alarm", "firstTime");
                        serviceIntent.putExtra("currentLec", 0);
                        Raneen.getContext().startService(serviceIntent);
                        // Builds the notification and issues it.
                        Raneen.mNotifyMgr.notify(Raneen.notificationID, Raneen.mBuilder.build());
                        triggerServiceEveryDay();
                        return;
                    }
                Toast.makeText(MainActivity.this, "No schedule to set!", Toast.LENGTH_SHORT).show();
            }

        });

        fabCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelBtns("lec1", btn1);
                cancelBtns("lec2", btn2);
                cancelBtns("lec3", btn3);
                cancelBtns("lec4", btn4);
                cancelBtns("lec5", btn5);
                for (int i=0; i<5; i++) btns[i] = false;
                Raneen.alarmManager.cancel(Raneen.lecsPendingIntent);
                Raneen.alarmManager.cancel(Raneen.triggerPendingIntent);
                Toast.makeText(MainActivity.this, "All schedules canceled!", Toast.LENGTH_SHORT).show();
                Raneen.mNotifyMgr.cancel(Raneen.notificationID);
            }

        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Display the fragment as the main content.
            Intent intent = new Intent(Raneen.getContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickBtn (View view){

        Button button = (Button) view;
        int id = button.getId();

        switch (id){
            case R.id.lec1: {
                toggleGreen("lec1", btn1,0);
                break;
            }
            case R.id.lec2:{
                    toggleGreen("lec2", btn2,1);
                    break;
            }
            case R.id.lec3: {
                toggleGreen("lec3", btn3,2);
                break;
            }
            case R.id.lec4: {
                toggleGreen("lec4", btn4,3);
                break;
            }
            case R.id.lec5: {
                toggleGreen("lec5", btn5,4);
                break;
            }

        }

    }


    private void toggleGreen (String lec, Button btn, int i){

        if(!btns[i]) {
            toggleEditor.putString(lec, "green");
            btns[i] = !btns[i]; // true
            btn.setBackgroundResource(R.drawable.green_button);
        }else {
            toggleEditor.putString(lec, "null");
            btn.setBackgroundResource(R.drawable.red_button);
            btns[i] = !btns[i]; // false
        }

    }

    private void restoreBtnPref (String lec, Button btn, int i){

        // check previous state of each button (red or green):

        if(Raneen.sharedPref.getString(lec,"null").equals("null")) { // create new key
            restoreEditor.putString(lec, "null");
            btns[i] = false;
            btn.setBackgroundResource(R.drawable.red_button);
        }else {
            btn.setBackgroundResource(R.drawable.green_button);
            btns[i] = true;
        }
        restoreEditor.apply(); // save
    }

    private void cancelBtns (String lec, Button btn){

        restoreEditor.putString(lec, "null");
        btn.setBackgroundResource(R.drawable.red_button);
        restoreEditor.apply(); // save
    }

    private void triggerServiceEveryDay(){

        // 1. Intent to the BCR
        Intent triggerIntent = new Intent(Raneen.getContext(), AlarmBCReceiver.class);
        triggerIntent.putExtra("alarm","trigger");
        // 2. Create PendingIntent to put the above intent inside it.
        // PendingIntent used to describe an Intent, and encapsulate info
        // This code returns the pending intent but suitable to use to broadcast intent
        Raneen.triggerPendingIntent = PendingIntent.getBroadcast(Raneen.getContext(),0,triggerIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        // 3. Get Alarm manager
        // AlarmManager alarmManager = (AlarmManager) Raneen.getContext().getSystemService(Context.ALARM_SERVICE);
        // 4. Pass the PendingIntent to the AlarmManager, the pending intent tells the alarm what to do after counting down
        Raneen.alarmManager.setRepeating(AlarmManager.RTC, Raneen.lecs.get(0).getTimeInMillis() - AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_DAY, Raneen.triggerPendingIntent);
        // when the alarm finishes counting, it will send back a broadcast as our AlarmBCR class will receive it
    }

}
