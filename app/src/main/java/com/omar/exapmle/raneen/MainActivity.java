package com.omar.exapmle.raneen;

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

    //private String firstLec = null;


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
        restoreBtnPref("lec1",btn1);
        restoreBtnPref("lec2",btn2);
        restoreBtnPref("lec3",btn3);
        restoreBtnPref("lec4",btn4);
        restoreBtnPref("lec5",btn5);


        fabSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //fabCancel.setVisibility(View.VISIBLE);
                // start service here
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                Intent serviceIntent = new Intent(Raneen.getContext(), RaneenService.class);
                serviceIntent.putExtra("alarm", "nextLec");
                Raneen.getContext().startService(serviceIntent);
            }

        });

        fabCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Raneen.alarmManager.cancel(Raneen.pendingIntent);
                Toast.makeText(MainActivity.this, "All schedules canceled", Toast.LENGTH_SHORT).show();
            }

        });


 /*       long diff = currentTimeMillis-requiredTimeMillis;

        if (requiredTimeMillis < currentTimeMillis)
            Toast.makeText(this, "fat el me3ad", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "remaining"+diff, Toast.LENGTH_SHORT).show();*/




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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickBtn (View view){

        Button button = (Button) view;
        int id = button.getId();

        switch (id){
            case R.id.lec1: {
                toggleGreen("lec1", btn1);
                break;
            }
            case R.id.lec2:{
                    toggleGreen("lec2", btn2);
                    break;
            }
            case R.id.lec3: {
                toggleGreen("lec3", btn3);
                break;
            }
            case R.id.lec4: {
                toggleGreen("lec4", btn4);
                break;
            }
            case R.id.lec5: {
                toggleGreen("lec5", btn5);
                break;
            }

        }

    }


    private void toggleGreen (String lec, Button btn){

        SharedPreferences.Editor toggleEditor = Raneen.sharedPref.edit(); // request editing shared pref file

        if(Raneen.sharedPref.getString(lec,"null").equals("null")) {
            toggleEditor.putString(lec, "green");
            btn.setBackgroundColor(getResources().getColor(R.color.LightGreen));
        }else {
            toggleEditor.putString(lec, "null");
            btn.setBackgroundColor(getResources().getColor(R.color.LightRed));
        }

        toggleEditor.apply();
    }

    private void restoreBtnPref (String lec, Button btn){

        // check previous state of each button (red or green):
         SharedPreferences.Editor restoreEditor = Raneen.sharedPref.edit(); // request editing shared pref file

        if(Raneen.sharedPref.getString(lec,"null").equals("null")) { // create new key
            restoreEditor.putString(lec, "null");
            btn.setBackgroundColor(getResources().getColor(R.color.LightRed));
        }else
            btn.setBackgroundColor(getResources().getColor(R.color.LightGreen));

        restoreEditor.apply(); // save
    }

}
