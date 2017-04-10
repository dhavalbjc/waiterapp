package in.app.waiter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.util.ArrayList;

public class BillActivity extends Activity {
private static final int REQUEST_ENABLE_BT = 2;
private static final int REQUEST_CONNECT_DEVICE = 1;
int id=0;
Button printbtn;
StringBuilder buf=new StringBuilder();
BluetoothService mService = null;
		 private final  Handler mHandler = new Handler() {
		     @Override
		     public void handleMessage(Message msg) {
		         switch (msg.what) {
		         case BluetoothService.MESSAGE_STATE_CHANGE:
		             switch (msg.arg1) {
		             case BluetoothService.STATE_CONNECTED:
		             	/*Toast.makeText(getApplicationContext(), "Connect successful",
		                         Toast.LENGTH_SHORT).show();*/
		             	 print_bt();
		     			//btnClose.setEnabled(true);
		     			//btnSend.setEnabled(true);
		     			//btnSendDraw.setEnabled(true);
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
		             /*Toast.makeText(getApplicationContext(), "Device connection was lost",
		                            Toast.LENGTH_SHORT).show();*/
		 		//	btnClose.setEnabled(false);
		 		//	btnSend.setEnabled(false);
		 		//	btnSendDraw.setEnabled(false);
		             break;
		         case BluetoothService.MESSAGE_UNABLE_CONNECT:
		         	Toast.makeText(getApplicationContext(), "Unable to connect device",
		                     Toast.LENGTH_SHORT).show();
		         	break;
		         }
		     }

		 };
BluetoothDevice con_dev = null;
private ReportCartitemArrayAdapter cardArrayAdapter;
private ListView listView;
private TextView total;
private TextView table;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.billactivity);
		mService = new BluetoothService(this, mHandler);

		if( mService.isAvailable() == false ){
		    Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
		  //  finish();
		}
		printbtn = (Button)findViewById(R.id.btn_print);

