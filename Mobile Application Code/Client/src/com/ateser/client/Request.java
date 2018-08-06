package com.ateser.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.ateser.client.GPSTracker;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

public class Request extends Activity {
    public static Float longitude;
    public static Float latitude;
    public static int ID;  
    //public static String URL="http://192.168.0.13/Users/Ýbrahim/Desktop/data.txt";
    public static String URL="http://ferhat.engineerswithoutfears.com/bridge.php";
	GPSTracker gps;


	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		
//		final String phonedate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	    String phonedate = sdf.format(new Date(System.currentTimeMillis()));
		
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		
		final Context context;
		final PendingIntent pendingIntent;
		
		final String tmDevice, tmSerial, androidId;
		  tmDevice = "" + tm.getDeviceId();
		    tmSerial = "" + tm.getSimSerialNumber();
		    androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

		    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
		    final String deviceId = deviceUuid.toString();
		    
		    final String pID = tm.getDeviceId();		    
		              
            
            
        gps = new GPSTracker(Request.this);
        
        if(gps.canGetLocation())
        {
        	
        	latitude = (float) gps.getLatitude();
        	longitude = (float) gps.getLongitude();

        	Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();	
        }
        else{

        	gps.showSettingsAlert();
        }
        
        if( Build.VERSION.SDK_INT >= 8){
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy); 
		}
        
//        button_send_info.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
            	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            	postParameters.add(new BasicNameValuePair("pID", pID));
            	postParameters.add(new BasicNameValuePair("latitude", latitude.toString()));
            	postParameters.add(new BasicNameValuePair("longitude", longitude.toString()));
            	postParameters.add(new BasicNameValuePair("Date", phonedate.toString()));

            	String response;
            	try 
            	{
            	    response = CustomHttpClient.executeHttpPost(URL, postParameters);
            	    String res=response.toString();
//            	    show_infos.setText(res);
            	} 
            	catch (Exception e) 
            	{
            		//hata alaný
              	}
   //         }
    //    });		
        
            	
            	Intent myIntent = new Intent(Request.this, OnAlarmReceiver.class);

    		    pendingIntent = PendingIntent.getService(Request.this, 0, myIntent, 0);

                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();

                calendar.setTimeInMillis(System.currentTimeMillis());

                calendar.add(Calendar.SECOND, 300);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                
                setResult(RESULT_OK);
                finish();
                
/*            	Intent i=new Intent(context, OnAlarmReceiver.class);
                PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                     SystemClock.elapsedRealtime()+60000,3000,pi);*/
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     finish();
	}
}