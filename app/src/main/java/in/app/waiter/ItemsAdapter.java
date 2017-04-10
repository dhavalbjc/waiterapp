package in.app.waiter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;


 public class ItemsAdapter extends BaseAdapter implements Filterable {
	     
	        Context _context;
	        ArrayList<Item> games;
	      ArrayList<Item> orig;
	      private GameFilter filter;
	   
	        public ItemsAdapter(Context context, ArrayList<Item> _games) {
	            _context = context;
	            games = _games;
	            orig = games;
	           filter = new GameFilter();
	       }
	    
	       @Override
	       public int getCount() {
	           if (games != null)
	               return games.size();
	           else
	               return 0;
	       }
	    
	       @Override
	       public Object getItem(int arg0) {
	          return games.get(arg0);
	       }
	    
	       @Override
	       public long getItemId(int arg0) {
	           return games.get(arg0).get_id();
	       }
	      
	      @Override
	       public View getView(int arg0, View arg1, ViewGroup arg2) {
	        /*   GameView gv;
	           if (arg1 == null)
	               gv = new GameView(_context, games.get(arg0));
	           else {
	               gv = (GameView) arg1;
	               gv.setID(games.get(arg0).getID());
	               gv.setName(games.get(arg0).getName());
	           }*/
	           return null ;
	       }

	       @Override
	       public Filter getFilter() {
	          return filter;
	      }
	      
	      private class GameFilter extends Filter {
	   
	          public GameFilter() {
	              
	          }
	          @Override
	          protected FilterResults performFiltering(CharSequence constraint) {
	                FilterResults oReturn = new FilterResults();
	                ArrayList<Item> results = new ArrayList<Item>();
	                if (orig == null)
	                    orig = games;
	                
	                if (constraint != null)
	                {
	                    if (orig != null && orig.size() > 0) {
	                       /* for (Item g   orig) {
	                            if (g.getName().contains(constraint))
	                                results.add(g);
	                        }*/
	                    }
	                    oReturn.values = results;
	                }
	                return oReturn;
	            }
	     
	            @SuppressWarnings("unchecked")
	            @Override
	            protected void publishResults(CharSequence constraint, FilterResults results) {
	                games = (ArrayList<Item>)results.values;
	                notifyDataSetChanged();
	            }
	        }
	     
	    }