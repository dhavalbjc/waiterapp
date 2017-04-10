
package in.app.waiter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;

public class MyAdapterNew extends BaseAdapter {
	    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	 private final Random mRandom;
	    private final ArrayList<Integer> mBackgroundColors;
	private Context context;
	private ArrayList<ModelCart> mycart;
	private int dialogwidth;

	public MyAdapterNew(Context context, ArrayList<ModelCart> mycart, int dialogwidth) {
		this.context = context;
		this.mycart = mycart;
		this.dialogwidth=dialogwidth;
		    mRandom = new Random();
	        mBackgroundColors = new ArrayList<Integer>();
	       /* mBackgroundColors.add(R.color.orange);
	        mBackgroundColors.add(R.color.green);
	        mBackgroundColors.add(R.color.blue);
	        mBackgroundColors.add(R.color.yellow);
	        mBackgroundColors.add(R.color.grey);*/
	      //  mBackgroundColors.add(R.color.light);
	      //  mBackgroundColors.add(R.color.WhiteSmoke);
	       // mBackgroundColors.add(R.color.FloralWhite);	        
	       // mBackgroundColors.add(R.color.GhostWhite);
	        //mBackgroundColors.add(R.color.snow1);
	    //    mBackgroundColors.add(R.color.snow2);	        
	       // mBackgroundColors.add(R.color.snow3);*/
	      //  mBackgroundColors.add(R.color.grey);
	        mBackgroundColors.add(R.color.White);
	}

	public static String serviceCharge(String TotalAmount)
	{
		String value="0";
		ECharge echarge=DBAdapter.getEChargeData("Service");
			if(echarge!=null)
			{
				if(echarge.get_param().equals("Percentage"))
				{
					try{

					Double total1=Double.parseDouble(TotalAmount);
					Double per=Double.parseDouble(echarge.get_quantity());
					double tax=(total1*per)/100;
					double Amount=total1+tax;
					double roundvalue=Math.round(Amount)-Amount;

					// total.setText(String.valueOf(Amount));
					// total.setText(String.valueOf(Math.round(Amount)));
 					return String.valueOf(Math.round(tax));

					}catch(Exception e)
					{

					}
				}
				else{
					try{


					Double total1=Double.parseDouble(TotalAmount);
					Double per=Double.parseDouble(echarge.get_quantity());
					double Amount=total1+per;
					double roundvalue=Math.round(Amount)-Amount;
					// total.setText(String.valueOf(Amount));
					 //total.setText(String.valueOf(Math.round(Amount)));
					return String.valueOf(Math.round(per));
					}catch(Exception e)
					{

					}
				}
			}
			return value;
	}

	public void updateResults(ArrayList<ModelCart> results){
		//mycart=results;
		mycart.clear();
		mycart.addAll(results);
		notifyDataSetChanged();
	}

