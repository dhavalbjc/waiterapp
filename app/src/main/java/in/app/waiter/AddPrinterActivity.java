package in.app.waiter;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class AddPrinterActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	LinearLayout find;
	TextView mac,name,txtprintorder;
	Button printsample;
	LinearLayout printorder;
	String popUpContents[];
    PopupWindow popupWindowDogs;
    StringBuilder buf=new StringBuilder();
	 StringBuilder buf2=new StringBuilder();
	 StringBuilder buf3=new StringBuilder();
	private final  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BluetoothService.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothService.STATE_CONNECTED:
                	Toast.makeText(getApplicationContext(), "Connect successful",
                            Toast.LENGTH_SHORT).show();
                	print_bt();
                	//printsample.setEnabled(true);
                	 // printsample.setAlpha(1.0f);
        			/*btnClose.setEnabled(true);
        			btnSend.setEnabled(true);
        			btnSendDraw.setEnabled(true);*/
                    break;
                case BluetoothService.STATE_CONNECTING:
                	Log.d("��������","��������.....");
                    break;
                case BluetoothService.STATE_LISTEN:
                case BluetoothService.STATE_NONE:
                	Log.d("��������","�ȴ�����.....");
                    break;
                }
                break;
            case BluetoothService.MESSAGE_CONNECTION_LOST:
                Toast.makeText(getApplicationContext(), "Device connection was lost",
                               Toast.LENGTH_SHORT).show();
    			/*btnClose.setEnabled(false);
    			btnSend.setEnabled(false);
    			btnSendDraw.setEnabled(false);*/
                break;
            case BluetoothService.MESSAGE_UNABLE_CONNECT:
            	Toast.makeText(getApplicationContext(), "Unable to connect device",
                        Toast.LENGTH_SHORT).show();
            	break;
            }
        }

    };
    int id=0;
	private boolean ischanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addprinter_layout);
        getActionBar().setDisplayHomeAsUpEnabled(true);
       // getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F92D2")));
		getActionBar().setTitle("Add Printer");
        mService = new BluetoothService(this, mHandler);

		if( mService.isAvailable() == false ){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
		}


    }

    @Override
    public void onStart() {
    	super.onStart();


		try {
			Bundle extras = getIntent().getExtras();
	         if (extras != null) {
           String value = extras.getString("ID");
          // Toast.makeText(getApplicationContext(),"Neworder "+value,Toast.LENGTH_LONG).show();
           id=Integer.parseInt(value);
	         }

			mac=(TextView) findViewById(R.id.addprintertxtmac);
			name=(TextView) findViewById(R.id.addprintertxtname);
			txtprintorder=(TextView) findViewById(R.id.addprintertxtprintorder);
			if(id>0)
	         {
	        	 Printer prtr=DBAdapter.getPrinter(id);
	        	 mac.setText(prtr.get_mac());
	        	 name.setText(prtr.get_name());
	        	 //Toast.makeText(getApplicationContext(),prtr.get_printorder(),Toast.LENGTH_LONG).show();
	        	 txtprintorder.setText("");
	        	 txtprintorder.setText(prtr.get_printorder());
	        	   con_dev = mService.getDevByMac(prtr.get_mac());
	         }

			find=(LinearLayout) findViewById(R.id.searchmac);
			printsample=(Button) findViewById(R.id.btnprintsample);
			printorder=(LinearLayout) findViewById(R.id.addprinterlinearPrintorder);
	        find.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ischanged=true;
					if( mService.isBTopen() == false)
					{
			            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
					}
					if(mService.isBTopen() == true)
					{
					Intent serverIntent = new Intent(AddPrinterActivity.this,DeviceListActivity.class);
					startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
					}
				}
			});
	        if(con_dev==null){
	        printsample.setEnabled(false);
	        printsample.setAlpha(0.5f);
	        }
	        printsample.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if( mService.isBTopen() == false)
					{
			            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
					}
					if(mService.isBTopen() == true)
					{
						if(con_dev!=null){
							mService.connect(con_dev);

							}
					}

				}
			});
	        List<String> dogsList = new ArrayList<String>();
	        dogsList.add("no::1");
	        dogsList.add("yes::2");

	        // convert to simple array
	        popUpContents = new String[dogsList.size()];
	        dogsList.toArray(popUpContents);


	        // initialize pop up window
	        popupWindowDogs = popupWindowDogs();

	        printorder.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					ischanged=true;
					//popupWindowDogs.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
					popupWindowDogs.showAsDropDown(v, -5, 0);
					//popupWindowDogs.setFocusable(true);
				//	popupWindowDogs.showAsDropDown(v,  Gravity.FILL, 0);

				}
			});
			/*btnClose.setEnabled(false);
			btnSend.setEnabled(false);
			btnSendDraw.setEnabled(false);*/
		} catch (Exception ex) {
            Log.e("error",ex.getMessage());
		}
    }
 
    public PopupWindow popupWindowDogs() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(dogsAdapter(popUpContents));

        // set the item click listener
        listViewDogs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v,
					int arg2, long arg3) {
				// get the context and main activity to access variables
		        //Context mContext = v.getContext();
		        //Reports_Fragment mainActivity = ((Reports_Fragment) mContext);

		        // add some animation when a list item was clicked
		        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
		        fadeInAnimation.setDuration(10);
		        v.startAnimation(fadeInAnimation);

		        // dismiss the pop up
		        popupWindowDogs.dismiss();

		        // get the text and set it as the button text
		        String selectedItemText = ((TextView) v).getText().toString();

		        txtprintorder.setText(selectedItemText);

		        // get the id

		       // Toast.makeText(this, "Dog ID is: " + selectedItemTag, Toast.LENGTH_SHORT).show();


			}
		});

        // some other visual settings
        Display display = this.getWindowManager().getDefaultDisplay();
	     Point screenSize = new Point();
	     display.getSize(screenSize);
	     int width = screenSize.x;
        popupWindow.setFocusable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
	    // layoutParams.copyFrom(dialog.getWindow().getAttributes());
	     layoutParams.width = (int) (width - (width * 0.05) );
        popupWindow.setWidth(layoutParams.width);
       // popupWindow.(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //popupWindow.showAtLocation(new View(this), Gravity.CENTER_VERTICAL, 10, 10);
        // set the list view as pop up window content
       // popupWindow.update(10,10, layoutParams.width,WindowManager.LayoutParams.WRAP_CONTENT);

       // WindowManager LayoutParams parm1=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
       // parm1.setMargins(20, 20, 0, 0);
       // layout.setLayoutParams(parm1);
        //ratePw.addView(layout,parm1);  // removed it
       // ratePw.setContentView(layout);

        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }

    /*
     * adapter where the list values will be set
     */
    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(getApplicationContext());

                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(18);
                listItem.setPadding(15, 20, 15, 20);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_printer, menu);
        return true;
    }

	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    if (keyCode == KeyEvent.KEYCODE_BACK) {

	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle("Save changes?");
	        builder.setMessage("Do you want to save the changes made?");
	        builder.setPositiveButton("Yes",
	                new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        // TODO Auto-generated method stub
	                    	SavePrinter();

	                        }
	                });

	        builder.setNegativeButton("No",
	                new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                        // TODO Auto-generated method stub
	                        dialog.dismiss();
	                        try{
	                        /*	 if (mService != null)
	         					 	mService.stop();
	         					 mService = null;
	         					 con_dev=null;*/

	                            	finish();
	                    		 }
	                    		 catch(Exception e)
	                    		 {}


	                        finish();
	                    }
	                });

	        AlertDialog alert = builder.create();
	        if(ischanged){
	        alert.show();
	        }
	        else{
	        	finish();
	        }
	        return true;

	    }
	      return super.onKeyDown(keyCode, event);
	    }

    protected void SavePrinter() {
    	if(!mac.getText().toString().trim().equals("")){
        	Printer print=new Printer();
        	print.set_protocol("ESC POS");
        	print.set_connection("bluetooth");
        	print.set_mac(mac.getText().toString());
        	print.set_ip("null");
        	print.set_model("null");
        	print.set_paperwidth("null");
        	print.set_name(name.getText().toString());
        	print.set_printorder(txtprintorder.getText().toString());
        	print.set_enable("true");
        	print.set_status("connected");
        	if(id==0){
        	int pid=DBAdapter.addPrinterData(print);
        	try
			 {
					 ArrayList<HashMap<String, String>> wordList;
			        wordList = new ArrayList<HashMap<String, String>>();
			        HashMap<String, String> map = new HashMap<String, String>();
			        map.put("oprn","Insert");
			        map.put("table", "Printer");
			        map.put("pid",String.valueOf(pid));
			        map.put("protocol","ESC POS");
			        map.put("connection","bluetooth");
			        map.put("mac",mac.getText().toString());
			        map.put("ip","null");
			        map.put("model","null");
			        map.put("paperwidth","null");
			        map.put("name",name.getText().toString());
			        map.put("printorder",txtprintorder.getText().toString());
			        map.put("enable","true");
			        map.put("status","connected");
			        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
			       wordList.add(map);
			        Gson gson = new GsonBuilder().create();
				 crudonserver crudop=new crudonserver();
				 crudop.callserverforcrudopern(gson.toJson(wordList),getApplicationContext());

			 }catch(Exception e)
			 {
				// error=String.valueOf(e.getMessage());
				 //Toast.makeText(context,Toast.LENGTH_LONG).show();
			 }
        	Toast.makeText(getApplicationContext(), "Save successfully", Toast.LENGTH_LONG).show();
        	}
        	else{
        		int pid=DBAdapter.updatePrinter(id, name.getText().toString(), mac.getText().toString(),txtprintorder.getText().toString());
        		try
				 {
						 ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				        HashMap<String, String> map = new HashMap<String, String>();
				        map.put("oprn","Update");
				        map.put("table", "Printer");
				        map.put("field", "All");
				        map.put("pid",String.valueOf(id));
				        map.put("name", name.getText().toString());
				        map.put("mac",mac.getText().toString());
				        map.put("printorder",txtprintorder.getText().toString());
				        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
				       wordList.add(map);
				        Gson gson = new GsonBuilder().create();
					 crudonserver crudop=new crudonserver();
					 crudop.callserverforcrudopern(gson.toJson(wordList),getApplicationContext());

				 }catch(Exception e)
				 {
					// error=String.valueOf(e.getMessage());
					 //Toast.makeText(context,Toast.LENGTH_LONG).show();
				 }
        		Toast.makeText(getApplicationContext(), "update successfully", Toast.LENGTH_LONG).show();
        	}

        	Intent intent1 = new Intent(this, MainActivity.class);
        	intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	Bundle mBundle1 = new Bundle();
        	Staff staff=DBAdapter.getStaffData(Main.getEmail(getApplicationContext()));
        	if(staff.get_staff_roll().equals("admin")){
        	mBundle1.putString("ID", "6");
        	}else{
        		mBundle1.putString("ID", "4");
        	}
        	intent1.putExtras(mBundle1);
        	startActivity(intent1);
        	finish();
        	}else{
        		Toast.makeText(getApplicationContext(), "Select printer", Toast.LENGTH_LONG).show();
        	}

	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {    
        switch (requestCode) {
        case REQUEST_ENABLE_BT:
            if (resultCode == Activity.RESULT_OK) {
            	Toast.makeText(getApplicationContext(), "Bluetooth open successful", Toast.LENGTH_LONG).show();
            } else {
            	finish();
            }
            break;
        case  REQUEST_CONNECT_DEVICE:
        	if (resultCode == Activity.RESULT_OK) {
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                con_dev = mService.getDevByMac(address);   
                
             
               mac.setText(con_dev.getAddress().toString());
               name.setText(con_dev.getName().toString());
               printsample.setEnabled(true);
         	  printsample.setAlpha(1.0f);
            }
            break;
        }
    } 
    @Override
	protected void onDestroy() {
		super.onDestroy();
		if (mService != null) 
			mService.stop();
		mService = null; 
	}
    

    @SuppressLint("SdCardPath")
	private void printImage() {
    	byte[] sendData = null;
    	PrintPic pg = new PrintPic();
    	pg.initCanvas(384);     
    	pg.initPaint();
    	pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
    	sendData = pg.printDraw();
    	mService.write(sendData);
    }
    private void print_bt() {
	 	
	 	//String msg = message.getText().toString();
	     //if( msg.length() > 0 ){
	         
    	 Restaurant restro=DBAdapter.getRestaurant(DBAdapter.getRestaurantId());
         buf3.append(System.getProperty("line.separator"));
        // buf3.append(System.getProperty("line.separator"));
         if(!restro.get_restro_name().equals(""))
         {
         buf3.append(restro.get_restro_name());
         buf3.append(System.getProperty("line.separator"));
         }
         if(!restro.get_restro_street().equals(""))
         {
         buf.append(restro.get_restro_street());
        
         }
         if(!restro.get_restro_civicnumber().equals(""))
         { 
         	 buf.append(System.getProperty("line.separator"));
         buf.append(restro.get_restro_civicnumber());
        
         }
         if(!restro.get_restro_zipcode().equals(""))
         {
         buf.append(System.getProperty("line.separator"));
         buf.append(restro.get_restro_zipcode()+", ");
         
         }
         if(!restro.get_restro_city().equals(""))
         {
         buf.append(restro.get_restro_city());
         buf.append(System.getProperty("line.separator"));
         }
         if(!restro.get_restro_state().equals(""))
         {
         buf.append(restro.get_restro_state()+", ");
        
         }
         if(!restro.get_restro_country().equals(""))
         {
         buf.append(restro.get_restro_country());
         buf.append(System.getProperty("line.separator"));
         }
         if(!restro.get_restro_phone().equals(""))
         {
         buf.append(restro.get_restro_phone());
         buf.append(System.getProperty("line.separator"));
         buf.append("--------------------------------");
         }
         buf2.append("Order 12345");
    	 buf2.append(System.getProperty("line.separator"));
    	 buf2.append("Table 5");
    	 buf2.append(System.getProperty("line.separator"));	  
    	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatter.setLenient(false);
			
			//feederCart.setTime(formatter.format(new Date()));
    	
    	 buf2.append("Date "+formatter.format(new Date()));
         buf2.append(System.getProperty("line.separator"));
         buf2.append("--------------------------------");
         buf2.append(System.getProperty("line.separator"));
    	 buf2.append("Qty  Items               Amount"); 
    	 buf2.append(System.getProperty("line.separator"));
    	 buf2.append("--------------------------------");
    	 buf2.append(System.getProperty("line.separator"));
    	 buf2.append("1"+" "+"item 1"+ "                   "+"9.00");
    	 buf2.append(System.getProperty("line.separator"));
    	 buf2.append("1"+" "+"item 2"+ "                   "+"19.00");
    	 buf2.append(System.getProperty("line.separator"));
    	 buf2.append("1"+" "+"item 3"+ "                   "+"29.00");
    	 buf2.append(System.getProperty("line.separator"));
    	 buf2.append("--------------------------------");
    	 buf2.append(System.getProperty("line.separator"));
    	 buf2.append("Total");       		 	        			
		 buf2.append("                      ");  
		// buf2.append(mycart.getTotal()); 
		 buf2.append("57.00"); 
    	 buf2.append(System.getProperty("line.separator"));
    	
    	 buf2.append("Thanks for visit");
    	 buf2.append(System.getProperty("line.separator"));
    	// buf2.append("--------------------------------");
    	 buf2.append(System.getProperty("line.separator"));
         buf2.append(System.getProperty("line.separator"));
	         if(mService.getState()==3){
	        	
	        	 byte[] cmd = new byte[3];
	             cmd[0] = 0x1B;
	             cmd[1] = 0x61;
	             cmd[2] = 0x01;       	 
	        	 mService.write(cmd);  
	        	 mService.sendMessage(String.valueOf(buf3), "GBK");
	        	  
	        	 mService.sendMessage(String.valueOf(buf), "GBK");
	        	 cmd[2] = 0x00;
	             mService.write(cmd); 
	        	 mService.sendMessage(String.valueOf(buf2)+"\n", "GBK");
	        	 buf.delete(0, buf.length());
	        	 buf2.delete(0, buf2.length());
	        	 buf3.delete(0, buf3.length());
				 Toast.makeText(this, "Print successful", Toast.LENGTH_LONG).show();
		           }
		           else{
		        	   
		        	   Toast.makeText(this, "Print Fail", Toast.LENGTH_LONG).show();
		           }
	     //}

	 }
    public boolean onOptionsItemSelected(MenuItem item) {

	    // some variable statements...

	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	
	        	Intent intent = new Intent(this, MainActivity.class);  
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	Bundle mBundle = new Bundle();
	        	Staff staff=DBAdapter.getStaffData(Main.getEmail(getApplicationContext()));
	        	if(staff.get_staff_roll().equals("admin")){	
	        		mBundle.putString("ID", "6");
	        	}else{
	        		mBundle.putString("ID", "4");
	        	}
	        	intent.putExtras(mBundle);
	        	startActivity(intent);
	        	finish();
	        	
	            return true;	
	        case R.id.Save:
	        	SavePrinter();
	            return true;	
	            
	    }
	    return super.onOptionsItemSelected(item);
	}
    
    
}
