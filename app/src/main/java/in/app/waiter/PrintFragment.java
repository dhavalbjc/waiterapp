package in.app.waiter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;


public class PrintFragment extends Fragment {

	Button addPrinter;
	
	
	 private PrinterArrayAdapter cardArrayAdapter;
	    private ListView listView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.main, container, false);
		
		return rootView;
	}
	  public void onActivityCreated(final Bundle savedInstanceState) {
	         super.onActivityCreated(savedInstanceState);

	      
	 		try {
	 			addPrinter = (Button) getView().findViewById(R.id.add_printer);
	 			
	 		} catch (Exception ex) {
	             Log.e("error",ex.getMessage());
	 		}
	 			
	 		 TextView emptyView=(TextView) getView().findViewById(R.id.emptytext);
	 	       //startRunnable();
	 	       
	 		listView = (ListView) getView().findViewById(R.id.printer_listView);
	 		cardArrayAdapter = new PrinterArrayAdapter(getActivity(),DBAdapter.getAllPrinterData());
	 		
	      /* for (Printer printer : DBAdapter.getAllPrinterData()) {
	    	   Printer staff=new Printer();
	    	   staff.set_id(printer.get_id());
        	   staff.set_name(printer.get_name().toString());
        	   staff.set_mac(printer.get_mac().toString());
               cardArrayAdapter.add(staff);
		}*/
	     
	 		        listView.setAdapter(cardArrayAdapter);
	 		       listView.setEmptyView(emptyView);
	 		      listView.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter, View v, int position,
 							long id) {
						try{
	 						final Printer smember=(Printer)adapter.getItemAtPosition(position);
	 						
	 						final Dialog dialog = new Dialog(getActivity());
	 						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 						 dialog.setContentView(R.layout.staff_delete);
	 						 RelativeLayout Deletebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_staff_delete);
	 						
	 						 Deletebtn.setOnClickListener(new View.OnClickListener() {
	 							
	 							@Override
	 							public void onClick(View arg0) {
	 								
	 								 //cardArrayAdapter.clear();
	 								DBAdapter.deletePrinter(smember.get_id());
	 								//cardArrayAdapter.remove(smember);
	 								/* for (Printer printer : DBAdapter.getAllPrinterData()) {
	 							    	   Printer staff=new Printer();
	 							    	   staff.set_id(printer.get_id());
	 						        	   staff.set_name(printer.get_name().toString());
	 						        	   staff.set_mac(printer.get_mac().toString());
	 						               cardArrayAdapter.add(staff);
	 								}*/
	 							     
	 								cardArrayAdapter=null;
	 								
	 								//cardArrayAdapter.addAll(DBAdapter.getAllPrinterData());
	 								cardArrayAdapter = new PrinterArrayAdapter(getActivity(),DBAdapter.getAllPrinterData());
	 								 cardArrayAdapter.notifyDataSetChanged();
	 								 listView.setAdapter(null);
	 								 listView.setAdapter(cardArrayAdapter);
	 								 
	 								try
	 								 {
	 										 ArrayList<HashMap<String, String>> wordList;
	 								        wordList = new ArrayList<HashMap<String, String>>();
	 								        HashMap<String, String> map = new HashMap<String, String>();
	 								        map.put("oprn","Delete");
	 								        map.put("table", "Printer");
	 								        map.put("pid",String.valueOf(smember.get_id()));										       
	 								        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
	 								       wordList.add(map);
	 								        Gson gson = new GsonBuilder().create();
	 									 crudonserver crudop=new crudonserver();
	 									 crudop.callserverforcrudopern(gson.toJson(wordList),getActivity());
	 									 
	 								 }catch(Exception e)
	 								 {
	 									// error=String.valueOf(e.getMessage());
	 									 //Toast.makeText(context,Toast.LENGTH_LONG).show();
	 								 }
	 								//listView.notifyAll();
	 								// listView.setAdapter(cardArrayAdapter);
	 								dialog.cancel();
	 							}
	 						});
	 						 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
	 						 dialog.show();
	 						
	 					}catch(Exception e)
	 					{
	 						Toast.makeText(getActivity(), String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
	 					}
						return true;
					}
				});
	 		     
	 		        listView.setOnItemClickListener(new OnItemClickListener() {
	 					
	 					
	 					@Override
	 					public void onItemClick(AdapterView<?> adapter, View v, int position,
	 							long id) {
	 						final Printer smember=(Printer)adapter.getItemAtPosition(position);
	 						Intent intent1 = new Intent(getActivity(), AddPrinterActivity.class);  
	 			        	intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 			        	Bundle mBundle1 = new Bundle();
	 			        	mBundle1.putString("ID", String.valueOf(smember.get_id()));
	 			        	intent1.putExtras(mBundle1);
	 			        	startActivity(intent1);
	 					}
	 					
	 				});
	 				
	  }
	 @Override
	    public void onStart() {
	    	super.onStart();

			
			addPrinter.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					Intent serverIntent = new Intent(getActivity(),AddPrinterActivity.class);
					startActivity(serverIntent);
					
				}
			});
			
	    }
	    
	
		
		

	    
	    public  int getWidth()
		 {try 
	     {
	         
			 Display display = getActivity().getWindowManager().getDefaultDisplay();
		     Point screenSize = new Point();
		     display.getSize(screenSize);
		     int width = screenSize.x;
			 WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		    // layoutParams.copyFrom(dialog.getWindow().getAttributes());
		     layoutParams.width = (int) (width - (width * 0.05) ); 
		     //layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
			return  layoutParams.width;
	     }
		 catch(Exception e)
		 {
			 //Toast.makeText(getActivity(),String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
			 return  400; 
		 }
		 }
}