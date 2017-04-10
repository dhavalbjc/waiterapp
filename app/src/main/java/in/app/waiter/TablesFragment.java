package in.app.waiter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TablesFragment  extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener  {
	 public static ArrayList<Category> ExpList= new ArrayList<Category>();
	 public  ArrayList<ModelCart> mCart=new ArrayList<ModelCart>();
	 LocalBroadcastManager mgr;
	 List<Category> cat;
	 private StaggeredGridView mGridView;
	 private boolean mHasRequestedMore;
	 private tablesAdapter mAdapter;
private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			MainActivity.aController.acquireWakeLock(context);
			// Waking up mobile if it is sleeping
			mCart.clear();

			mCart.addAll(DBAdapter.getAllCartData());

			//String newMessage = intent.getExtras().getString(Config.EXTRA_MESSAGE);
			/*Toast.makeText(context,
					"Got Message: " + newMessage,
					Toast.LENGTH_LONG).show();	*/
			//onLoadMoreItems(cart);

			mAdapter.notifyDataSetChanged();
			//needToRefresh=true;
			//gridView.notifyAll();
			//gridView.getAdapter().notify();
			//gridView.setAdapter(mAdapter);
			//gridView.getAdapter().notifyAll();
			//gridView.refreshDrawableState();
			MainActivity.aController.releaseWakeLock();
		}
	};
	 private ArrayList<String> mData;

	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		//aController = (Controller) getApplicationContext();
		final View rootView = inflater.inflate(R.layout.activity_sgv, container, false);

		return rootView;
	}

	 @Override
     public void onActivityCreated(final Bundle savedInstanceState) {
         super.onActivityCreated(savedInstanceState);

       //  mGridView = (StaggeredGridView) getView().findViewById(R.id.grid_view);

         mGridView = (StaggeredGridView) getView().findViewById(R.id.staggeredGridView1);
         if (savedInstanceState == null) {
	         final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

	         View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
	         View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
	         TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
	         TextView txtFooterTitle = (TextView) footer.findViewById(R.id.txt_title);
	         txtHeaderTitle.setText("THE HEADER!");
	         txtFooterTitle.setText("THE FOOTER!");

	        // mGridView.addHeaderView(header);
	        // mGridView.addFooterView(footer);
	     }
         getActivity().runOnUiThread(new Runnable() {

	            @Override
	            public void run() {
		 mCart=DBAdapter.getAllCartData();
		 try{
	        	//Cursor c=DBAdapter.fetchGroup();
	         cat=DBAdapter.getAllCategoryData();
	         Collections.sort(cat,new myCateComparable());

		        ExpList= (ArrayList<Category>) cat;
	      //  Toast.makeText(getBaseContext(), "ok",Toast.LENGTH_SHORT).show();
	        }
	        catch(Exception e)
	        {
	        	Toast.makeText(getActivity(), e.toString(),Toast.LENGTH_LONG).show();
	        }
			//aController = (Controller) container.getContext();
			// Register custom Broadcast receiver to show messages on activity
			mgr = LocalBroadcastManager.getInstance(getActivity());
			mgr.registerReceiver(mHandleMessageReceiver, new IntentFilter(Config.CART_UPDATE));


	     if (mAdapter == null) {
	         mAdapter = new tablesAdapter(getActivity(),mCart, R.id.txt_line1);
	     }
	     else
	     {

	    	 mAdapter.clear();
	    	 mAdapter.notifyDataSetChanged();
	    	 mGridView.setAdapter(null);

	     }

	     if (mData == null) {
	         mData = SampleData.generateSampleData();
	     }

	     for (String data : mData) {
	         mAdapter.add(data);
	     }

	     mGridView.setAdapter(mAdapter);
	            }});
	     mGridView.setOnScrollListener(this);
	     mGridView.setOnItemClickListener(this);

     }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent mIntent=new Intent(getActivity(),newOrderActivity.class);
		for (ModelCart cart : DBAdapter.getAllCartData()) {
        	if(cart.getTableName().equals(String.valueOf(arg2+1))){
        		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		Bundle mBundle = new Bundle();
        		mBundle.putString("ID", String.valueOf(cart.getId()));
        		mIntent.putExtras(mBundle);
        	}
		}
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//Bundle mBundle = new Bundle();
		//mBundle.putString("ID", String.valueOf(m.getId()));
		//mIntent.putExtras(mBundle);
		newOrderActivity.tableName=String.valueOf(arg2+1);
		getActivity().startActivity(mIntent);

	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public void onPause() {
		try {
			// Unregister Broadcast Receiver
		//	mgr.unregisterReceiver(mHandleMessageReceiver);
			
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onPause();
	}

	@Override
	public void onStop() {
		try {
			// Unregister Broadcast Receiver
		//	mgr.unregisterReceiver(mHandleMessageReceiver);
			
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onStop();
	}
	
	
	@Override
	public void onDestroy() {
		try {
			// Unregister Broadcast Receiver
			mgr.unregisterReceiver(mHandleMessageReceiver);
			
			
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

}
