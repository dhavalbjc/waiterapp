package in.app.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


	class PaymentCartArrayAdapter extends ArrayAdapter {
		 
	    private Context mContext;
	    private ArrayList<CartItems> mList;
	    private int index;
	 
	    public PaymentCartArrayAdapter(Context context, ArrayList<CartItems> list) {
	        super(context, R.layout.bill_activity, list);
	 
	        mContext = context;
	        mList = list;
	 //       this.index=index;
	    }
	 
	    @Override
	    public View getView(final int position, View convertView, ViewGroup parent) {
	 
	        View view;
	 
	        if (convertView == null) {
	            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.bill_activity, null);
	        }
	        else {
	            view = convertView;
	        }
	 
	        TextView itemNameView = (TextView) view.findViewById(R.id.bill_itemname);
	        final TextView tv_no_pluse = (TextView) view.findViewById(R.id.bill_qty);
	        TextView price = (TextView) view.findViewById(R.id.billprice);
	        
	       	 
	        itemNameView.setText( mList.get(position).get_item().get_name() );
	        
	       // price.setText( mList.get(position).get_item().get_price() );
	        try{
	        					
				// IEtotal+=d;
	        	if(mList.get(position).get_item().get_flag().equals("I")){
					String strprice = null;
					if(DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", mList.get(position).get_item().get_name()))
					{Item I=DBAdapter.getItemData(mList.get(position).get_item().get_id());
					strprice=I.get_price();
					}
					else{
						strprice=	mList.get(position).get_item().get_price();
					}
				//Toast.makeText(getContext(), mList.get(position).get_quantity(), Toast.LENGTH_SHORT).show();
					int q=mList.get(position).get_quantity();
					Double d=q*Double.parseDouble(strprice);
				price.setText( String.valueOf(d) );
				}
				else{
					price.setText(mList.get(position).get_item().get_price() );
				}
				tv_no_pluse.setText( String.valueOf( mList.get(position).get_quantity()) );
				//x.get_item().set_price(I.get_price());
			}
			catch(Exception e)
			{
				price.setText(mList.get(position).get_item().get_price() );
			}
	      
	        return view;
	    }
	}
