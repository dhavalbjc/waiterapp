package in.app.waiter;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class Item_selected extends Fragment {

	 /*
     * Change to type CustomAutoCompleteView instead of AutoCompleteTextView 
     * since we are extending to customize the view and disable filter
     * The same with the XML view, type will be CustomAutoCompleteView
     */
	public static int lastExpandedPosition = -1;
    public static LinearLayout Payment_option;
    static CustomAutoCompleteView myAutoComplete;
  //   static CartItemsAdapter cartItemadapter;
   //  static cartItemExpandableListAdapter cartItemadapter1;
    static CustomExpandableListAdapter cartItemadapter1;
    static ExpandableListView listView =null;
    static ArrayAdapter<Item> myAdapter;
    static ValueChangeListener activityCallback;
	 	private final Handler handler = new Handler() {

         public void handleMessage(Message msg) {

             String aResponse = msg.getData().getString("message");

             if ((null != aResponse)) {

                 // ALERT MESSAGE
                 Toast.makeText(
                         getActivity(),
                         aResponse,
                         Toast.LENGTH_SHORT).show();
             }
             else
             {

                     // ALERT MESSAGE
                     Toast.makeText(
                             getActivity(),
                             "Not Got Response From Server.",
                             Toast.LENGTH_SHORT).show();
             }

         }
     };
    // adapter for auto-complete
	Handler mHandler = new Handler();
    StringBuilder buf=new StringBuilder();

  /*  public static  void calltocallback()
    {
    	
    	activityCallback.onButtonClick();
    	 
    }*/
    @Override
    public void onAttach(Activity activity)
    {
    	super.onAttach(activity);
    	try{
    		activityCallback=(ValueChangeListener)activity;
    		
    	}catch(ClassCastException e)
    	{
    		throw new ClassCastException(activity.toString() 
    		+"must imliment ValueChangeListener");
    	}
    }
   
	
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.is_main_layout, container, false);
		
	//	RelativeLayout itemPlus=(RelativeLayout)rootView.findViewById(R.id.Rel_Item_plus);
		/*
		itemPlus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try{
				String itemName=myAutoComplete.getText().toString();
				if(!itemName.trim().equals("") ){
				if(DBAdapter.hasItemData(itemName)){
					if(newOrderActivity.myCartHash.containsKey(itemName)){
					//	Toast.makeText(getActivity(),"in if"+itemName,Toast.LENGTH_LONG).show();
						CartItems i1=newOrderActivity.myCartHash.get(itemName);
                    	int q=newOrderActivity.myCartHash.get(i1.get_item().get_name()).get_quantity();
                    	Toast.makeText(getActivity(),"q= "+q,Toast.LENGTH_LONG).show();
                    	q++;
                    	newOrderActivity.myCartHash.get(i1.get_item().get_name()).set_quantity(q);
                    	
                    	//newOrderActivity.cartItems.get(newOrderActivity.cartItems.indexOf(i1.get_item())).set_quantity(q);
                    }
                    else {
                    	//Toast.makeText(getActivity(),"in else"+itemName,Toast.LENGTH_LONG).show();
                    	
                    		//Toast.makeText(getActivity(),"in my if"+itemName,Toast.LENGTH_LONG).show();
                    	Item di=DBAdapter.getItemData(itemName);
                    	 CartItems c=new CartItems();
                         ArrayList<Extra> _extra=DBAdapter.getExtrasbyItemId(di.get_id());
                         ArrayList<Item> _extra_items=DBAdapter.getItemsbyCatIdnFlagOff(di.get_category().get_id());
                         for(Item i1:_extra_items)
                    	     di.addExtra(i1);
                    	 for(Extra e1:_extra)            	 
                    		 di.addExtra(e1);

                    	 for(Extra y:_extra)
                 			y.set_item(di);
                         
                         c.set_item(di);
                         c.set_quantity(1);
                    	newOrderActivity.myCartHash.put(di.get_name(), c);
                    	 newOrderActivity.cartItems.add(c);
                    	// newOrderActivity.mycart.setProducts(c);
                    	
                    	
                    	 
					}
				}
				else{
					
					//Toast.makeText(getActivity(),"in bigelse"+itemName,Toast.LENGTH_LONG).show();
				int _index=GamesFragment.addProduct("Uncategorized", itemName);
				Category c=GamesFragment.ExpListItems.get(_index);
				CartItems ci=new CartItems();
				Item i=DBAdapter.getItemData( itemName);
				//i.set_name( itemName);
				//i.set_flag("I");
				//i.set_price("0.00");
                //ArrayList<Extra> _extra=DBAdapter.getExtrasbyItemId(selected.get_id());
                //selected.set_extra(_extra);
                ci.set_item(i);
                ci.set_quantity(1);
           	newOrderActivity.myCartHash.put( itemName, ci);
           	 newOrderActivity.cartItems.add(ci);
				
           	// newOrderActivity.mycart.setProducts(ci);
				}
				 cartItemadapter1.notifyDataSetChanged();
                 activityCallback.onButtonClick();
                 myAutoComplete.setText("");
                 	}
             	else{
             		Toast.makeText(getActivity(),"Enter Item Name",Toast.LENGTH_LONG).show();
             	}
                 }catch(Exception e)
				{
			Toast.makeText(getActivity(), e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		});
		*/
		return rootView;
	}
	@Override
	public void onStart() {
				super.onStart();

				final RelativeLayout table=(RelativeLayout) getView().findViewById(R.id.rel_is_table);
				RelativeLayout is_addNewItem=(RelativeLayout) getView().findViewById(R.id.is_addNewItem);
				Payment_option=(LinearLayout) getView().findViewById(R.id.Payment_option);
				LinearLayout print=(LinearLayout) getView().findViewById(R.id.btn_print);
				LinearLayout pay=(LinearLayout) getView().findViewById(R.id.btn_pay);
				 Staff s=DBAdapter.getStaffData(Main.getEmail(getActivity()));
				 if(s.get_staff_roll().equals("admin")||s.get_staff_roll().equals("owner")||s.get_staff_roll().equals("manager")){
					// is_addNewItem.setVisibility(View.VISIBLE);
					 is_addNewItem.setVisibility(View.GONE);
				 }
				 else{
					 is_addNewItem.setVisibility(View.GONE);
				 }
			  
				if(newOrderActivity.id==0){
				Payment_option.setVisibility(View.GONE);
				}
				if(newOrderActivity.changed)
				{
					Item_selected.Payment_option.setVisibility(View.GONE);
				}
				if(newOrderActivity.cartItems.isEmpty()){
					if(newOrderActivity.tableName.equals(""))
					       
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
												//fillData();
												table.performClick();    
											}

										});
										//Thread.sleep(27000);
										// secondTime=true;

									} catch (Exception e) {
										// TODO: handle exception
									}
								
							}
						}).start();
			
					}
			pay.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 /*if (newOrderActivity.mService != null) 
						 newOrderActivity.mService.stop();
					 newOrderActivity.mService = null; 
					 newOrderActivity.con_dev=null;*/
					Intent intent11 = new Intent(getActivity(), PaymentActivity.class);  
		        	intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        	Bundle mBundle1 = new Bundle();
		    		mBundle1.putString("ID", String.valueOf(newOrderActivity.mycart.getId()));
		    		intent11.putExtras(mBundle1);
		    		startActivity(intent11);
				}
			});
			print.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
				  	 try{
			        	// listView = (ListView) findViewById(R.id.payment_listview);
			        	 ModelCart mycart=DBAdapter.getCartData(newOrderActivity.mycart.getId());
		                 buf.delete(0, buf.length());
			        	 buf.append("Table "+mycart.getTableName());
			        	 buf.append(System.getProperty("line.separator"));
			 			//Toast.makeText(getApplicationContext(),mycart.getTableName(),Toast.LENGTH_LONG).show();
			 			//tableName=mycart.getTableName();
			 			//cartItems=DBAdapter.getCartItemsDataByCartId(id);
			 			//cartItems=DBAdapter.getCartItemsDataByCartId(mycart.getId());
			     		//cardArrayAdapter.addAll(DBAdapter.getAllStaff());
			     		//ModelCart c=Reports_Fragment.BillCart.get(String.valueOf(id));
			        	// ModelCart c=Reports_Fragment.BillCart.get(String.valueOf(id));
			        	 for (CartItems cartItems : DBAdapter.getAllCartItemsDataByCartId(mycart.getId())) {
			        		 mycart.setProduct(cartItems);
			        		 Item I=DBAdapter.getItemData(cartItems.get_item().get_id());
								//Toast.makeText(context, I.get_price(), Toast.LENGTH_SHORT).show();
								int q=cartItems.get_quantity();
								Double d=q*Double.parseDouble(I.get_price());					 
								
								cartItems.get_item().set_price(I.get_price());
								
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
			        		 for (CartItems iterable_element : DBAdapter.getCartItemsExtraforDeleteData(cartItems.get_id())) {
			        		//Toast.makeText(getApplicationContext(), String.valueOf(iterable_element.get_item().get_id()), Toast.LENGTH_LONG).show();
			        			if(iterable_element.get_item().get_id()!=0)
								{
									Extra e=DBAdapter.getExtraData(iterable_element.get_item().get_id());
									iterable_element.get_item().set_price(String.valueOf(Double.parseDouble(e.get_price())));	
									buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name());
									 int size1=25-cartItems.get_item().get_name().length();
					        		 if(size1>0){
					        		 for(int i=0;i < size1;i++)
					        		 {	        			
					        			 buf.append(" ");
					        		 }
					        		 }
					        		 buf.append(String.valueOf(d));
					 				buf.append(System.getProperty("line.separator"));
								}
								else
								{Item ip=DBAdapter.getItemData(iterable_element.get_item().get_name());
								iterable_element.get_item().set_price(String.valueOf(Double.parseDouble(ip.get_price())));
								buf.append(cartItems.get_quantity()+" "+cartItems.get_item().get_name()+"   "+String.valueOf(ip.get_price()));

				 				buf.append(System.getProperty("line.separator"));
								//dp=Double.parseDouble(ip.get_price());
									
								}
			        			 mycart.setProduct(iterable_element);
							}
						}
			        	 buf.append(System.getProperty("line.separator"));
			        	 buf.append("Total");       		 	        			
		        		 buf.append("                      ");  
		        		 buf.append(mycart.getTotal()); 
		        		 
			        	 buf.append(System.getProperty("line.separator"));
			        	
			        	// buf.append("Thanks for visit");
			        	// buf.append(System.getProperty("line.separator"));
			        	 buf.append("------------------------------");
			        	 buf.append(System.getProperty("line.separator"));
			        	 buf.append(System.getProperty("line.separator"));
			        	 buf.append(System.getProperty("line.separator"));
			        	 buf.append(System.getProperty("line.separator"));
			       //String msg=newOrderActivity.print_total(buf);
			     
		 		   
		 			 ArrayList<Printer> prtrs=DBAdapter.getAllPrinterData();
					    int size=prtrs.size();
					    if(size==1){
					    	pairPrinter(prtrs.get(0).get_mac()); 	     	
					      
					    }else if(size>=1){
					    	 final Dialog dialog = new Dialog(getActivity());	                         
			    			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			    			 dialog.setContentView(R.layout.select_restro);
			    			 final SelectPrinterAdapter adapter = new SelectPrinterAdapter(getActivity(),prtrs);
			    			 final TextView title = (TextView) dialog.findViewById(R.id.txt_select_restro);
			    			 title.setText("Select Printer");
			    		        final ListView listView = (ListView) dialog.findViewById(R.id.lv_select_restro);
			    		        listView.setAdapter(adapter);
			    		        listView.setOnItemClickListener(new OnItemClickListener() {

			    				@Override
			    				public void onItemClick(AdapterView<?> parent, View v,
			    						int groupPosition, long id) {
			    					Printer printer=(Printer)parent.getItemAtPosition(groupPosition);
			    					pairPrinter(printer.get_mac());
			    			        dialog.dismiss();
			    				//	Toast.makeText(getBaseContext(),String.valueOf(Restro.get_restro_id()),Toast.LENGTH_LONG).show();
			    		
			    				}
			    			});
			    		        	            		     	            			
			    			 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
			    			 dialog.show();
					    }
					  
			     //  buf.delete(0, buf.length());
			      /*8 if(msg.equals("printer")){
			    	   Toast.makeText(getActivity(), "No printer found ", Toast.LENGTH_LONG).show();
			       }
			       if(msg.equals("false")){
			    	   Toast.makeText(getActivity(), "Blutooth is off ", Toast.LENGTH_LONG).show();
			       }
				  	if(msg.equals("true")){
				    	   Toast.makeText(getActivity(), "Print successful ", Toast.LENGTH_LONG).show();
				       }*/
				  	}
			        	 catch(Exception e)
			        	 {
			        		// Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			        	 }
			        	 
					
				}
			});
				//table.setVisibility(View.GONE);
				//cartItemadapter = new CartItemsAdapter(container.getContext(),newOrderActivity.cartItems);
				cartItemadapter1 = new CustomExpandableListAdapter(getActivity(),newOrderActivity.cartItems,getWidth(),"a");
		        
				listView = (ExpandableListView) getView().findViewById(R.id.lv_cartitems);
				
				listView.setOnGroupExpandListener(new OnGroupExpandListener() {

		    	    @Override
		    	    public void onGroupExpand(int groupPosition) {
		    	            if (lastExpandedPosition != -1
		    	                    && groupPosition != lastExpandedPosition) {
		    	            	listView.collapseGroup(lastExpandedPosition);
		    	            }
		    	            lastExpandedPosition = groupPosition;
		    	    }
		    	});
				listView.setGroupIndicator(null);
		        listView.setAdapter(cartItemadapter1);
		        Helper.getListViewSize(listView);
		        listView.requestFocus();
		      
		       // listView.setFocusable(false);
		        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					
					@Override
					public boolean onGroupClick(ExpandableListView parent, View v,
							int groupPosition, long id) {
						// TODO Auto-generated method stub
					/*	Item group = (Item) adapter.getGroup(groupPosition);
						Toast
			            .makeText(container.getContext(), group.get_name(), Toast.LENGTH_SHORT)
			            .show();*/
						return true;
					}
				
				    });
		         
		        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
		            @Override
		            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		              /*  Extra child = (Extra) adapter.getChild(groupPosition, childPosition);
		         
		                Toast
		                    .makeText(container.getContext(), child.get_name(), Toast.LENGTH_SHORT)
		                    .show();
		         */
		                return true;
		            }
		        });
		       
				 final TextView tableName=(TextView) getView().findViewById(R.id.tv_tablename);
				 if(!tableName.equals(""))
				 tableName.setText(newOrderActivity.tableName);
				 is_addNewItem.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						 final Dialog dialog = new Dialog(getActivity());	
						 
						 RelativeLayout rel_add_item_btn,rel_cancel_item_btn;
						   
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						 dialog.setContentView(R.layout.add_new_item);
						 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
						 rel_cancel_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_btn);
						 rel_add_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
						 rel_add_item_btn.setOnClickListener(new RelativeLayout.OnClickListener() {
							EditText et=(EditText)dialog.findViewById(R.id.et_new_item);
							EditText et2=(EditText)dialog.findViewById(R.id.et_new_itemprice);
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								 String item = et.getText().toString();
								 String price = et2.getText().toString();
								 try{
								 if(!item.trim().equals(""))
								 {
									 if(! DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", item.toString().trim())){
								 
									 int cid = 0;
									 Boolean hasData=false;
									   et.setText("");
									   Category cat = new Category();
									   if(DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_category","category_name","Uncategorized")){
										   cat =DBAdapter.getCategoryData("Uncategorized");
										   hasData=true;
									   }
									   else{
										   cat.set_name("Uncategorized");							 
										   cid=DBAdapter.addCategoryData(cat);							
										   cat.set_sequence(cid);
										   cat.set_id(cid);
										 //  GamesFragment.ExpListItems.add(cat);
									   }
									   Item detailInfo = new Item();
										 // detailInfo.setSequence(String.valueOf(listSize));
										  detailInfo.set_name(item);
										  //detailInfo.set_price("0.00");
										  if(!price.toString().trim().equals("")){
												Float Rprice=Float.parseFloat(price.toString());
												et2.setText("");
												detailInfo.set_price(String.format("%.2f", Rprice ));
												
																
										 }
										 else{
											 detailInfo.set_price("0"); 
										 }
										  detailInfo.set_category(DBAdapter.getCategoryData(cat.get_id()));
										  detailInfo.set_flag("I");
										  int a=DBAdapter.addItemData(detailInfo);
										  // DBAdapter.addExtraSeq(a, a);
										 // DBAdapter.addItemSeq(a, a);
										   detailInfo.set_sequence(a);
										   detailInfo.set_id(a);
										//int _index=GamesFragment.addProduct("Uncategorized", item);
										//Category c=GamesFragment.ExpListItems.get(_index);
										CartItems ci=new CartItems();
										//Item i=DBAdapter.getItemData( item);								
						                ci.set_item(detailInfo);
						                ci.set_quantity(1);
						           	newOrderActivity.myCartHash.put( item, ci);
						           	 newOrderActivity.cartItems.add(ci);
						           	 try
									 {	if(!hasData){
											 ArrayList<HashMap<String, String>> wordList;
									        wordList = new ArrayList<HashMap<String, String>>();
									        HashMap<String, String> map = new HashMap<String, String>();
									        map.put("oprn","Insert");
									        map.put("table", "Combo");
									        map.put("cid",String.valueOf(cat.get_id()));
									        map.put("catname",cat.get_name());
									       // map.put("catseq", String.valueOf(headerInfo.get_id()));
									        map.put("id",String.valueOf(a));
									        map.put("name",detailInfo.get_name());
									        map.put("price",detailInfo.get_price());
									        map.put("flag","I");
									       // map.put("seq", String.valueOf(a));
									        //map.put("cid", String.valueOf(headerInfo.get_id()));
									        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
									       wordList.add(map);
									        Gson gson = new GsonBuilder().create();
										 crudonserver crudop=new crudonserver();
										 crudop.callserverforcrudopern(gson.toJson(wordList),getActivity());}
									 else{
										 ArrayList<HashMap<String, String>> wordList;
									        wordList = new ArrayList<HashMap<String, String>>();
									        HashMap<String, String> map = new HashMap<String, String>();
									        map.put("oprn","Insert");
									        map.put("table", "Item");
									        map.put("id",String.valueOf(a));
									        map.put("name",item);
									        map.put("price",detailInfo.get_price());
									        map.put("flag","I");
									        map.put("seq", String.valueOf(a+1000));
									        map.put("cid", String.valueOf(cat.get_id()));
									        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
									       wordList.add(map);
									        Gson gson = new GsonBuilder().create();
										 crudonserver crudop=new crudonserver();
										 crudop.callserverforcrudopern(gson.toJson(wordList),getActivity());
									 }
									 }catch(Exception e)
									 {
										// error=String.valueOf(e.getMessage());
										 //Toast.makeText(context,Toast.LENGTH_LONG).show();
									 }
						           	// newOrderActivity.mycart.setProducts(ci);
										
										 cartItemadapter1.notifyDataSetChanged();
										 Helper.getListViewSize(listView);
						                 activityCallback.onButtonClick();
						                
									dialog.dismiss();
									 }else{
										 Toast.makeText(getActivity(), "Item already exists", Toast.LENGTH_LONG).show();
									 }
								 }else{
									 Toast.makeText(getActivity(), "Enter Item Name", Toast.LENGTH_LONG).show();
								 }
								 
							}catch(Exception e)
							{
								Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
							}
							
							}	
							
						});
						 rel_cancel_item_btn.setOnClickListener(new RelativeLayout.OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								dialog.dismiss();
								
							}
						});
						 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
						 dialog.show();
						 
						
					}
				});
				table.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						final Dialog dialog = new Dialog(getActivity());
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						 dialog.setContentView(R.layout.order_for);
										 RelativeLayout _1=(RelativeLayout) dialog.findViewById(R.id.Rel_is_1);
						 RelativeLayout _2=(RelativeLayout) dialog.findViewById(R.id.Rel_is_2);
						 RelativeLayout _3=(RelativeLayout) dialog.findViewById(R.id.Rel_is_3);
						 RelativeLayout _4=(RelativeLayout) dialog.findViewById(R.id.Rel_is_4);
						 RelativeLayout _5=(RelativeLayout) dialog.findViewById(R.id.Rel_is_5);
						 RelativeLayout _6=(RelativeLayout) dialog.findViewById(R.id.Rel_is_6);
						 RelativeLayout _7=(RelativeLayout) dialog.findViewById(R.id.Rel_is_7);
						 RelativeLayout _8=(RelativeLayout) dialog.findViewById(R.id.Rel_is_8);
						 RelativeLayout _9=(RelativeLayout) dialog.findViewById(R.id.Rel_is_9);
						 RelativeLayout _10=(RelativeLayout) dialog.findViewById(R.id.Rel_is_10); 
						 RelativeLayout _11=(RelativeLayout) dialog.findViewById(R.id.Rel_is_11);
						 RelativeLayout _12=(RelativeLayout) dialog.findViewById(R.id.Rel_is_12);
						 RelativeLayout _13=(RelativeLayout) dialog.findViewById(R.id.Rel_is_13);
						 RelativeLayout _14=(RelativeLayout) dialog.findViewById(R.id.Rel_is_14);
						 RelativeLayout _15=(RelativeLayout) dialog.findViewById(R.id.Rel_is_15);
						 RelativeLayout _16=(RelativeLayout) dialog.findViewById(R.id.Rel_is_16);
						 RelativeLayout _17=(RelativeLayout) dialog.findViewById(R.id.Rel_is_17);
						 RelativeLayout _18=(RelativeLayout) dialog.findViewById(R.id.Rel_is_18);
						 RelativeLayout _19=(RelativeLayout) dialog.findViewById(R.id.Rel_is_19);
						 RelativeLayout _20=(RelativeLayout) dialog.findViewById(R.id.Rel_is_20);
						 RelativeLayout _21=(RelativeLayout) dialog.findViewById(R.id.Rel_is_21);
						 RelativeLayout _22=(RelativeLayout) dialog.findViewById(R.id.Rel_is_22);
						 RelativeLayout _23=(RelativeLayout) dialog.findViewById(R.id.Rel_is_23);
						 RelativeLayout _24=(RelativeLayout) dialog.findViewById(R.id.Rel_is_24);
						 RelativeLayout _25=(RelativeLayout) dialog.findViewById(R.id.Rel_is_25);
						 RelativeLayout _26=(RelativeLayout) dialog.findViewById(R.id.Rel_is_26);
						 RelativeLayout _27=(RelativeLayout) dialog.findViewById(R.id.Rel_is_27);
						 RelativeLayout _28=(RelativeLayout) dialog.findViewById(R.id.Rel_is_28);
						 RelativeLayout _29=(RelativeLayout) dialog.findViewById(R.id.Rel_is_29);
						 RelativeLayout _30=(RelativeLayout) dialog.findViewById(R.id.Rel_is_30);
						 RelativeLayout.OnClickListener tableSelected
						 = new RelativeLayout.OnClickListener(){

						 @Override
						 public void onClick(View arg0) {
						  // TODO Auto-generated method stub
							
							 //Toast.makeText(getBaseContext(), "Colosed", Toast.LENGTH_SHORT).show();
							 RelativeLayout rl = (RelativeLayout) arg0;
			                    TextView tv = (TextView) rl.getChildAt(0);
							
							 tableName.setText(tv.getText().toString());
							// newOrderActivity.mycart.setTableName("Table "+tv.getText().toString());
							 newOrderActivity.tableName=tv.getText().toString();
							 
							 GamesFragment.closeDialogs();
							 
							  // closeDialogs();
							  // dialog.dismiss();
							 
							 
						 }};
						
						_1.setOnClickListener(tableSelected);
						_2.setOnClickListener(tableSelected);				
						_3.setOnClickListener(tableSelected);
						_4.setOnClickListener(tableSelected);
						_5.setOnClickListener(tableSelected);				
						_6.setOnClickListener(tableSelected);
						_7.setOnClickListener(tableSelected);
						_8.setOnClickListener(tableSelected);				
						_9.setOnClickListener(tableSelected);
						_10.setOnClickListener(tableSelected);
						_11.setOnClickListener(tableSelected);				
						_12.setOnClickListener(tableSelected);
						_13.setOnClickListener(tableSelected);
						_14.setOnClickListener(tableSelected);				
						_15.setOnClickListener(tableSelected);				
						_16.setOnClickListener(tableSelected);
						_17.setOnClickListener(tableSelected);				
						_18.setOnClickListener(tableSelected);
						
						_19.setOnClickListener(tableSelected);
						_20.setOnClickListener(tableSelected);				
						_21.setOnClickListener(tableSelected);				
						_22.setOnClickListener(tableSelected);
						_23.setOnClickListener(tableSelected);				
						_24.setOnClickListener(tableSelected);
						
						_25.setOnClickListener(tableSelected);
						_26.setOnClickListener(tableSelected);				
						_27.setOnClickListener(tableSelected);				
						_28.setOnClickListener(tableSelected);
						_29.setOnClickListener(tableSelected);				
						_30.setOnClickListener(tableSelected);
						
						
						
						 GamesFragment.dialogs.add(dialog);
						 GamesFragment.closeDialogs();
						 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
						 dialog.show();	
					}
					 public  int getWidth()
					 {try 
				     {
				         
						 Display display = getActivity().getWindowManager().getDefaultDisplay();
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
					
				});
			
				try{
		            
		            // instantiate database handler
		          //  databaseH = new DatabaseHandler(MainActivity.this);
		             
		            // put sample data to database
		           // insertSampleData();
		             
		            // autocompletetextview is in activity_main.xml
					
		            myAutoComplete = (CustomAutoCompleteView) getView().findViewById(R.id.myautocomplete);
		            
		            myAutoComplete.setOnItemClickListener(new OnItemClickListener() {
		 
		                @Override
		                public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
		                     
		                    RelativeLayout rl = (RelativeLayout) arg1;
		                  //  TextView tv = (TextView) rl.getChildAt(0);
		                  
		                  //  MyObject selected = ((ArrayAdapter<MyObject>) parent).getItem(pos);
		                  //  MyObject[] selected = (MyObject[]) parent.getAdapter().getItem(pos);
		                  //  myAutoComplete.setText(tv.getText().toString());
		                  //  myAutoComplete.setText(selected[0].objectName.toString());
		                    Item selected=(Item) parent.getAdapter().getItem(pos);
		                   /* Toast.makeText(container.getContext(),
		                    		selected.get_name().toString()+pos,
		                            Toast.LENGTH_SHORT).show();*/
		                 /*   Toast.makeText(container.getContext(),
		                    		selected.get_name()+" added",
		                            Toast.LENGTH_SHORT).show();*/
		                    myAutoComplete.setText("");
		                    newOrderActivity.changed=true;
		                    Item_selected.Payment_option.setVisibility(View.GONE);
		                    if(newOrderActivity.myCartHash.containsKey(selected.get_name()+"0")){
		                    	try{
		                    	int q=newOrderActivity.myCartHash.get(selected.get_name()+"0").get_quantity();
		                    	q++;
		                    	newOrderActivity.myCartHash.get(selected.get_name()+"0").set_quantity(q);
		                    	//newOrderActivity.cartItems.get(newOrderActivity.cartItems.indexOf(selected)).set_quantity(q);
		                    	}
		                    	catch(Exception e)
		                    	{
		                    		Toast.makeText(getActivity(),e.getMessage().toString(),Toast.LENGTH_LONG).show();
		                    	}
		                    }
		                    else {
		                    	/*   ArrayList<Extra> _extra=null;
		                    	 _extra=DBAdapter.getExtrasbyItemId(selected.get_category().get_id());
		                		 ArrayList<Item> _extra_items=DBAdapter.getItemsbyCatIdnFlagOff(selected.get_category().get_id());
		                		
		                		 for(Item i1:_extra_items)
		                    	     selected.addExtra(i1);
		                    	 for(Extra e1:_extra)            	 
		                    		 selected.addExtra(e1);            
		                    	 for(Extra y:_extra)
		                    		 y.set_item(selected);
		                	         	*/
		                    	
		                    	
		                    	
		                    	 CartItems c=new CartItems();
		                         
		                         c.set_item(selected);
		                         c.set_quantity(1);
		                    	newOrderActivity.myCartHash.put(selected.get_name()+"0", c);
		                    	 newOrderActivity.cartItems.add(c);
		                    	// newOrderActivity.mycart.setProducts(c);
		                    	
		                    	 
							}
		                    Collections.sort(newOrderActivity.cartItems,new myCartitemComparableStatus());
							Collections.sort(newOrderActivity.cartItems,new myCartitemComparable());
		               
		                    cartItemadapter1.notifyDataSetChanged();
		                    Helper.getListViewSize(listView);
		                    activityCallback.onButtonClick();
		                     
		                }
		 
		            });
		             
		            // add the listener so it will tries to suggest while the user types
		           // myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(container.getContext()));
		             
		            // ObjectItemData has no value at first
		           // Item[] ObjectItemData = new Item[0];
		             
		            // set the custom ArrayAdapter
		       
		            
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
										//fillData();
									     myAutoComplete.setThreshold(2);
									        
								          //  myAdapter = new AutocompleteCustomArrayAdapter(container.getContext(), R.layout.list_view_row, ObjectItemData);
								        	 myAdapter=new AutocompleteCustomArrayAdapter(getActivity(),R.layout.list_view_row,DBAdapter.getAllItemData());
									          
								           myAutoComplete.setAdapter(myAdapter);    
									}

								});
								//Thread.sleep(27000);
								// secondTime=true;

							} catch (Exception e) {
								// TODO: handle exception
							}
						
					}
				}).start();
		          /*  ArrayList<Item> list = new ArrayList<Item>();
		            list=DBAdapter.getAllItemData();
		            ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(
		                    container.getContext(), android.R.layout.simple_dropdown_item_1line, list);
		            myAutoComplete.setAdapter(adapter);

		            myAutoComplete.setOnItemClickListener(new OnItemClickListener() {

		                @Override
		                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		                        long arg3) {
		                    Item selected = (Item) arg0.getAdapter().getItem(arg2);
		                    Toast.makeText(container.getContext(),
		                            "Clicked " + arg2 + " name: " + selected.get_name(),
		                            Toast.LENGTH_SHORT).show();
		                }
		            });
		         */
		        } catch (NullPointerException e) {
		            e.printStackTrace();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
	}
	public  int getWidth()
	 {try 
    {
        
		 Display display = getActivity().getWindowManager().getDefaultDisplay();
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
	 public void pairPrinter(final String macAdd)  {    
	        final UUID SerialPortServiceClass_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");    
	        final BluetoothAdapter BA = BluetoothAdapter.getDefaultAdapter();    
	        final String PrinterBsid = "00:19:5D:25:1B:4A"; // This is My Printer Bluetooth MAC Address    
	      
	       // final Runnable showToastMessage = new Runnable()
	        Thread t = new Thread(new Runnable() {  
	            @Override  
	            public void run() {  
	                OutputStream sOut;  
	                BluetoothSocket socket;  
	                BA.cancelDiscovery();  
	  
	  
	               // BluetoothDevice BD = BA.getRemoteDevice(PrinterBsid);  
	                BluetoothDevice BD = BA.getRemoteDevice(macAdd);  
	                try {  
	                    socket = BD.createInsecureRfcommSocketToServiceRecord(SerialPortServiceClass_UUID);  
	  
	  
	                    if (!socket.isConnected()) {  
	                        socket.connect();  
	                        Thread.sleep(1000); // <-- WAIT FOR SOCKET  
	                    }  
	                    sOut = socket.getOutputStream();  
	                    String cpclData = "! 0 200 200 210 1\r\n"  
	                            + "TEXT 4 0 30 40 This is a CPCL test.\r\n"  
	                            + "FORM\r\n"  
	                            + "PRINT\r\n";  
	                  //  sOut.write(cpclData.getBytes());  
	                    String cpclData1=buf.toString();
	                    sOut.write(cpclData1.getBytes());  
	                   // Toast.makeText(newOrderActivity.this, "Print Successfull", Toast.LENGTH_SHORT).show();
	                  
	                    sOut.close();  
	                   // msg="Print Successfull";
	                    socket.close();  
	                    BA.cancelDiscovery();  
	                    buf.delete(0, buf.length());
	                   // mHandler1.post(mUpdateResults);
	                    threadMsg("Print Successfull");
	                } catch (IOException e) {  
	                    Log.e("","IOException"); 
	                  //  msg="Print Fail "+e.getMessage();
	                    threadMsg("Print Fail "+e.getMessage());
	                    e.printStackTrace();  
	                    //return;  
	                } catch (InterruptedException e) {  
	                    e.printStackTrace();  
	                    threadMsg("Print Fail "+e.getMessage());
	                  //  msg="Print Fail "+e.getMessage();
	                }  
	            }  
	        });  
	  
	        t.start(); 
	       // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            
	        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
         
	    }  
	 
	/* final Runnable mUpdateResults = new Runnable() {
	 	    public void run() {
	 	    	Toast.makeText(getApplicationContext(),"Print Successfull",Toast.LENGTH_SHORT).show();
	 	    }
	 	};*/
	 	 private void threadMsg(String msg) {
	 		 
          if (!msg.equals(null) && !msg.equals("")) {
              Message msgObj = handler.obtainMessage();
              Bundle b = new Bundle();
              b.putString("message", msg);
              msgObj.setData(b);
              handler.sendMessage(msgObj);
          }
      }

    public interface ValueChangeListener{
	   public void onButtonClick();

   }
}