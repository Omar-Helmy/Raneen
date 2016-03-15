package com.omar.exapmle.raneen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Omar on 12/03/2016.
 */
///////////////////////////////BroadCast Receiver//////////////////////////////////
public class AlarmBCReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(Raneen.getContext(), "BCR Intent Detected.", Toast.LENGTH_SHORT).show();
        // start service here
        Intent serviceIntent = new Intent(Raneen.getContext(), RaneenService.class);
        serviceIntent.putExtra("alarm", intent.getStringExtra("alarm"));
        context.startService(serviceIntent);
    }
}