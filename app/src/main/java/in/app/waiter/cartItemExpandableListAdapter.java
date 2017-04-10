package in.app.waiter;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class cartItemExpandableListAdapter extends BaseExpandableListAdapter {
	 
    private Context mContext;
    private ArrayList<CartItems>  mGroups;
    private LayoutInflater mInflater;
    private int count=0;
    //public Item_selected _item=new Item_selected();
    public cartItemExpandableListAdapter(Context context, ArrayList<CartItems> groups) {
        mContext = context;
        mGroups = groups;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
    }
 
    @Override
    public int getGroupCount() {
        return mGroups.size();
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).get_item().get_extra().size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).get_item().get_extra().get(childPosition);
        
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
 
       
        if (convertView == null) {
            
            convertView = mInflater.inflate(R.layout.is_selecteditem_layout, parent,false);
            
        }
 
        // Get the group item
        final CartItems group = (CartItems) getGroup(groupPosition);
      
        
        // Set group name
        TextView textView = (TextView) convertView.findViewById(R.id.tv_no_itemname);
        final TextView plusebtn = (TextView) convertView.findViewById(R.id.tv_no_pluse);
        //final TextView minusbtn = (TextView) convertView.findViewById(R.id.tv_no_minus);
        textView.setText(group.get_item().get_name());
        RelativeLayout expandcollesp =(RelativeLayout) convertView.findViewById(R.id.Rel_no_item_click);
        RelativeLayout pluse=(RelativeLayout)convertView.findViewById(R.id.Rel_Item_plus);
        final RelativeLayout minus=(RelativeLayout)convertView.findViewById(R.id.Rel_Item_minus);
        //minus.setEnabled(false);
       // minus.setVisibility(100);
        //RelativeLayout
        plusebtn.setText(String.valueOf(group.get_quantity()));
        pluse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try
				{
			        
			       
			        	int _quantity;
			        	_quantity=group.get_quantity();
			        	_quantity++;
			        	group.set_quantity(_quantity);
			        	newOrderActivity.myCartHash.get(group.get_item().get_name()).set_quantity(_quantity);
			        	newOrderActivity.cartItems.get(newOrderActivity.cartItems.indexOf(group)).set_quantity(_quantity);
				        Item_selected.cartItemadapter1.notifyDataSetChanged();
			        	//GamesFragment.ExpAdapter.notifyDataSetChanged();
				        plusebtn.setText(String.valueOf(_quantity));
				        Item_selected.activityCallback.onButtonClick();
			        
				}
				catch(Exception e)
				{
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
        minus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try
				{
			        
			       
			        	int _quantity;
			        	_quantity=group.get_quantity();
			        	_quantity--;
			        	if(_quantity<=0){
			        		ArrayList<Extra> e=group.get_item().get_extra();
					        for(Extra x:e)
					        {
					        	//Toast.makeText(mContext, x.get_name(), Toast.LENGTH_LONG).show();
					        	if(newOrderActivity.itemExtra.containsKey(x.get_item().get_name()+x.get_name()))
					        	{
					        		newOrderActivity.itemExtra.remove(x.get_item().get_name()+x.get_name());
					        		//Toast.makeText(mContext, x.get_name()+ "Removed", Toast.LENGTH_LONG).show();
					        	}
					        	else{
					        		//Toast.makeText(mContext, x.get_name()+ "Not Removed", Toast.LENGTH_LONG).show();
					        	}
					        	
					        }
					        if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
			        	newOrderActivity.cartItems.remove(group);
			        	newOrderActivity.myCartHash.remove(group.get_item().get_name());
			        	}
			        	else{
			        		plusebtn.setText(String.valueOf(_quantity));
			        	group.set_quantity(_quantity);
			        	newOrderActivity.myCartHash.get(group.get_item().get_name()).set_quantity(_quantity);
			        	newOrderActivity.cartItems.get(newOrderActivity.cartItems.indexOf(group)).set_quantity(_quantity);
			        	}
				        Item_selected.cartItemadapter1.notifyDataSetChanged();
			        	//GamesFragment.ExpAdapter.notifyDataSetChanged();
				        Item_selected.activityCallback.onButtonClick();
				       //  _item.calltocallback();
				}
				catch(Exception e)
				{
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
        final Resources res = convertView.getResources();
       // Toast.makeText(mContext,"ok",Toast.LENGTH_LONG).show();
         expandcollesp.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			//  Toast.makeText(mContext, String.valueOf(isExpanded), Toast.LENGTH_SHORT).show();
               
				 if(isExpanded)
					 ((ExpandableListView) parent).collapseGroup(groupPosition);					
		            else		           
		            	((ExpandableListView) parent).expandGroup(groupPosition);
				

		}
	});
        return convertView;
    }
 
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
 
 

        if (convertView == null) {
        	
            convertView = mInflater.inflate(R.layout.no_extra_item_selecteed,parent, false);
        }
       // Toast.makeText(mContext,"In child",Toast.LENGTH_SHORT).show();
        final Extra children = (Extra) getChild(groupPosition, childPosition);
        // Set group name
        final Resources res = convertView.getResources();
        TextView textView = (TextView) convertView.findViewById(R.id.tv_no_extraname);
        final TextView plusebtn = (TextView) convertView.findViewById(R.id.tv_noextra_pluse);
      //  final TextView minusbtn = (TextView) convertView.findViewById(R.id.tv_no_minus);
       
        final RelativeLayout expandcollesp =(RelativeLayout) convertView.findViewById(R.id.Rel_no_extra_click);
        final RelativeLayout pluse=(RelativeLayout)convertView.findViewById(R.id.Rel_Extra_plus);
       // final RelativeLayout rel_item_name=(RelativeLayout)convertView.findViewById(R.id.Rel_no_item_click);
        
        final RelativeLayout minus=(RelativeLayout)convertView.findViewById(R.id.Rel_Extra_minus);
        minus.setVisibility(View.GONE);
        final ImageView imgView = (ImageView) convertView.findViewById(R.id.no_extra_imag);
        LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        //childParam1.setMargins(0, 0, 0, 2);
        childParam1.weight =70f;
        expandcollesp.setLayoutParams(childParam1);
       
        
        LinearLayout.LayoutParams childParam12 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
		//childParam12.setMargins(0, 0, 0, 2);
        childParam12.weight = 15f;
        minus.setLayoutParams(childParam12);
        
        LinearLayout.LayoutParams childParam121 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
	//	childParam121.setMargins(2, 0, 0, 2);
       // childParam121.setMargins(0, 0, -2, 0);
        childParam121.weight = 15f;
        pluse.setLayoutParams(childParam121);
        // Set child name
        //TextView text = (TextView) convertView.findViewById(R.id.tv_no_extraname);
        //text.setText(children.get_name());
        
        	 textView.setText(children.get_name());
        	if(newOrderActivity.itemExtra.containsKey(children.get_item().get_name()+children.get_name())){
        		// Toast.makeText(mContext,children.get_name() +"Contains",Toast.LENGTH_SHORT).show();
        		 minus.setVisibility(View.VISIBLE);
				 imgView.setVisibility(View.GONE);  					 
				 expandcollesp.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground_small_corners));
				 pluse.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground_small_corners));
				
				 plusebtn.setText("1");
                   
                    LinearLayout.LayoutParams childParam11 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
                     childParam11.weight = 55f;
                    // childParam11.setMargins(0, 0, 2, 2);
                     expandcollesp.setLayoutParams(childParam11);
        	}
        	else
        	{
        		 expandcollesp.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground));
				 pluse.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground));
				 plusebtn.setText("");
				 imgView.setVisibility(View.VISIBLE);
        	}
        	
        	
        // Get child name
       
       
        //minus.setEnabled(false);
       // minus.setVisibility(100);
        //RelativeLayout
       
 
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
                
                try{
                	
    					 minus.setVisibility(View.VISIBLE);
    					 imgView.setVisibility(View.GONE);  					 
    					 expandcollesp.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground_small_corners));
    					 pluse.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground_small_corners));
    					
    					 plusebtn.setText("1");
    	                   
    	                    LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
	                         childParam1.weight = 55f;
	                       //  childParam1.setMargins(0, 0, 2, 2);
	                         expandcollesp.setLayoutParams(childParam1);
    	                    
    	                   // Item_selected.cartItemadapter1.notifyDataSetChanged();
	                         if(!newOrderActivity.itemExtra.containsKey(children.get_item().get_name()+children.get_name())){
	                        	 newOrderActivity.itemExtra.put(children.get_item().get_name()+children.get_name(), children);
	                        	 
	 	                // newOrderActivity.itemExtra.put(children,children);
	 	             //   Toast.makeText(mContext,  children.get_name()+ " Added", Toast.LENGTH_SHORT).show();
	 	                
	 	                    }
	 	                    
	                         Item_selected.cartItemadapter1.notifyDataSetChanged();
	 			        	//GamesFragment.ExpAdapter.notifyDataSetChanged();
	 				        Item_selected.activityCallback.onButtonClick();
	 				       //  _item.calltocallback();
	 			
	 	                 //   Item_selected.cartItemadapter1.notifyDataSetChanged();
    		
    				}catch(Exception e)
    				{
    					Toast.makeText(mContext, e.toString(),Toast.LENGTH_LONG).show();
    				}
    				               
                
            }
        });
        
        
  minus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//minus.setVisibility(0);
				try
				{
			        
			        //	Drawable drawable = res.getDrawable(R.drawable.btn_back_selector); //new Image that was added to the res folder
			        	imgView.setVisibility(View.VISIBLE);
						//pluse.setBackgroundDrawable(drawable);
						 expandcollesp.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground));
						 pluse.setBackgroundDrawable(res.getDrawable(R.drawable.newbackground));
						 plusebtn.setText("");
						//expandcollesp.setBackgroundDrawable(drawable);
						minus.setVisibility(View.GONE);
						LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
				        childParam1.weight = 70f;
				      //  childParam1.setMargins(0, 0, 2, 2);
				        expandcollesp.setLayoutParams(childParam1);
				        if(newOrderActivity.itemExtra.containsKey(children.get_item().get_name()+children.get_name())){
         	   		        
		 	                 newOrderActivity.itemExtra.remove(children.get_item().get_name()+children.get_name());
		 	              //  Toast.makeText(mContext,  children.get_name()+ " Removed", Toast.LENGTH_SHORT).show();
			 	           
		 	               
			 	               Item_selected.cartItemadapter1.notifyDataSetChanged();
					        	//GamesFragment.ExpAdapter.notifyDataSetChanged();
						        Item_selected.activityCallback.onButtonClick();
		 	                    }
				        
			        	
				}
				catch(Exception e)
				{
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
      
        return convertView;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}