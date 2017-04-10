package in.app.waiter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SampleBC extends BroadcastReceiver {
	static int noOfTimes = 0;
	private GcmKeepAlive gcmKeepAlive;
	// Method gets called when Broad Case is issued from MainActivity for every 10 seconds
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		noOfTimes++;
		//Toast.makeText(context, "BC Service Running for " + noOfTimes + " times", Toast.LENGTH_SHORT).show();
		  gcmKeepAlive = new GcmKeepAlive(context);
	        gcmKeepAlive.broadcastIntents();
        // Checks if new records are inserted in Remote MySQL DB to proceed with Sync operation
       }	
}
