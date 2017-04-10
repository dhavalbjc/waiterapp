package in.app.waiter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

public class GamesFragment extends Fragment {

	     static final int ID_SCREENDIALOG = 1;
	 public static ExpandListAdapter ExpAdapter;
	    public static ArrayList<Category> ExpListItems= new ArrayList<Category>();
	    public static ExpandableListView ExpandList;
	    public static LinkedHashMap<String, Category> myDepartments = new LinkedHashMap<String, Category>();
	    public static  Vector<Dialog> dialogs = new Vector<Dialog>();
	    List<Category> cat;
	   //  TextView item1 ;
	     Button btnScreenDialog_OK;
	     Dialog dialog ;
	     ListView listView;
	  //  private ArrayList<ArrayList<Category>> list = new ArrayList<ArrayList<Category>>();

	   // ArrayList<Category> alcohol = new ArrayList<Category>();
	    RelativeLayout r,add_item_btn,add_category_btn,rel_cancel_item_btn,rel_add_item_btn,rel_cancel_category_btn,rel_add_category_btn;
	    EditText et, et2,itemprice;
	private RelativeLayout.OnClickListener btnitem_CancelOnClickListener
	 = new RelativeLayout.OnClickListener(){

	 @Override
	 public void onClick(View arg0) {


		 closeDialogs();
		// Toast.makeText(getBaseContext(), "Colosed", Toast.LENGTH_SHORT).show();

	 }};
	 private RelativeLayout.OnClickListener btnitem_addOnClickListener
	 = new RelativeLayout.OnClickListener(){

	 @Override
	 public void onClick(View arg0) {
	  // TODO Auto-generated method stub

		 //Toast.makeText(getBaseContext(), "Colosed", Toast.LENGTH_SHORT).show();

		 String item = et.getText().toString();
		 String price = itemprice.getText().toString();
		 if(!item.trim().equals(""))
		 {
			   et.setText("");
			   itemprice.setText("");
			   closeDialogs();
		   addProduct("Uncategorized", item,price,getActivity());
				   //addProduct(department,product);
		   //notify the list so that changes can take effect
		   GamesFragment.ExpAdapter.notifyDataSetChanged();
		   expandAll();
		 }
		 else
		 {
			 itemprice.setText("");
			 Toast.makeText(getActivity(),"Enter Item Name",Toast.LENGTH_SHORT).show();
			// Toast.makeText(, "Colosed", Toast.LENGTH_SHORT).show();
		 }
		  // closeDialogs();
		  // dialog.dismiss();


	 }};
	 private RelativeLayout.OnClickListener btncategory_addOnClickListener
	 = new RelativeLayout.OnClickListener(){

	 @Override
	 public void onClick(View arg0) {
	  // TODO Auto-generated method stub

		 //Toast.makeText(getBaseContext(), "Colosed", Toast.LENGTH_SHORT).show();

		 String cat = et2.getText().toString();
		 if(!cat.trim().equals(""))
		 {
			 closeDialogs();

		   et2.setText("");

		   addCategory(cat,getActivity());
				   //addProduct(department,product);
		   //notify the list so that changes can take effect
		   ExpAdapter.notifyDataSetChanged();
		   expandAll();
		  // dialog.dismiss();
		 }
		 else
		 {
			 Toast.makeText(getActivity(),"Enter Category Name",Toast.LENGTH_SHORT).show();
			// Toast.makeText(, "Colosed", Toast.LENGTH_SHORT).show();
		 }
	 }};

