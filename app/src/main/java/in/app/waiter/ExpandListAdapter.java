package in.app.waiter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ExpandListAdapter extends BaseExpandableListAdapter {

    public static ReorderAdapter adapter ;
    public static ReorderItemAdapter itemadapter ;
    public static CustExpListview SecondLevelexplv;
    public static SecondLevelAdapter Second;
   public Staff staff;
    private Context context;
    private int dialogwidth;
    private ArrayList<Category> groups;

    public ExpandListAdapter(Context context, ArrayList<Category> groups, int i) {
        this.context = context;
        this.groups = groups;
        this.dialogwidth=i;
      this.staff=DBAdapter.getStaffData(Main.getEmail(context));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Item> chList = groups.get(groupPosition).get_items();
        
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
       final Item current = (Item) getChild(groupPosition, childPosition);
       final Category group = (Category) getGroup(groupPosition);
        //if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.secondlevel_addnewitem, null);
        //}
            RelativeLayout is_addNewItem=(RelativeLayout)convertView.findViewById(R.id.is_addNewItem);
            //Toast.makeText(context, staff.get_staff_roll(),Toast.LENGTH_LONG).show();
            if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
        		is_addNewItem.setVisibility(View.VISIBLE);

			}
            else{
            	is_addNewItem.setVisibility(View.GONE);
            }
            is_addNewItem.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    			calldialog(group,groupPosition);	
    			}
    		});
            
        
     //   ArrayList<Item> current = categories.get(groupPosition).get_items();
        SecondLevelexplv = new CustExpListview(context);
     	// ExpandableListView SecondLevelexplv=new ExpandableListView(Home.this);
     	//  DebugExpandableListView SecondLevelexplv=new DebugExpandableListView(Home.this);
     	//  SecondLevelexplv.setRows(current.size());
         Second=new SecondLevelAdapter(context,current,groupPosition,dialogwidth);
        SecondLevelexplv.setAdapter(Second);
       // SecondLevelexplv.setHeaderDividersEnabled(false);
        //SecondLevelexplv.setFooterDividersEnabled(false);
        
        //SecondLevelexplv.
        SecondLevelexplv.setGroupIndicator(null);  
        SecondLevelexplv.setChildIndicator(null);
        SecondLevelexplv.setDivider(null);
        for(int i=0; i < Second.getGroupCount(); i++)
     	   SecondLevelexplv.expandGroup(i);
       /* if(getGroupCount()-1==groupPosition){
        	SecondLevelexplv.addFooterView(convertView);
        
        }*/
        if(getChildrenCount(groupPosition)-1==childPosition)
        {
        	SecondLevelexplv.addFooterView(convertView);	
        }
       
        return SecondLevelexplv;
    	}
    	

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Item> chList = groups.get(groupPosition).get_items();
        
        return chList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded,
            View convertView, final ViewGroup parent) {
    	
        final Category group = (Category) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.group_item, parent,false);
         
        }
        
        final View viewcopy=convertView;
        TextView listHeaderText = (TextView) convertView.findViewById(R.id.group_name);
        final ImageView listHeaderArrow = (ImageView) convertView.findViewById(R.id.left_menu_list_header_arrow);
        RelativeLayout rel=(RelativeLayout)convertView.findViewById(R.id.Rel_left_menu_list_header_arrow);
        RelativeLayout groupclick=(RelativeLayout)convertView.findViewById(R.id.Rel_group_click);
        RelativeLayout is_addNewItem=(RelativeLayout)convertView.findViewById(R.id.is_addNewItem);
        
        listHeaderText.setText(group.get_name());
        if(group.get_items().size()>0){
        	is_addNewItem.setVisibility(View.GONE);
        }
        else{
        	 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
        		is_addNewItem.setVisibility(View.VISIBLE);

			}
        	        }
        is_addNewItem.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			calldialog(group,groupPosition);	
			}
		});
          //Set the arrow programatically, so we can control it
	   // int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
       // int imageResourceId=viewcopy.getResources().getIdentifier("com.example.menu3:drawable/arrow_down_float",null,null);
	   // listHeaderArrow.setImageResource(imageResourceId);
        listHeaderArrow.setImageResource(isExpanded? viewcopy.getResources().getIdentifier("in.app.waiter:drawable/arrow_up_float",null,null):viewcopy.getResources().getIdentifier("in.app.waiter:drawable/arrow_down_float",null,null));
		
	   
	    rel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
		            else ((ExpandableListView) parent).expandGroup(groupPosition);
				 

			}
		});
	    groupclick.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String nm=group.get_name();
				//if(!nm.contentEquals("Uncategorized")){
			//	int imageResourceId=viewcopy.getResources().getIdentifier("com.example.menu3:drawable/arrow_up_float",null,null);
			  //  listHeaderArrow.setImageResource(imageResourceId);
				 final Dialog dialog = new Dialog(context);
				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 
				 dialog.setContentView(R.layout.group_item_select);
				 TextView itemname=(TextView) dialog.findViewById(R.id.tv_cat_name);
				 
				// itemname.setText(group.get_name());
				 itemname.setText(group.get_name());
				// rel_cancel_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_btn);
				// rel_add_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
				// rel_add_item_btn.setOnClickListener(btnitem_addOnClickListener);
				 //rel_cancel_item_btn.setOnClickListener(btnitem_CancelOnClickListener);
				 RelativeLayout Dlt=(RelativeLayout) dialog.findViewById(R.id.Rel_cat_delete);
				 Dlt.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						
						
						  AlertDialog.Builder builder = new AlertDialog.Builder(context);
					        builder.setTitle("Delete "+nm+"?");
					        builder.setMessage("Do you want to delete the category "+nm+" ?? All the item belongs to this category will be deleted ");
					        builder.setPositiveButton("Delete",
					                new DialogInterface.OnClickListener() {

					                    @Override
					                    public void onClick(DialogInterface dialog, int which) {
					                    	DBAdapter.deleteCategory(group.get_id());
											GamesFragment.ExpListItems.remove(groupPosition);
											GamesFragment.myDepartments.remove(group.get_name());
											GamesFragment.ExpAdapter.notifyDataSetChanged();
											GamesFragment.closeDialogs(); 
											try
											 {
													 ArrayList<HashMap<String, String>> wordList;
											        wordList = new ArrayList<HashMap<String, String>>();
											        HashMap<String, String> map = new HashMap<String, String>();
											        map.put("oprn","Delete");
											        map.put("table", "Category");			        
											        map.put("cid",String.valueOf(group.get_id()));
											        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
											       wordList.add(map);
											        Gson gson = new GsonBuilder().create();
												 crudonserver crudop=new crudonserver();
												 crudop.callserverforcrudopern(gson.toJson(wordList),context);
												 
											 }catch(Exception e)
											 {
												// error=String.valueOf(e.getMessage());
												 //Toast.makeText(context,Toast.LENGTH_LONG).show();
											 }
											dialog.dismiss();
					                        }
					                });

					        builder.setNegativeButton("No",
					                new DialogInterface.OnClickListener() {

					                    @Override
					                    public void onClick(DialogInterface dialog, int which) {
					                        // TODO Auto-generated method stub
					                        dialog.dismiss();
					                       
					                    }
					                });

					        AlertDialog alert = builder.create();
					       // if(myCartHash.size()>0){
					        alert.show();
					        GamesFragment.closeDialogs(); 
						// TODO Auto-generated method stub
					
					}
				});
				 RelativeLayout Rename=(RelativeLayout) dialog.findViewById(R.id.Rel_cat_rename);
				 Rename.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						final Dialog dialog = new Dialog(context);
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						 dialog.setContentView(R.layout.rename_item);
						 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
						 TextView pricename=(TextView) dialog.findViewById(R.id.tv_item_rename);
						 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_rename);
						 RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_save_item_rename);
						 EditText price=(EditText) dialog.findViewById(R.id.et_item_rename);
						 price.setText(group.get_name());
						 final Editable price1=price.getText();
						String s="Rename "+group.get_name();
						pricename.setText(s);
						cancelbtn.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								GamesFragment.closeDialogs(); 
							}
						});
						savebtn.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								//Category c=DBAdapter.ge
								//GamesFragment m=new GamesFragment();
								//GamesFragment.myDepartments.remove(group.get_name());
								group.set_name(price1.toString());
								//GamesFragment.myDepartments.get(group);
								//GamesFragment.myDepartments.put(group.get_name(),group);
												//tv2.setText(price1);
								DBAdapter.updateCatName(group.get_id(), price1.toString());
								
								GamesFragment.ExpAdapter.notifyDataSetChanged();
								GamesFragment.closeDialogs();
								 try
								 {
										 ArrayList<HashMap<String, String>> wordList;
								        wordList = new ArrayList<HashMap<String, String>>();
								        HashMap<String, String> map = new HashMap<String, String>();
								        map.put("oprn","Update");
								        map.put("table", "Category");
								        map.put("field", "Name");
								        map.put("cid",String.valueOf(group.get_id()));
								        map.put("name",String.valueOf(price1));
								        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
								       wordList.add(map);
								        Gson gson = new GsonBuilder().create();
									 crudonserver crudop=new crudonserver();
									 crudop.callserverforcrudopern(gson.toJson(wordList),context);
									 
								 }catch(Exception e)
								 {
									// error=String.valueOf(e.getMessage());
									 //Toast.makeText(context,Toast.LENGTH_LONG).show();
								 }
							}
						});
						 GamesFragment.dialogs.add(dialog);
						 GamesFragment.closeDialogs();
						 dialog.getWindow().setLayout(dialogwidth, LayoutParams.WRAP_CONTENT);
						 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
							dialog.show();
						}
						
					}
				});
				 RelativeLayout addnewitem=(RelativeLayout) dialog.findViewById(R.id.Rel_add__new_item);
				// addnewitem.setVisibility(View.GONE);
				 addnewitem.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							 final Dialog dialog = new Dialog(context);													 
							 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							 dialog.setContentView(R.layout.add_new_item);
							 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
							// TextView pricename=(TextView) dialog.findViewById(R.id.tv_item_rename);
							 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_btn);
							 final RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
							 final EditText itemname=(EditText) dialog.findViewById(R.id.et_new_item);
							 final EditText itemprice=(EditText) dialog.findViewById(R.id.et_new_itemprice);
								
							cancelbtn.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									GamesFragment.closeDialogs();
								}
							});
							
						/*	itemname.addTextChangedListener(new TextWatcher() {
								
								@Override
								public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
									// TODO Auto-generated method stub
									//flag=true;
									
								}
								
								@Override
								public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
										int arg3) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void afterTextChanged(Editable arg0) {
									// TODO Auto-generated method stub
									
								}
							});*/
							
							savebtn.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									 String item = itemname.getText().toString();
									 String price = itemprice.getText().toString();
									 if(!item.trim().equals("")){
										if(! DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", item.toString().trim())){
									 itemname.setText("");
									 
									   //add a new item to the list
									   //int groupPosition = GamesFragment.addProduct(group.get_name(), item) ;
									 Item i=new Item();
									 i.set_name(item);
									
										 if(!price.toString().trim().equals("")){
												Float Rprice=Float.parseFloat(price.toString());
												itemprice.setText("");
												i.set_price(String.format("%.2f", Rprice ));																
										 }
										 else{
											 i.set_price("0"); 
										 }
									 
									
									 i.set_flag("I");
									 i.set_category(group);
									// Category cat = (Category) adapter.getItem(groupPosition);
										int d=DBAdapter.addItemData(i);
										//DBAdapter.addItemSeq(d, d);
										
										i.set_sequence(d+1000);
										i.set_id(d);
										//Toast.makeText(context, String.valueOf(group.get_id()), Toast.LENGTH_LONG).show();
										Toast.makeText(context, "Item Saved", Toast.LENGTH_LONG).show();
										//(child.get_id(), cat.get_id());
										//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
										//GamesFragment.ExpListItems.get(cat.get_id()).addChild(child);
										//int index=GamesFragment.ExpListItems.indexOf(cat);
										GamesFragment.ExpListItems.get(groupPosition).addChild(i);
										//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
										GamesFragment.ExpAdapter.notifyDataSetChanged();
										GamesFragment.closeDialogs();
											   //addProduct(department,product);
									   //notify the list so that changes can take effect
										
									   GamesFragment.ExpAdapter.notifyDataSetChanged();
									   GamesFragment.expandAll();
									
									GamesFragment.closeDialogs();
									try
									 {
											 ArrayList<HashMap<String, String>> wordList;
									        wordList = new ArrayList<HashMap<String, String>>();
									        HashMap<String, String> map = new HashMap<String, String>();
									        map.put("oprn","Insert");
									        map.put("table", "Item");
									        map.put("id",String.valueOf(d));
									        map.put("name",i.get_name());
									        map.put("price",i.get_price());
									        map.put("flag",i.get_flag());
									        map.put("seq", String.valueOf(d+1000));
									        map.put("cid", String.valueOf(group.get_id()));
									        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
									       wordList.add(map);
									        Gson gson = new GsonBuilder().create();
										 crudonserver crudop=new crudonserver();
										 crudop.callserverforcrudopern(gson.toJson(wordList),context);
										 
									 }catch(Exception e)
									 {
										// error=String.valueOf(e.getMessage());
										// Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
									 }
									 		}else{
									 			Toast.makeText(context, "Item already exists", Toast.LENGTH_LONG).show();
									 		}
									 
									 }
									 else{
										 Toast.makeText(context, "Enter Item Name", Toast.LENGTH_LONG).show();
											
									 }
								}
							});
							
							 GamesFragment.dialogs.add(dialog);
							 GamesFragment.closeDialogs();
							 dialog.getWindow().setLayout(dialogwidth, LayoutParams.WRAP_CONTENT);
							 dialog.show();
						}
					});
				 
				 RelativeLayout reorderCat=(RelativeLayout) dialog.findViewById(R.id.Rel_cat_reorder_cat);
				 RelativeLayout reorderItem=(RelativeLayout) dialog.findViewById(R.id.Rel_cat_reorderitem);
				 RelativeLayout addNewExtra=(RelativeLayout) dialog.findViewById(R.id.Rel_cat_addnewextra);
				 addNewExtra.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							final Dialog dialog = new Dialog(context);
							
							 
							 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							 dialog.setContentView(R.layout.add_new_extra);
							 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
							// TextView pricename=(TextView) dialog.findViewById(R.id.tv_item_rename);
							 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_extra_btn);
							 RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_extra_btn);
							 final EditText itemname=(EditText) dialog.findViewById(R.id.et_new_extra);
							
							cancelbtn.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									GamesFragment.closeDialogs();
								}
							});
							savebtn.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									 String item = itemname.getText().toString();
									 if(!item.trim().equals("")){
										 Boolean isExists=false;
										// if(! DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", item.toString().trim())){
									if(!DBAdapter.CheckIsCatExtraAlreadyInDBorNot(item.toString().trim(),group.get_id())){
										for (Item Citem : DBAdapter.getItemsbyCatIdnFlag(group.get_id())) {
											if(DBAdapter.CheckIsExtraAlreadyInDBorNot(item.toString().trim(), Citem.get_id()))
											{
												isExists=true;
												break;
											}
										}
									}else{
										isExists=true;
									}
									if(!isExists){
										 itemname.setText("");
									    
									   //add a new item to the list
									   //int groupPosition = GamesFragment.addProduct(group.get_name(), item) ;
									 Item i=new Item();
									 i.set_name(item);
									 i.set_price("0");
									 i.set_flag("E");
									 i.set_category(group);
									// Category cat = (Category) adapter.getItem(groupPosition);
										int d=DBAdapter.addItemData(i);
										//DBAdapter.addItemSeq(d, d);
										i.set_sequence(d);
										i.set_id(d);
										try
										 {
												 ArrayList<HashMap<String, String>> wordList;
										        wordList = new ArrayList<HashMap<String, String>>();
										        HashMap<String, String> map = new HashMap<String, String>();
										        map.put("oprn","Insert");
										        map.put("table", "Item");
										        map.put("id",String.valueOf(d));
										        map.put("name",i.get_name());
										        map.put("price","0.00");
										        map.put("flag",i.get_flag());
										        map.put("seq", String.valueOf(d));
										        map.put("cid", String.valueOf(group.get_id()));
										        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
										       wordList.add(map);
										        Gson gson = new GsonBuilder().create();
											 crudonserver crudop=new crudonserver();
											 crudop.callserverforcrudopern(gson.toJson(wordList),context);
											 
										 }catch(Exception e)
										 {
											// error=String.valueOf(e.getMessage());
											 //Toast.makeText(context,Toast.LENGTH_LONG).show();
										 }
										ArrayList<Item> its=DBAdapter.getItemsbyCatId(group.get_id());
										Collections.sort(its,new myItemComparable());
										//Item minSeqItem;
										int upseq,myseq;
										/*for(int x=0;x<its.size()-1;x++)
								
										{		myseq=	its.get(x).get_sequence();
												upseq=	its.get(x+1).get_sequence();
												DBAdapter.addItemSeq(its.get(x).get_id(), upseq);												
												DBAdapter.addItemSeq(its.get(x+1).get_id(), myseq);
												try
												 {
														 ArrayList<HashMap<String, String>> wordList;
												        wordList = new ArrayList<HashMap<String, String>>();
												        HashMap<String, String> map = new HashMap<String, String>();
												        map.put("oprn","Update");
												        map.put("table", "Item");
												        map.put("field", "Seq");
												        map.put("id",String.valueOf(its.get(x).get_id()));
												        map.put("seq",String.valueOf(upseq));
												        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
												       wordList.add(map);
												        Gson gson = new GsonBuilder().create();
													 crudonserver crudop=new crudonserver();
													 crudop.callserverforcrudopern(gson.toJson(wordList));
													 
												 }catch(Exception e)
												 {
													// error=String.valueOf(e.getMessage());
													 //Toast.makeText(context,Toast.LENGTH_LONG).show();
												 }
									        	try
												 {
														 ArrayList<HashMap<String, String>> wordList;
												        wordList = new ArrayList<HashMap<String, String>>();
												        HashMap<String, String> map = new HashMap<String, String>();
												        map.put("oprn","Update");
												        map.put("table", "Item");
												        map.put("field", "Seq");
												        map.put("id",String.valueOf(its.get(x+1).get_id()));
												        map.put("seq",String.valueOf(myseq));
												        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
												       wordList.add(map);
												        Gson gson = new GsonBuilder().create();
													 crudonserver crudop=new crudonserver();
													 crudop.callserverforcrudopern(gson.toJson(wordList));
													 
												 }catch(Exception e)
												 {
													// error=String.valueOf(e.getMessage());
													 //Toast.makeText(context,Toast.LENGTH_LONG).show();
												 }
												its.get(x).set_sequence(upseq);
												its.get(x+1).set_sequence(myseq);
										}
										*/
										//GamesFragment.ExpListItems.get(groupPosition).set_items(its);
										//ArrayList<Item> its=DBAdapter.getItemsbyCatId(group.get_id());
										Collections.sort(its,new myItemComparable());
										//GamesFragment.ExpListItems.get(groupPosition).addChild(i);
										//group.addChild(i);
										//Collections.sort(group.get_items(),new myItemComparable());
										for(Item x:its)
							        	{
							        		x.set_category(group);
							        		try{
							        		ArrayList<Extra> _extra=DBAdapter.getExtrasbyItemId(x.get_id());
							        		x.set_extra(_extra);
							        		for(Extra y:_extra)
							        		{
							        			y.set_item(x);
							        		}
							        		}
							        		catch(Exception e){}
							        	}
										group.set_items(its);
											//	DBAdapter.addItemSeq(i1.get_id(),d);
												//minSeqItem=i1;
											
										// for(int i=0; i < GamesFragment.ExpAdapter.getGroupCount(); i++)
									//	GamesFragment.ExpandList.expandGroup(groupPosition);
									//	Toast.makeText(context, i.get_id()+" "+i.get_sequence(), Toast.LENGTH_LONG).show();
										//(child.get_id(), cat.get_id());
										//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
										//GamesFragment.ExpListItems.get(cat.get_id()).addChild(child);
										//int index=GamesFragment.ExpListItems.indexOf(cat);
										//GamesFragment.ExpListItems.get(groupPosition).addChild(i);
										//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
										GamesFragment.ExpAdapter.notifyDataSetChanged();
										ExpandListAdapter.Second.notifyDataSetChanged();
										//GamesFragment.closeDialogs();
											   //addProduct(department,product);
									   //notify the list so that changes can take effect
									   
									//   GamesFragment.ExpAdapter.notifyDataSetChanged();
									   GamesFragment.expandAll();
									  expandAll();
								//	expandAll();
									GamesFragment.closeDialogs();
										 }else{
											 Toast.makeText(context, "Extra already exists", Toast.LENGTH_LONG).show();
										 }
									 }
									 else{
										 Toast.makeText(context, "Enter Item Name", Toast.LENGTH_LONG).show();
											
									 }
								}
							});
							
							 GamesFragment.dialogs.add(dialog);
							 GamesFragment.closeDialogs();
							 dialog.getWindow().setLayout(dialogwidth, LayoutParams.WRAP_CONTENT);
							 dialog.show();
						}
					});
				

				 reorderCat.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							final Dialog dialog = new Dialog(context);
							 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							 dialog.setContentView(R.layout.reorder_category);
							adapter = new ReorderAdapter(context, GamesFragment.ExpListItems);
					         
						        ListView listView = (ListView) dialog.findViewById(R.id.lv_reordercat);
						        listView.setAdapter(adapter);
						      
						     //  RelativeLayout uparrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_up_arrow);
						      // RelativeLayout downarrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_down_arrow);
						       	 GamesFragment.dialogs.add(dialog);
						       	GamesFragment.closeDialogs();
							 dialog.show();
						}
					});
				 reorderItem.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							final Dialog dialog = new Dialog(context);
							 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							 dialog.setContentView(R.layout.reorder_item);
							 //ArrayList<Item> it
							 GamesFragment.ExpListItems.get(groupPosition).set_items(DBAdapter.getItemsbyCatIdnFlag(group.get_id()));
							itemadapter = new ReorderItemAdapter(context, GamesFragment.ExpListItems.get(groupPosition).get_items(),groupPosition);
					         
						        ListView listView = (ListView) dialog.findViewById(R.id.lv_reorderitem);
						        listView.setAdapter(itemadapter);
						      
						     //  RelativeLayout uparrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_up_arrow);
						      // RelativeLayout downarrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_down_arrow);
						       	 GamesFragment.dialogs.add(dialog);
						       	GamesFragment.closeDialogs();
						       	dialog.getWindow().setLayout(dialogwidth, LayoutParams.WRAP_CONTENT);
							 dialog.show();
						}
					});
					
				 
				 GamesFragment.dialogs.add(dialog);
				 GamesFragment.closeDialogs();
				 dialog.getWindow().setLayout(dialogwidth, LayoutParams.WRAP_CONTENT);
				 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
						dialog.show();
					}
				 
			
			}
		});
	    
	    
        return convertView;
        
    /*	String headerTitle=(String) getGroup(groupPosition);
    	if(convertView==null){
    		LayoutInflater infalInflater=(LayoutInflater) context
    				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		convertView=infalInflater.inflate(R.layout.group_item,parent,false);
    	}
    	 TextView listHeaderText = (TextView) convertView
    	            .findViewById(R.id.group_name);
    	    ImageView listHeaderArrow = (ImageView) convertView.findViewById(R.id.left_menu_list_header_arrow);

    	    listHeaderText.setText(headerTitle);
    	    
    	    //Set the arrow programatically, so we can control it
    	    int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
    	    listHeaderArrow.setImageResource(imageResourceId);
    	    listHeaderArrow.setOnClickListener(new View.OnClickListener() {

    	        @Override
    	        public void onClick(View v) {

    	            if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
    	            else ((ExpandableListView) parent).expandGroup(groupPosition, true);

    	        }
    	    });
    	    listHeaderArrow.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(isExpanded) ((ExpandableListView)parent).collapseGroup(groupPosition);
					
					
				}
			});

    	
    	return convertView;*/
    }

    protected void calldialog(final Category group,final int groupPosition) {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(context);	
		 
		 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 dialog.setContentView(R.layout.add_new_item);
		 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		// TextView pricename=(TextView) dialog.findViewById(R.id.tv_item_rename);
		 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_btn);
		 final RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
		 final EditText itemname=(EditText) dialog.findViewById(R.id.et_new_item);
		 final EditText itemprice=(EditText) dialog.findViewById(R.id.et_new_itemprice);
			
		cancelbtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GamesFragment.closeDialogs();
			}
		});
		
	/*	itemname.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				//flag=true;
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});*/
		
		savebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 String item = itemname.getText().toString();
				 String price = itemprice.getText().toString();
				 if(!item.trim().equals("")){
					 if(! DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", item.toString().trim())){
				 itemname.setText("");
				    
				   //add a new item to the list
				   //int groupPosition = GamesFragment.addProduct(group.get_name(), item) ;
				 Item i=new Item();
				 i.set_name(item);
				 if(!price.toString().trim().equals("")){
						Float Rprice=Float.parseFloat(price.toString());
						itemprice.setText("");
						i.set_price(String.format("%.2f", Rprice ));
						
										
				 }
				 else{
					 i.set_price("0"); 
				 }
			 
				// i.set_price("0");
				 i.set_flag("I");
				 i.set_category(group);
				// Category cat = (Category) adapter.getItem(groupPosition);
					int d=DBAdapter.addItemData(i);
					//DBAdapter.addItemSeq(d, d);
					
					i.set_sequence(d+1000);
					i.set_id(d);
					//Toast.makeText(context, String.valueOf(group.get_id()), Toast.LENGTH_LONG).show();
					//Toast.makeText(context, i.get_id()+" "+i.get_sequence(), Toast.LENGTH_LONG).show();
					//(child.get_id(), cat.get_id());
					//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
					//GamesFragment.ExpListItems.get(cat.get_id()).addChild(child);
					//int index=GamesFragment.ExpListItems.indexOf(cat);
					GamesFragment.ExpListItems.get(groupPosition).addChild(i);
					//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
					GamesFragment.ExpAdapter.notifyDataSetChanged();
					GamesFragment.closeDialogs();
						   //addProduct(department,product);
				   //notify the list so that changes can take effect
					
				   GamesFragment.ExpAdapter.notifyDataSetChanged();
				   GamesFragment.expandAll();
				
				GamesFragment.closeDialogs();
				
				try
				 {
						 ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				        HashMap<String, String> map = new HashMap<String, String>();
				        map.put("oprn","Insert");
				        map.put("table", "Item");
				        map.put("id",String.valueOf(d));
				        map.put("name",i.get_name());
				        map.put("price",i.get_price());
				        map.put("flag",i.get_flag());
				        map.put("seq", String.valueOf(d+1000));
				        map.put("cid", String.valueOf(group.get_id()));
				        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
				       wordList.add(map);
				        Gson gson = new GsonBuilder().create();
					 crudonserver crudop=new crudonserver();
					 crudop.callserverforcrudopern(gson.toJson(wordList),context);
					 calldialog(group,groupPosition);
						
				 }catch(Exception e)
				 {
					// error=String.valueOf(e.getMessage());
					 //Toast.makeText(context,Toast.LENGTH_LONG).show();
				 }
					 }
					 else{
						 Toast.makeText(context, "Item already exists", Toast.LENGTH_LONG).show();
					 }
				 }
				 else{
					 Toast.makeText(context, "Enter Item Name", Toast.LENGTH_LONG).show();
						
				 }
			}
		});
		
		 GamesFragment.dialogs.add(dialog);
		 GamesFragment.closeDialogs();
		 dialog.getWindow().setLayout(dialogwidth, LayoutParams.WRAP_CONTENT);
		 dialog.show();
	
	}

	protected void calldialog() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    public  void expandAll() {
     	  int count = Second.getGroupCount();
     	// Toast.makeText(context, count, Toast.LENGTH_LONG).show();
     	  for (int i = 0; i < count; i++){
     		 SecondLevelexplv.expandGroup(i);
     	  }
     	 }
    
}




