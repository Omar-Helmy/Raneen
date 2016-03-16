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
    private SharedPreferences.Editor toggleEditor = Raneen.sharedPref.edit(); // request editing shared pref file;
    SharedPreferences.Editor restoreEditor = Raneen.sharedPref.edit(); // request editing shared pref file
    private boolean[] btns = new boolean[5];

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
        restoreBtnPref("lec1",btn1,0);
        restoreBtnPref("lec2",btn2,1);
        restoreBtnPref("lec3",btn3,2);
        restoreBtnPref("lec4",btn4,3);
        restoreBtnPref("lec5",btn5,4);


        fabSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //fabCancel.setVisibility(View.VISIBLE);
                toggleEditor.apply();
                // start service here
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                Intent serviceIntent = new Intent(Raneen.getContext(), RaneenService.class);
                serviceIntent.putExtra("alarm", "firstTime");
                serviceIntent.putExtra("currentLec",0);
                Raneen.getContext().startService(serviceIntent);
            }

        });

        fabCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancelBtns("lec1",btn1);
                cancelBtns("lec2",btn2);
                cancelBtns("lec3",btn3);
                cancelBtns("lec4",btn4);
                cancelBtns("lec5",btn5);
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
            btn.setBackgroundColor(getResources().getColor(R.color.LightGreen));
        }else {
            toggleEditor.putString(lec, "null");
            btn.setBackgroundColor(getResources().getColor(R.color.LightRed));
            btns[i] = !btns[i]; // false
        }

    }

    private void restoreBtnPref (String lec, Button btn, int i){

        // check previous state of each button (red or green):

        if(Raneen.sharedPref.getString(lec,"null").equals("null")) { // create new key
            restoreEditor.putString(lec, "null");
            btns[i] = false;
            btn.setBackgroundColor(getResources().getColor(R.color.LightRed));
        }else {
            btn.setBackgroundColor(getResources().getColor(R.color.LightGreen));
            btns[i] = true;
        }
        restoreEditor.apply(); // save
    }

    private void cancelBtns (String lec, Button btn){

        restoreEditor.putString(lec, "null");
        btn.setBackgroundColor(getResources().getColor(R.color.LightRed));
        restoreEditor.apply(); // save
    }

}
