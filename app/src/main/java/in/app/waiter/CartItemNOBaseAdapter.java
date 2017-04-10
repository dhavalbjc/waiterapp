package in.app.waiter;


import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;

import java.util.ArrayList;


public class CartItemNOBaseAdapter extends BaseAdapter {

    ArrayList<CartItems> myList = new ArrayList<CartItems>();
    LayoutInflater inflater;
    Context context;


    public CartItemNOBaseAdapter(Context context, ArrayList<CartItems> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public CartItems getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.no_cartitem_with_status, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        StringBuilder extra_buf=new StringBuilder();
        
      
        try{
        	 CartItems currentListData = getItem(position);
        	// Toast.makeText(context,String.valueOf(currentListData.get_item().get_name()),Toast.LENGTH_LONG).show();
             // mViewHolder.tvQty.setText(currentListData.get_quantity());
            //  mViewHolder.tvQty.setText(String.valueOf(currentListData.get_quantity()));
			String ip=null;
			if(DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_item", "item_name", currentListData.get_item().get_name()))
			{
				Item I=DBAdapter.getItemData(currentListData.get_item().get_name());
				if(currentListData.get_item().get_flag().equals("I")){
					ip=I.get_price();
					}
					else{
						ip=currentListData.get_item().get_price();
					}
			}else{
				ip=currentListData.get_item().get_price();
			}
			
			 currentListData.get_item().set_price(ip);
		
        mViewHolder.tvQty.setText(String.valueOf(currentListData.get_quantity()));
        if(Float.parseFloat(currentListData.get_item().get_price())>0.0){
        	 mViewHolder.tvItemname.setText(currentListData.get_item().get_name());
        }else{
        	 mViewHolder.tvItemname.setText(currentListData.get_item().get_name()+ " *");
        }
        ArrayList<Extra> extras=currentListData.get_item().get_extra();
		int i=0;
        for(Extra extra:extras)
		{
        	i++;
        	try{
				Double extraPrice=0.0d;
				if(extra.get_id()!=0)
				{
					Extra e=DBAdapter.getExtraData(extra.get_id());
					extraPrice=Double.parseDouble(e.get_price());	
				}
				else
				{
					Item ip1=DBAdapter.getItemData(extra.get_name());
					extraPrice=Double.parseDouble(ip1.get_price());
					
				}
				if(extraPrice>0.0d){
					
					extra_buf.append("with "+extra.get_name());
					if(extras.size()!=i)
					extra_buf.append(System.getProperty("line.separator"));
				}else{
					extra_buf.append("with "+extra.get_name()+" *");
					if(extras.size()!=i)
					extra_buf.append(System.getProperty("line.separator"));
				}
				}
        	catch(Exception e){
        		
        	}        	
		}
        int status=0;
        if(currentListData.get_status().equals("")){
        	status=0;
        }else{
         status=Integer.parseInt(currentListData.get_status());
        }
        View viewcopy=convertView;
        int resId;
        switch (status) {
       	case 0:
       		resId=viewcopy.getResources().getIdentifier("in.app.waiter:drawable/minus_arrow",null,null);
       		mViewHolder.ivIcon.setImageResource(resId);
       		break;
     	case 1:
     		resId=viewcopy.getResources().getIdentifier("in.app.waiter:drawable/minus_arrow",null,null);
       		mViewHolder.ivIcon.setImageResource(resId);
       		break;
     	case 2:
     		resId=viewcopy.getResources().getIdentifier("in.app.waiter:drawable/status_coking",null,null);
       		mViewHolder.ivIcon.setImageResource(resId);
       		break;
     	case 3:
     		resId=viewcopy.getResources().getIdentifier("in.app.waiter:drawable/status_ready",null,null);
       		mViewHolder.ivIcon.setImageResource(resId);
       		break;
     	case 4:
     		resId=viewcopy.getResources().getIdentifier("in.app.waiter:drawable/status_served",null,null);
       		mViewHolder.ivIcon.setImageResource(resId);
       		break;
     	case 5:
     		resId=viewcopy.getResources().getIdentifier("in.app.waiter:drawable/status_cancelled_three",null,null);
       		mViewHolder.ivIcon.setImageResource(resId);
       		break;	
       		
       	default:
       		break;
       		}
        if(status<2){
        	mViewHolder.Rel_status_img.setVisibility(View.INVISIBLE);
        }
       
        	if(currentListData.get_status().equals("5"))
        		mViewHolder.tvItemname.setPaintFlags(mViewHolder.tvItemname.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
		catch(Exception e)
		{
			Toast.makeText(context,String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
		}
        mViewHolder.txtExtra.setText(extra_buf.toString());
        
        //mViewHolder.ivIcon.setImageResource(currentListData.get_status());
       if(extra_buf.toString().equals("")){
    	   mViewHolder.txtExtra.setVisibility(View.GONE);
       }
  
       // mViewHolder.Rel_status_img.setVisibility(View.INVISIBLE);
        return convertView;
    }

    private class MyViewHolder {
        DynamicHeightTextView tvQty, tvItemname,txtExtra;
        DynamicHeightImageView ivIcon;
        RelativeLayout Rel_status_img;
        //ImageView ivIcon;

        public MyViewHolder(View item) {
        	tvQty = (DynamicHeightTextView) item.findViewById(R.id.txt_qty);
        	tvItemname = (DynamicHeightTextView) item.findViewById(R.id.txt_item);
        	txtExtra = (DynamicHeightTextView) item.findViewById(R.id.txt_extra_buf);
        	ivIcon=(DynamicHeightImageView)item.findViewById(R.id.img_status);
        	Rel_status_img=(RelativeLayout) item.findViewById(R.id.Rel_status_img);
           // ivIcon = (ImageView) item.findViewById(R.id.img_status);
        }
    }
}

