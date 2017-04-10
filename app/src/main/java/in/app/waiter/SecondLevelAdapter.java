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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class SecondLevelAdapter extends BaseExpandableListAdapter
{
	  public static ReorderExtraAdapter itemadapter;
	  public static ReorderCExtraAdapter extraadapter;
	  public Staff staff;
	private Item item;
	private int dialogWidth;
	  private Context context;
	  private int index;

public SecondLevelAdapter(Context context,Item childs,int index,int dialogwidth) {
  
		// TODO Auto-generated constructor stub
	this.context=context;
  this.item=childs;
  this.index=index;
  this.dialogWidth=dialogwidth;
  this.staff=DBAdapter.getStaffData(Main.getEmail(context));
  
	}

@Override
public Object getChild(int groupPosition, int childPosition) 
{   
// return childPosition;
return item.get_extra().get(childPosition);
}

@Override
public long getChildId(int groupPosition, int childPosition) 
{   
return childPosition;
}

@Override
public View getChildView(int groupPosition, final int childPosition,
boolean isLastChild, View convertView, ViewGroup parent) 
{
  final Extra current = item.get_extra().get(childPosition);
	//textView_catName.setText(groupPosition + " , " + childPosition);
 // Item current=item.get(groupPosition);
  //final Item child = (Item) getChild(groupPosition, childPosition);
if (convertView == null) {
  LayoutInflater infalInflater = (LayoutInflater) context
          .getSystemService(context.LAYOUT_INFLATER_SERVICE);
  convertView = infalInflater.inflate(R.layout.child_item, null);
}

//final int groupPositionCopy=groupPosition;
// final int childPositionCopy=childPosition;
final TextView tv = (TextView) convertView.findViewById(R.id.item_name);
final TextView tv2 = (TextView) convertView.findViewById(R.id.price);
//ImageView iv = (ImageView) convertView.findViewById(R.id.flag);

tv.setText("  + "+current.get_name().toString());
tv2.setText(current.get_price());
RelativeLayout rel_item_bt=(RelativeLayout) convertView.findViewById(R.id.Rel_item_select);
RelativeLayout rel_price_btn=(RelativeLayout) convertView.findViewById(R.id.Rel_price_select);
// iv.setImageResource(child.getImage());
final String name=tv.getText().toString();
final String itemPrice=tv2.getText().toString();
rel_item_bt.setOnClickListener(new RelativeLayout.OnClickListener() {

@Override
public void onClick(View arg0) {
// TODO Auto-generated method stub
final Dialog dialog = new Dialog(context);
 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 dialog.setContentView(R.layout.extra_item_select);
 TextView itemname=(TextView) dialog.findViewById(R.id.tv_extra_name);
 itemname.setText(name);
 RelativeLayout Rename=(RelativeLayout) dialog.findViewById(R.id.Rel_extra_rename);
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
		 price.setText(current.get_name());
		 final Editable price1=price.getText();
		String s="Rename "+name;
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
				 if(!price1.toString().trim().equals("")){
				 
				 Boolean isExists=false;
					// if(! DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", item.toString().trim())){
				if(!DBAdapter.CheckIsCatExtraAlreadyInDBorNot(price1.toString().trim(),current.get_item().get_category().get_id())){
					for (Item Citem : DBAdapter.getItemsbyCatIdnFlag(current.get_item().get_category().get_id())) {
						if(DBAdapter.CheckIsExtraAlreadyInDBorNot(price1.toString().trim(), Citem.get_id()))
						{
							isExists=true;
						}
					}
				}else{
					isExists=true;
				}
				
				 if(!isExists ){
				 
				 
				
				current.set_name(price1.toString());
								tv2.setText(price1);
				DBAdapter.updateExtraName(current.get_id(), price1.toString());
				GamesFragment.ExpAdapter.notifyDataSetChanged();
				GamesFragment.closeDialogs(); 
				 try
				 {
						 ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				        HashMap<String, String> map = new HashMap<String, String>();
				        map.put("oprn","Update");
				        map.put("table", "Extra");
				        map.put("field", "Name");
				        map.put("id",String.valueOf(current.get_id()));
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
			}else{
				GamesFragment.closeDialogs(); 
	 			//Toast.makeText(context, "Item already exists", Toast.LENGTH_LONG).show();
	 		}
	 
	 }
	 else{
		 Toast.makeText(context, "Enter Extra Name", Toast.LENGTH_LONG).show();
			
	 }
			}
		
		});
		 GamesFragment.dialogs.add(dialog);
		 GamesFragment.closeDialogs();
		 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
		 dialog.show();
	}
});
 
 RelativeLayout ReorderExtra=(RelativeLayout) dialog.findViewById(R.id.Rel_extra_reorder);
 ReorderExtra.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			
			
			final Dialog dialog = new Dialog(context);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			 dialog.setContentView(R.layout.reorder_item);
			 //ArrayList<Item> it
			// GamesFragment.ExpListItems.get(groupPosition).set_items(DBAdapter.getItemsbyCatIdnFlag(group.get_id()));
			itemadapter = new ReorderExtraAdapter(context,item.get_extra());
	         
		        ListView listView = (ListView) dialog.findViewById(R.id.lv_reorderitem);
		        listView.setAdapter(itemadapter);
		      
		     //  RelativeLayout uparrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_up_arrow);
		      // RelativeLayout downarrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_down_arrow);
		       	 GamesFragment.dialogs.add(dialog);
		       	GamesFragment.closeDialogs();
		       	dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
			 dialog.show();
		}
	});
 RelativeLayout Dlt=(RelativeLayout) dialog.findViewById(R.id.Rel_extra_delete);
 Dlt.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle("Delete "+current.get_name()+"?");
	        builder.setMessage("Do you want to delete the extra "+current.get_name()+" ?? ");
	        builder.setPositiveButton("Delete",
	                new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	// TODO Auto-generated method stub
	                		DBAdapter.deleteExtra(current.get_id());
	                	//	int a=GamesFragment.ExpListItems.indexOf(child.get_category());
	                		//ExpandListAdapter.SecondLevelexplv.getChildAt(groupPositionCopy);
	                		//GamesFragment.ExpListItems
	                		//Item childa=(Item) getChild(groupPositionCopy, childPositionCopy);
	                		//ExpandListAdapter.(groupPositionCopy).rem;
	                	item.removeChild(childPosition);
	                	
	                		//GamesFragment.ExpListItems.get(index).gremoveChild(child);
	                		GamesFragment.ExpAdapter.notifyDataSetChanged();
	                		ExpandListAdapter.Second.notifyDataSetChanged();
	                		GamesFragment.closeDialogs(); 
	                		try
	                		 {
	                				 ArrayList<HashMap<String, String>> wordList;
	                		        wordList = new ArrayList<HashMap<String, String>>();
	                		        HashMap<String, String> map = new HashMap<String, String>();
	                		        map.put("oprn","Delete");
	                		        map.put("table", "Extra");			        
	                		        map.put("id",String.valueOf(current.get_id()));
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
		
	}
});
// rel_add_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
// rel_add_item_btn.setOnClickListener(btnitem_addOnClickListener);
 //rel_cancel_item_btn.setOnClickListener(btnitem_CancelOnClickListener);


 GamesFragment.dialogs.add(dialog);
 GamesFragment.closeDialogs();
 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
 
 
 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
	 dialog.show();

	}

	
 //et=(EditText)dialog.findViewById(R.id.et_new_item);

}
});
rel_price_btn.setOnClickListener(new RelativeLayout.OnClickListener() {

@Override
public void onClick(View arg0) {
final Dialog dialog = new Dialog(context);
 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 dialog.setContentView(R.layout.price_for_item);
 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
 TextView pricename=(TextView) dialog.findViewById(R.id.tv_titleforprice);
 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_price_btn);
 RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_save_price_btn);
 EditText price=(EditText) dialog.findViewById(R.id.et_item_price);
 if(Double.parseDouble(itemPrice)>0.0d){
 price.setText(itemPrice);}
 else{
	 price.setHint("0.00");
 }
 final Editable price1=price.getText();
String s="Price for "+name;
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
		try
		 {
		if(!price1.toString().trim().equals("")){
		Float Rprice=Float.parseFloat(price1.toString());
		
		current.set_price(String.format("%.2f", Rprice ));
		
						tv2.setText(price1);
		DBAdapter.updateExtraPrice(current.get_id(), price1.toString());
		GamesFragment.ExpAdapter.notifyDataSetChanged();
		GamesFragment.closeDialogs(); 
		
				 ArrayList<HashMap<String, String>> wordList;
		        wordList = new ArrayList<HashMap<String, String>>();
		        HashMap<String, String> map = new HashMap<String, String>();
		        map.put("oprn","Update");
		        map.put("table", "Extra");
		        map.put("field", "Price");
		        map.put("id",String.valueOf(current.get_id()));
		        map.put("price",String.valueOf(price1));
		        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
		       wordList.add(map);
		        Gson gson = new GsonBuilder().create();
			 crudonserver crudop=new crudonserver();
			 crudop.callserverforcrudopern(gson.toJson(wordList),context);
		 }
		 else{
			 Toast.makeText(context, "Enter Extra Price", Toast.LENGTH_SHORT).show();
			 }
		 }catch(Exception e)
		 {
			// error=String.valueOf(e.getMessage());
			 //Toast.makeText(context,Toast.LENGTH_LONG).show();
		 }
	}
});
 GamesFragment.dialogs.add(dialog);
 GamesFragment.closeDialogs();
 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
	 dialog.show();

	}

}
});

  /*
TextView tv = new TextView(Home.this);
tv.setText(current.get_name()+" "+groupPosition + " , " + childPosition);
tv.setPadding(15, 5, 5, 5);
tv.setBackgroundColor(Color.WHITE);
tv.setLayoutParams(new ListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
return tv;*/
return convertView;
}

