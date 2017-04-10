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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 2;
	private static final int REQUEST_CONNECT_DEVICE = 1;
	int id=0;
	/** Called when the activity is first created. */

	Button printbtn;
	 StringBuilder buf=new StringBuilder();
	 StringBuilder buf2=new StringBuilder();
	 StringBuilder buf3=new StringBuilder();
	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
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
		         	if (mService != null)
		    		 	mService.stop();
		         	con_dev=null;
		    		// mService = null;

		         	break;
		         }
		     }

		 };
	private PaymentCartArrayAdapter cardArrayAdapter;
	private ListView listView;
	private TextView total;
	private TextView table;
	private LinearLayout lback,lpay,lprint,lhide;
	private Button pay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		mService = new BluetoothService(this, mHandler);

		if( mService.isAvailable() == false ){
		    Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
		    //finish();
		}
		//message = (EditText)findViewById(R.id.message);
		printbtn = (Button)findViewById(R.id.btn_print);
		 lback=(LinearLayout) findViewById(R.id.lin_back);
		    lpay=(LinearLayout) findViewById(R.id.lin_pay);
		    lprint=(LinearLayout) findViewById(R.id.lin_print);
		    lhide=(LinearLayout) findViewById(R.id.lin_hide);
		    lprint.setVisibility(View.GONE);
		    lhide.setVisibility(View.GONE);
		pay=(Button) findViewById(R.id.btn_pay);