	 public static int addCategory(String category,Context con)
	 {
		 int groupPosition = 0;
		 Category headerInfo = myDepartments.get(category);
		/* Category headerInfo=null;
		 try
		 {
		  headerInfo = DBAdapter.getCategoryData(category);
		 }
		 catch(CursorIndexOutOfBoundsException e)
		 {
			 headerInfo=null;
		 }*/
				 if(headerInfo == null){
			   headerInfo = new Category();
			   headerInfo.set_name(category);
			  // Category c=new Category();
			//   c.set_name(category);

			 int a=DBAdapter.addCategoryData(headerInfo);
			 headerInfo.set_sequence(a);
			 headerInfo.set_id(a);
			// Toast.makeText(getApplicationContext(), String.valueOf(a),Toast.LENGTH_LONG).show();
			// DBAdapter.addCatSeq(a, a);
			// DBAdapter.getCategoryData(a);
			// final String response1;
			 String error="";

			 //insert cat on server
			 try
			 {
					 ArrayList<HashMap<String, String>> wordList;
			        wordList = new ArrayList<HashMap<String, String>>();
			        HashMap<String, String> map = new HashMap<String, String>();
			        map.put("oprn","Insert");
			        map.put("table", "Category");
			        map.put("cid",String.valueOf(a));
			        map.put("name",headerInfo.get_name());
			        map.put("seq", String.valueOf(a));
			        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
			       wordList.add(map);
			        Gson gson = new GsonBuilder().create();
				 crudonserver crudop=new crudonserver();
				 crudop.callserverforcrudopern(gson.toJson(wordList),con);

			 }catch(Exception e)
			 {
				 error=String.valueOf(e.getMessage());
				 //Toast.makeText(context,Toast.LENGTH_LONG).show();
			 }
			 // server entry done
			// headerInfo.set_name(error);


			 myDepartments.put(category, headerInfo);
			// Toast.makeText(this, String.valueOf(c.get_sequence()),Toast.LENGTH_LONG).show();
			// Category c=new Category(1,"ddd",5);
			  // DBAdapter.addCategoryData(headerInfo);
			// Toast.makeText(getApplicationContext(),String.valueOf(a).toString(), Toast.LENGTH_LONG).show();
			   try{
				 // c= DBAdapter.getCategoryData(headerInfo.get_name());
				//  Toast.makeText(getApplicationContext(),headerInfo.get_id(), Toast.LENGTH_LONG).show();
				 // headerInfo.set_sequence(c.get_id());
			   }
			   catch(Exception e)
			   {
				  // Toast.makeText(getApplicationContext(),c.get_id(), Toast.LENGTH_LONG).show();

			   }
			 // Category c= DBAdapter.getCategoryData(category);

			  //  int id=DBAdapter
			    GamesFragment.ExpListItems.add(headerInfo);
			//    Toast.makeText(getApplicationContext(),c.get_id(), Toast.LENGTH_LONG).show();

			  }
		  groupPosition = ExpListItems.indexOf(headerInfo);


		 return groupPosition;
	 }