		printbtn.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
		connect();
		}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Reports");
		// getActionBar().setDisplayHomeAsUpEnabled(false);
		    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F92D2")));
		  Bundle extras = getIntent().getExtras();
	         if (extras != null) {
          String value = extras.getString("ID");
          total=(TextView) findViewById(R.id.bill_total);
          table=(TextView) findViewById(R.id.bill_table);


         // Toast.makeText(getApplicationContext(),"Neworder "+value,Toast.LENGTH_LONG).show();
          id=Integer.parseInt(value);
	         }
	         if(id!=0)
	         {
	        	 try{
	        		    Restaurant restro=DBAdapter.getRestaurant(DBAdapter.getRestaurantId());
	        	        buf.append(System.getProperty("line.separator"));
	        	        buf.append(System.getProperty("line.separator"));
	        	        if(!restro.get_restro_name().equals(""))
	        	        {
	        	        buf.append(restro.get_restro_name());
	        	        buf.append(System.getProperty("line.separator"));
	        	        }
	        	        if(!restro.get_restro_street().equals(""))
	        	        {
	        	        buf.append(restro.get_restro_street());

	        	        }
	        	        if(!restro.get_restro_civicnumber().equals(""))
	        	        {
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
	        	        buf.append(System.getProperty("line.separator"));
	        	        }
	        	 listView = (ListView) findViewById(R.id.bill_listview);

	     		//cardArrayAdapter.addAll(DBAdapter.getAllStaff());
	     		ModelCart mycart=Reports_Fragment.BillCart.get(String.valueOf(id));
	     		buf.append("Order "+id);
	        	 buf.append(System.getProperty("line.separator"));
	        	 buf.append("Table "+mycart.getTableName());
	        	 buf.append(System.getProperty("line.separator"));
	        	 buf.append("Date "+mycart.getTime());
		         buf.append(System.getProperty("line.separator"));
		         buf.append(System.getProperty("line.separator"));
	        	 buf.append("Items");
	        	 buf.append(System.getProperty("line.separator"));
	        	 for (CartItems cartItems : mycart.getUniqueCartProducts()) {
	        		//	Item I=DBAdapter.getItemData(cartItems.get_item().get_id());
					//	Toast.makeText(getApplicationContext(), String.valueOf(cartItems.get_quantity()), Toast.LENGTH_SHORT).show();
	        		 String ip=null;
						if(DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", cartItems.get_item().get_name()))
						{
							Item I=DBAdapter.getItemData(cartItems.get_item().get_name());
							ip=I.get_price();
						}else{
							ip=cartItems.get_item().get_price();
						}


	        		 int q=cartItems.get_quantity();
						Double d=q*Double.parseDouble(ip);
					//	cartItems.get_item().set_price(I.get_price());
	        		// buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name()+"   "+String.valueOf(d));
	        		 buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name());
	        		 int size=25-cartItems.get_item().get_name().length();
	        		 if(size>0){
	        		 for(int i=0;i < size;i++)
	        		 {
	        			 buf.append(" ");
	        		 }
	        		 }

	        		 buf.append(String.valueOf(d));
	  				buf.append(System.getProperty("line.separator"));
	        	 }
	     	/*	for (CartItems cartItems : mycart.getProducts()) {
	     			if(cartItems.get_item().get_flag().equals("I")){
	     			Item I=DBAdapter.getItemData(cartItems.get_item().get_id());
					//Toast.makeText(context, I.get_price(), Toast.LENGTH_SHORT).show();
					int q=cartItems.get_quantity();
					Double d=q*Double.parseDouble(I.get_price());
					cartItems.get_item().set_price(I.get_price());
        		// buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name()+"   "+String.valueOf(d));
        		 buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name());
        		 int size=25-cartItems.get_item().get_name().length();
        		 if(size>0){
        		 for(int i=0;i < size;i++)
        		 {
        			 buf.append(" ");
        		 }
        		 }

        		 buf.append(String.valueOf(d));
  				buf.append(System.getProperty("line.separator"));
	     			}
	     			else{
	     				if(cartItems.get_item().get_id()!=0)
						{
							Extra e=DBAdapter.getExtraData(cartItems.get_item().get_id());
							cartItems.get_item().set_price(String.valueOf(Double.parseDouble(e.get_price())));
							//buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name()+"   "+String.valueOf(e.get_price()));
							 buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name());
			        		 int size=25-cartItems.get_item().get_name().length();
			        		 if(size>0){
			        		 for(int i=0;i < size;i++)
			        		 {
			        			 buf.append(" ");
			        		 }
			        		 }

			        		 buf.append(String.valueOf(e.get_price()));
			 				buf.append(System.getProperty("line.separator"));
						}
						else
						{Item ip=DBAdapter.getItemData(cartItems.get_item().get_name());
						cartItems.get_item().set_price(String.valueOf(Double.parseDouble(ip.get_price())));
						buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name());
						 int size=25-cartItems.get_item().get_name().length();
		        		 if(size>0){
		        		 for(int i=0;i < size;i++)
		        		 {
		        			 buf.append(" ");
		        		 }
		        		 }

		        		 buf.append(String.valueOf(ip.get_price()));
		 				buf.append(System.getProperty("line.separator"));
						//dp=Double.parseDouble(ip.get_price());

						}
	     			}
				}*/
	     		 buf.append(System.getProperty("line.separator"));
	        	 buf.append("Total  "+mycart.getTotal());
	        	 buf.append(System.getProperty("line.separator"));
	        	 buf.append("------------------------------");
	        	 buf.append(System.getProperty("line.separator"));
	        	 buf.append(System.getProperty("line.separator"));
	     		 table.setText("Table "+mycart.getTableName());
	     		cardArrayAdapter = new ReportCartitemArrayAdapter(getBaseContext(),mycart.getUniqueCartProducts());

	     		/*cardArrayAdapter.add(new Staff("Bijal","bijal@yahoo.com"));
	     				cardArrayAdapter.add(new Staff("Manan","manan@yahoo.com"));*/
	     		        listView.setAdapter(cardArrayAdapter);
	     		       Helper.getListViewSize(listView);
	     		     //  Toast.makeText(getApplicationContext(),String.valueOf(c.getTotal()), Toast.LENGTH_LONG).show();
	     		       total.setText(String.valueOf(mycart.getTotal()));
	        	 }catch(Exception e)
	        	 {
	        		 Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
	        	 }

	     		//  listView.setOnItemClickListener(new OnItemClickListener() {



	        	 //Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
	         }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bill, menu);
		return true;
	}

	 protected void connect() {
		 try{
		 if( mService.isBTopen() == false)
		 	{
		         Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		         startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		 	}
		 if( mService.isBTopen() == true)
		 	{
		 if(con_dev == null){
		 	/*SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",0);

		  for (Entry<String, ?> entry : pref.getAll().entrySet()) {
		      Object val = entry.getValue();
		      if (val == null) {
		          //output.append(String.format("%s = <null>%n", entry.getKey()));
		      } else {
		     	 con_dev = mService.getDevByMac(String.valueOf(entry.getValue()));

		          mService.connect(con_dev);


		      }

		  }*/
			 ArrayList<Printer> prtrs=DBAdapter.getAllPrinterData();
			    int size=prtrs.size();
			    if(size==1){
			    	con_dev = mService.getDevByMac(prtrs.get(0).get_mac());
			        mService.connect(con_dev);
			    }else if(size>=1){
			    	 final Dialog dialog = new Dialog(BillActivity.this);
	    			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    			 dialog.setContentView(R.layout.select_restro);
	    			 final SelectPrinterAdapter adapter = new SelectPrinterAdapter(BillActivity.this,prtrs);
	    			 final TextView title = (TextView) dialog.findViewById(R.id.txt_select_restro);
	    			 title.setText("Select Printer");
	    		        final ListView listView = (ListView) dialog.findViewById(R.id.lv_select_restro);
	    		        listView.setAdapter(adapter);
	    		        listView.setOnItemClickListener(new OnItemClickListener() {

	    				@Override
	    				public void onItemClick(AdapterView<?> parent, View v,
	    						int groupPosition, long id) {
	    					Printer printer=(Printer)parent.getItemAtPosition(groupPosition);
	    					con_dev = mService.getDevByMac(printer.get_mac());
	    			        mService.connect(con_dev);
	    			        dialog.dismiss();
	    				//	Toast.makeText(getBaseContext(),String.valueOf(Restro.get_restro_id()),Toast.LENGTH_LONG).show();

	    				}
	    			});

	    			 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
	    			 dialog.show();
			    }
		  if(con_dev == null){
		 	Intent serverIntent = new Intent(BillActivity.this,DeviceListActivity.class);
		 	startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
		  }
		 }
		 else{

		 print_bt();

		 }

	 }
		 }
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
			}
		 }

		 private void print_bt() {

		 	//String msg = message.getText().toString();
		     //if( msg.length() > 0 ){
		         mService.sendMessage(String.valueOf(buf)+"\n", "GBK");
		         Toast.makeText(this, "Print successful", Toast.LENGTH_LONG).show();
		     //}

		 }

		 @Override
		 protected void onDestroy() {

		 super.onDestroy();
		 if (mService != null)
		 	mService.stop();
		 mService = null;
		 }

		 @Override
		 public void onActivityResult(int requestCode, int resultCode, Intent data) {    
		     switch (requestCode) {
		     case REQUEST_ENABLE_BT:
		         if (resultCode == Activity.RESULT_OK) {
		         	Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
		         } else {
		         	finish();
		         }
		         break;
		     case  REQUEST_CONNECT_DEVICE:
		     	if (resultCode == Activity.RESULT_OK) {
		             String address = data.getExtras()
		                                  .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		             con_dev = mService.getDevByMac(address);   
		             
		             mService.connect(con_dev);
		             print_bt();
		         }
		         break;
		     }
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
		 public boolean onOptionsItemSelected(MenuItem item) {

			    // some variable statements...

			    switch (item.getItemId()) {
			        case android.R.id.home:
			        	
			        	
			        	finish();
			            return true;	            
			            

			    }
			    return super.onOptionsItemSelected(item);
			}
		 public  int getWidth()
		 {try 
	     {
	         
			 Display display =this.getWindowManager().getDefaultDisplay();
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

}
