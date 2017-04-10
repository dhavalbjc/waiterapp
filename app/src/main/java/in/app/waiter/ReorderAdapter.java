
package in.app.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class ReorderAdapter extends ArrayAdapter {
	 
    private Context mContext;
    private ArrayList<Category> mList;
 
    public ReorderAdapter(Context context, ArrayList<Category> list) {
        super(context, R.layout.reorder_category_item, list);
 
        mContext = context;
        mList = list;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        View view;
 
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.reorder_category_item, null);
        }
        else {
            view = convertView;
        }
 
        TextView contactNameView = (TextView) view.findViewById(R.id.tv_reorder_catname);
        RelativeLayout uparrow=(RelativeLayout)view.findViewById(R.id.Rel_reorder_up_arrow);
	    RelativeLayout downarrow=(RelativeLayout)view.findViewById(R.id.Rel_reorder_down_arrow);
 
        contactNameView.setText( mList.get(position).get_name() );
       
        uparrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try
				{
			        
			        if(position>0)
			        {
			        	
			        	/*Category c=DBAdapter.getCategoryData(mList.get(position).get_name());
			        	long myid=c.get_id();
			        	long myseq=c.get_sequence();
			        	//long myseq=mList.get(position).get_sequence();
			        	
			        	 c=DBAdapter.getCategoryData(mList.get(position-1).get_name());
			        	long upseq=c.get_sequence();
			        	long upid=c.get_id();
			        //	Toast.makeText(mContext,String.valueOf(myid)+" and "+String.valueOf(myseq) , Toast.LENGTH_LONG).show();
			        //	Toast.makeText(mContext,String.valueOf(upid)+" and "+String.valueOf(upseq) , Toast.LENGTH_LONG).show();
			        	*/
			        	int myid=mList.get(position).get_id();
			        	int myseq=mList.get(position).get_sequence();
			        	//long myseq=mList.get(position).get_sequence();
			        	
			        	// c=DBAdapter.getCategoryData(mList.get(position+1).get_name());
			        	int upseq=mList.get(position-1).get_sequence();
			        	int upid=mList.get(position-1).get_id();
			        	DBAdapter.addCatSeq(myid, upseq);
			        	DBAdapter.addCatSeq(upid, myseq);
			        	int i=GamesFragment.ExpListItems.indexOf(mList.get(position));
			        	int i1=GamesFragment.ExpListItems.indexOf(mList.get(position-1));
			        	Toast.makeText(mContext,String.valueOf(myid)+" and "+mList.get(position).get_name() , Toast.LENGTH_LONG).show();
			        //	Toast.makeText(mContext,String.valueOf(i1)+" and "+mList.get(position-1).get_name()  , Toast.LENGTH_LONG).show();
			        	//GamesFragment.ExpListItems.remove(i);
			        	//GamesFragment.ExpListItems.remove(i1);
			        	//List<Category> cats=GamesFragment.ExpListItems;
			        	GamesFragment.ExpListItems.get(i).set_sequence(upseq);
			        	GamesFragment.ExpListItems.get(i1).set_sequence(myseq);
			        	Collections.sort(GamesFragment.ExpListItems,new myCateComparable());
			        //	GamesFragment.ExpListItems.set(i, mList.get(position-1));
			        	//GamesFragment.ExpListItems.set(i1, mList.get(position));
			        	
			        //	Toast.makeText(mContext,String.valueOf(i)+" and "+GamesFragment.ExpListItems.get(i).get_name() , Toast.LENGTH_LONG).show();
			      //  	Toast.makeText(mContext,String.valueOf(i1)+" and "+GamesFragment.ExpListItems.get(i1).get_name()  , Toast.LENGTH_LONG).show();
			        	
			        	//mList.set(position, mList.get(position-1));
			        	//mList.set(position-1, mList.get(position));
			        	//GamesFragment.ExpListItems.
			        	//GamesFragment.ExpListItems.
				        ExpandListAdapter.adapter.notifyDataSetChanged();
			        	GamesFragment.ExpAdapter.notifyDataSetChanged();
			        	try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Category");
						        map.put("field", "SeqCombo");
						        map.put("cid1",String.valueOf(myid));
						        map.put("seq1",String.valueOf(upseq));
						        map.put("cid2",String.valueOf(upid));
						        map.put("seq2",String.valueOf(myseq));
						        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
						       wordList.add(map);
						        Gson gson = new GsonBuilder().create();
							 crudonserver crudop=new crudonserver();
							 crudop.callserverforcrudopern(gson.toJson(wordList),getContext());
							 
						 }catch(Exception e)
						 {
							// error=String.valueOf(e.getMessage());
							 //Toast.makeText(context,Toast.LENGTH_LONG).show();
						 }
			        /*	try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Category");
						        map.put("field", "Seq");
						        map.put("cid",String.valueOf(upid));
						        map.put("seq",String.valueOf(myseq));
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
				}
				catch(Exception e)
				{
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
        downarrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//mList.
				try
				{
			        
			        if(position<mList.size()-1)
			        {
			        	
			        //	Category c=DBAdapter.getCategoryData(mList.get(position).get_name());
			        	int myid=mList.get(position).get_id();
			        	int myseq=mList.get(position).get_sequence();
			        	//long myseq=mList.get(position).get_sequence();
			        	
			        	// c=DBAdapter.getCategoryData(mList.get(position+1).get_name());
			        	int upseq=mList.get(position+1).get_sequence();
			        	int upid=mList.get(position+1).get_id();
			         	DBAdapter.addCatSeq(myid, upseq);
			        	DBAdapter.addCatSeq(upid, myseq);
			        	int i=GamesFragment.ExpListItems.indexOf(mList.get(position));
			        	int i1=GamesFragment.ExpListItems.indexOf(mList.get(position+1));
			     
			        	GamesFragment.ExpListItems.get(i).set_sequence(upseq);
			        	GamesFragment.ExpListItems.get(i1).set_sequence(myseq);
			        	Collections.sort(GamesFragment.ExpListItems,new myCateComparable());
			      
				        ExpandListAdapter.adapter.notifyDataSetChanged();
			        	GamesFragment.ExpAdapter.notifyDataSetChanged();
			        	try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Category");
						        map.put("field", "SeqCombo");
						        map.put("cid1",String.valueOf(myid));
						        map.put("seq1",String.valueOf(upseq));
						        map.put("cid2",String.valueOf(upid));
						        map.put("seq2",String.valueOf(myseq));
						        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
						       wordList.add(map);
						        Gson gson = new GsonBuilder().create();
							 crudonserver crudop=new crudonserver();
							 crudop.callserverforcrudopern(gson.toJson(wordList),getContext());
							 
						 }catch(Exception e)
						 {
							// error=String.valueOf(e.getMessage());
							 //Toast.makeText(context,Toast.LENGTH_LONG).show();
						 }
			        	/*try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Category");
						        map.put("field", "Seq");
						        map.put("cid",String.valueOf(myid));
						        map.put("seq",String.valueOf(upseq));
						        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
						       wordList.add(map);
						        Gson gson = new GsonBuilder().create();
							 crudonserver crudop=new crudonserver();
							 crudop.callserverforcrudopern(gson.toJson(wordList));
							 
						 }catch(Exception e)
						 {
							// error=String.valueOf(e.getMessage());
							 //Toast.makeText(context,Toast.LENGTH_LONG).show();
						 }
			        	try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Category");
						        map.put("field", "Seq");
						        map.put("cid",String.valueOf(upid));
						        map.put("seq",String.valueOf(myseq));
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
				}
				catch(Exception e)
				{
					Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
				}
				
			}
		});
        //phoneNumberView.setText( mList.get(position).getNumber() );
 
        return view;
    }
}