	 public static int addProduct(String department, String product, String price,Context con){
		 int groupPosition = 0;
		 try
	 {

		 // int image=R.drawable.arrow_down_float;
		  //check the hash map if the group already exists
			 //Category headerInfo=null;
		  Category headerInfo = myDepartments.get(department);

			  //Category c=new Category();

		//  Group headerInfo1 = myDepartments.get("GROUP A");

		//  Toast.makeText(getApplicationContext(),headerInfo1.getName() , Toast.LENGTH_LONG).show();
		  //add the group if doesn't exists
		  Boolean combo=false;
		  int cid = 0;
		  if(headerInfo == null){
			   headerInfo = new Category();
			   headerInfo.set_name(department);
			   //headerInfo.setImage(image);
			  // Category c=new Category(1,"ddd",5);
			   cid=DBAdapter.addCategoryData(headerInfo);
			  // DBAdapter.addCatSeq(a, a);

				  headerInfo.set_sequence(cid);
				  headerInfo.set_id(cid);
			   myDepartments.put(department, headerInfo);
			   //ArrayList<Item> pl=new ArrayList<Item>();
			   //headerInfo.setItems(pl);
			   GamesFragment.ExpListItems.add(headerInfo);
			   combo=true;
			  // DBAdapter.addCategoryData(headerInfo);
			   //Toast.makeText(getApplicationContext(),c.get_id(), Toast.LENGTH_LONG).show();
			  /* try
				 {
						 ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				        HashMap<String, String> map = new HashMap<String, String>();
				        map.put("oprn","Insert");
				        map.put("table", "Category");
				        map.put("cid",String.valueOf(a));
				        map.put("name",headerInfo.get_name());
				        map.put("seq", String.valueOf(a));
				        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
				       wordList.add(map);
				        Gson gson = new GsonBuilder().create();
					 crudonserver crudop=new crudonserver();
					 crudop.callserverforcrudopern(gson.toJson(wordList));

				 }catch(Exception e)
				 {
					// error=String.valueOf(e.getMessage());
					 //Toast.makeText(context,Toast.LENGTH_LONG).show();
				 }*/
			  }
		  //get the children for the group

		  ArrayList<Item> productList = headerInfo.get_items();

		  Item detailInfo = new Item();
		 // detailInfo.setSequence(String.valueOf(listSize));
		  detailInfo.set_name(product);
		 // detailInfo.set_price("0.00");
		  if(!price.toString().trim().equals("")){
				Float Rprice=Float.parseFloat(price.toString());

				detailInfo.set_price(String.format("%.2f", Rprice ));


		 }
		 else{
			 detailInfo.set_price("0.00");
		 }
		  detailInfo.set_category(DBAdapter.getCategoryData(headerInfo.get_id()));
		  detailInfo.set_flag("I");
		  int a=DBAdapter.addItemData(detailInfo);
		  // DBAdapter.addExtraSeq(a, a);
		 // DBAdapter.addItemSeq(a, a);
		   detailInfo.set_sequence(a);
		   detailInfo.set_id(a);
		   productList.add(detailInfo);
			  headerInfo.set_items(productList);
			  groupPosition = ExpListItems.indexOf(headerInfo);
	//	Toast.makeText(getApplicationContext(),detailInfo.get_category().get_name(), Toast.LENGTH_LONG).show();
			  try
				 {	if(combo){
						 ArrayList<HashMap<String, String>> wordList;
				        wordList = new ArrayList<HashMap<String, String>>();
				        HashMap<String, String> map = new HashMap<String, String>();
				        map.put("oprn","Insert");
				        map.put("table", "Combo");
				        map.put("cid",String.valueOf(headerInfo.get_id()));
				        map.put("catname",headerInfo.get_name());
				       // map.put("catseq", String.valueOf(headerInfo.get_id()));
				        map.put("id",String.valueOf(a));
				        map.put("name",product);
				        map.put("price",price);
				        map.put("flag","I");
				       // map.put("seq", String.valueOf(a));
				        //map.put("cid", String.valueOf(headerInfo.get_id()));
				        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
				       wordList.add(map);
				        Gson gson = new GsonBuilder().create();
					 crudonserver crudop=new crudonserver();
					 crudop.callserverforcrudopern(gson.toJson(wordList),con);
				 }
				 else
				 {ArrayList<HashMap<String, String>> wordList;
			        wordList = new ArrayList<HashMap<String, String>>();
			        HashMap<String, String> map = new HashMap<String, String>();
			        map.put("oprn","Insert");
			        map.put("table", "Item");
			        map.put("id",String.valueOf(a));
			        map.put("name",product);
			        map.put("price",price);
			        map.put("flag","I");
			        map.put("seq", String.valueOf(a));
			        map.put("cid", String.valueOf(headerInfo.get_id()));
			        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
			       wordList.add(map);
			        Gson gson = new GsonBuilder().create();
				 crudonserver crudop=new crudonserver();
				 crudop.callserverforcrudopern(gson.toJson(wordList),con);

				 }

				 }catch(Exception e)
				 {
					// error=String.valueOf(e.getMessage());
					 //Toast.makeText(context,Toast.LENGTH_LONG).show();
				 }
	 }
	 catch(Exception e)
	 {
		// Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	 }
	 return groupPosition;
	 }
	 
	 public static void closeDialogs() {
  	   for (Dialog dialog : dialogs)
  	      if (dialog.isShowing()) dialog.dismiss();
  	  // dialogs.removeAllElements();
  	}
	
	 public static void expandAll() {
   	  int count = ExpAdapter.getGroupCount();
   	  for (int i = 0; i < count; i++){
   	   ExpandList.expandGroup(i);
   	  }
   	  //ExpandListAdapter.expandAll();
   	 }

	    @Override
	    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.menu_firstview, container, false);



