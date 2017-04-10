package in.app.waiter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class CartItemsAdapter extends ArrayAdapter {
	 
    private Context mContext;
    private ArrayList<CartItems> mList;
    private int index;
 
    public CartItemsAdapter(Context context, ArrayList<CartItems> list) {
        super(context, R.layout.is_selecteditem_layout, list);
 
        mContext = context;
        mList = list;
 //       this.index=index;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        View view;
 
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.is_selecteditem_layout, null);
        }
        else {
            view = convertView;
        }
 
        TextView itemNameView = (TextView) view.findViewById(R.id.tv_no_itemname);
        final TextView tv_no_pluse = (TextView) view.findViewById(R.id.tv_no_pluse);
        
        RelativeLayout Rel_Item_plus=(RelativeLayout)view.findViewById(R.id.Rel_Item_plus);
	    RelativeLayout Rel_Item_minus=(RelativeLayout)view.findViewById(R.id.Rel_Item_minus);
 
        itemNameView.setText( mList.get(position).get_item().get_name() );
       
        Rel_Item_plus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try
				{
			        
			       
			        	int _quantity;
			        	_quantity=mList.get(position).get_quantity();
			        	_quantity++;
			        	mList.get(position).set_quantity(_quantity);
			        	
				        Item_selected.cartItemadapter1.notifyDataSetChanged();
			        	
			        	//GamesFragment.ExpAdapter.notifyDataSetChanged();
				        tv_no_pluse.setText(String.valueOf(_quantity));
			     
				}
				catch(Exception e)
				{
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
        Rel_Item_minus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try
				{
			        
			       
			        	int _quantity;
			        	_quantity=mList.get(position).get_quantity();
			        	_quantity--;
			        	if(_quantity<=0){
			        	newOrderActivity.cartItems.remove(mList.get(position));
			        	newOrderActivity.myCartHash.remove(mList.get(position).get_item().get_name());
			        	}
			        	else{
			        		tv_no_pluse.setText(String.valueOf(_quantity));
			        	mList.get(position).set_quantity(_quantity);
			        	}
				        Item_selected.cartItemadapter1.notifyDataSetChanged();
			        	//GamesFragment.ExpAdapter.notifyDataSetChanged();
				     //   _item.calltocallback();  
			        
				}
				catch(Exception e)
				{
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
 
        return view;
    }
}
