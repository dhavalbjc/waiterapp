
package in.app.waiter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightTextView;

import java.util.ArrayList;
import java.util.Random;
	/***
	 * ADAPTER
	 */

		public class tablesAdapter extends ArrayAdapter<String> {
		   
		    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
		    private final LayoutInflater mLayoutInflater;
		    private final Random mRandom;
		    private final ArrayList<Integer> mBackgroundColors;
		    private ArrayList<ModelCart> carts=null;
		    public tablesAdapter(final Context context, ArrayList<ModelCart> mycart, final int textViewResourceId) {
		        super(context, textViewResourceId);
		        mLayoutInflater = LayoutInflater.from(context);
		        mRandom = new Random();
		        mBackgroundColors = new ArrayList<Integer>();
		        mBackgroundColors.add(R.color.orange);
		        mBackgroundColors.add(R.color.orange);
		        mBackgroundColors.add(R.color.orange);
		        mBackgroundColors.add(R.color.orange);
		        mBackgroundColors.add(R.color.orange);

		       // mBackgroundColors.add(R.color.green);
		       // mBackgroundColors.add(R.color.blue);
		      //  mBackgroundColors.add(R.color.yellow);
		      //  mBackgroundColors.add(R.color.grey);
		        mBackgroundColors.add(R.color.White);
		        carts=mycart;
		    }

		    @Override
		    public View getView(final int position, View convertView, final ViewGroup parent) {

		        ViewHolder vh;
		        if (convertView == null) {
		            convertView = mLayoutInflater.inflate(R.layout.tables_view, parent, false);
		            vh = new ViewHolder();
		            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
		          //  vh.btnGo = (Button) convertView.findViewById(R.id.btn_go);

		            convertView.setTag(vh);

		        }
		        else {
		            vh = (ViewHolder) convertView.getTag();
		        }

		       // double positionHeight = getPositionRatio(position);


		        int backgroundIndex=5;
		        int value=position+1;
		        Boolean found=false;
		        for (ModelCart cart : carts ) {
		        	if(cart.getTableName().equals(String.valueOf(value))){
		        		 backgroundIndex = position > mBackgroundColors.size() ?
		 		                position % mBackgroundColors.size() : position;
		        		backgroundIndex=1;
		        		 found=true;
		        		/* Toast.makeText(getContext(), "Position " +
			                        position, Toast.LENGTH_SHORT).show();*/
		        	}

				}
		        if(found){
		        //	convertView.setBackgroundResource(mBackgroundColors.get(3));
		        }
		        else{
		       // 	convertView.setBackgroundResource(mBackgroundColors.get(5));
		        }
		        if(backgroundIndex!=5){
		        	convertView.setBackgroundResource(R.drawable.newbackground_small_corners);
		        }else{
		        	convertView.setBackgroundResource(R.drawable.newbackground);
		        }

		       // convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

		        //Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

		    //    vh.txtLineOne.setHeightRatio(positionHeight);
		        vh.txtLineOne.setText(String.valueOf(position+1));

		  /*      vh.btnGo.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(final View v) {
		                Toast.makeText(getContext(), "Button Clicked Position " +
		                        position, Toast.LENGTH_SHORT).show();
		            }
		        });
*/
		        return convertView;
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
		          //  Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
		        }
		        return ratio;
		    }

		    private double getRandomHeightRatio() {
		        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
		    }

		    static class ViewHolder {
		        DynamicHeightTextView txtLineOne;
		      //  Button btnGo;
		    }
		}