	        ExpandList = (ExpandableListView) rootView.findViewById(R.id.exp_list);
	        TextView emptyText = (TextView) rootView.findViewById(R.id.empty);
	        ExpandList.setEmptyView(emptyText);
	        ExpandList.setGroupIndicator(null);
	    //    ExpListItems=(ArrayList<Category>) DBAdapter.getAllCategoryData();
	        if(ExpListItems.size()<=0){
	    /*    getActivity().runOnUiThread(new Runnable() {

	            @Override
	            public void run() {
	        try{
	        	//Cursor c=DBAdapter.fetchGroup();
	         cat=DBAdapter.getAllCategoryData();
	      //  Toast.makeText(getBaseContext(), "ok",Toast.LENGTH_SHORT).show();
	        }
	        catch(Exception e)
	        {
	        	Toast.makeText(container.getContext(), e.toString(),Toast.LENGTH_LONG).show();
	        }
	       // ArrayList<Item> it=DBAdapter.getAllItemData();
	        for (Category a1 : cat) {
	        	//Toast.makeText(getBaseContext(), a1.get_name().toString(),Toast.LENGTH_SHORT).show();
	        	myDepartments.put(a1.get_name(), a1);
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
	        		Toast.makeText(container.getContext(), e.toString(),Toast.LENGTH_SHORT).show();
	        	}

	        }
	            }});
	        Collections.sort(cat,new myCateComparable());
	        ExpListItems= (ArrayList<Category>) cat;  */
	        }
	        View convertView=null;
	        LayoutInflater infalInflater = (LayoutInflater) getActivity()
	                .getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
	        convertView = infalInflater.inflate(R.layout.list_item_header_footer, null);

	        ExpAdapter = new ExpandListAdapter(container.getContext(), ExpListItems,getWidth());
	        ExpandList.setAdapter(ExpAdapter);
	        for(int i=0; i < ExpAdapter.getGroupCount(); i++)
	        	ExpandList.expandGroup(i);

	       // ExpandList.addFooterView(convertView);

	        ExpandList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
	      	  @Override
	      	  public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

	      		//initiatePopupWindow();

