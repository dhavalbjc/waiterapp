package in.app.waiter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SplashScreen extends Activity {
	 
    public static Controller aController;
    String now_playing, earned;
    int id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        aController = (Controller) getApplicationContext();
        
        /**
         * Showing splashscreen while making network calls to download necessary
         * data before launching the app Will use AsyncTask to make http call
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
       String value = extras.getString("ID");
      // Toast.makeText(getApplicationContext(),"Neworder "+value,Toast.LENGTH_LONG).show();
       id = Integer.parseInt(value);
       }
       try{
        	
        if(!GCMIntentService.secondTime && aController.isConnectingToInternet()){
    	//   if(aController.isConnectingToInternet()&& id==9){	
        //new PrefetchData().execute();
			if (Build.VERSION.SDK_INT >= 11) {
				new PrefetchData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			} else {
				new PrefetchData().execute();
			}
        }
        else{
        	
       	// GCMIntentService.secondTime=true;
       	 //}
       Intent i = new Intent(SplashScreen.this, MainActivity.class);
       
       startActivity(i);

       // close this activity
       finish();
        }
         }catch(Exception e)
         {
			 if (Build.VERSION.SDK_INT >= 11) {
				 new PrefetchData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			 } else {
				 new PrefetchData().execute();
			 }
        	
        	 
         }
 
    }
 
    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
 
    	 private String Error = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls         
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            /*
             * Will make http call here This call will download required data
             * before launching the app 
             * example: 
             * 1. Downloading and storing in SQLite 
             * 2. Downloading images 
             * 3. Fetching and parsing the xml / json 
             * 4. Sending device information to server 
             * 5. etc.,
             */
            JSONParser jsonParser = new JSONParser();
            String serverURL = Config.YOUR_SERVER_URL+"clientSync.php";
            try {
           // String data ="&" + URLEncoder.encode("data", "UTF-8") + "="+"0";
            	String data ="&" + URLEncoder.encode("data2", "UTF-8") + "="+String.valueOf(DBAdapter.getRestaurantId());	
	            
            String json;
			
				//json = jsonParser.getJSONFromUrl2(serverURL,data);
		    	BufferedReader reader=null;
	        	String Content = "";
	        	 URL url = new URL(serverURL);
	         	
	             
	             // Send POST data request

	             URLConnection conn = url.openConnection(); 
	             conn.setDoOutput(true); 
	             conn.setConnectTimeout(15000);
	             conn.setReadTimeout(20000);
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
	               json = sb.toString();
	              
	     

			//Error=String.valueOf(DBAdapter.getRestaurantId());
 
            Log.e("Response: ", "> " + json);
 
            if (json != null) {
                JSONObject jsonResponse;
				
				try { 
				      
				     /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
				     jsonResponse = new JSONObject(json);
				      
				     /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
				     /*******  Returns null otherwise.  *******/
				    
				     /*********** Process each JSON Node ************/
				     
				    // Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
				     
				     
				
				     JSONArray jsonMainNode = jsonResponse.optJSONArray("Category");
				     
				     if(jsonMainNode!=null){
				     manager.syncCatJson(jsonMainNode);
				     }
				     jsonMainNode = jsonResponse.optJSONArray("Item");
				     if(jsonMainNode!=null){
				     manager.syncItemJson(jsonMainNode);
				     }
				     
				     jsonMainNode = jsonResponse.optJSONArray("Extra");
				     if(jsonMainNode!=null){
				     manager.syncExtraJson(jsonMainNode);
				     }
				   
				     jsonMainNode = jsonResponse.optJSONArray("Staff");
				     if(jsonMainNode!=null){
				     manager.syncStaffJson(jsonMainNode);
				     }
				    
				    
                     jsonMainNode = jsonResponse.optJSONArray("Echarge");
                     if(jsonMainNode!=null){
                    	 DBAdapter.deleteAllEcharge();
                     manager.syncEchargeJson(jsonMainNode);
                     }
                     jsonMainNode = jsonResponse.optJSONArray("Printer");
                     if(jsonMainNode!=null){
                    	 DBAdapter.deleteAllPrinter();
                     manager.syncPrinterJson(jsonMainNode);
                     }
                       jsonMainNode = jsonResponse.optJSONArray("CartItems");
				     if(jsonMainNode!=null){
				     DBAdapter.deleteAllcartItem();
				     manager.syncCartItemsJson(jsonMainNode);
				     }
				   			    
				     jsonMainNode = jsonResponse.optJSONArray("Cart");
				     if(jsonMainNode!=null){
				    	 DBAdapter.deleteAllcart();
				     manager.syncCartJson(jsonMainNode);
				     }
                     //echarge.get_name()
                    
                    
                    // manager.syncEchargeJson(jsonMainNode);
				   //  aController.CartUpdate(SplashScreen.this, "Hello");
				 /****************** End Parse Response JSON Data *************/
				     
				  //Add user data to controller class UserDataArr arraylist
				 // gridView.setAdapter(new CustomGridAdapter(getBaseContext(), aController));
				    
				      
				 } catch (JSONException e) {
					 Error+= "2 "+e.getMessage();
				//	 Toast.makeText(getApplicationContext(),"2 " +e.getMessage(), Toast.LENGTH_LONG).show();
				     e.printStackTrace();
				 //    Intent i = new Intent(SplashScreen.this, MainActivity.class);
				       
				   //    startActivity(i);

				       // close this activity
				     //  finish();
				 }
				catch (Exception e1) {
					 Error+="3 "+ e1.getMessage();
				//	 Toast.makeText(getApplicationContext(),"3 " +e1.getMessage(), Toast.LENGTH_LONG).show();
					// TODO Auto-generated catch block
					e1.printStackTrace();
				//	Intent i = new Intent(SplashScreen.this, MainActivity.class);
				       
				  //     startActivity(i);

				       // close this activity
				    //   finish();
				}
  
				Log.e("JSON", "> " + now_playing + earned);
 
            }
            else{
            	 Intent i = new Intent(SplashScreen.this, MainActivity.class);
                 
                 startActivity(i);

                 // close this activity
                 finish();
            }
            } catch (IOException e1) {
            	 Error+="4 "+ e1.getMessage();
            //	 Toast.makeText(getApplicationContext(),"5 " +e1.getMessage(), Toast.LENGTH_LONG).show();
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            catch (Exception e1) {
            	 Error+="5 "+ e1.getMessage();
            //	 Toast.makeText(getApplicationContext(),"6 " +e1.getMessage(), Toast.LENGTH_LONG).show();
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            if (Error != null) {
            	
              /*  new AlertDialog.Builder(getApplicationContext())
	    		.setTitle("No Internet connection")
	           .setMessage(Error)
	          .setCancelable(false)
	           .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   Intent intent = new Intent(getApplicationContext(), Main.class);  
	   	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   	        	Bundle mBundle = new Bundle();
	   	        	mBundle.putString("ID", "1");
	   	        	intent.putExtras(mBundle);
	   	        	startActivity(intent);
	   	        	//finish();
	                    SplashScreen.this.finish();
	               }
	           })
	          
	           .show();*/
            	 Toast.makeText(getApplicationContext(),"Timeout", Toast.LENGTH_LONG).show();
            	 Intent intent = new Intent(SplashScreen.this, Main.class);  
	   	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   	        	Bundle mBundle = new Bundle();
	   	        	mBundle.putString("ID", "1");
	   	        	intent.putExtras(mBundle);
	   	        	startActivity(intent);
            	finish();
            	
	            } 
            else{
            	Staff staff=DBAdapter.getStaffData(Main.getEmail(getApplicationContext()));
            	if(staff!=null){
            	 Intent i = new Intent(SplashScreen.this, MainActivity.class);
            	 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   	        	Bundle mBundle = new Bundle();
	   	        	mBundle.putString("ID", "1");
	   	        	i.putExtras(mBundle);
	   	        	startActivity(i);
                 try{
    	        	 GCMIntentService.secondTime=true;
               }catch(Exception e)
               {
            	   
               }
            	}else{
            		
            		if(id==0){
            		Intent intent = new Intent(SplashScreen.this, Main.class);  
	   	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   	        	Bundle mBundle = new Bundle();
	   	        	mBundle.putString("ID", "1");
	   	        	intent.putExtras(mBundle);
	   	        	startActivity(intent);
	   	        	finish();}
            		else{
            			 Intent i = new Intent(SplashScreen.this, MainActivity.class);
            			 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     	   	        	Bundle mBundle = new Bundle();
     	   	        	mBundle.putString("ID", "1");
     	   	        	i.putExtras(mBundle);
     	   	        	startActivity(i);
                         
                         try{
            	        	 GCMIntentService.secondTime=true;
                       }catch(Exception e)
                       {
                    	   
                       }
            		}
            	}
            }
           
	        	 //}
           
            // close this activity
            finish();
        }
 
    }
 
}