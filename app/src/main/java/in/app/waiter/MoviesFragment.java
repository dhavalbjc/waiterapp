package in.app.waiter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;

import java.util.ArrayList;

public class MoviesFragment extends Fragment implements
AbsListView.OnScrollListener, AbsListView.OnItemClickListener{

    ArrayList<ModelCart> mData = null;
	private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private MyAdapter adpter;

    //private SampleAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	/*	View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
		if(adpter==null)
		{
			adpter=new MyAdapter(getActivity(), DBAdapter.getAllCartData());
			
		}*/
		final View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
		
		//mCart=DBAdapter.getAllCartData();
		mData=DBAdapter.getAllCartData();
		if(adpter==null)
		{
			adpter=new MyAdapter(getActivity(), mData,450);
			
		}
		mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
		
		return rootView;
	}

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // mGridView = (StaggeredGridView) getView().findViewById(R.id.grid_view);
       // mGridView = (StaggeredGridView) rootView.findViewById(R.id.grid_view);
		//gridview.setAlpha(alpha);
try{
ArrayList<ModelCart> cart=DBAdapter.getAllCartData();

mGridView.setAdapter(adpter);
mGridView.setOnScrollListener(this);
mGridView.setOnItemClickListener(this);
}
catch(Exception e)
{
	Toast.makeText(getActivity(), String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
}

    }
   

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		 Toast.makeText(getActivity(), "On Scroll",Toast.LENGTH_LONG).show();
		  mGridView.setAdapter(adpter);
		  mGridView.setOnScrollListener(this);
		  mGridView.setOnItemClickListener(this);
	}

	
	  @Override
      public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
         
          // our handling
          if (!mHasRequestedMore) {
              int lastInScreen = firstVisibleItem + visibleItemCount;
              if (lastInScreen >= totalItemCount) {
                 // Log.d(TAG, "onScroll lastInScreen - so load more");
                  mHasRequestedMore = true;
                  onLoadMoreItems();
                  
              }
          }
          Toast.makeText(getActivity(), "On Scroll",Toast.LENGTH_LONG).show();
		  //mGridView.setAdapter(adpter);
		  //mGridView.setOnScrollListener(this);
		  //mGridView.setOnItemClickListener(this);
      }
	  private void onLoadMoreItems() {
	      //  final ArrayList<String> sampleData = SampleData.generateSampleData();
	      
	        
			// stash all the data in our backing store
		  mData.clear();
	        mData.addAll(DBAdapter.getAllCartData());
	        // notify the adapter that we can update now
	        adpter.notifyDataSetChanged();
	       mHasRequestedMore = false;
	    }
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//super.onScrollChanged(scrollState);  
		// TODO Auto-generated method stub
		
	}


}
