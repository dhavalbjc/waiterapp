package in.app.waiter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class Reports_Fragment extends Fragment {
    //public static ArrayList<ModelCart> BillCart=new ArrayList<ModelCart>();
    public static HashMap<String, ModelCart> BillCart = new HashMap<String, ModelCart>();
    public static LinkedHashMap<String,ItemsReport> hashItems = new LinkedHashMap< String,ItemsReport>();
    private static int count=0;
	String popUpContents[];
    PopupWindow popupWindowDogs;
    Button buttonShowDropDown;
    Double rtotal;
    int orderCount=0;
    ScrollView sv;
    TextView tvRtotal,tvOrder,tvitems;
    LinearLayout lOrder,lOrder1,lOrder2;
    ArrayList<UsersAvenue> arrayList =new ArrayList<UsersAvenue>();
    ArrayList<ItemsReport> itemsArrayList =new ArrayList<ItemsReport>();
    private ReportArrayAdapeter cardArrayAdapter;
    private usersArrayAdapter usersAdapter;
    private IremsReportArrayAdapter itemArrayAdapter;
    private ListView listView,usersListview,itemsListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.reports_main, container, false);
		listView = (ListView) rootView.findViewById(R.id.report_listView);
		usersListview= (ListView) rootView.findViewById(R.id.users_listView);
		itemsListView= (ListView) rootView.findViewById(R.id.Items_listView);
		 tvRtotal=(TextView) rootView.findViewById(R.id.tvrevenuesTotal);
		 tvOrder=(TextView) rootView.findViewById(R.id.tvOrder);
		 tvitems=(TextView)rootView.findViewById(R.id.tv_items);
		 lOrder=(LinearLayout) rootView.findViewById(R.id.linearOrder);
		 lOrder.setVisibility(View.GONE);
		 lOrder1=(LinearLayout) rootView.findViewById(R.id.linearOrder2);
		 lOrder1.setVisibility(View.GONE);
		 lOrder2=(LinearLayout) rootView.findViewById(R.id.lin2);
		 lOrder2.setVisibility(View.GONE);
		  sv = (ScrollView) rootView.findViewById(R.id.orderScroll);
		  listView.setFocusable(false);
		  usersListview.setFocusable(false);
		  itemsListView.setFocusable(false);
		cardArrayAdapter = new ReportArrayAdapeter(container.getContext(), R.layout.child_item);
		itemArrayAdapter=new IremsReportArrayAdapter(container.getContext(),itemsArrayList);
		
		arrayList.removeAll(arrayList);
		for (Staff staff : DBAdapter.getAllStaff()) {
			UsersAvenue ua=new UsersAvenue();
			ua.set_uname(staff.get_staff_fname());
			ua.set_avenue("0.00");
			arrayList.add(ua);
		}
		
		usersAdapter= new usersArrayAdapter(container.getContext(), arrayList);
		
		count=0;
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		formatter.setLenient(false);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");			
		Date oldDate;
		String oldTime="";
		try {
			oldDate = formatter.parse(formatter.format(new Date()));
			oldTime = format.format(oldDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// users Listview
    	
    	
		//Toast.makeText(getActivity(), oldTime, Toast.LENGTH_LONG).show();
		String	serverURL = Config.YOUR_SERVER_URL+"ordersync.php";		     
        LongOperation serverRequest = new LongOperation();



		if (Build.VERSION.SDK_INT >= 11) {
			serverRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,serverURL,"1",String.valueOf(DBAdapter.getRestaurantId()),oldTime);
		} else {
			serverRequest.execute(serverURL,"1",String.valueOf(DBAdapter.getRestaurantId()),oldTime);
		}
        Helper.getListViewSize(listView);
       // Helper.getListViewSize(usersListview);
 listView.setOnItemClickListener(new OnItemClickListener() {
			
			
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long id) {
				final ModelCart cart=(ModelCart)adapter.getItemAtPosition(position);
				Intent intent11 = new Intent(getActivity(), BillActivity.class);  
            	intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	Bundle mBundle1 = new Bundle();
        		mBundle1.putString("ID", String.valueOf(cart.getId()));
        		intent11.putExtras(mBundle1);
            	startActivity(intent11);
				
			}
			
		});
		
		//cardArrayAdapter.addAll(DBAdapter.getAllStaff());
		//for (ModelCart cart : DBAdapter.getAllCartData()) {
			//cardArrayAdapter.add(cart);
	//	}
		//listView.setAdapter(cardArrayAdapter);
	        // initialize pop up window items list
	         
	        // add items on the array dynamically
	        // format is DogName::DogID
	        List<String> dogsList = new ArrayList<String>();
	        dogsList.add("Today::1");
	        dogsList.add("Yesterday::2");
	        dogsList.add("This week::3");
	        dogsList.add("This month::4");
	        dogsList.add("Last 7 days::5");
	        dogsList.add("Last 30 days::6");
	        dogsList.add("Last 90 days::7");
	 
	        // convert to simple array
	        popUpContents = new String[dogsList.size()];
	        dogsList.toArray(popUpContents);
	 
	         
	        // initialize pop up window
	        popupWindowDogs = popupWindowDogs();
	 
	         
	        // button on click listener
	         
	        View.OnClickListener handler = new View.OnClickListener() {
	            public void onClick(View v) {                                                                                                                                                                                                                                                                                                 
	 
	                switch (v.getId()) {
	 
	                case R.id.buttonShowDropDown:
	                    // show the list view as dropdown
	                    popupWindowDogs.showAsDropDown(v, -5, 0);
	                    break;
	                }
	            }
	        };
	        
	 
	        // our button
	        buttonShowDropDown = (Button) rootView.findViewById(R.id.buttonShowDropDown);
	        buttonShowDropDown.setOnClickListener(handler);
	        
	        
	        
		return rootView;
	}

	 public PopupWindow popupWindowDogs() {
		 
	        // initialize a pop up window type
	        PopupWindow popupWindow = new PopupWindow(getActivity());
	 
	        // the drop down list is a list view
	        ListView listViewDogs = new ListView(getActivity());
	        
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
			       buttonShowDropDown.setText(selectedItemText);
			         
			        // get the id
			       cardArrayAdapter = new ReportArrayAdapeter(getActivity(), R.layout.child_item);
			       count=1;
			       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					  formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
					formatter.setLenient(false);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
					Date oldDate;
					String oldTime="";
					try {
						oldDate = formatter.parse(formatter.format(new Date()));
						oldTime = format.format(oldDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        String selectedItemTag = ((TextView) v).getTag().toString();
			        String	serverURL = Config.YOUR_SERVER_URL+"ordersync.php";		     
			        LongOperation serverRequest = new LongOperation(); 
			        serverRequest.execute(serverURL,selectedItemTag,String.valueOf(DBAdapter.getRestaurantId()),oldTime);
			       // Toast.makeText(getActivity(), "Dog ID is: " + selectedItemTag, Toast.LENGTH_SHORT).show();
			        lOrder.setVisibility(View.GONE);
	       			lOrder1.setVisibility(View.GONE);
	       			lOrder2.setVisibility(View.GONE);
					
				}
			});
	 
	        // some other visual settings
	        Display display = getActivity().getWindowManager().getDefaultDisplay();
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
	        //popupWindow.showAtLocation(new View(getActivity()), Gravity.CENTER_VERTICAL, 10, 10);
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
	 
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dogsArray) {
	 
	            @Override
	            public View getView(int position, View convertView, ViewGroup parent) {
	 
	                // setting the ID and text for every items in the list
	                String item = getItem(position);
	                String[] itemArr = item.split("::");
	                String text = itemArr[0];
	                String id = itemArr[1];
	 
	                // visual settings for the list item
	                TextView listItem = new TextView(getActivity());
	 
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
	 // Class with extends AsyncTask class
		public class LongOperation  extends AsyncTask<String, Void, String> {
		         
		    	// Required initialization
		    	
		        String data ="";
		        int sizeData = 0;
		        //private final HttpClient Client = new DefaultHttpClient();
		       // private Controller aController = null;
		        private String Error = null;
		        private ProgressDialog Dialog = new ProgressDialog(getActivity());
		        
		        protected void onPreExecute() {
		            // NOTE: You can call UI Element here.
		             
		            //Start Progress Dialog (Message)
		           
		            Dialog.setMessage("Please Wait..");
		            if(count>0){
		            
		            Dialog.show();
		            }
		            
		        }
		 
		        // Call after onPreExecute method
		        protected String doInBackground(String... params) {
		        	
		        	//syncSQLiteMySQLDB();
		        	/************ Make Post Call To Web Server ***********/
		        	BufferedReader reader=null;
		        	String Content = "";
			             // Send data 
			            try{
			            	// Defined URL  where to send data
				               URL url = new URL(params[0]);
			            	
				            // Set Request parameter
				            if(!params[1].equals(""))
			               	   data +="&" + URLEncoder.encode("data", "UTF-8") + "="+params[1].toString();
				            if(!params[2].equals(""))
				               	   data +="&" + URLEncoder.encode("data2", "UTF-8") + "="+params[2].toString();	
				            if(!params[3].equals(""))
				               	   data +="&" + URLEncoder.encode("data3", "UTF-8") + "="+params[3].toString();
			              Log.i("GCM",data);
				            
				          // Send POST data request
			   
			              URLConnection conn = url.openConnection(); 
			              conn.setDoOutput(true); 
			              conn.setConnectTimeout(5000);
			              conn.setReadTimeout(10000);
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
			               Content = sb.toString();
			            }catch (java.net.SocketTimeoutException e) {
			            	Error = e.getMessage();

			            } catch (java.io.IOException e) {
			            	Error = e.getMessage();

			            }
			            catch(Exception ex)
			            {
			            	Error = ex.getMessage();
			            }
			            finally
			            {
			                try
			                {
			     
			                    reader.close();
			                }
			   
			                catch(Exception ex) {}
			            }
		        	
		            /*****************************************************/
		            return Content;
		        //    return "";
		        }
		         
		        protected void onPostExecute(String Content) {
		            // NOTE: You can call UI Element here.
		             
		            // Close progress dialog
		            
		         // Toast.makeText(getApplicationContext(), Content, Toast.LENGTH_LONG).show();
		            if (Error != null) {
		                 
		                 	            } else {
		              
		            	// Show Response Json On Screen (activity)
		            	
		             /****************** Start Parse Response JSON Data *************/
		            	//aController.clearUserData();
		            	
		            	JSONObject jsonResponse;
		                      hashItems.clear();
		                try { 
		                     BillCart.clear(); 
		                     /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
		                     jsonResponse = new JSONObject(Content);
		                      
		                     /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
		                     /*******  Returns null otherwise.  *******/
		                    
		                     /*********** Process each JSON Node ************/
		                     
		                   //  JSONArray  jsonMainNode = jsonResponse.optJSONArray("CartItems");
		                    // DBAdapter.deleteAllcartItem();
		                    // manager.syncCartItemsJson(jsonMainNode);
		                    
		                     //DBAdapter.deleteAllcart();
		                     JSONArray jsonMainNode = jsonResponse.optJSONArray("Cart");
		                     //manager.syncCartJson(jsonMainNode);
		           
		                     int lengthJsonArr = jsonMainNode.length();  
		       		      HashMap<String, ModelCart> cartMap = new HashMap<String, ModelCart>();
		                     ArrayList<ModelCart> Mcart=new ArrayList<ModelCart>();
		       		      for(int i=0; i < lengthJsonArr; i++) 
		       		      {
		       		     	 /****** Get Object for each JSON node.***********/
		       		          JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		       		           
		       		          /******* Fetch node values **********/
		       		          String cart_id       = jsonChildNode.optString("id").toString();
		       		          String cart_table       = jsonChildNode.optString("cart_table").toString();
		       		          String cart_total       = jsonChildNode.optString("cart_total").toString();
		       		          String cart_user       = jsonChildNode.optString("cart_user").toString();
		       		          String cart_paid       = jsonChildNode.optString("cart_paid").toString();
		       		          String created_at       = jsonChildNode.optString("created_at").toString();
		       		          //String itemid       = jsonChildNode.optString("itemid").toString();
		       		          
		       		        ModelCart cart=new ModelCart();
		       		        cart.setId(Integer.parseInt(cart_id));
		       		        cart.setTableName(cart_table);
		       		        cart.setTotal(cart_total);
		       		        cart.setUser(cart_user);
		       		        cart.setPaid(cart_paid);
		       		        cart.setTime(created_at);
		       		         		                   
		       		     cartMap.put(String.valueOf(cart_id), cart);
		       		       Mcart.add(cart);	 
		       		       
		       		   
		       		   
		       		     }
		       		   JSONArray  jsonMainNode1 = jsonResponse.optJSONArray("CartItems");
		       		    int lengthJsonArr1 = jsonMainNode1.length();  
		       		    HashMap<String, CartItems> entryMap = new HashMap<String, CartItems>();
		       		    for(int i1=0; i1 < lengthJsonArr1; i1++) 
		       		    {
		       		   	 /****** Get Object for each JSON node.***********/
		       		        JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(i1);
		       		         
		       		        /******* Fetch node values **********/
		       		        String cartitem_id       = jsonChildNode1.optString("id").toString();
		       		        String cartitem_itemid       = jsonChildNode1.optString("cartitem_itemid").toString();
		       		        String cartitem_name       = jsonChildNode1.optString("cartitem_name").toString();
		       		        String cartitem_price       = jsonChildNode1.optString("cartitem_price").toString();
		       		        String cartitem_quantity       = jsonChildNode1.optString("cartitem_quantity").toString();
		       		        String cartitem_flag       = jsonChildNode1.optString("cartitem_flag").toString();
		       		        String cartitem_note       = jsonChildNode1.optString("cartitem_note").toString();
		       		        String cartitem_course       = jsonChildNode1.optString("cartitem_course").toString();
		       		        String cartitem_mark       = jsonChildNode1.optString("cartitem_mark").toString();
		       		        String cartitem_status       = jsonChildNode1.optString("cartitem_status").toString();
		       		        String cart_id1       = jsonChildNode1.optString("cart_id").toString();
		       		      
		       		        Item item=new Item();
		       		        item.set_id(Integer.parseInt(cartitem_itemid));
		       		        item.set_name(cartitem_name);
		       		        item.set_price(cartitem_price);
		       		        item.set_flag(cartitem_flag);
		       		        CartItems citem=new CartItems();
		       		        citem.set_id(Integer.parseInt(cartitem_id));
		       		        citem.set_quantity(Integer.parseInt(cartitem_quantity));
		       		        citem.set_mark(Integer.parseInt(cartitem_mark));
		       		        citem.set_note(cartitem_note);
		       		     citem.set_status(cartitem_status);
		       		     citem.set_course(Integer.parseInt(cartitem_course));
		       		        citem.set_item(item);
		       		       // ModelCart cart=DBAdapter.getCartData(Integer.parseInt(cart_id));
		       		        citem.set_cartid(Integer.parseInt(cart_id1));
		       		        //item.set_sequence(Integer.parseInt(item_seq));
		       		        //item.set_category(DBAdapter.getCategoryData(Integer.parseInt(cid)));
		       		                 
		       		        entryMap.put(String.valueOf(cartitem_id), citem);
		       		       // Toast.makeText(getActivity(), citem.get_item().get_name(), Toast.LENGTH_LONG).show();
		       		      	                 // cart.setProduct(citem);       
		       		   }    
		       		   try{
		       		    for (ModelCart modelCart : Mcart) {
		       		     for (CartItems cartItems : entryMap.values()) {
		       		    	//Toast.makeText(getActivity(), cartItems.get_item().get_name(),Toast.LENGTH_LONG).show();
								if(modelCart.getId()==cartItems.get_cartid())
								{
									//Toast.makeText(getActivity(), String.valueOf(cartItems.get_quantity()),Toast.LENGTH_LONG).show();
									modelCart.setProduct(cartItems);
									ItemsReport ir=new ItemsReport();
									ir.set_id(cartItems.get_quantity());
									ir.set_name(cartItems.get_item().get_name());
									int qty1=	cartItems.get_quantity();
									double total1=qty1*Double.parseDouble(cartItems.get_item().get_price());
									
									ir.set_total(String.valueOf(total1));
									if(hashItems.containsKey(ir.get_name())){
										hashItems.get(ir.get_name()).set_id(hashItems.get(ir.get_name()).get_id()+ir.get_id());									
										
										//try{
									//	Item I=DBAdapter.getItemData(cartItems.get_item().get_id());
									//	}catch()
										//Toast.makeText(getContext(), mList.get(position).get_quantity(), Toast.LENGTH_SHORT).show();
										int qty=	cartItems.get_quantity();
										double total=qty*Double.parseDouble(cartItems.get_item().get_price());
										
										 total+=Double.parseDouble(hashItems.get(ir.get_name()).get_total());
										//price.setText( String.valueOf(d) );
										hashItems.get(ir.get_name()).set_total( String.valueOf(total));
										//Toast.makeText(getActivity(), ir.get_name()+" "+total, Toast.LENGTH_SHORT).show();
									}
									else{
										hashItems.put(ir.get_name(), ir);
									}
									
									for (CartItems cartItems1 : entryMap.values()) {
										
										if(cartItems.get_id()==cartItems1.get_cartid())
										{
											modelCart.setProduct(cartItems1);
											ItemsReport ir1=new ItemsReport();
											ir1.set_id(cartItems1.get_quantity());
											ir1.set_name(cartItems1.get_item().get_name());
											ir1.set_total(cartItems1.get_item().get_price());
											int qty2=	cartItems1.get_quantity();
											double total2=qty2*Double.parseDouble(cartItems1.get_item().get_price());
											
											ir.set_total(String.valueOf(total2));
											if(hashItems.containsKey(ir1.get_name())){
												hashItems.get(ir1.get_name()).set_id(hashItems.get(ir1.get_name()).get_id()+1);									
												//int q=hashItems.get(ir1.get_name()).get_id();
												Double d=1*Double.parseDouble(cartItems1.get_item().get_price());
												 d+=Double.parseDouble(hashItems.get(ir1.get_name()).get_total());
											//price.setText( String.valueOf(d) );
											hashItems.get(ir1.get_name()).set_total( String.valueOf(d));
											}
											else{
												hashItems.put(ir1.get_name(), ir1);
											}
											//Toast.makeText(getActivity(), modelCart.getTableName(),Toast.LENGTH_LONG).show();
										//	entryMap.remove(String.valueOf(cartItems1.get_id()));
										}
									}
									//entryMap.remove(String.valueOf(cartItems.get_id()));
									
								}
							}
		       		  BillCart.put(String.valueOf(modelCart.getId()), modelCart);
						}
		       		/* for (CartItems cartItems : entryMap.values()) {
		       			try{
		       			 ModelCart cart=cartMap.get(cartItems.get_cartid());
		       			 if(cart==null)
		       			 {
		       				CartItems c=entryMap.get(cartItems.get_id());
		       			 }
		       			 else{
		       			cart.setProduct(cartItems);}
		       			}catch(Exception e)
		       			{
		       				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		       			}
		       		 }*/
		       		    
		       		   }catch(Exception e)
		       		   {
		       			   Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
		       		   }
		       		 
		       		   cardArrayAdapter.clear();
		       		
		       		  /* for (ModelCart e : entryMap.values()) {
		 		          
		       			cardArrayAdapter.add(e);
		 		    	//  DBAdapter.addCartData(e);
		 		    	//  manager.addCartData(e);	 		         
		 		    	 
		 		      }*/
		       		cardArrayAdapter.notifyDataSetChanged();
		       		rtotal=0.0d;
		       		//rtotalByUser=0.0d;
		       		orderCount=0;
		       		tvRtotal.setText("0.00");
		       		lOrder.setVisibility(View.GONE);
		       		lOrder1.setVisibility(View.GONE);
		       		lOrder2.setVisibility(View.GONE);
		       		tvOrder.setText("Orders");
		       		itemsArrayList.removeAll(itemsArrayList);
		       		
		       		for (int i=0;i<arrayList.size();i++) {
		       			arrayList.get(i).set_avenue("0.00");
					}
		       		   for (ModelCart modelCart : Mcart) {
		       			cardArrayAdapter.add(modelCart);
		       			rtotal+=Double.parseDouble(modelCart.getTotal());
		       			++orderCount;
		       			for (int i=0;i<arrayList.size();i++) {
							if(arrayList.get(i).get_uname().toString().equals(modelCart.getUser().toString()))
									{
								Double d=0.0d;
								try{
									d=Double.parseDouble(arrayList.get(i).get_avenue());
									}catch(Exception e)
									{
										
									}
								d+=Double.parseDouble(modelCart.getTotal());
								arrayList.get(i).set_avenue(String.valueOf(d));
									}
						}
					}
		       		tvRtotal.setText(String.valueOf(rtotal));
		       		tvOrder.setText(String.valueOf(orderCount)+" Orders");
		       		if(orderCount==0){
		       			tvOrder.setText(String.valueOf("Orders"));
		       			tvitems.setText(String.valueOf("Items"));
		       		}
		       			lOrder.setVisibility(View.VISIBLE);
		       			lOrder1.setVisibility(View.VISIBLE);
		       			lOrder2.setVisibility(View.VISIBLE);
		       		//}
		       		if(orderCount==1){
		       			tvOrder.setText(String.valueOf(orderCount)+" Order");
		       		}
		       		listView.setAdapter(null);
		       		listView.setAdapter(cardArrayAdapter);
		       
		       
		      // 	sv.scrollTo(0, 0);
		       	listView.setFocusable(false);
		       	
		    	Helper.getListViewSize(listView);
		    	
		    	usersAdapter.notifyDataSetChanged();
	    		
		    	 usersListview.setAdapter(null);
		    	 usersListview.setAdapter(usersAdapter);
		    	 usersListview.setFocusable(false);
			    	Helper.getListViewSize(usersListview);
			    	itemsArrayList.clear();
			    	
			    	for(Map.Entry<String, ItemsReport> entry: hashItems.entrySet())
	        		{
	    				String key = entry.getKey();
	    				itemsArrayList.add(entry.getValue());
	    					    				
	        		}
			    	int count=0;
			    	for (ItemsReport r : itemsArrayList) {
						count+=r.get_id();
					}
			    	//tvitems.setText(itemsArrayList.size()+" Items");
			    	tvitems.setText(count+" Items");
			    	if(orderCount==0){
		       			tvOrder.setText(String.valueOf("Orders"));
		       			tvitems.setText(String.valueOf("Items"));
		       		}
			    	Collections.sort(itemsArrayList,new Comparator<ItemsReport>() {
			            @Override
			            public int compare(ItemsReport  fruite1, ItemsReport  fruite2)
			            {
			            	int x=fruite2.get_id();
			            	int y=fruite1.get_id();
			            	 return x < y ? -1
			            	         : x > y ? 1
			            	         : 0;
			              //  return  String.valueOf(fruite2.get_id()).compareTo(String.valueOf(fruite1.get_id()));
			            }
			        });
			    	Collections.sort(arrayList,new Comparator<UsersAvenue>() {
			            @Override
			            public int compare(UsersAvenue  fruite1, UsersAvenue  fruite2)
			            {
			            	Double x=Double.parseDouble(fruite2.get_avenue().toString());
			            	Double y=Double.parseDouble(fruite1.get_avenue().toString());
			            	 return x < y ? -1
			            	         : x > y ? 1
			            	         : 0;
			               // return  String.valueOf(fruite2.get_avenue()).compareTo(String.valueOf(fruite1.get_avenue()));
			            }
			        });
			    	//Collections.reverse(arrayList);
			    	//Collections.reverse(itemsArrayList);
			    	
			    	//usersArrayAdapter.notifyDataSetChanged();
			    	//Collections.reverse(itemsArrayList);
		       itemArrayAdapter.notifyDataSetChanged();
		       itemsListView.setAdapter(itemArrayAdapter);
		       itemsListView.setFocusable(false);
		    	Helper.getListViewSize(itemsListView);
		                 } catch (JSONException e) {
		          
		                     e.printStackTrace();
		                 }
		  
		                 
		             }
		            
		            Dialog.dismiss();
		         
		    }
		       
		
		        
	}
		
	}
