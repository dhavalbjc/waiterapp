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

class ReorderItemAdapter extends ArrayAdapter {
	 
    private Context mContext;
    private ArrayList<Item> mList;
    private int index;
 
    public ReorderItemAdapter(Context context, ArrayList<Item> list,int index) {
        super(context, R.layout.reorder_item_view, list);
 
        mContext = context;
        mList = list;
        this.index=index;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
 
        View view;
 
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.reorder_item_view, null);
        }
        else {
            view = convertView;
        }
 
        TextView contactNameView = (TextView) view.findViewById(R.id.tv_reorder_itemname);
        RelativeLayout uparrow=(RelativeLayout)view.findViewById(R.id.Rel_reorder_item_up_arrow);
	    RelativeLayout downarrow=(RelativeLayout)view.findViewById(R.id.Rel_reorder_item_down_arrow);
 
        contactNameView.setText( mList.get(position).get_name() );
       
        uparrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try
				{
			        
			        if(position>0)
			        {
			        	
			        	//Item c=DBAdapter.getItemData(mList.get(position).get_name());
			        	int myid=mList.get(position).get_id();
			        	int myseq=mList.get(position).get_sequence();
			        	//Item c=DBAdapter.getItemData(myid);
			        	//long myseq=c.get_sequence();
			        	
			        	int upid=mList.get(position-1).get_id();
			        	//Item fc=DBAdapter.getItemData(upid);
			        	//long upseq=fc.get_sequence();
			        	// c=DBAdapter.getCategoryData(mList.get(position-1).get_name());
			        	int upseq=mList.get(position-1).get_sequence();
			        	
			        //	Toast.makeText(mContext,String.valueOf(myid)+" and "+String.valueOf(myseq) , Toast.LENGTH_LONG).show();
			        //	Toast.makeText(mContext,String.valueOf(upid)+" and "+String.valueOf(upseq) , Toast.LENGTH_LONG).show();
			        	
			        	DBAdapter.addItemSeq(myid, upseq);
			        	DBAdapter.addItemSeq(upid, myseq);
			        	mList.get(position).set_sequence(upseq);
			        	mList.get(position-1).set_sequence(myseq);
			        	Collections.sort(mList,new myItemComparable());
			        //	Toast.makeText(mContext,String.valueOf(mList.get(position).get_category().get_id())+" and "+String.valueOf(mList.get(position).get_category().get_name()) , Toast.LENGTH_LONG).show();
				        int gid=mList.get(position).get_category().get_id();
			     // int _index= GamesFragment.ExpListItems.indexOf(mList.get(position).get_category());
			  	//Toast.makeText(mContext,String.valueOf(_index)+" and "+String.valueOf(upseq) , Toast.LENGTH_LONG).show();
				        ArrayList<Item> _items=DBAdapter.getItemsbyCatId(gid);
				        try{
				        for(Item x:_items)
			        	{
			        		x.set_category(mList.get(position).get_category());
			        		try{
			        		ArrayList<Extra> _extra=DBAdapter.getExtrasbyItemId(x.get_id());
			        		x.set_extra(_extra);
			        		for(Extra y:_extra)
			        		{
			        			y.set_item(x);
			        		}
			        		}
			        		catch(Exception e){}
			        	}
			        	//a1.set_items(_items);
				        }
			        	catch(Exception e)
			        	{
			        		//Toast.makeText(container.getContext(), e.toString(),Toast.LENGTH_SHORT).show();
			        	}
			       GamesFragment.ExpListItems.get(index).set_items(_items);
			      // Collections.sort(GamesFragment.ExpListItems.get(index).get_items(),new myItemComparable());
				        ExpandListAdapter.itemadapter.notifyDataSetChanged();
				       
			        	GamesFragment.ExpAdapter.notifyDataSetChanged();		
			        	try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Item");
						        map.put("field", "SeqCombo");
						        map.put("id1",String.valueOf(myid));
						        map.put("seq1",String.valueOf(upseq));
						        map.put("id2",String.valueOf(upid));
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
						        map.put("table", "Item");
						        map.put("field", "Seq");
						        map.put("id",String.valueOf(upid));
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
			        	int myid=mList.get(position).get_id();
			        	int myseq=mList.get(position).get_sequence();
			        
			        	int upid=mList.get(position+1).get_id();			        	
			        	int upseq=mList.get(position+1).get_sequence();
			        	
			        	
			        	DBAdapter.addItemSeq(myid, upseq);
			        	DBAdapter.addItemSeq(upid, myseq);
			        	mList.get(position).set_sequence(upseq);
			        	mList.get(position+1).set_sequence(myseq);
			        	Collections.sort(mList,new myItemComparable());
			        	 int gid=mList.get(position).get_category().get_id();
			        	 GamesFragment.ExpListItems.get(index).set_items(DBAdapter.getItemsbyCatId(gid));
				        ExpandListAdapter.itemadapter.notifyDataSetChanged();
				        
			        	GamesFragment.ExpAdapter.notifyDataSetChanged();
			        	try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Item");
						        map.put("field", "SeqCombo");
						        map.put("id1",String.valueOf(myid));
						        map.put("seq1",String.valueOf(upseq));
						        map.put("id2",String.valueOf(upid));
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
						        map.put("table", "Item");
						        map.put("field", "Seq");
						        map.put("id",String.valueOf(myid));
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
						        map.put("table", "Item");
						        map.put("field", "Seq");
						        map.put("id",String.valueOf(upid));
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
