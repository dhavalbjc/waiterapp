package in.app.waiter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	public static Controller aController;
	// Create a broadcast receiver to get message and show on screen
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			String newMessage = intent.getExtras().getString(
					Config.EXTRA_MESSAGE);

			// Waking up mobile if it is sleeping
			aController.acquireWakeLock(getApplicationContext());

			/*
			 * Toast.makeText(getApplicationContext(), "Got Message: " +
			 * newMessage, Toast.LENGTH_LONG).show();
			 */
			// String[] StringAll;
			// StringAll = newMessage.split("\\^");
			menusynker.sync(newMessage);
			// String name = "";
			// String seq = "";

			// int StringLength = StringAll.length;
			/*
			 * if(StringAll[0].equals("00")) { //name = StringAll[1]; //seq =
			 * StringAll[2]; } int key=Integer.parseInt(StringAll[0]); switch
			 * (key) { case 00: try{ //name = StringAll[1]; //seq =
			 * StringAll[2]; Category cat =new Category(); int
			 * id=Integer.parseInt(StringAll[1]); String name=StringAll[2];
			 * cat.set_id(id); cat.set_name(name); cat.set_sequence(id);
			 * //generateNotification(context, name,message,seq);
			 * //DBAdapter.addCategoryData(cat);
			 *
			 * GamesFragment g=new GamesFragment(); g.addcat(cat);
			 * //GamesFragment.addCategory(name); //cat.set_items(null);
			 *
			 * //GamesFragment.expandAll(); }catch(Exception e) {
			 * Toast.makeText(getApplicationContext(), "Got Error: " +
			 * String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show(); }
			 *
			 * break;
			 *
			 * default: break; }
			 */
			// Releasing wake lock
			aController.releaseWakeLock();
		}
	};
	List<Category> cat;
	Handler mHandler = new Handler();
	boolean isRunning = true;
	boolean secondTimedialog = false;
	FrameLayout nic;
	int id = 0;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	// Tab titlesz
	private WaiterPageAdapter wAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "ORDERS", "TABLES", "MENU", "STAFF", "REPORTS",
			"PRINTING", "RESTAURANT" };
	private String[] wtabs = { "ORDERS", "TABLES", "MENU" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		GCMIntentService.notificationCount = 0;

		aController = (Controller) getApplicationContext();
		// Check if Internet present


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String value = extras.getString("ID");
			// Toast.makeText(getApplicationContext(),"Neworder "+value,Toast.LENGTH_LONG).show();
			id = Integer.parseInt(value);
		}
		// WebServer Request URL to get All registered devices
		if (id == 1) {

			// DBAdapter.deleteAllcart();
			// String serverURL = Config.YOUR_SERVER_URL+"clientSync.php";

			// Use AsyncTask execute Method To Prevent ANR Problem
			// LongOperation serverRequest = new LongOperation();

			// serverRequest.execute(serverURL,"rrr",String.valueOf(DBAdapter.getRestaurantId()),"");
		} else {
			// aController.CartUpdate(MainActivity.this, "Hello");
			if (aController.isConnectingToInternet()) {
				GCMIntentService.secondTime = true;
			}
			// TopRatedFragment.mCart.clear();
			// TopRatedFragment.mCart.addAll(DBAdapter.getAllCartData());
			// TopRatedFragment.mAdapter.notifyDataSetChanged();
		}

	}

	@Override
	protected void onStart() {
		isRunning = true;
		super.onStart();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		// Toast.makeText(getApplicationContext(),"MainActivity",
		// Toast.LENGTH_LONG).show();
		// BroadCase Receiver Intent Object
		Intent alarmIntent = new Intent(getApplicationContext(), SampleBC.class);
		// Pending Intent Object
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, alarmIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		// Alarm Manager Object
		AlarmManager alarmManager = (AlarmManager) getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		// Alarm Manager calls BroadCast for every Ten seconds (10 * 1000),
		// BroadCase further calls service to check if new records are inserted
		// in
		// Remote MySQL DB
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar
				.getInstance().getTimeInMillis() + 5000, 100 * 1000,
				pendingIntent);

		// Register custom Broadcast receiver to show messages on activity
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				Config.MESSAGE_ACTION));

		nic = (FrameLayout) findViewById(R.id.lable_nointernet);
		nic.setVisibility(View.GONE);
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		wAdapter = new WaiterPageAdapter(getSupportFragmentManager());

		// View homeIcon=findViewById(android.R.id.home);
		// ((View) homeIcon.getParent()).setVisibility(View.GONE);

		// actionBar.setHomeButtonEnabled(false);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// whats app 004d40 ,3F92D2
		// getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setSplitBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#3F92D2")));
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#3F92D2")));
		// actionBar.setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#FFFFFF")));
		getActionBar().setStackedBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#DCE1E3")));
		// actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP |
		// ActionBar.DISPLAY_SHOW_CUSTOM |ActionBar.DISPLAY_SHOW_TITLE);
		// ((Object) actionBar).setLeftStripDrawable(new
		// ColorDrawable(Color.parseColor("#ffffff")));

		// actionBar.setBackgroundDrawable(new ColorDrawable(0x3F48CC));
		// actionBar.setHomeAsUpIndicator ( R.drawable.add_arrow );
		// Toast.makeText(getApplicationContext(),
		// "name= "+DBAdapter.getRestaurantName(), Toast.LENGTH_LONG).show();
		// Adding Tabs

		Staff staff = null;
		try{
		 staff = DBAdapter.getStaffData(Main
				.getEmail(getApplicationContext()));
		}catch(Exception e)		{		}
		if (staff != null) {
			if (!staff.get_staff_roll().equals("admin")
					&& !staff.get_staff_roll().equals("owner")
					&& !staff.get_staff_roll().equals("manager")) {

				viewPager.setAdapter(wAdapter);
				for (String tab_name : wtabs) {
					actionBar.addTab(actionBar.newTab().setText(tab_name)
							.setTabListener(this));

					// Tab tab=actionBar
				}
			} else {

				viewPager.setAdapter(mAdapter);
				for (String tab_name : tabs) {
					actionBar.addTab(actionBar.newTab().setText(tab_name)
							.setTabListener(this));
				}
			}
		} else {
			isRunning=false;

			Intent intent = new Intent(getApplicationContext(), Main.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Bundle mBundle = new Bundle();
			mBundle.putString("ID", "1");
			intent.putExtras(mBundle);
			startActivity(intent);
			MainActivity.this.finish();
		}

		// actionBar.getTabAt(0).b
		try{
		if (id == 0) {
			actionBar.setSelectedNavigationItem(id);
		} else {
			actionBar.setSelectedNavigationItem(id - 1);
		}
		}catch(Exception e)
		{
			finish();
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOffscreenPageLimit(1);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		// DBAdapter.init(this);
		// Initilization

		/*new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (id==0) {
					try {
						//Thread.sleep(2000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.
								displayData();

							}
						});
						//Thread.sleep(27000);
						// secondTime=true;

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();*/
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub

					try {
						//Thread.sleep(2000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.
								fillData();

							}

						});
						//Thread.sleep(27000);
						// secondTime=true;

					} catch (Exception e) {
						// TODO: handle exception
					}

			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isRunning) {
					try {

						// Thread.sleep(2000);
						// if(id>1){
						// Thread.sleep(10000);
						// id=0;
						// }
						Thread.sleep(20000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.

								feachdata();
								/*int delay = 1000; // delay for 1 sec.
								int period = 10000; // repeat every 10 sec.
								Timer timer = new Timer();
								timer.scheduleAtFixedRate(new TimerTask()
								    {
								        public void run()
								        {
								        	feachdata();  // display the data
								        }
								    }, delay, period);*/
								// mHandler.postDelayed(mUpdateUI, 1000);
							}
						});

						// secondTime=true;

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isRunning) {
					try {
						// Thread.sleep(2000);
						// if(id>1){
						// Thread.sleep(10000);
						// id=0;
						// }
						//Thread.sleep(3000);
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								// Write your code here to update the UI.

								CheckInternetConnection();
							}
						});
						Thread.sleep(5000);
						// secondTime=true;

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}).start();


	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		// some variable statements...

		switch (item.getItemId()) {
		case android.R.id.home:
isRunning=false;
			Intent intent = new Intent(this, Main.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Bundle mBundle = new Bundle();
			mBundle.putString("ID", "1");
			intent.putExtras(mBundle);
			startActivity(intent);
			MainActivity.this.finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dialogOnBackPress();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	protected void dialogOnBackPress() {

		new AlertDialog.Builder(this)
				.setTitle("Exit")
				.setMessage("Do you want to exit?")

				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MainActivity.this.finish();
							}
						}).setNegativeButton("Cancel", null).show();

	}

	@Override
	protected void onDestroy() {
		try {
			// Unregister Broadcast Receiver
			unregisterReceiver(mHandleMessageReceiver);
			isRunning = false;

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		isRunning = false;
		super.onPause();

	}

	@Override
	protected void onResume() {
		//isRunning = true;
		super.onResume();

	}

	@Override
	protected void onStop() {
		isRunning = false;
		super.onStop();
	}

	public void refreshGrid() {
		aController.CartUpdate(MainActivity.this, "Hello");
	}

	private void CheckInternetConnection() {

		if (!aController.isConnectingToInternet()) {

			/*
			 * aController.showAlertDialog(MainActivity.this,
			 * "Internet Connection Error",
			 * "Please connect to Internet connection", false);
			 */
			nic.setVisibility(View.VISIBLE);

		} else {
			nic.setVisibility(View.GONE);
			if (id == 0) {
				try{
				if (!GCMIntentService.secondTime) {

					String serverURL = Config.YOUR_SERVER_URL + "clientSync.php";

					LongOperation serverRequest = new LongOperation();

					if (Build.VERSION.SDK_INT >= 11) {
						serverRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,serverURL, "0",
								String.valueOf(DBAdapter.getRestaurantId()), "");
					} else {
						serverRequest.execute(serverURL, "0",
								String.valueOf(DBAdapter.getRestaurantId()), "");
					}
					GCMIntentService.secondTime = true;
				} else{

				}
				}catch(Exception e)
				{
					GCMIntentService.secondTime = true;
				}
			} else {
				id = 0;
			}


		}

	}

	private void fillData() {
		//if(GamesFragment.ExpListItems.size()<=0){
		// TODO Auto-generated method stub
		   try{
	        	//Cursor c=DBAdapter.fetchGroup();
	         cat=DBAdapter.getAllCategoryData();
	      //  Toast.makeText(getBaseContext(), "ok",Toast.LENGTH_SHORT).show();
	        }
	        catch(Exception e)
	        {
	        	//Toast.makeText(container.getContext(), e.toString(),Toast.LENGTH_LONG).show();
	        }
	       // ArrayList<Item> it=DBAdapter.getAllItemData();
	        for (Category a1 : cat) {
	        	//Toast.makeText(getBaseContext(), a1.get_name().toString(),Toast.LENGTH_SHORT).show();
	        	GamesFragment.myDepartments.put(a1.get_name(), a1);
	        	try
	        	{
	        	ArrayList<Item> _items=DBAdapter.getItemsbyCatId(a1.get_id());

	        	for(Item x:_items)
	        	{
	        		x.set_category(a1);
	        		try{
	        		ArrayList<Extra> _extra=DBAdapter.getExtrasbyItemId(x.get_id());

	        		x.set_extra(_extra);
	        		for(Extra y:_extra)
	        		{
	        			y.set_item(x);
	        		}
	        		Collections.sort(_extra,new myExtraComparable());
	        		}
	        		catch(Exception e){}
	        	}
	        	Collections.sort(_items,new myItemComparable());
	        	a1.set_items(_items);
	        	}
	        	catch(Exception e)
	        	{
	        	//	Toast.makeText(container.getContext(), e.toString(),Toast.LENGTH_SHORT).show();
	        	}

	        }
	        Collections.sort(cat,new myCateComparable());

	        GamesFragment.ExpListItems= (ArrayList<Category>) cat;
//	}

		}

	private void feachdata() {

		if (!aController.isConnectingToInternet()) {

			/*
			 * aController.showAlertDialog(MainActivity.this,
			 * "Internet Connection Error",
			 * "Please connect to Internet connection", false);
			 */
			nic.setVisibility(View.VISIBLE);

		} else {
			nic.setVisibility(View.GONE);
			if (id == 0) {
				try{
				if (!GCMIntentService.secondTime) {

					String serverURL = Config.YOUR_SERVER_URL + "clientSync.php";

					LongOperation serverRequest = new LongOperation();
					serverRequest.execute();
					if (Build.VERSION.SDK_INT >= 11) {
						serverRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,serverURL, "0",
								String.valueOf(DBAdapter.getRestaurantId()), "");
					} else {
						serverRequest.execute(serverURL, "0",
								String.valueOf(DBAdapter.getRestaurantId()), "");
					}
					GCMIntentService.secondTime = true;
				} else {
					String serverURL = Config.YOUR_SERVER_URL + "ordersync.php";
					LongOperation serverRequest = new LongOperation();


					if (Build.VERSION.SDK_INT >= 11) {
						serverRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverURL, "0",
								String.valueOf(DBAdapter.getRestaurantId()), "");
					} else {
						serverRequest.execute(serverURL, "0",
								String.valueOf(DBAdapter.getRestaurantId()), "");
					}
				}
				}catch(Exception e)
				{
					GCMIntentService.secondTime = true;
				}
			} else {
				id = 0;
			}


		}

	}

	private void displayData() {
		if (!aController.isConnectingToInternet()) {
			if (!secondTimedialog) {
				// Internet Connection is not present
				/*
				 * aController.showAlertDialog(MainActivity.this,
				 * "Internet Connection Error",
				 * "Please connect to Internet connection", false);
				 */
				nic.setVisibility(View.VISIBLE);
				GCMIntentService.secondTime = false;
				secondTimedialog = true;
			} else {
				GCMIntentService.secondTime = false;
				// stop executing code by return
				// return;
				nic.setVisibility(View.VISIBLE);
				// Toast.makeText(this, "Network Not Available",
				// Toast.LENGTH_SHORT).show();
			}
			// myTextView.setText("Network Not Available");
		} else {
			nic.setVisibility(View.GONE);
			// Toast.makeText(this,secondTime+ "Network Available",
			// Toast.LENGTH_SHORT).show();
			//if (!GCMIntentService.secondTime) {

				String serverURL = Config.YOUR_SERVER_URL + "clientSync.php";

				LongOperation serverRequest = new LongOperation();

			if (Build.VERSION.SDK_INT >= 11) {
				serverRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,serverURL, "0",
						String.valueOf(DBAdapter.getRestaurantId()), "");
			} else {
				serverRequest.execute(serverURL, "0",
						String.valueOf(DBAdapter.getRestaurantId()), "");
			}
				GCMIntentService.secondTime = true;
			//}
		}

	}

	// Class with extends AsyncTask class
	public class LongOperation extends AsyncTask<String, Void, String> {

		// Required initialization

		String data = "";
		int sizeData = 0;
		// private final HttpClient Client = new DefaultHttpClient();
		// private Controller aController = null;
		private String Error = null;
		private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

		protected void onPreExecute() {
			// NOTE: You can call UI Element here.

			// Start Progress Dialog (Message)

			Dialog.setMessage("Fetching Orders");
			if (!GCMIntentService.secondTime) {
				Dialog.show();
			}

		}

		// Call after onPreExecute method
		protected String doInBackground(String... params) {

			// syncSQLiteMySQLDB();
			/************ Make Post Call To Web Server ***********/
			BufferedReader reader = null;
			String Content = "";
			// Send data
			try {
				// Defined URL where to send data
				URL url = new URL(params[0]);

				// Set Request parameter
				if (!params[1].equals(""))
					data += "&" + URLEncoder.encode("data", "UTF-8") + "="
							+ params[1].toString();
				if (!params[2].equals(""))
					data += "&" + URLEncoder.encode("data2", "UTF-8") + "="
							+ params[2].toString();
				if (!params[3].equals(""))
					data += "&" + URLEncoder.encode("data3", "UTF-8") + "="
							+ params[3].toString();
				Log.i("GCM", data);

				// Send POST data request

				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				conn.setConnectTimeout(15000);
				conn.setReadTimeout(15000);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();

				// Get the server response

				reader = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;

				// Read Server Response
				while ((line = reader.readLine()) != null) {
					// Append server response in string
					sb.append(line + "\n");
				}

				// Append Server Response To Content String
				Content = sb.toString();
			} catch (java.net.SocketTimeoutException e) {
				Error = e.getMessage();

			} catch (java.io.IOException e) {
				Error = e.getMessage();

			} catch (Exception ex) {
				Error = ex.getMessage();
			} finally {
				try {

					reader.close();
				}

				catch (Exception ex) {
				}
			}

			/*****************************************************/
			return Content;
			// return "";
		}

		protected void onPostExecute(String Content) {
			// NOTE: You can call UI Element here.

			// Close progress dialog

			// Toast.makeText(getApplicationContext(), Content,
			// Toast.LENGTH_LONG).show();
			if (Error != null) {

			} else {

				// Show Response Json On Screen (activity)

				/****************** Start Parse Response JSON Data *************/
				aController.clearUserData();

				JSONObject jsonResponse;

				try {

					/******
					 * Creates a new JSONObject with name/value mappings from
					 * the JSON string.
					 ********/
					jsonResponse = new JSONObject(Content);

					/*****
					 * Returns the value mapped by name if it exists and is a
					 * JSONArray.
					 ***/
					/******* Returns null otherwise. *******/

					/*********** Process each JSON Node ************/

					JSONArray jsonMainNode = jsonResponse
							.optJSONArray("CartItems");
					if (jsonMainNode != null) {
						DBAdapter.deleteAllcartItem();
						manager.syncCartItemsJson(jsonMainNode);
					}

					jsonMainNode = jsonResponse.optJSONArray("Cart");
					if (jsonMainNode != null) {
						DBAdapter.deleteAllcart();
						manager.syncCartJson(jsonMainNode);
					}

					// if(id==0){
					// if(!GCMIntentService.secondTime){
					jsonMainNode = jsonResponse.optJSONArray("Category");
					if (jsonMainNode != null) {
						manager.syncCatJson(jsonMainNode);
					}

					jsonMainNode = jsonResponse.optJSONArray("Item");
					if (jsonMainNode != null) {
						manager.syncItemJson(jsonMainNode);
					}

					jsonMainNode = jsonResponse.optJSONArray("Extra");
					if (jsonMainNode != null) {
						manager.syncExtraJson(jsonMainNode);
					}

					jsonMainNode = jsonResponse.optJSONArray("Staff");
					if (jsonMainNode != null) {
						manager.syncStaffJson(jsonMainNode);
					}

					jsonMainNode = jsonResponse.optJSONArray("Echarge");
					if (jsonMainNode != null) {
						DBAdapter.deleteAllEcharge();
						manager.syncEchargeJson(jsonMainNode);
					}

					jsonMainNode = jsonResponse.optJSONArray("Printer");
					if (jsonMainNode != null) {
						DBAdapter.deleteAllPrinter();
						manager.syncPrinterJson(jsonMainNode);
					}

					// }
					aController.CartUpdate(MainActivity.this, "Hello");
					/****************** End Parse Response JSON Data *************/

					// Add user data to controller class UserDataArr arraylist
					// gridView.setAdapter(new
					// CustomGridAdapter(getBaseContext(), aController));

				} catch (JSONException e) {

					e.printStackTrace();
				}

			}

			Dialog.dismiss();

		}

		public void syncSQLiteMySQLDB() {
			try {
				ArrayList<HashMap<String, String>> wordList;
				wordList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> map = new HashMap<String, String>();

				map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
				wordList.add(map);
				Gson gson = new GsonBuilder().create();
				crudonserver crudop = new crudonserver();
				crudop.callClientSync(gson.toJson(wordList), getBaseContext());

			} catch (Exception e) {
				// error=String.valueOf(e.getMessage());
				// Toast.makeText(context,Toast.LENGTH_LONG).show();
			}
			// Create AsycHttpClient object
			/*
			 * AsyncHttpClient client = new AsyncHttpClient(); RequestParams
			 * params = new RequestParams(); // ArrayList<HashMap<String,
			 * String>> userList = DBAdapter.getAllUserData();
			 *
			 *
			 * // prgDialog.show();
			 *
			 * // gson.toJson(wordList); ArrayList<HashMap<String, String>>
			 * wordList; wordList = new ArrayList<HashMap<String, String>>();
			 * HashMap<String, String> map = new HashMap<String, String>();
			 * map.put("name","Dhaval"); map.put("email", "ssdsa");
			 * map.put("regId","0000asdsad000"); map.put("imei", "000000");
			 * wordList.add(map); Gson gson = new GsonBuilder().create();
			 * params.put("usersJSON", gson.toJson(wordList));
			 * client.post("http://10.0.0.6/d2dgcm/insertuser.php",params ,new
			 * AsyncHttpResponseHandler() {
			 *
			 * @Override public void onSuccess(String response) { //
			 * System.out.println(response); }
			 *
			 * @Override public void onFailure(int statusCode, Throwable error,
			 * String content) { // TODO Auto-generated method stub
			 *
			 * } } );
			 */
		}
	}

}