@Override
public int getChildrenCount(int groupPosition) 
{
// return 5;
  return item.get_extra().size();
}

@Override
public Object getGroup(int groupPosition) 
{   
//return groupPosition;
  return item;
}

@Override
public int getGroupCount() 
{
return 1;
 // return item;
}

@Override
public long getGroupId(int groupPosition) 
{
return groupPosition;
}

@Override
public View getGroupView(final int groupPosition, boolean isExpanded,
View convertView, ViewGroup parent) 
{/*
  Item current=item;
  //final Item child = (Item) getChild(groupPosition, childPosition);
if (convertView == null) {
  LayoutInflater infalInflater = (LayoutInflater) context
          .getSystemService(context.LAYOUT_INFLATER_SERVICE);
  convertView = infalInflater.inflate(R.layout.child_item, null);
}

//final int groupPositionCopy=groupPosition;
// final int childPositionCopy=childPosition;
final TextView tv = (TextView) convertView.findViewById(R.id.item_name);
final TextView tv2 = (TextView) convertView.findViewById(R.id.price);
//ImageView iv = (ImageView) convertView.findViewById(R.id.flag);

tv.setText(current.get_name().toString());
tv2.setText("0.00");
/*  
  Item current=item.get(groupPosition);
TextView tv = new TextView(Home.this);
tv.setText(current.get_name()+" " + groupPosition);
tv.setPadding(12, 7, 7, 7);
tv.setBackgroundColor(Color.RED);

return tv;
return convertView;
*/

//final Item child = (Item) getChild(groupPosition, childPosition);
final Item child=item;
if (convertView == null) {
    LayoutInflater infalInflater = (LayoutInflater) context
            .getSystemService(context.LAYOUT_INFLATER_SERVICE);
    convertView = infalInflater.inflate(R.layout.child_item, null);
}

//final int groupPositionCopy=groupPosition;
//final int childPositionCopy=childPosition;
final TextView tv = (TextView) convertView.findViewById(R.id.item_name);
final TextView tv2 = (TextView) convertView.findViewById(R.id.price);
//  ImageView iv = (ImageView) convertView.findViewById(R.id.flag);
if(item.get_flag().equals("I")){
tv.setText(child.get_name().toString());
tv2.setText(child.get_price().toString());
RelativeLayout rel_item_bt=(RelativeLayout) convertView.findViewById(R.id.Rel_item_select);
RelativeLayout rel_price_btn=(RelativeLayout) convertView.findViewById(R.id.Rel_price_select);
// iv.setImageResource(child.getImage());
final String name=tv.getText().toString();
final String itemPrice=tv2.getText().toString();
rel_item_bt.setOnClickListener(new RelativeLayout.OnClickListener() {

@Override
public void onClick(View arg0) {
// TODO Auto-generated method stub
final Dialog dialog = new Dialog(context);
 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 dialog.setContentView(R.layout.child_item_select);
 TextView itemname=(TextView) dialog.findViewById(R.id.tv_item_name);
 itemname.setText(name);
 RelativeLayout Rename=(RelativeLayout) dialog.findViewById(R.id.Rel_item_rename);
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
		 price.setText(child.get_name());
		 final Editable price1=price.getText();
		String s="Rename "+name;
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
				 if(!price1.toString().trim().equals("")){
						if(! DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", price1.toString().trim())){
					// itemname.setText("");
				child.set_name(price1.toString());
								tv2.setText(price1);
				DBAdapter.updateItemName(child.get_id(), price1.toString());
				GamesFragment.ExpAdapter.notifyDataSetChanged();
				GamesFragment.closeDialogs(); 
				try
				 {
						 ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				        HashMap<String, String> map = new HashMap<String, String>();
				        map.put("oprn","Update");
				        map.put("table", "Item");
				        map.put("field", "Name");
				        map.put("id",String.valueOf(child.get_id()));
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
						}else{
							GamesFragment.closeDialogs(); 
				 			//Toast.makeText(context, "Item already exists", Toast.LENGTH_LONG).show();
				 		}
				 
				 }
				 else{
					 Toast.makeText(context, "Enter Item Name", Toast.LENGTH_LONG).show();
						
				 }
			}
		});
		 GamesFragment.dialogs.add(dialog);
		 GamesFragment.closeDialogs();
		 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
		 dialog.show();
	}
});
 RelativeLayout Dlt=(RelativeLayout) dialog.findViewById(R.id.Rel_item_delete);
 Dlt.setOnClickListener(new View.OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        builder.setTitle("Delete "+child.get_name()+"?");
	        builder.setMessage("Do you want to delete the item "+child.get_name()+" ?? ");
	        builder.setPositiveButton("Delete",
	                new DialogInterface.OnClickListener() {

	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	// TODO Auto-generated method stub
	                		DBAdapter.deleteItem(child.get_id());
	                	//	int a=GamesFragment.ExpListItems.indexOf(child.get_category());
	                		//ExpandListAdapter.SecondLevelexplv.getChildAt(groupPositionCopy);
	                		//GamesFragment.ExpListItems
	                		//Item childa=(Item) getChild(groupPositionCopy, childPositionCopy);
	                		//ExpandListAdapter.(groupPositionCopy).rem;
	                		GamesFragment.ExpListItems.get(index).removeChild(child);
	                		GamesFragment.ExpAdapter.notifyDataSetChanged();
	                		GamesFragment.closeDialogs(); 
	                		try
	                		 {
	                				 ArrayList<HashMap<String, String>> wordList;
	                		        wordList = new ArrayList<HashMap<String, String>>();
	                		        HashMap<String, String> map = new HashMap<String, String>();
	                		        map.put("oprn","Delete");
	                		        map.put("table", "Item");			        
	                		        map.put("id",String.valueOf(child.get_id()));
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
		
		
	}
});
// rel_add_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
// rel_add_item_btn.setOnClickListener(btnitem_addOnClickListener);
 //rel_cancel_item_btn.setOnClickListener(btnitem_CancelOnClickListener);
 RelativeLayout changecat=(RelativeLayout) dialog.findViewById(R.id.Rel_item_changecategory);
 changecat.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			final Dialog dialog = new Dialog(context);
			 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			 dialog.setContentView(R.layout.change_category_view);
			 final CustomAdapter adapter = new CustomAdapter(context, GamesFragment.ExpListItems);
	         
		        final ListView listView = (ListView) dialog.findViewById(R.id.lv_changecat);
		        listView.setAdapter(adapter);
		       listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v,
						int groupPosition, long id) {
					Category cat = (Category) adapter.getItem(groupPosition);
					DBAdapter.updateItemcategory(child.get_id(), cat.get_id());
					//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
					//GamesFragment.ExpListItems.get(cat.get_id()).addChild(child);
					int index=GamesFragment.ExpListItems.indexOf(cat);
					GamesFragment.ExpListItems.get(index).addChild(child);
					int a=GamesFragment.ExpListItems.indexOf(child.get_category());
					GamesFragment.ExpListItems.get(a).removeChild(child);
					child.set_category(cat);
					//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(groupPosition);
					
					GamesFragment.ExpAdapter.notifyDataSetChanged();
					GamesFragment.closeDialogs();
					 try
					 {
							 ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					        HashMap<String, String> map = new HashMap<String, String>();
					        map.put("oprn","Update");
					        map.put("table", "Item");
					        map.put("field", "Cat");
					        map.put("id",String.valueOf(child.get_id()));
					        map.put("cid",String.valueOf(cat.get_id()));
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
		       RelativeLayout newcat=(RelativeLayout)dialog.findViewById(R.id.rel_changecat);
		       newcat.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 Dialog dialog = new Dialog(context);
					 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					 dialog.setContentView(R.layout.add_new_category);
					 RelativeLayout rel_cancel_category_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_Category_btn);
					 RelativeLayout rel_add_category_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_category_btn);
					 final EditText et2=(EditText)dialog.findViewById(R.id.et_new_category);
					 rel_cancel_category_btn.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							GamesFragment.closeDialogs();
						}
					});
					 rel_add_category_btn.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							
							 //Toast.makeText(getBaseContext(), "Colosed", Toast.LENGTH_SHORT).show();
							
							 String cat = et2.getText().toString();
							 if(!cat.trim().equals(""))
							 {
							   et2.setText("");			  
							   
							    GamesFragment.addCategory(cat,context) ;											
							   GamesFragment.ExpAdapter.notifyDataSetChanged();
							   GamesFragment.closeDialogs();
							 }
							 else{
								 Toast.makeText(context, "Enter Category Name", Toast.LENGTH_SHORT).show();
							 }
						}
					});
					 
					 GamesFragment.dialogs.add(dialog);
					 GamesFragment.closeDialogs();
					 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
					 dialog.show();
				}
			});
			 GamesFragment.dialogs.add(dialog);
			 GamesFragment.closeDialogs();
			 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
			 dialog.show();
		}
	});
 RelativeLayout addNewExtra=(RelativeLayout) dialog.findViewById(R.id.Rel_item_addnewextra);
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
						if(!DBAdapter.CheckIsCatExtraAlreadyInDBorNot(item.toString().trim(),child.get_category().get_id())){
							for (Item Citem : DBAdapter.getItemsbyCatIdnFlag(child.get_category().get_id())) {
								if(DBAdapter.CheckIsExtraAlreadyInDBorNot(item.toString().trim(), Citem.get_id()))
								{
									isExists=true;
								}
							}
						}else{
							isExists=true;
						}
						
						 if(!isExists ){
					 itemname.setText("");
					    
					   //add a new item to the list
					   //int groupPosition = GamesFragment.addProduct(group.get_name(), item) ;
					 Extra i=new Extra();
					 i.set_name(item);
					 i.set_price("0.00");
					
					 i.set_item(DBAdapter.getItemData(child.get_id()));
					// Category cat = (Category) adapter.getItem(groupPosition);
						int a=DBAdapter.addExtraData(i);
						//DBAdapter.addExtraSeq(a, a);
						i.set_id(a);
						i.set_sequence(a);
						//Toast.makeText(context, i.get_category().get_name(), Toast.LENGTH_LONG).show();
						//(child.get_id(), cat.get_id());
						//GamesFragment.ExpListItems.get(groupPositionCopy).removeChild(childPositionCopy);
						//GamesFragment.ExpListItems.get(cat.get_id()).addChild(child);
						//int index=GamesFragment.ExpListItems.indexOf(cat);
						//GamesFragment.ExpListItems.get(groupPositionCopy).getChild(groupPositionCopy).addExtra(i);
						child.addExtra(i);
						
					//	Category c=child.get_category();
					//	int a1=GamesFragment.ExpListItems.indexOf(c);
					//	GamesFragment.ExpListItems.get(a1);
					//	ArrayList<Item> it=GamesFragment.ExpListItems.get(a1).get_items();
					//	it.add(child);
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
						        map.put("table", "Extra");
						        map.put("id",String.valueOf(a));
						        map.put("name",String.valueOf(i.get_name()));
						        map.put("price","0.00");						        
						        map.put("seq", String.valueOf(a));
						        map.put("itemid", String.valueOf(i.get_item().get_id()));
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
						 else{
						 Toast.makeText(context, "Extra Already Exists", Toast.LENGTH_SHORT).show();
						 }
					 }
					 else{
					 Toast.makeText(context, "Enter Extra Name", Toast.LENGTH_SHORT).show();
					 }
				}
			});
			 GamesFragment.dialogs.add(dialog);
			 GamesFragment.closeDialogs();
			 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
			 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
				 dialog.show();

				}
		}
	});

 GamesFragment.dialogs.add(dialog);
 GamesFragment.closeDialogs();
 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
	 dialog.show();

	}
 //et=(EditText)dialog.findViewById(R.id.et_new_item);

}
});
rel_price_btn.setOnClickListener(new RelativeLayout.OnClickListener() {

@Override
public void onClick(View arg0) {
final Dialog dialog = new Dialog(context);
 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 dialog.setContentView(R.layout.price_for_item);
 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
 TextView pricename=(TextView) dialog.findViewById(R.id.tv_titleforprice);
 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_price_btn);
 RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_save_price_btn);
 EditText price=(EditText) dialog.findViewById(R.id.et_item_price);
	try {
		if (Double.parseDouble(itemPrice) > 0.0d) {
			price.setText(itemPrice);
		} else {
			price.setHint("0.00");
		}
	}catch (Exception e){
		price.setHint("0.00");
	}
 final Editable price1=price.getText();
String s="price for "+name;
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
		 try
		 {
		if(!price1.toString().trim().equals("")){
		Float Rprice=Float.parseFloat(price1.toString());
		child.set_price(String.format("%.2f", Rprice ));
						tv2.setText(price1);
		DBAdapter.updateItemPrice(child.get_id(), price1.toString());
		GamesFragment.ExpAdapter.notifyDataSetChanged();
		GamesFragment.closeDialogs(); 
		
				 ArrayList<HashMap<String, String>> wordList;
		        wordList = new ArrayList<HashMap<String, String>>();
		        HashMap<String, String> map = new HashMap<String, String>();
		        map.put("oprn","Update");
		        map.put("table", "Item");
		        map.put("field", "Price");
		        map.put("id",String.valueOf(child.get_id()));
		        map.put("price",String.valueOf(price1));
		        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
		       wordList.add(map);
		        Gson gson = new GsonBuilder().create();
			 crudonserver crudop=new crudonserver();
			 crudop.callserverforcrudopern(gson.toJson(wordList),context);
		 }
		 else{
			 Toast.makeText(context, "Enter Item Price", Toast.LENGTH_SHORT).show();
			 }
		 }catch(Exception e)
		 {
			// error=String.valueOf(e.getMessage());
			 //Toast.makeText(context,Toast.LENGTH_LONG).show();
		 }
		 
	}
});
 GamesFragment.dialogs.add(dialog);
 GamesFragment.closeDialogs();
 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
	 dialog.show();

	}

}
});
}
else{
	tv.setText("  + "+child.get_name().toString());
	tv2.setText(child.get_price().toString());
	RelativeLayout rel_item_bt=(RelativeLayout) convertView.findViewById(R.id.Rel_item_select);
	RelativeLayout rel_price_btn=(RelativeLayout) convertView.findViewById(R.id.Rel_price_select);
	final String name=tv.getText().toString();
	final String itemPrice=tv2.getText().toString();
	rel_item_bt.setOnClickListener(new RelativeLayout.OnClickListener() {

	@Override
	public void onClick(View arg0) {
	// TODO Auto-generated method stub
	final Dialog dialog = new Dialog(context);
	 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 dialog.setContentView(R.layout.extra_item_select);
	 TextView itemname=(TextView) dialog.findViewById(R.id.tv_extra_name);
	 itemname.setText(name);
	 RelativeLayout Rename=(RelativeLayout) dialog.findViewById(R.id.Rel_extra_rename);
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
			 price.setText(child.get_name());
			 final Editable price1=price.getText();
			String s="Rename "+name;
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
					 if(!price1.toString().trim().equals("")){
						 Boolean isExists=false;
							// if(! DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", item.toString().trim())){
						if(!DBAdapter.CheckIsCatExtraAlreadyInDBorNot(price1.toString().trim(),child.get_category().get_id())){
							for (Item Citem : DBAdapter.getItemsbyCatIdnFlag(child.get_category().get_id())) {
								if(DBAdapter.CheckIsExtraAlreadyInDBorNot(price1.toString().trim(), Citem.get_id()))
								{
									isExists=true;
								}
							}
						}else{
							isExists=true;
						}
						
							if(! isExists){
					child.set_name(price1.toString());
									tv2.setText(price1);
					DBAdapter.updateExtraName(child.get_id(), price1.toString());
					GamesFragment.ExpAdapter.notifyDataSetChanged();
					GamesFragment.closeDialogs(); 
					 try
					 {
							 ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					        HashMap<String, String> map = new HashMap<String, String>();
					        map.put("oprn","Update");
					        map.put("table", "Item");
					        map.put("field", "Name");
					        map.put("id",String.valueOf(child.get_id()));
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
							}else{
								GamesFragment.closeDialogs(); 
					 		//	Toast.makeText(context, "Item already exists", Toast.LENGTH_LONG).show();
					 		}
					 
					 }
					 else{
						 Toast.makeText(context, "Enter Item Name", Toast.LENGTH_LONG).show();
							
					 }
				}
			});
			 GamesFragment.dialogs.add(dialog);
			 GamesFragment.closeDialogs();
			 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
			 dialog.show();
		}
	});
	 
	 RelativeLayout ReorderExtra=(RelativeLayout) dialog.findViewById(R.id.Rel_extra_reorder);
	 ReorderExtra.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final Dialog dialog = new Dialog(context);
				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 dialog.setContentView(R.layout.reorder_item);
				 //ArrayList<Item> it
				 ArrayList<Item> items= DBAdapter.getItemsbyCatIdnFlagOff(child.get_category().get_id());
				// ArrayList<Item> items= child.get_category().get_extras();//d
				 if(items.size()>1)
				 {
					 GamesFragment.ExpListItems.get(groupPosition).set_items(DBAdapter.getItemsbyCatIdnFlagOff(child.get_category().get_id()));
					  extraadapter = new ReorderCExtraAdapter(context, GamesFragment.ExpListItems.get(groupPosition).get_items(),index);
					// extraadapter = new ReorderCExtraAdapter(context, items,index);//d
						 
				 }
				 else
				 {
					 extraadapter = new ReorderCExtraAdapter(context, items,index);
						
				 }
				 
				 
			        ListView listView = (ListView) dialog.findViewById(R.id.lv_reorderitem);
			        listView.setAdapter(extraadapter);
			      
			     //  RelativeLayout uparrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_up_arrow);
			      // RelativeLayout downarrow=(RelativeLayout)dialog.findViewById(R.id.Rel_reorder_down_arrow);
			       	 GamesFragment.dialogs.add(dialog);
			       	GamesFragment.closeDialogs();
			       	dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
				 dialog.show();
				 
			}
		});
	 RelativeLayout Dlt=(RelativeLayout) dialog.findViewById(R.id.Rel_extra_delete);
	 Dlt.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			 AlertDialog.Builder builder = new AlertDialog.Builder(context);
		        builder.setTitle("Delete "+child.get_name()+"?");
		        builder.setMessage("Do you want to delete the item "+child.get_name()+" ?? ");
		        builder.setPositiveButton("Delete",
		                new DialogInterface.OnClickListener() {

		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	// TODO Auto-generated method stub
		            			DBAdapter.deleteItem(child.get_id());
		            		//	int a=GamesFragment.ExpListItems.indexOf(child.get_category());
		            			//ExpandListAdapter.SecondLevelexplv.getChildAt(groupPositionCopy);
		            			//GamesFragment.ExpListItems
		            			//Item childa=(Item) getChild(groupPositionCopy, childPositionCopy);
		            			//ExpandListAdapter.(groupPositionCopy).rem;
		            		//item.removeChild(childPosition);
		            			GamesFragment.ExpListItems.get(index).removeChild(child);
		            			GamesFragment.ExpAdapter.notifyDataSetChanged();
		            			GamesFragment.closeDialogs(); 
		            			try
		            			 {
		            					 ArrayList<HashMap<String, String>> wordList;
		            			        wordList = new ArrayList<HashMap<String, String>>();
		            			        HashMap<String, String> map = new HashMap<String, String>();
		            			        map.put("oprn","Delete");
		            			        map.put("table", "Item");			        
		            			        map.put("id",String.valueOf(child.get_id()));
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
			
		}
	});
	// rel_add_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
	// rel_add_item_btn.setOnClickListener(btnitem_addOnClickListener);
	 //rel_cancel_item_btn.setOnClickListener(btnitem_CancelOnClickListener);


	 GamesFragment.dialogs.add(dialog);
	 GamesFragment.closeDialogs();
	 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
	 dialog.show();
	 //et=(EditText)dialog.findViewById(R.id.et_new_item);

	}
	});
	rel_price_btn.setOnClickListener(new RelativeLayout.OnClickListener() {

	@Override
	public void onClick(View arg0) {
	final Dialog dialog = new Dialog(context);
	 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 dialog.setContentView(R.layout.price_for_item);
	 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	 TextView pricename=(TextView) dialog.findViewById(R.id.tv_titleforprice);
	 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_price_btn);
	 RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_save_price_btn);
	 EditText price=(EditText) dialog.findViewById(R.id.et_item_price);
	 if(Double.parseDouble(itemPrice)>0.0d){
		 price.setText(itemPrice);}
		 else{
			 price.setHint("0.00");
		 }
	 final Editable price1=price.getText();
	String s="Price for "+name;
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
			 try
			 {
			if(!price1.toString().trim().equals("")){
			Float Rprice=Float.parseFloat(price1.toString());
			
			child.set_price(String.format("%.2f", Rprice ));
			
							tv2.setText(price1);
			DBAdapter.updateItemPrice(child.get_id(), price1.toString());
			GamesFragment.ExpAdapter.notifyDataSetChanged();
			GamesFragment.closeDialogs(); 
			
					 ArrayList<HashMap<String, String>> wordList;
			        wordList = new ArrayList<HashMap<String, String>>();
			        HashMap<String, String> map = new HashMap<String, String>();
			        map.put("oprn","Update");
			        map.put("table", "Item");
			        map.put("field", "Price");
			        map.put("id",String.valueOf(child.get_id()));
			        map.put("price",String.valueOf(price1));
			        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
			       wordList.add(map);
			        Gson gson = new GsonBuilder().create();
				 crudonserver crudop=new crudonserver();
				 crudop.callserverforcrudopern(gson.toJson(wordList),context);
			}
			 else{
				 Toast.makeText(context, "Enter Item Price", Toast.LENGTH_SHORT).show();
				 }
			 }catch(Exception e)
			 {
				// error=String.valueOf(e.getMessage());
				 //Toast.makeText(context,Toast.LENGTH_LONG).show();
			 }
		}
	});
	 GamesFragment.dialogs.add(dialog);
	 GamesFragment.closeDialogs();
	 dialog.getWindow().setLayout(dialogWidth, LayoutParams.WRAP_CONTENT);
	 if(staff.get_staff_roll().equals("admin")||staff.get_staff_roll().equals("owner")||staff.get_staff_roll().equals("manager")){
		 dialog.show();

		}

	}
	});

	  
}
return convertView;
}

@Override
public boolean hasStableIds() {
// TODO Auto-generated method stub
return true;
}

@Override
public boolean isChildSelectable(int groupPosition, int childPosition) {
// TODO Auto-generated method stub
return true;
}
 
}