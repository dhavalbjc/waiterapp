package in.app.waiter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

public class ServerRegister extends Activity {
	public static String name;
	public static String email;
	public static String imei;
	public static String from;
	// label to display gcm messages
	//TextView lblMessage;
	Controller aController;
	// Create a broadcast receiver to get message and show on screen
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);

			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());

			final ProgressDialog Dialog = ProgressDialog.show(ServerRegister.this, "Registering", newMessage, false);

			// Display message on the screen
			//lblMessage.append(newMessage + "\n");

		/*	Toast.makeText(getApplicationContext(),
					"Got Message: " + newMessage,
					Toast.LENGTH_LONG).show();
			*/
			// Releasing wake lock
			//finish();
			// displayRegistrationMessageOnScreen(context, message);
			if(newMessage.equals(context.getString(R.string.server_registered))){
			 Intent i1 = new Intent(ServerRegister.this, SplashScreen.class);
				i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Bundle mBundle = new Bundle();
   	        	mBundle.putString("ID", "1");
   	        	i1.putExtras(mBundle);
				startActivity(i1);
				Dialog.dismiss();
				finish();
			}
			aController.releaseWakeLock();
		}
	};
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		// Get Global Controller Class object
		// (see application tag in AndroidManifest.xml)
		aController = (Controller) getApplicationContext();


		// Check if Internet present
		if (!aController.isConnectingToInternet()) {

			// Internet Connection is not present
			aController.showAlertDialog(ServerRegister.this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			// stop executing code by return
			return;
		}

		String deviceIMEI = "";
		if(Config.SECOND_SIMULATOR){

			//Make it true in CONFIG if you want to open second simutor
			// for testing actually we are using IMEI number to save a unique device

			deviceIMEI = "000000000000001";
		}
		else
		{
		  // GET IMEI NUMBER
			try{
		/* TelephonyManager tManager = (TelephonyManager) getBaseContext()
		    .getSystemService(Context.TELEPHONY_SERVICE);
		  deviceIMEI = tManager.getDeviceId(); */
				WifiManager m_wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
				 deviceIMEI = m_wm.getConnectionInfo().getMacAddress();
			}catch(Exception e){
				 deviceIMEI= "000000000000001";
			}
		}

		 // Getting name, email from intent
		Intent i = getIntent();

		name = i.getStringExtra("name");
		email = i.getStringExtra("email");
		imei  = deviceIMEI;
		//Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest permissions was properly set
		GCMRegistrar.checkManifest(this);

		//lblMessage = (TextView) findViewById(R.id.lblMessage);

		// Register custom Broadcast receiver to show messages on activity
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Config.DISPLAY_REGISTRATION_MESSAGE_ACTION));

		// Get GCM registration id
		final String regId = GCMRegistrar.getRegistrationId(this);

		// Check if regid already presents
		if (regId.equals("")) {
			//Toast.makeText(getApplicationContext(), "GCM K"+ "--- Regid = ''"+regId, Toast.LENGTH_LONG).show();
			 Log.i("GCM K", "--- Regid = ''"+regId);
			// Register with GCM
			GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
			//Toast.makeText(getApplicationContext(), "GCM K"+ "--- Regid2 = ''"+regId, Toast.LENGTH_LONG).show();
			//dhaval
			//String Id = GCMRegistrar.getRegistrationId(this);
			//aController.register(getBaseContext(), ServerRegister.name, ServerRegister.email, Id,ServerRegister.imei);
			//DBAdapter.addDeviceData(ServerRegister.name, ServerRegister.email, Id, ServerRegister.imei);

		} else {

			// Device is already registered on GCM Server
			if (GCMRegistrar.isRegisteredOnServer(this)) {

				final Context context = this;
				// Skips registration.
				/*Toast.makeText(getApplicationContext(),
						"Already registered with GCM Server",
						Toast.LENGTH_LONG).show();
				Log.i("GCM K", "Already registered with GCM Server");*/
				//Intent i1 = new Intent(getApplicationContext(), GridViewExample.class);

				//startActivity(i1);
			//	GCMRegistrar.unregister(context);
				final Context context1 = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						// Register on our server
						// On server creates a new user

						aController.register(context1, name, email, regId,imei);
						//finish();
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;

						finish();
					}

				};

				// execute AsyncTask
				//mRegisterTask.execute(null, null, null);
				if (Build.VERSION.SDK_INT >= 11) {
					mRegisterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null, null, null);
				} else {
					mRegisterTask.execute(null, null, null);
				}
			} else {

				Log.i("GCM K", "-- gO for registration--");
			//	Toast.makeText(getApplicationContext(), "gO for registration ", Toast.LENGTH_LONG).show();
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;


				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						// Register on our server
						// On server creates a new user


						aController.register(context, name, email, regId,imei);
						//finish();
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;

						finish();
					}

				};

				// execute AsyncTask
				//mRegisterTask.execute(null, null, null);
				if (Build.VERSION.SDK_INT >= 11) {
					mRegisterTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null, null, null);
				} else {
					mRegisterTask.execute(null, null, null);
				}
			}
		}
		//Dialog.dismiss();
	}
	
	@Override
	protected void onDestroy() {
		// Cancel AsyncTask
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);
			
			//Clear internal resources.
			GCMRegistrar.onDestroy(this);
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