	@SuppressWarnings("deprecation")
	public View getView(final int position, View convertView, final ViewGroup parent) {

		 View gridView;
		    if (convertView == null) {  // if it's not recycled, initialize some attributes
		    	gridView = new View(context);
		    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		    	gridView = inflater.inflate(R.layout.gridview_tableorders_new, null);
				//gridView = inflater.inflate(R.layout.gridview_tableorders, null);
		    	//LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(     Context.LAYOUT_INFLATER_SERVICE );
		        //v = inflater.inflate(R.layout.gridview_item_layout, parent, false);
		    } else {
		    	gridView = (View) convertView;
		    }

			 final Resources res = context.getResources();
			 double positionHeight = getPositionRatio(position);
			int backgroundIndex = position >= mBackgroundColors.size() ?
	                position % mBackgroundColors.size() : position;

	              //  gridView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
	              //  gridView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));
	                //android:background="@drawable/newbackground_small_corners"
	               gridView.setBackgroundResource(R.drawable.taborder_back);

	                DynamicHeightTextView table = (DynamicHeightTextView) gridView.findViewById(R.id.grid_TableName);
	                DynamicHeightTextView time = (DynamicHeightTextView) gridView.findViewById(R.id.grid_Time);
	                DynamicHeightTextView user = (DynamicHeightTextView) gridView.findViewById(R.id.user);
	                //make ready
	                final RelativeLayout rel_make_status=(RelativeLayout) gridView.findViewById(R.id.rel_make_status);
	                final View view_make_status=(View) gridView.findViewById(R.id.view_make);
	                final View view_last=(View) gridView.findViewById(R.id.view_last);
	                Button btn_make_status = (Button) gridView.findViewById(R.id.btn_make_status);

	                Button print = (Button) gridView.findViewById(R.id.btn_print);
	        		Button pay = (Button) gridView.findViewById(R.id.btn_pay);
	        		LinearLayout payment_lay=(LinearLayout) gridView.findViewById(R.id.payment_lay);
	        		View payment_view=(View) gridView.findViewById(R.id.payment_view);

	        		payment_lay.setVisibility(View.GONE);
	        		payment_view.setVisibility(View.GONE);
	        		print.setText("Share");
	        		print.setOnClickListener(new OnClickListener() {

	        			@Override
	        			public void onClick(View v) {
	        				// TODO Auto-generated method stub
	        				//Toast.makeText(context, "Coming Soon", Toast.LENGTH_LONG).show();
	        				StringBuilder buf=new StringBuilder();
	        				buf.append(System.getProperty("line.separator"));
	        				ArrayList<CartItems> items=mycart.get(position).getProducts();

	        				//buf.append(mycart.get(position).getTime().toString());
	        				//buf.append(System.getProperty("line.separator"));
	        				Double IEtotal=0.0d;
	        				int i=0;

	        				for(CartItems x:items)
	        				{
	        					i++;
	        					if(!x.get_status().equals("5")){
	        					try{
	        						String ip=null;
	        						if(DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", x.get_item().get_name()))
	        						{
	        							Item I=DBAdapter.getItemData(x.get_item().get_name());
	        							if(x.get_item().get_flag().equals("I")){
	        							ip=I.get_price();
	        							}
	        							else{
	        								ip=x.get_item().get_price();
	        							}
	        						}else{
	        							ip=x.get_item().get_price();
	        						}

	        						if(Float.parseFloat(ip)>0.0){
	        							int q=x.get_quantity();
	        							Double d=q*Double.parseDouble(x.get_item().get_price());

	        							 IEtotal+=d;
	        							buf.append(x.get_quantity()+" "+x.get_item().get_name()+" "+String.valueOf(d));
	        						}
	        						else
	        						{
	        							buf.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
	        						}
	        						//IEtotal+=Float.parseFloat(y.get_price());
	        					}

	        					catch(Exception e)
	        					{
	        						buf.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
	        					}
	        					ArrayList<Extra> extras=x.get_item().get_extra();
	        					int z=0;
	        					for(Extra y:extras)
	        					{
	        						if(extras.size()!=0)
	        							buf.append(System.getProperty("line.separator"));
	        					try{
	        						Double dp=0.0d;
	        						if(y.get_id()!=0)
	        						{
	        							Extra e=DBAdapter.getExtraData(y.get_id());
	        							dp=Double.parseDouble(e.get_price());
	        						}
	        						else
	        						{Item ip=DBAdapter.getItemData(y.get_name());
	        						dp=Double.parseDouble(ip.get_price());

	        						}
	        						if(dp>0.0d){

	        						buf.append("   "+"with "+y.get_name()+"  "+String.valueOf(dp));
	        						}
	        						else
	        						{
	        							buf.append("   "+"with "+y.get_name()+" *");
	        						}


	        						//Toast.makeText(context,String.valueOf(y.get_id())+y.get_name(), Toast.LENGTH_SHORT).show();
	        						//IEtotal+=Double.parseDouble(y.get_price());
	        						IEtotal+=dp;
	        					}

	        					catch(Exception e)
	        					{
	        						buf.append("   "+"with "+y.get_name()+" *");
	        					}
	        						z++;

	        					}
	        					if(items.size()!=i)
	        						buf.append(System.getProperty("line.separator"));

	        				}
	        				}
	        				buf.append(System.getProperty("line.separator"));
	        				buf.append(System.getProperty("line.separator"));
	        				buf.append("Total: "+mycart.get(position).getTotal());
	        				buf.append(System.getProperty("line.separator"));
	        				buf.append("via Feeder app");
	        				//table.setText(mycart.get(position).getTableName());
	        				String sub="Order for "+mycart.get(position).getTableName()+":";
	        				Intent i1=new Intent(android.content.Intent.ACTION_SEND);
	        				i1.setType("text/plain");
	        				i1.putExtra(android.content.Intent.EXTRA_SUBJECT,sub);
	        				i1.putExtra(android.content.Intent.EXTRA_TEXT, buf.toString());
	        				context.startActivity(Intent.createChooser(i1,"Share via"));

	        				// Intent intent = new Intent(context, PrintMainActivity.class);
	        				 //context.startActivity(intent);

	        			}
	        		});


			//table.setHeightRatio(positionHeight);
			table.setText(String.valueOf("Table "+mycart.get(position).getTableName()));
			final Staff staff=DBAdapter.getStaffData(Main.getEmail(context));
			if(mycart.get(position).getUser().equals(staff.get_staff_fname()))
			{
				user.setText("You");
			}else{
			user.setText(mycart.get(position).getUser());}

			try{
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

				Date oldDate1 = format.parse(oldTime);
				long oldMillis = oldDate1.getTime();


				Date orgDate = format.parse(mycart.get(position).getTime());
				//String oldTime = format.format(orgDate);
				//Toast.makeText(context,format.format(oldDate),Toast.LENGTH_LONG).show();

				//Date oldDate1 = format.parse(oldTime);
				//oldDate.getYear();
				long orgoldMillis = orgDate.getTime();
			//long cartTime=Long.parseLong(d);
			//TimeUtils.formatMillis(cartTime, HOURS, MINUTES);
				Calendar cal = Calendar.getInstance();
				long a=cal.getTimeInMillis();
			time.setText(TimeUtils.millisToLongDHMS(oldMillis - orgoldMillis));

	}catch(Exception e)
	{
		Toast.makeText(context,String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
	}

			DynamicHeightTextView Total = (DynamicHeightTextView) gridView.findViewById(R.id.grid_total);

			//Total.setHeightRatio(positionHeight);
			//Total.setText(mycart.get(position).getTotal());
			StringBuilder buf=new StringBuilder();
			StringBuilder buf2=new StringBuilder();
			StringBuilder buf3=new StringBuilder();
			ArrayList<CartItems> items=mycart.get(position).getProducts();
			ArrayList<CartItems> itemsofFCourse=mycart.get(position).getProductsOfFirstCourse();
			ArrayList<CartItems> itemsofSCourse=mycart.get(position).getProductsOfSecondCourse();
			ArrayList<CartItems> itemsofTCourse=mycart.get(position).getProductsOfThirdCourse();


			try{
				 //First Course
				NestedListView lv=(NestedListView) gridView.findViewById(R.id.cartitems_listView);
			   final CartItemNOBaseAdapter itemNOBaseAdapter=new CartItemNOBaseAdapter(context,itemsofFCourse);
			   itemNOBaseAdapter.notifyDataSetChanged();
			 // lv.setAdapter(null);
			  lv.setAdapter(itemNOBaseAdapter);
			 lv.setEnabled(false);
			lv.setFocusable(false);
			lv.setClickable(false);
			lv.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			 // Helper.getListViewSize(lv);
			//Second Course
			NestedListView lv2=(NestedListView) gridView.findViewById(R.id.cartitems_sndlistView);
			   final CartItemNOBaseAdapter itemNOBaseAdapter2=new CartItemNOBaseAdapter(context,itemsofSCourse);
			   itemNOBaseAdapter2.notifyDataSetChanged();
			 // lv.setAdapter(null);
			  lv2.setAdapter(itemNOBaseAdapter2);
			 lv2.setEnabled(false);
			lv2.setFocusable(false);
			lv2.setClickable(false);
			lv2.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

			//Third Course
			NestedListView lv3=(NestedListView) gridView.findViewById(R.id.cartitems_TrdlistView);
			   final CartItemNOBaseAdapter itemNOBaseAdapter3=new CartItemNOBaseAdapter(context,itemsofTCourse);
			   itemNOBaseAdapter3.notifyDataSetChanged();
			 // lv.setAdapter(null);
			  lv3.setAdapter(itemNOBaseAdapter3);
			 lv3.setEnabled(false);
			lv3.setFocusable(false);
			lv3.setClickable(false);
			lv3.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

			  }catch(Exception e){
				  Toast.makeText(context,String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
			  }
			/*	lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				 //((StaggeredGridView) parent).getChildAt(position).setSelected(true);
				 ((StaggeredGridView) parent).performItemClick(arg1, position+1, 0);
				// ((StaggeredGridView) parent).setSelected(false);
			}
		});*/
			  //Helper.getListViewSize(lv);

			//buf.append(mycart.get(position).getTime().toString());
			//buf.append(System.getProperty("line.separator"));
			Double IEtotal=0.0d;
			int i=0;
			String whichbuf="";
			rel_make_status.setVisibility(View.GONE);
			 view_make_status.setVisibility(View.GONE);
			 view_last.setVisibility(View.VISIBLE);
			for(CartItems x:items)
			{
				i++;
				boolean isSet=false;
				if(!x.get_status().equals("5")){
					if(staff.get_staff_roll().equals("waiter") && !isSet){
						if(x.get_status().equals("3")){
							 gridView.setBackgroundResource(R.drawable.taborder_back_waiter);
							 isSet=true;
							 rel_make_status.setVisibility(View.VISIBLE);
							 view_make_status.setVisibility(View.VISIBLE);
							 view_last.setVisibility(View.GONE);
							 btn_make_status.setText("MARK SERVED");
						}
					}
					if(staff.get_staff_roll().equals("cook")&& !isSet){
						if(x.get_status().equals("0")|| x.get_status().equals("1") ||x.get_status().equals("2")){
							 gridView.setBackgroundResource(R.drawable.taborder_bac_cook);
							 isSet=true;
							 rel_make_status.setVisibility(View.VISIBLE);
							 view_make_status.setVisibility(View.VISIBLE);
							 view_last.setVisibility(View.GONE);
							 btn_make_status.setText("MARK READY");
						}
					}
				if(!(i==1)){
					if(x.get_course()==1){
						if(!buf.toString().isEmpty())
						buf.append(System.getProperty("line.separator"));
					}
					if(x.get_course()==2){
						if(!buf2.toString().isEmpty())
						buf2.append(System.getProperty("line.separator"));
					}
					if(x.get_course()==3){
						if(!buf2.toString().isEmpty())
						buf3.append(System.getProperty("line.separator"));
					}
				}
				try{
					String ip=null;
					if(DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", x.get_item().get_name()))
					{
						Item I=DBAdapter.getItemData(x.get_item().get_name());
						if(x.get_item().get_flag().equals("I")){
							ip=I.get_price();
							}
							else{
								ip=x.get_item().get_price();
							}
					}else{
						ip=x.get_item().get_price();
					}

					//Toast.makeText(context, I.get_price(), Toast.LENGTH_SHORT).show();
					int q=x.get_quantity();
					Double d=q*Double.parseDouble(ip);
					 IEtotal+=d;

					x.get_item().set_price(ip);
				}
				catch(Exception e)
				{

				}
				/*if(Integer.parseInt(x.get_item().get_price())!=0)
				{
					buf.append(x.get_quantity()+" "+x.get_item().get_name());
				}
				else
				{
					buf.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
				}*/
				try{
					if(Float.parseFloat(x.get_item().get_price())>0.0){
						if(x.get_course()==1){
						buf.append(x.get_quantity()+" "+x.get_item().get_name());
						whichbuf="buf";
						}
						if(x.get_course()==2){
						buf2.append(x.get_quantity()+" "+x.get_item().get_name());
						whichbuf="buf2";
							}
						if(x.get_course()==3){
						buf3.append(x.get_quantity()+" "+x.get_item().get_name());
						whichbuf="buf3";
						}
					}
					else
					{
						if(x.get_course()==1){
						buf.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
						whichbuf="buf";
						}
						if(x.get_course()==2){
							buf2.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
							whichbuf="buf2";
							}
						if(x.get_course()==3){
							buf3.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
							whichbuf="buf3";
							}
					}
					//IEtotal+=Float.parseFloat(y.get_price());
				}

				catch(Exception e)
				{
					if(x.get_course()==1){
					buf.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
					whichbuf="buf";
					}
					if(x.get_course()==2){
						buf2.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
						whichbuf="buf2";
						}
					if(x.get_course()==3){
						buf3.append(x.get_quantity()+" "+x.get_item().get_name()+ " *");
						whichbuf="buf3";
						}
				}
				ArrayList<Extra> extras=x.get_item().get_extra();
				int z=0;
				for(Extra y:extras)
				{
					/*if(extras.size()!=0){
						if(x.get_course()==1){
						buf.append(System.getProperty("line.separator"));
						}
						if(x.get_course()==2){
							buf2.append(System.getProperty("line.separator"));
							}
						if(x.get_course()==3){
							buf3.append(System.getProperty("line.separator"));
							}
					}*/
				try{
					Double dp=0.0d;
					if(y.get_id()!=0)
					{
						Extra e=DBAdapter.getExtraData(y.get_id());
						dp=Double.parseDouble(e.get_price());
					}
					else
					{Item ip=DBAdapter.getItemData(y.get_name());
					dp=Double.parseDouble(ip.get_price());

					}
					/*if(dp>0.0d){
						if(x.get_course()==1){
					buf.append("   "+"with "+y.get_name());
						}
						if(x.get_course()==2){
							buf2.append("   "+"with "+y.get_name());
								}
						if(x.get_course()==3){
							buf3.append("   "+"with "+y.get_name());
								}

					}
					else
					{if(x.get_course()==1){
						buf.append("   "+"with "+y.get_name()+" *");
					}
					if(x.get_course()==2){
						buf2.append("   "+"with "+y.get_name()+" *");
					}
					if(x.get_course()==3){
						buf3.append("   "+"with "+y.get_name()+" *");
					}
					}*/


					//Toast.makeText(context,String.valueOf(y.get_id())+y.get_name(), Toast.LENGTH_SHORT).show();
					//IEtotal+=Double.parseDouble(y.get_price());
					IEtotal+=dp;
				}

				catch(Exception e)
				{
					if(x.get_course()==1){
					buf.append("   "+"with "+y.get_name()+" *");
					}
					if(x.get_course()==2){
						buf2.append("   "+"with "+y.get_name()+" *");
						}
					if(x.get_course()==3){
						buf3.append("   "+"with "+y.get_name()+" *");
						}
				}
					z++;

				}
				/*if(items.size()!=i){
					if(whichbuf.equals("buf")){
						buf.append(System.getProperty("line.separator"));
					}
					if(whichbuf.equals("buf2")){
						buf2.append(System.getProperty("line.separator"));
					}
					if(whichbuf.equals("buf3")){
						buf3.append(System.getProperty("line.separator"));
					}*/
					/*	if(!buf.toString().isEmpty()){
						buf.append(System.getProperty("line.separator"));
					}
					if(!buf2.toString().isEmpty()){
						buf2.append(System.getProperty("line.separator"));
					}
					if(!buf3.toString().isEmpty()){
						buf3.append(System.getProperty("line.separator"));
					}

					if(x.get_course()==1){
					buf.append(System.getProperty("line.separator"));
					}
					if(x.get_course()==2){
						buf2.append(System.getProperty("line.separator"));
						}
					if(x.get_course()==3){
						buf3.append(System.getProperty("line.separator"));
						}*/
				//}

			}
			}
			/*if(!buf2.toString().isEmpty()){
				buf.append("\n");
			//	buf2.append(System.getProperty("line.separator"));
			}
			if(!buf3.toString().isEmpty()){
				buf2.append("\n");
				if(!buf.toString().isEmpty()){
					buf.append("\n");
				}
				//buf3.append(System.getProperty("line.separator"));
			}*/
			//DynamicHeightTextView items1 = (DynamicHeightTextView) gridView.findViewById(R.id.grid_Tableitems);
			//DynamicHeightTextView items2 = (DynamicHeightTextView) gridView.findViewById(R.id.grid_Tableitems2);
			//DynamicHeightTextView items3 = (DynamicHeightTextView) gridView.findViewById(R.id.grid_Tableitems3);
			DynamicHeightTextView scheader=(DynamicHeightTextView) gridView.findViewById(R.id.sc_header);
			DynamicHeightTextView tcheader=(DynamicHeightTextView) gridView.findViewById(R.id.tc_header);
			View view1=(View) gridView.findViewById(R.id.sc_header_btm);
			View view2=(View) gridView.findViewById(R.id.tc_header_btm);
		//	items1.setVisibility(View.GONE);
		//	items2.setVisibility(View.GONE);
		//	items3.setVisibility(View.GONE);
			scheader.setVisibility(View.GONE);
			tcheader.setVisibility(View.GONE);
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.GONE);
			//items1.setHeightRatio(positionHeight);
			if(!buf2.toString().trim().isEmpty()){
			//	items2.setVisibility(View.VISIBLE);
				scheader.setVisibility(View.VISIBLE);
				view1.setVisibility(View.VISIBLE);
			//	items2.setText(buf2.toString());

			}
			if(!buf3.toString().trim().isEmpty()){
			//	items3.setVisibility(View.VISIBLE);
				tcheader.setVisibility(View.VISIBLE);
				view2.setVisibility(View.VISIBLE);
			//	items3.setText(buf3.toString());
			}
			if(!buf.toString().trim().isEmpty()){
			//	items1.setVisibility(View.VISIBLE);
				//items1.setText(buf.toString());
			}
			//items1.setText(buf.toString());

			//Total.setText("Total: "+mycart.get(position).getTotal());


			Double awithtax=IEtotal;
			awithtax+=Double.parseDouble(MyAdapter.serviceCharge(String.valueOf(IEtotal)));
			Total.setText("Total: "+awithtax);
			try{
				DBAdapter.updateCartTTT(mycart.get(position).getId(), "cart_total", IEtotal.toString());
				mycart.get(position).setTotal(IEtotal.toString());


			}catch(Exception e)
			{

			}
			//titleTotal=IEtotal;
			pay.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				Intent intent11 = new Intent(context, PaymentActivity.class);
                	intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                	Bundle mBundle1 = new Bundle();
            		mBundle1.putString("ID", String.valueOf(mycart.get(position).getId()));
            		intent11.putExtras(mBundle1);
            		context.startActivity(intent11);


    			}
    		});
			btn_make_status.setOnClickListener(new OnClickListener() {

    			@Override
    			public void onClick(View v) {
    				 String role=staff.get_staff_roll();

    			        	if(role.equals("waiter")){
    			        		Toast.makeText(context, "Press long to Mark Served", Toast.LENGTH_LONG).show();
    			        	}else{
    			        		Toast.makeText(context, "Press long to Mark Ready", Toast.LENGTH_LONG).show();
    			        	}



    			}
    		});
			btn_make_status.setOnLongClickListener(new OnLongClickListener() {
		        @Override
		        public boolean onLongClick(View v) {
		       String role=staff.get_staff_roll();
		       String status="3";
		        	if(role.equals("waiter")){
		        		status="4";
		        	}else{
		        		status="3";
		        	}
		        	try
					 {
							 ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					        HashMap<String, String> map = new HashMap<String, String>();
					        map.put("oprn","Update");
					        map.put("table", "CartItem");
					        map.put("field", "Status");
					        map.put("id",String.valueOf(mycart.get(position).getId()));
					        map.put("status",status);
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


		        	DBAdapter.updateCartItemStatus(mycart.get(position).getId(), status);
		        	for (CartItems cartItems : mycart.get(position).getProducts()) {
		        		cartItems.set_status(status);
					}
		        	//gridView.setBackgroundResource(R.drawable.taborder_back);
		        	rel_make_status.setVisibility(View.GONE);
					 view_make_status.setVisibility(View.GONE);
		        	updateResults(DBAdapter.getAllCartData());

		        	//aController.CartUpdate(context, "Hello");
		            return true;
		        }
		    });

		return gridView;
	}

	  private double getPositionRatio(final int position) {
	        double ratio = sPositionHeightRatios.get(position, 0.0);
	        // if not yet done generate and stash the columns height
	        // in our real world scenario this will be determined by
	        // some match based on the known height and width of the image
	        // and maybe a helpful way to get the column height!
	        if (ratio == 0) {
	            ratio = getRandomHeightRatio();
	            sPositionHeightRatios.append(position, ratio);
	           // Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
	        }
	        return ratio;
	    }

	    private double getRandomHeightRatio() {
	        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
	    }

	@Override
	public int getCount() {
		return mycart.size();
	}

	@Override
	public Object getItem(int position) {
		return mycart.get(position);
	}

	@Override
	public long getItemId(int position) {
		return  mycart.get(position).getId();
	}

	private String convertDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
            Date d = format.parse(date);
            SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            return serverFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}