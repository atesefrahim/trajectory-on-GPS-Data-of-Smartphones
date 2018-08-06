package com.ateser.client;
import android.app.Service;

import android.content.Intent;

import android.os.IBinder;

import android.widget.Toast;

public class OnAlarmReceiver extends Service {



@Override

public void onCreate() {

// TODO Auto-generated method stub
	Intent myIntent = new Intent(OnAlarmReceiver.this, Request.class);

}



@Override

public IBinder onBind(Intent intent) {

// TODO Auto-generated method stub

Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();

return null;

}



@Override

public void onDestroy() {

// TODO Auto-generated method stub

super.onDestroy();

Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();

}



@Override

public void onStart(Intent intent, int startId) {

// TODO Auto-generated method stub

super.onStart(intent, startId);
Intent dialogIntent = new Intent(getBaseContext(), Request.class);
dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
getApplication().startActivity(dialogIntent);

Toast.makeText(this, "GPS data is sending again", Toast.LENGTH_LONG).show();

}



@Override

public boolean onUnbind(Intent intent) {

// TODO Auto-generated method stub

Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();

return super.onUnbind(intent);

}



}