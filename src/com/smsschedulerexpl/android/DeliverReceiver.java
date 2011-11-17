package com.smsschedulerexpl.android;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DeliverReceiver extends BroadcastReceiver{

	boolean successdeliver = true;
	DBAdapter mdba;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int msgSize = intent.getIntExtra("SIZE", 0);
		int part = (int)intent.getIntExtra("PART", 0);
		String number = intent.getStringExtra("NUMBER");
		long id = intent.getLongExtra("ID", 0);
		Log.i("MESSAGE", "ID in DeliverReceiver : " + id);
		mdba = new DBAdapter(context);
		
		switch (getResultCode())
        {
            case Activity.RESULT_OK:

            	mdba.open();
            	mdba.increaseDeliver(id);
            	mdba.close();
            	
            	if(part==msgSize){
            		mdba.open();
            		if(mdba.checkDelivery(id)){
            			Toast.makeText(context, "Message delivered to " + number, Toast.LENGTH_SHORT).show();
            		}else{
            			Toast.makeText(context, "Message not delivered to " + number, Toast.LENGTH_SHORT).show();
            		}
            		mdba.close();
            	}
            	Intent mIntent = new Intent();
                mIntent.setAction("My special action");
                PendingIntent pi = PendingIntent.getBroadcast(context, 0, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        		
        		AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
            	
            	
                break;
            case Activity.RESULT_CANCELED:
            	
                break;                        
        }
	}

	
}