lback.setOnClickListener(new OnClickListener() {

	@Override
	public void onClick(View arg0) {
		String value=table.getText().toString();
		if(value.toString().equals("Unpaid"))
		{
			finish();
		}
		else{
			newOrderActivity.myCartHash.clear();
        	newOrderActivity.itemExtra.clear();
        	newOrderActivity.markHash.clear();
        	newOrderActivity.myCartItemsWithExtras.clear();
        	if(newOrderActivity.mynewCartItemsWithExtras!=null)
            	newOrderActivity.mynewCartItemsWithExtras.clear();
            	if(newOrderActivity.mynewCartHash!=null)
            		newOrderActivity.mynewCartHash.clear();
            	newOrderActivity.tableName="";
            	newOrderActivity.id=0;
            //	newOrderActivity.id2=1;
            	newOrderActivity.mycart=null;
            	//changed=false;
	    	Intent intent11 = new Intent(getApplicationContext(), MainActivity.class);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	Bundle mBundle1 = new Bundle();
    		mBundle1.putString("ID", "1");
    		intent11.putExtras(mBundle1);
        	startActivity(intent11);


        	finish();
		}

	}
});
		lprint.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			try{
		connect();
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
			}
		}
		});

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Payment");
		// getActionBar().setDisplayHomeAsUpEnabled(false);
		    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F92D2")));


		  Bundle extras = getIntent().getExtras();
	         if (extras != null) {
        String value = extras.getString("ID");
        total=(TextView) findViewById(R.id.bill_total);
        table=(TextView) findViewById(R.id.bill_table);
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
       // Toast.makeText(getApplicationContext(),"Neworder "+value,Toast.LENGTH_LONG).show();
        id=Integer.parseInt(value);
	         }
	         if(id!=0)
	         {


	        	 try{
	        	 listView = (ListView) findViewById(R.id.payment_listview);
	        	 ModelCart mycart=DBAdapter.getCartData(id);
	        	 buf2.append("Order "+id);
	        	 buf2.append(System.getProperty("line.separator"));
	        	 buf2.append("Table "+mycart.getTableName());
	        	 buf2.append(System.getProperty("line.separator"));
	        	 buf2.append("Date "+mycart.getTime());
		         buf2.append(System.getProperty("line.separator"));
		         buf2.append("--------------------------------");
		         buf2.append(System.getProperty("line.separator"));
	        	 buf2.append("Qty  Items               Amount");
	        	 buf2.append(System.getProperty("line.separator"));
	        	 buf2.append("--------------------------------");
	        	 buf2.append(System.getProperty("line.separator"));
	        	   // System.out.println(convertedDate);
	 			//Toast.makeText(getApplicationContext(),mycart.getTableName(),Toast.LENGTH_LONG).show();
	 			//tableName=mycart.getTableName();
	 			//cartItems=DBAdapter.getCartItemsDataByCartId(id);
	 			//cartItems=DBAdapter.getCartItemsDataByCartId(mycart.getId());
	     		//cardArrayAdapter.addAll(DBAdapter.getAllStaff());
	     		//ModelCart c=Reports_Fragment.BillCart.get(String.valueOf(id));
	        	// ModelCart c=Reports_Fragment.BillCart.get(String.valueOf(id));
	        	 for (CartItems cartItems : DBAdapter.getAllCartItemsDataByCartId(mycart.getId())) {
	        		 if(!cartItems.get_status().equals("5")){
	        		 mycart.setProduct(cartItems);
	        		 Item I=DBAdapter.getItemData(cartItems.get_item().get_id());
						//Toast.makeText(context, I.get_price(), Toast.LENGTH_SHORT).show();
						int q=cartItems.get_quantity();
						Double d=q*Double.parseDouble(I.get_price());

						cartItems.get_item().set_price(I.get_price());

	        		 buf2.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name());
	        		 int size=25-cartItems.get_item().get_name().length();
	        		 if(size>0){
	        		 for(int i=0;i < size;i++)
	        		 {
	        			 buf2.append(" ");
	        		 }
	        		 }

	        		 buf2.append(String.valueOf(d));
	  				buf2.append(System.getProperty("line.separator"));
	        		 for (CartItems iterable_element : DBAdapter.getCartItemsExtraforDeleteData(cartItems.get_id())) {
	        		//Toast.makeText(getApplicationContext(), String.valueOf(iterable_element.get_item().get_id()), Toast.LENGTH_LONG).show();

	        			 if(iterable_element.get_item().get_id()!=0)
						{
							Extra e=DBAdapter.getExtraData(iterable_element.get_item().get_id());
							iterable_element.get_item().set_price(String.valueOf(Double.parseDouble(e.get_price())));
							//buf2.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name());
							buf2.append("1"+" "+e.get_name());
							 int size1=27-e.get_name().length();
			        		 if(size1>0){
			        		 for(int i=0;i < size1;i++)
			        		 {
			        			 buf2.append(" ");
			        		 }
			        		 }
			        		 buf2.append(String.valueOf(d));
			 				buf2.append(System.getProperty("line.separator"));
						}
						else
						{Item ip=DBAdapter.getItemData(iterable_element.get_item().get_name());
						iterable_element.get_item().set_price(String.valueOf(Double.parseDouble(ip.get_price())));
						//buf2.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name()+"   "+String.valueOf(ip.get_price()));
						buf2.append("1"+" "+ip.get_name());
						int size1=27-ip.get_name().length();
		        		 if(size1>0){
		        		 for(int i=0;i < size1;i++)
		        		 {
		        			 buf2.append(" ");
		        		 }
		        		 }
		        		 buf2.append(String.valueOf(ip.get_price()));
		 				buf2.append(System.getProperty("line.separator"));
						//dp=Double.parseDouble(ip.get_price());

						}
	        			 mycart.setProduct(iterable_element);
					}
	        		 }
	        		 //end
				}
	        	  total.setText(String.valueOf(mycart.getTotal()));
	        	 LinearLayout service_charge=(LinearLayout) findViewById(R.id.linear_service_charge);
	     			TextView name=(TextView) findViewById(R.id.service_charge);
	     			TextView amt=(TextView) findViewById(R.id.service_charge_amt);
	     			ECharge echarge=DBAdapter.getEChargeData("Service");
	     			if(echarge!=null)
	     			{
	     				if(echarge.get_param().equals("Percentage"))
	     				{
	     					try{
	     					name.setText(echarge.get_name()+" "+echarge.get_quantity()+"%");
	     					Double total1=Double.parseDouble(mycart.getTotal());
	     					Double per=Double.parseDouble(echarge.get_quantity());
	     					double tax=(total1*per)/100;
	     					double Amount=total1+tax;
	     					double roundvalue=Math.round(Amount)-Amount;
	     					amt.setText(String.valueOf(tax));
	     					service_charge.setVisibility(View.VISIBLE);
	     					// total.setText(String.valueOf(Amount));
	     					 total.setText(String.valueOf(Math.round(Amount)));

	     					buf2.append("--------------------------------");
	     					 buf2.append(System.getProperty("line.separator"));
	     		        	 buf2.append("Sub Total");
	     		        	 buf2.append("                 ");
	     	        		 buf2.append(mycart.getTotal());
	     	        	   	 buf2.append(System.getProperty("line.separator"));
	     	        		 buf2.append(name.getText().toString());
	     	        		 int size1=27-name.getText().toString().length();
			        		 if(size1>0){
			        		 for(int i=0;i < size1;i++)
			        		 {
			        			 buf2.append(" ");
			        		 }
			        		 }
	     		        	 //buf2.append("                      ");
	     	        		 buf2.append(tax);
	     	        		 buf2.append(System.getProperty("line.separator"));
	     	        		buf2.append("Round off");
	     		        	 buf2.append("                 ");
	     		        	//DecimalFormat twoDForm = new DecimalFormat("#.##");
	     	        		 buf2.append(Double.valueOf(String.format("%.2f", roundvalue)).toString());
	     	        		 buf2.append(System.getProperty("line.separator"));
	     					}catch(Exception e)
	     					{

	     					}
	     				}
	     				else{
	     					try{
	     						service_charge.setVisibility(View.GONE);
	     					CartItems item=new CartItems();
	     					Item i=new Item();
	     					i.set_name(echarge.get_name());
	     					i.set_price(echarge.get_quantity());
	     					i.set_flag("I");

	     					item.set_item(i);
	     					item.set_quantity(1);
	     					mycart.setProduct(item);
	     					Double total1=Double.parseDouble(mycart.getTotal());
	     					Double per=Double.parseDouble(echarge.get_quantity());
	     					double Amount=total1+per;
	     					double roundvalue=Math.round(Amount)-Amount;
	     					// total.setText(String.valueOf(Amount));
	     					 total.setText(String.valueOf(Math.round(Amount)));
	     					 buf2.append("--------------------------------");
	     					 buf2.append(System.getProperty("line.separator"));
	     		        	 buf2.append("Sub Total");
	     		        	buf2.append("                ");
	     	        		 buf2.append(mycart.getTotal());
	     	        		buf2.append(name.getText().toString());
	     	        		buf2.append(System.getProperty("line.separator"));
	     	        		int size1=25-name.getText().toString().length();
			        		 if(size1>0){
			        		 for(int i1=0;i1 < size1;i1++)
			        		 {
			        			 buf2.append(" ");
			        		 }
			        		 }
	     		        	// buf2.append("                      ");
	     	        		 buf2.append(per.toString());
	     	        		 buf2.append(System.getProperty("line.separator"));
	     	        		buf2.append("Round off");
	     		        	 buf2.append("                 ");
	     		        	//DecimalFormat twoDForm = new DecimalFormat("#.##");
	     		        	 buf2.append(Double.valueOf(String.format("%.2f", roundvalue)).toString());
	     	        		 buf2.append(System.getProperty("line.separator"));
	     					}catch(Exception e)
	     					{

	     					}
	     				}
	     			}
	     			else{
	     				service_charge.setVisibility(View.GONE);
	     			}
	        	// buf2.append(System.getProperty("line.separator"));
	        	 buf2.append("--------------------------------");
	        	 buf2.append(System.getProperty("line.separator"));
	        	 buf2.append("Total");
        		 buf2.append("                      ");
        		// buf2.append(mycart.getTotal());
        		 buf2.append(total.getText().toString());
	        	 buf2.append(System.getProperty("line.separator"));

	        	 buf2.append("Thanks for visit");
	        	 buf2.append(System.getProperty("line.separator"));
	        	// buf2.append("--------------------------------");
	        	 buf2.append(System.getProperty("line.separator"));
	             buf2.append(System.getProperty("line.separator"));
	        	 cardArrayAdapter = new PaymentCartArrayAdapter(getBaseContext(),mycart.getNotCancelledProducts());
	     		//cartItems=DBAdapter.getCartItemsDataByCartId(id);
	     		/*cardArrayAdapter.add(new Staff("Bijal","bijal@yahoo.com"));
	     				cardArrayAdapter.add(new Staff("Manan","manan@yahoo.com"));*/
	     		        listView.setAdapter(cardArrayAdapter);
	     		       Helper.getListViewSize(listView);
	     		     //  Toast.makeText(getApplicationContext(),String.valueOf(c.getTotal()), Toast.LENGTH_LONG).show();


	        	 }catch(Exception e)
	        	 {
	        		 Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
	        	 }

	     		//  listView.setOnItemClickListener(new OnItemClickListener() {


	        	 //Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
	        	   lpay.setOnClickListener(new OnClickListener() {

		    			@Override
		    			public void onClick(View v) {
		    				 final ModelCart mycart=DBAdapter.getCartData(id);

		    				final Dialog dialog = new Dialog(PaymentActivity.this);
							 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							 dialog.setContentView(R.layout.payment);
							 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
							 TextView tv_paidtitle=(TextView) dialog.findViewById(R.id.tv_paidtitle);
							 final EditText txtPaid=(EditText) dialog.findViewById(R.id.txtPaid);
							 final EditText txtDue=(EditText) dialog.findViewById(R.id.txtDue);
							 final EditText txtChange=(EditText) dialog.findViewById(R.id.txtChange);

							 RelativeLayout Rel_paid_cacel=(RelativeLayout) dialog.findViewById(R.id.Rel_paid_cacel);
							 RelativeLayout Rel_paid=(RelativeLayout) dialog.findViewById(R.id.Rel_paid);
							// EditText price=(EditText) dialog.findViewById(R.id.et_item_rename);
							// tv_paidtitle.setText("Pay "+mycart.getTotal());
							 //txtDue.setText(mycart.getTotal());
							 //txtPaid.setHint(mycart.getTotal());
							 tv_paidtitle.setText("Pay "+total.getText().toString());
							 txtDue.setText(total.getText().toString());
							 txtPaid.setHint(total.getText().toString());
							 txtPaid.addTextChangedListener(new TextWatcher() {

								    @SuppressLint("ResourceAsColor") public void onTextChanged(CharSequence s, int start, int before,
								            int count) {
								        if(!s.equals("") )
								                { //do your work here }
								        	try{
								        	Double pay=Double.parseDouble(s.toString());
								        	//Double due=Double.parseDouble(mycart.getTotal());
								        	Double due=Double.parseDouble(total.getText().toString());
								        	Double change=pay-due;
								        	DecimalFormat twoDForm = new DecimalFormat("#.##");

								        	txtChange.setText(Double.valueOf(twoDForm.format(change)).toString());
								        	if(change<0){
								        	txtChange.setTextColor(Color.rgb(200,0,0));
								        	}
								        	else{
								        		txtChange.setTextColor(Color.BLACK);
								        	}
								        	}catch(Exception e)
								        	{

								        	}

								        }

								    }

								    public void beforeTextChanged(CharSequence s, int start, int count,
								            int after) {

								    }

								    public void afterTextChanged(Editable s) {
								    	try{
								        	Double pay=Double.parseDouble(s.toString());
								        	//Double due=Double.parseDouble(mycart.getTotal());
								        	Double due=Double.parseDouble(total.getText().toString());
								        	Double change=pay-due;
								        	//txtChange.setText(change.toString());
								        	DecimalFormat twoDForm = new DecimalFormat("#.##");

								        	txtChange.setText(Double.valueOf(twoDForm.format(change)).toString());
								        	if(change<0){
									        	txtChange.setTextColor(Color.rgb(200,0,0));
									        	}
								        	else{
								        		txtChange.setTextColor(Color.BLACK);
								        	}
								        	}catch(Exception e)
								        	{

								        	}
								    }
								});

							 Rel_paid_cacel.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
							 Rel_paid.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub

									//group.set_name(price1.toString());
									if(!txtPaid.getText().toString().equals("")){

									if(Double.parseDouble(txtPaid.getText().toString())>=Double.parseDouble(txtDue.getText().toString())){
									DBAdapter.updateCartTTT(mycart.getId(), "cart_paid","true");
									table.setText("Paid");

									/* try
									 {
											 ArrayList<HashMap<String, String>> wordList;
									        wordList = new ArrayList<HashMap<String, String>>();
									        HashMap<String, String> map = new HashMap<String, String>();
									        map.put("oprn","Update");
									        map.put("table", "Cart");
									        map.put("field", "Paid");
									        map.put("id",String.valueOf(mycart.getId()));
									        map.put("total",String.valueOf(total.getText().toString()));
									        map.put("paid","true");
									        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
									       wordList.add(map);
									        Gson gson = new GsonBuilder().create();
										 crudonserver crudop=new crudonserver();
										 crudop.callserverforcrudopern(gson.toJson(wordList),getApplicationContext());

									 }catch(Exception e)
									 {
										// error=String.valueOf(e.getMessage());
										 //Toast.makeText(context,Toast.LENGTH_LONG).show();
									 }*/
										try
										{
											Staff staff=DBAdapter.getStaffData(Main.getEmail(getApplicationContext()));
											ArrayList<HashMap<String, String>> wordList;
											wordList = new ArrayList<HashMap<String, String>>();
											HashMap<String, String> map = new HashMap<String, String>();
											map.put("oprn","Update");
											map.put("table", "Cart");
											map.put("field", "PaidNew");
											map.put("id",String.valueOf(mycart.getId()));
											map.put("name",staff.get_staff_fname());
											map.put("total",String.valueOf(total.getText().toString()));
											map.put("paid","true");
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
									 //updateResults(DBAdapter.getAllCartData());
									 pay.setVisibility(View.GONE);
									 lpay.setVisibility(View.GONE);
									 lprint.setVisibility(View.VISIBLE);
									 dialog.cancel();
									}
									else{
										Toast.makeText(getApplicationContext(), "Enter valid payment", Toast.LENGTH_SHORT).show();
									}
									}else{
										Toast.makeText(getApplicationContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
									}


								}
							});

							 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
							 dialog.show();

		    			}
		    		});

	         }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.payment, menu);

		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

	    // some variable statements...

	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	newOrderActivity.myCartHash.clear();
            	newOrderActivity.itemExtra.clear();
            	newOrderActivity.markHash.clear();
            	newOrderActivity.myCartItemsWithExtras.clear();

            	if(newOrderActivity.mynewCartItemsWithExtras!=null)
            	newOrderActivity.mynewCartItemsWithExtras.clear();

                	if(newOrderActivity.mynewCartHash!=null)
                		newOrderActivity.mynewCartHash.clear();
                	newOrderActivity.tableName="";
                	newOrderActivity.id=0;
                //	newOrderActivity.id2=1;
                	newOrderActivity.mycart=null;
                	//changed=false;

	        	Intent intent = new Intent(this, MainActivity.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	Bundle mBundle = new Bundle();
	        	mBundle.putString("ID", "1");
	        	intent.putExtras(mBundle);
	        	startActivity(intent);
	        	finish();
	            return true;

	    }
	    return super.onOptionsItemSelected(item);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	newOrderActivity.myCartHash.clear();
        	newOrderActivity.itemExtra.clear();
        	newOrderActivity.markHash.clear();
        	newOrderActivity.myCartItemsWithExtras.clear();
        	if(newOrderActivity.mynewCartItemsWithExtras!=null)
            	newOrderActivity.mynewCartItemsWithExtras.clear();
            	if(newOrderActivity.mynewCartHash!=null)
            		newOrderActivity.mynewCartHash.clear();
            	newOrderActivity.tableName="";
            	newOrderActivity.id=0;
            //	newOrderActivity.id2=1;
            	newOrderActivity.mycart=null;
            	//changed=false;
	    	Intent intent11 = new Intent(getApplicationContext(), MainActivity.class);
        	intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	Bundle mBundle1 = new Bundle();
    		mBundle1.putString("ID", "1");
    		intent11.putExtras(mBundle1);
        	startActivity(intent11);


        	finish();

	        return true;
	        }

	      return super.onKeyDown(keyCode, event);
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

	 @Override
	 public void onStart() {
	 	super.onStart();




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
			    	 final Dialog dialog = new Dialog(PaymentActivity.this);
	    			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    			 dialog.setContentView(R.layout.select_restro);
	    			 final SelectPrinterAdapter adapter = new SelectPrinterAdapter(PaymentActivity.this,prtrs);
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



		/*  if(con_dev == null){
		 	Intent serverIntent = new Intent(PaymentActivity.this,DeviceListActivity.class);
		 	startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
		  }*/
		 }
		 else{
if(mService.getState()==3){
		 print_bt();
}

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
					 Toast.makeText(this, "Print successful", Toast.LENGTH_LONG).show();
			           }
			           else{

			        	   Toast.makeText(this, "Print Fail", Toast.LENGTH_LONG).show();
			           }
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
		         	//finish();
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
}