	      	    return false;
	      	  }

	        });

	       r=(RelativeLayout)rootView.findViewById(R.id.menu_a);
	       Staff s=DBAdapter.getStaffData(Main.getEmail(getActivity()));

	    	   if(s.get_staff_roll().equals("admin")||s.get_staff_roll().equals("owner")||s.get_staff_roll().equals("manager"))
	       {
	    	   r.setVisibility(View.VISIBLE);
	       }else{
	    	   r.setVisibility(View.GONE);
	       }
	       r.setOnClickListener(new View.OnClickListener() {


				@Override
		        public void onClick(View v) {

	        	//showDialog(ID_SCREENDIALOG);
					callDialog(3,container.getContext());
	        	//dismissDialog(ID_SCREENDIALOG);


		        }
		    });


		return rootView;
	    }

	public void callDialog(int id,final Context _context)
	{

    	dialog = null;
	 switch(id){
	 case(ID_SCREENDIALOG):


		 dialog = new Dialog(_context);
	//Toast.makeText(getBaseContext(),String.valueOf(dialog.isShowing()),Toast.LENGTH_SHORT).show();
	 //dialog.isShowing();
	 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//Window window=dialog.getWindow();
	//window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	//window.setGravity(Gravity.CENTER);
	 dialog.setContentView(R.layout.test);

	 add_item_btn=(RelativeLayout) dialog.findViewById(R.id.dialog_Rel_item);
	 add_item_btn.setOnClickListener(new RelativeLayout.OnClickListener() {

		@Override
		public void onClick(View arg0) {

			 if(dialog.isShowing())
	         {
	           dialog.dismiss();
	           dialog = null;
	         //  Toast.makeText(getBaseContext(), "dialog dismiss", Toast.LENGTH_SHORT).show();
	          }
			 callDialog(2, _context);
			// showDialog(2);
		}
	});
	// add_item_btn.setOnClickListener(btnitemadd_OnClickListener);

	 add_category_btn=(RelativeLayout) dialog.findViewById(R.id.dialog_Rel_item2);
	 add_category_btn.setOnClickListener(new RelativeLayout.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			 if(dialog.isShowing())
		        {
		          dialog.dismiss();
		          dialog = null;
		         }
				 callDialog(3,_context);

		}
	});

	 break;
	 case(2):
	 dialog = new Dialog(_context);
	 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 dialog.setContentView(R.layout.add_new_item);
	 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	 rel_cancel_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_btn);
	 rel_add_item_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
	 rel_add_item_btn.setOnClickListener(btnitem_addOnClickListener);
	 rel_cancel_item_btn.setOnClickListener(btnitem_CancelOnClickListener);


	 et=(EditText)dialog.findViewById(R.id.et_new_item);
	 itemprice=(EditText)dialog.findViewById(R.id.et_new_itemprice);


	 break;
	 case(3):
	 dialog = new Dialog(_context);
	 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 dialog.setContentView(R.layout.add_new_category);
	 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	 rel_cancel_category_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_Category_btn);
	 rel_add_category_btn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_category_btn);

	 rel_cancel_category_btn.setOnClickListener(btnitem_CancelOnClickListener);
	 rel_add_category_btn.setOnClickListener(btncategory_addOnClickListener);

	 et2=(EditText)dialog.findViewById(R.id.et_new_category);
	 break;

	 }
	 dialogs.add(dialog);

	 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
	 dialog.show();

	 //return dialog;
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
		 
	 
	 /*
	 public void addcat(Category cat)
	 {
		 try{
			
		 GamesFragment.myDepartments.put(cat.get_name(), cat);
			GamesFragment.ExpListItems.add(cat);
			GamesFragment.ExpAdapter.notifyDataSetChanged();
			
		 }
		 catch(Exception e)
		 {
			 Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
		 }
	 }
	 public void addItem(Item item)
	 {
		 int index=0;
		 try{
			 for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==item.get_category().get_id()) {
					   index=i;
					   GamesFragment.ExpListItems.get(index).addChild(item);
						GamesFragment.ExpAdapter.notifyDataSetChanged();
						GamesFragment.expandAll();
				    break;
				  }
				}		
				
			
				//Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_LONG).show();
		 }
		 catch(Exception e)
		 {
			 Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
		 }
	 }





	public void addExtra(Extra extra) {
		int index=0;
		Item itm=DBAdapter.getItemData(extra.get_item().get_id());
		
		 try{
			 for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					   index=i;
					   for (Item i1 : GamesFragment.ExpListItems.get(index).get_items()) {
						if(i1.get_id()==itm.get_id()){
							i1.addExtra(extra);
							GamesFragment.ExpAdapter.notifyDataSetChanged();
							GamesFragment.expandAll();
							 break;
						}
					}
					   
						
				    break;
				  }
				}
			
			
				
			
				//Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_LONG).show();
		 }
		 catch(Exception e)
		 {
			 Toast.makeText(getActivity(), String.valueOf(e.getMessage()), Toast.LENGTH_LONG).show();
		 }
	}
	*/
	
}
