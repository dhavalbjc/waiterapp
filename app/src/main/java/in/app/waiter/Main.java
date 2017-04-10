package in.app.waiter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.ArrayList;



public class Main extends Activity {
	
	// label to display gcm messages
	TextView lblMessage;
	Controller aController;
	public static final int REQUEST_GET_ACCOUNT = 112;
	String email="";
	// Asyntask
		AsyncTask<Void, Void, Void> mRegisterTask;
		 int id=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		 //getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().hide();
		   // getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F92D2")));
		/******************* Intialize Database *************/
		//try{
		DBAdapter.init(this);
		PermissionManager.check(this, android.Manifest.permission.GET_ACCOUNTS, REQUEST_GET_ACCOUNT);
		/*}catch(Exception e)
		{
			Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();	
		}*/

	
	}		
	@Override
    protected void onStart() {
       // TODO Auto-generated method stub
       super.onStart(); 
       Bundle extras = getIntent().getExtras();
       if (extras != null) {
     // String value = extras.getString("ID");
     // Toast.makeText(getApplicationContext(),"Neworder "+value,Toast.LENGTH_LONG).show();
     // id = Integer.parseInt(value);
      id=1;
       }
       
		// Get Global Controller Class object 
		// (see application tag in AndroidManifest.xml)
		aController = (Controller) getApplicationContext();
		
		
		// Check if Internet present
		if (!aController.isConnectingToInternet()) {
			
			// Internet Connection is not present
			aController.showAlertDialog(Main.this,
					"Internet Connection Error",
					"Please connect to Internet connection", false);
			// stop executing code by return
			//return;
		}
	Button login=(Button) findViewById(R.id.Login_bt);
	login.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (!aController.isConnectingToInternet()) {
				
				// Internet Connection is not present
				aController.showAlertDialog(Main.this,
						"Internet Connection Error",
						"Please connect to Internet connection", false);
				// stop executing code by return
				//return;
			}else{
			String deviceIMEI = "";
			if(Config.SECOND_SIMULATOR){
				
				//Make it true in CONFIG if you want to open second simutor
				// for testing actually we are using IMEI number to save a unique device
				
				deviceIMEI = "000000000000011";
			}	
			else
			{
			  // GET IMEI NUMBER      
				try{


					WifiManager m_wm = (WifiManager)getSystemService(Context.WIFI_SERVICE); 
					 deviceIMEI = m_wm.getConnectionInfo().getMacAddress();


/*			 TelephonyManager tManager = (TelephonyManager) getBaseContext()
			    .getSystemService(Context.TELEPHONY_SERVICE);
			  deviceIMEI = tManager.getDeviceId(); */
				}catch(Exception e)
				{
					//Toast.makeText(C, text, duration)
					deviceIMEI = "000000000000011";	
				}
			}
			//Toast.makeText(getApplicationContext(), String.valueOf(Main.getEmail(getBaseContext())),Toast.LENGTH_LONG).show();
           
			/******* Validate device from server ******/
			// WebServer Request URL
	        String serverURL = Config.YOUR_SERVER_URL+"validate_member.php";
	      
	        // Use AsyncTask execute Method To Prevent ANR Problem
	        LongOperation serverRequest = new LongOperation(); 
	        try{
				email=getEmail(getApplicationContext());
	        //serverRequest.execute(serverURL,Main.getEmail(getBaseContext()),deviceIMEI,"");

				if (Build.VERSION.SDK_INT >= 11) {
					 serverRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,serverURL,email,deviceIMEI,"");
				} else {
					serverRequest.execute(serverURL,email,deviceIMEI,"");
				}
	        }catch(Exception e)
	        {
	        	Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
	        	finish();
	        }
		}
			
		}
	});
		//Check device contains self information in sqlite database or not. 
		int vDevice = DBAdapter.validateDevice();	
		//Toast.makeText(getApplicationContext(), String.valueOf(vDevice),Toast.LENGTH_LONG).show();
		if(vDevice > 0 && id==0)
		{	
			// Launch Main Activity
			Intent i = new Intent(getApplicationContext(), SplashScreen.class);
			 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
		else
		{

			
		}
       // Toast.makeText(LifeCycleActivity.this,"ON START", Toast.LENGTH_SHORT).show();
    } 
	

	protected void onDestroy() {
		
		super.onDestroy();
	}
	 public  int getWidth()
	 {try 
     {
         
		 Display display =(Main.this).getWindowManager().getDefaultDisplay();
	     Point screenSize = new Point();
	     display.getSize(screenSize);
	     int width = screenSize.x;
		 WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
	    // layoutParams.copyFrom(dialog.getWindow().getAttributes());
	     layoutParams.width = (int) (width - (width * 0.05) ); 
	     //layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		return  layoutParams.width;
     }
	 catch(Exception e)
	 {
		 //Toast.makeText(getActivity(),String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
		 return  400; 
	 }
	 }
	 static String getEmail(Context context) {
		    AccountManager accountManager = AccountManager.get(context);
		    Account account = getAccount(accountManager); 
		    if (account == null) {
		        return null; 
		    } else {
		        return account.name; 
		    } 
		} 
		private static Account getAccount(AccountManager accountManager) { 
		    Account[] accounts = accountManager.getAccountsByType("com.google");
		    Account account;
		    if (accounts.length > 0) { 
		        account = accounts[0]; 
		    } else {
		        account = null; 
		    } return account;
		}
		public void registerUserONGcm()
		{
			//Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(this);

			// Make sure the manifest permissions was properly set 
			GCMRegistrar.checkManifest(this);
			// Get GCM registration id
			final String regId = GCMRegistrar.getRegistrationId(this);
			//Toast.makeText(getBaseContext(),regId,Toast.LENGTH_LONG).show();
			
			// Check if regid already presents
			if (regId.equals("")) {
			//	Toast.makeText(getApplicationContext(), "GCM K"+ "--- Regid = ''"+regId, Toast.LENGTH_LONG).show();
				 Log.i("GCM K", "--- Regid = ''"+regId);
				// Register with GCM		
				 GCMRegistrar.unregister(this);
				 GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);
				//Toast.makeText(getApplicationContext(), "GCM K"+ "--- Regid2 = ''"+regId, Toast.LENGTH_LONG).show();
				//dhaval
				
				
				
			} else {
				
				// Device is already registered on GCM Server
				if (GCMRegistrar.isRegisteredOnServer(this)) {
					
					final Context context = this;
					// Skips registration.				
					/*Toast.makeText(getApplicationContext(), 
							"Already registered with GCM Server", 
							Toast.LENGTH_LONG).show();*/
					Log.i("GCM K", "Already registered with GCM Server");
					//Intent i1 = new Intent(getApplicationContext(), GridViewExample.class);
					
					//startActivity(i1);
				//	GCMRegistrar.unregister(context);
					    Intent i1 = new Intent(getApplicationContext(), SplashScreen.class);
					    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						Bundle mBundle = new Bundle();
						mBundle.putString("ID", "9");
						i1.putExtras(mBundle);
            			startActivity(i1);
            			//startActivity(i1);
            			finish();
					
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
							aController.register(context, ServerRegister.name, ServerRegister.email, regId,ServerRegister.imei); 
							
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mRegisterTask = null;
							 Intent i1 = new Intent(getApplicationContext(), SplashScreen.class);
							 i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								Bundle mBundle = new Bundle();
								mBundle.putString("ID", "9");
								i1.putExtras(mBundle);
		            			startActivity(i1);
		            			finish();
							//finish();
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
		}
	@Override
	public
	// Class with extends AsyncTask class
	public class LongOperation  extends AsyncTask<String, Void, String> {

	        // Required initialization

	        //private ProgressDialog Dialog = new ProgressDialog(Main.this);
	        final ProgressDialog Dialog1 = ProgressDialog.show(Main.this, "Please wait ...", "Fetching Restaurant...", false);
	        String data ="";
	        int sizeData = 0;
	       //private final HttpClient Client = new DefaultHttpClient();
	       // private Controller aController = null;
	        private String Error = null;

	        protected void onPreExecute() {
	            // NOTE: You can call UI Element here.

	            //Start Progress Dialog (Message)

	            //Dialog.setMessage("Fetching Restaurant...");
	            //Dialog.show();

	        }

	        // Call after onPreExecute method
	        protected String doInBackground(String... params) {

	        	/************ Make Post Call To Web Server ***********/
	        	BufferedReader reader=null;
	        	String Content = "";
		             // Send data
		            try{

		            	// Defined URL  where to send data
		            	//  URL url = new URL(params[0]);
			            // Set Request parameter
			            if(!params[1].equals(""))
		               	   data +="&" + URLEncoder.encode("email", "UTF-8") + "="+params[1].toString();
			            if(!params[2].equals(""))
			               	   data +="&" + URLEncoder.encode("imei", "UTF-8") + "="+params[2].toString();
			           /* if(!params[3].equals(""))
			               	   data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();*/
		              Log.i("GCM",data);
			      /*
			          // Send POST data request
		              URLConnection conn = url.openConnection();
		              conn.setDoOutput(true);
		              OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		              wr.write( data );
		              wr.flush();
		              // Get the server response

		              reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		              StringBuilder sb = new StringBuilder();
		              String line = null;
			            // Read Server Response
			            while((line = reader.readLine()) != null)
			                {
			                       // Append server response in string
			                       sb.append(line + "\n");
			                }

		                // Append Server Response To Content String
		               Content = sb.toString();*/
		              JSONParser jParser = new JSONParser();
		              try{
		              Content = jParser.excutePost(params[0],data);
		              }
		              catch(Exception ex)
			            {
			            	Error = ex.getMessage();
			            }

		            }
		            catch(Exception ex)
		            {
		            	Error = ex.getMessage();
		            }
		            finally
		            {
		                try
		                {

		                  //  reader.close();
		                }

		                catch(Exception ex) {}
		            }

	            /*****************************************************/

	            return Content;
	        }

	        protected void onPostExecute(String Content) {
	            // NOTE: You can call UI Element here.

	            // Close progress dialog
	        	//Toast.makeText(getApplicationContext(), Content,Toast.LENGTH_LONG).show();
	            Dialog1.dismiss();
	            if (Error != null) {
	            	Toast.makeText(getApplicationContext(), "Could not connect to server",Toast.LENGTH_LONG).show();
	            } else {

	            	// Show Response Json On Screen (activity)

	             /****************** Start Parse Response JSON Data *************/
	            	aController.clearUserData();

	            	JSONObject jsonResponse;
	                try {

	                     /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
	                /*	Content = Content.replaceFirst("<font>.*?</font>", "");
	                	//Content = Content.replaceFirst("{main}()", "");
	                	int jsonStart = Content.indexOf("{");
	                	int jsonEnd = Content.lastIndexOf("}");

	                	if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
	                	    Content = Content.substring(jsonStart, jsonEnd + 1);
	                	} else {
	                	    // deal with the absence of JSON content here
	                	}*/

	                     jsonResponse = new JSONObject(Content);

	                     /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
	                     /*******  Returns null otherwise.  *******/
	                     JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");

	                     /*********** Process each JSON Node ************/
	                   //  Toast.makeText(getApplicationContext(), "1",Toast.LENGTH_LONG).show();
	                     int lengthJsonArr = jsonMainNode.length();
	                     ArrayList<Restaurant> arrayList=new ArrayList<Restaurant>();
	                     String total="0";
	                     String gcm_regid="";
	                     String stafName="";
	                     String staffEmail="";
	                     String deviceImei="";
	                     for(int i=0; i < lengthJsonArr; i++)
	                     {
	                         /****** Get Object for each JSON node.***********/
	                         JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

	                         /******* Fetch node values **********/
	                         String Status       = jsonChildNode.optString("status").toString();

	                         Log.i("GCM","---"+Status);
	                      // toast.makeText(getApplicationContext(), String.valueOf(Status),Toast.LENGTH_LONG).show();
	                         // IF server response status is update

	                        // Restros restros=new Restros();

	                         if(Status.equals("update")){

	                        	String RegID      = jsonChildNode.optString("rid").toString();
	                            String Name       = jsonChildNode.optString("r_name").toString();
	                            String r_description       = jsonChildNode.optString("r_description").toString();
	                            String r_phone       = jsonChildNode.optString("r_phone").toString();
	                            String r_website       = jsonChildNode.optString("r_website").toString();
	                            String r_street       = jsonChildNode.optString("r_street").toString();
	                            String r_civicnumber       = jsonChildNode.optString("r_civicnumber").toString();
	                            String r_zipcode       = jsonChildNode.optString("r_zipcode").toString();
	                            String r_city       = jsonChildNode.optString("r_city").toString();
	                            String r_state       = jsonChildNode.optString("r_state").toString();
	                            String r_country       = jsonChildNode.optString("r_country").toString();

	                            String Email      = jsonChildNode.optString("r_email").toString();
	                            String gcm      = jsonChildNode.optString("gcm_regid").toString();
	                            String sname      = jsonChildNode.optString("s_fname").toString();
	                            String semail      = jsonChildNode.optString("s_email").toString();
	                            String imei      = jsonChildNode.optString("imei").toString();

	                            gcm_regid=gcm;
	                           stafName=sname;
	                           staffEmail=semail;
	                           deviceImei=imei;
	                           // String IMEI       = jsonChildNode.optString("imei").toString();

	                           // add device self data in sqlite database
	                           // DBAdapter.addDeviceData(Name, Email,RegID, IMEI);

	                        	 //Restaurant restro=new Restaurant(Integer.parseInt(RegID), Name, Email);

	                        	 Restaurant restro=new Restaurant(Integer.parseInt(RegID), Name,r_description,r_phone,r_website,Email,r_street,r_civicnumber,r_zipcode,r_city,r_state,r_country);
	                        	// Toast.makeText(getApplicationContext(), Name,Toast.LENGTH_LONG).show();

	                        	// restros.setRestro(restro);
	                        	 arrayList.add(restro);
	                        	// DBAdapter.addRestaurantData(restro);
	                        	  total   = jsonChildNode.optString("Total").toString();
	                        	/* if(Integer.parseInt(total)>1)
	                        	 {
	                        		 try{

	                     				final Dialog dialog = new Dialog(Main.this);
	                     				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	                     				 dialog.setContentView(R.layout.staff_delete);

	                     				 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
	                     				 dialog.show();

	                     			}catch(Exception e)
	                     			{
	                     				Toast.makeText(getApplicationContext(), String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
	                     			}
	                        	 }*/
	                            // Launch GridViewExample Activity
	                			//Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
	                			//startActivity(i1);
	                			//finish();
	                           // Log.i("GCM","---"+Name);
	                         }
	                         else if(Status.equals("install")){

	                        	 // Launch RegisterActivity Activity
		                		Intent i1 = new Intent(getApplicationContext(), RegisterActivity.class);
		                		startActivity(i1);
		                		finish();

	                         }

	                    }
	                     if(Integer.parseInt(total)>1){
	                    	 final String gcm=gcm_regid;
	                    	 final String s_name=stafName;
	                    	 final String s_email=staffEmail;
	                    	 final String imei=deviceImei;
	                         final Dialog dialog = new Dialog(Main.this);
	            			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	            			 dialog.setContentView(R.layout.select_restro);
	            			 final SelectRestroAdapter adapter = new SelectRestroAdapter(Main.this,arrayList);

	            		        final ListView listView = (ListView) dialog.findViewById(R.id.lv_select_restro);
	            		        listView.setAdapter(adapter);
	            		        listView.setOnItemClickListener(new OnItemClickListener() {

	            				@Override
	            				public void onItemClick(AdapterView<?> parent, View v,
	            						int groupPosition, long id) {
	            					Restaurant Restro=(Restaurant)parent.getItemAtPosition(groupPosition);

	            				//	Toast.makeText(getBaseContext(),String.valueOf(Restro.get_restro_id()),Toast.LENGTH_LONG).show();
	            					if(gcm.trim().equals(""))
	            					{
	            						String deviceIMEI = "";

	            						if(Config.SECOND_SIMULATOR){

	            							//Make it true in CONFIG if you want to open second simutor
	            							// for testing actually we are using IMEI number to save a unique device

	            							deviceIMEI = "000000000000001";
	            						}
	            						else
	            						{try{
	            						  // GET IMEI NUMBER
	            					/*	 TelephonyManager tManager = (TelephonyManager) getBaseContext()
	            						    .getSystemService(Context.TELEPHONY_SERVICE);
	            						  deviceIMEI = tManager.getDeviceId(); */
	            							WifiManager m_wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
	            							 deviceIMEI = m_wm.getConnectionInfo().getMacAddress();
	            						}catch(Exception e)
	            						{
	            							deviceIMEI = "000000000000001";
	            						}
	            						}
	            						ServerRegister.name=s_name ;
	            						ServerRegister.email=s_email;
	            						ServerRegister.imei=deviceIMEI;
	            						ServerRegister.from="invite";
	            						//Toast.makeText(getBaseContext(),"Go for Reg",Toast.LENGTH_LONG).show();

	            						registerUserONGcm();

	            					}
	            					else{try{
	            						DBAdapter.deleteAllRestro();
		            					DBAdapter.deleteAllCategory();
		            					DBAdapter.deleteAllItem();
		            					DBAdapter.deleteAllExtra();
		            					DBAdapter.deleteAllcart();
		            					DBAdapter.deleteAllItem();
		            					DBAdapter.deleteAllStaff();
		            					DBAdapter.deleteAllEcharge();
		            					DBAdapter.deleteAllPrinter();
		            					DBAdapter.addRestaurantData(Restro);
		            					GCMIntentService.secondTime=false;}
	            					catch(Exception e)
	            					{

	            					}
	            				//	if(Integer.parseInt(imei)==0){
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
	            								deviceIMEI = "000000000000001";
	            							}
	            						}
	            						ServerRegister.name=s_name ;
	            						ServerRegister.email=s_email;
	            						ServerRegister.imei=deviceIMEI;
	            						ServerRegister.from="invite";
	            					registerUserONGcm();
	            					//}
	            					//	Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
			                		//	startActivity(i1);
			                		//	finish();
	            					}

		                           // Log.i("GCM","---"+Name);

	            				}
	            			});

	            			 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
	            			 dialog.show();

	                         }else if(Integer.parseInt(total)==1)
	                         {
	                        	 final String gcm=gcm_regid;
	                        	 final String s_name=stafName;
		                    	 final String s_email=staffEmail;
		                    	 final String imei=deviceImei;
	                        	 //Toast.makeText(getBaseContext(),String.valueOf(imei),Toast.LENGTH_LONG).show();

	                        	 if(gcm.trim().equals(""))
	            					{
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
	            								deviceIMEI = "000000000000001";
	            							}
	            						}
	            						ServerRegister.name=s_name ;
	            						ServerRegister.email=s_email;
	            						ServerRegister.imei=deviceIMEI;
	            						ServerRegister.from="invite";
	            						registerUserONGcm();
	            					}
	                        	 else{
	                        		 DBAdapter.deleteAllRestro();
			                    	 DBAdapter.deleteAllCategory();
		            					DBAdapter.deleteAllItem();
		            					DBAdapter.deleteAllExtra();
		            					DBAdapter.deleteAllcart();
		            					DBAdapter.deleteAllItem();
		            					DBAdapter.deleteAllStaff();
		            					DBAdapter.deleteAllEcharge();
		            					DBAdapter.deleteAllPrinter();
		                        	 DBAdapter.addRestaurantData(arrayList.get(0));
		                        	 GCMIntentService.secondTime=false;
		                        	// if(Integer.parseInt(imei)==0){
		                        		 try{
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
			            					/*	 TelephonyManager tManager = (TelephonyManager) getBaseContext()
			            						    .getSystemService(Context.TELEPHONY_SERVICE);
			            						  deviceIMEI = tManager.getDeviceId(); */
			            								WifiManager m_wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			            								 deviceIMEI = m_wm.getConnectionInfo().getMacAddress();
			            							}catch(Exception e)
			            							{
			            								deviceIMEI = "000000000000001";
			            							}
			            						}
			            						ServerRegister.name=s_name ;
			            						ServerRegister.email=s_email;
			            						ServerRegister.imei=deviceIMEI;
			            						ServerRegister.from="invite";
			            						//Toast.makeText(getBaseContext(),"Go for Reg",Toast.LENGTH_LONG).show();

			            					registerUserONGcm();
		                        		 }
		                        		 catch(Exception e)
		                        		 {
		                        			 Toast.makeText(getApplicationContext(), String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
		                        		 }
			            					}
	                        		 //   Intent i1 = new Intent(getApplicationContext(), MainActivity.class);
			                		//	startActivity(i1);
			                		//	finish();
	            					}

	                         //}
	                         else{

	                         }

	                 /****************** End Parse Response JSON Data *************/


	                 } catch (JSONException e) {
	                	 e.printStackTrace();
	                 }


	             }
	        }

	    }




	@Override void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if(requestCode==REQUEST_GET_ACCOUNT){//response for SMS permission request
			if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
				//What to do if User allowed SMS permission
				email=getEmail(getApplicationContext());

			}else{
				//What to do if user disallowed requested SMS permission
				if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
					Toast.makeText(this, "This application needs permission to read your EMAIL.", Toast.LENGTH_LONG)
							.show();
				}
			}
		}
	}
}
