package in.app.waiter;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;


public class StaffFragment extends Fragment {

	   
    private StaffArrayAdapter cardArrayAdapter;
    private ListView listView;

	 public final static boolean isValidEmail(CharSequence target) {
		    if (target == null) {
		        return false;
		    } else {
		        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		    }
		}
	 
    //private SampleAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

	/*	View rootView = inflater.inflate(R.layout.fragment_movies, container, false);
		if(adpter==null)
		{
			adpter=new MyAdapter(getActivity(), DBAdapter.getAllCartData());

		}*/
		final View rootView = inflater.inflate(R.layout.add_staff_activity, container, false);
		listView = (ListView) rootView.findViewById(R.id.card_listView);
		TextView emptyView=(TextView) rootView.findViewById(R.id.emptytext);
		final ArrayList<Staff> staffs=(ArrayList<Staff>) DBAdapter.getAllStaff();
		cardArrayAdapter = new StaffArrayAdapter(container.getContext(), staffs);
		//cardArrayAdapter.addAll(DBAdapter.getAllStaff());
	/*	for (Staff staff : DBAdapter.getAllStaff()) {
			cardArrayAdapter.add(staff);
		}*/
		//cardArrayAdapter.add(new Staff("Bijal","bijal@yahoo.com"));
				//cardArrayAdapter.add(new Staff("Manan","manan@yahoo.com"));
		        listView.setAdapter(cardArrayAdapter);
				listView.setEmptyView(emptyView);
				listView.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> adapter, View v, final int position,
							long id) {
						try{
							final Staff smember=(Staff)adapter.getItemAtPosition(position);
							if(!smember.get_staff_roll().equals("admin")){
								if(!Main.getEmail(getActivity()).equals(smember.get_staff_email())){


							final Dialog dialog = new Dialog(getActivity());
							 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							 dialog.setContentView(R.layout.staff_delete);
							 RelativeLayout Deletebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_staff_delete);

							 Deletebtn.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View arg0) {

									 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			  					        builder.setTitle("Remove staff member "+smember.get_staff_fname() +"?");
			  					        builder.setMessage("Do you want to remove staff member "+smember.get_staff_fname() +"??");
			  					        builder.setPositiveButton("Remove",
			  					                new DialogInterface.OnClickListener() {

			  					                    @Override
			  					                    public void onClick(DialogInterface dialog, int which) {
			  					                   // TODO Auto-generated method stub
			  											DBAdapter.deleteStaff(smember.get_staff_id());
			  											staffs.remove(position);
			  											//cardArrayAdapter.remove(smember);
			  											cardArrayAdapter.notifyDataSetChanged();
			  											try
			  											 {
			  													 ArrayList<HashMap<String, String>> wordList;
			  											        wordList = new ArrayList<HashMap<String, String>>();
			  											        HashMap<String, String> map = new HashMap<String, String>();
			  											        map.put("oprn","Delete");
			  											        map.put("table", "Staff");
			  											       // map.put("field", "role");
			  											        map.put("email", smember.get_staff_email());
			  											        map.put("id",String.valueOf(smember.get_staff_id()));
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
			  											dialog.cancel();
			  				    						//mAdapter.notifyDataSetChanged();
			  											dialog.dismiss();
			  					                        }
			  					                });

			  					        builder.setNegativeButton("No",
			  					                new DialogInterface.OnClickListener() {

			  					                    @Override
			  					                    public void onClick(DialogInterface dialog, int which) {
			  					                        // TODO Auto-generated method stub
			  					                        dialog.dismiss();

			  					                    }
			  					                });

			  					        AlertDialog alert = builder.create();
			  					       // if(myCartHash.size()>0){
			  					        alert.show();
			  					      dialog.cancel();
								}
							});
							 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
							 dialog.show();
								}
								else{
									Toast.makeText(getActivity(), "You are not able to delete Your account",Toast.LENGTH_LONG).show();
								}
							}
							else{
								Toast.makeText(getActivity(), "You are not able to delete admin",Toast.LENGTH_LONG).show();
							}
						}catch(Exception e)
						{
							Toast.makeText(getActivity(), String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
						}
						return false;
					}
				});

        Button addnewitem=(Button) rootView.findViewById(R.id.lin2);
		 addnewitem.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent addstaffactivity = new Intent(getActivity(), AddstaffActivity.class);
					addstaffactivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

	 		    	startActivity(addstaffactivity);
	 		    		        	//finish();
					final Dialog dialog = new Dialog(getActivity());


					 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					 dialog.setContentView(R.layout.add_staff);
					 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					// TextView pricename=(TextView) dialog.findViewById(R.id.tv_item_rename);
					 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_staff_btn);
					 final RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_staff_btn);
					 final EditText email=(EditText) dialog.findViewById(R.id.et_staffemail);
					 final EditText name=(EditText) dialog.findViewById(R.id.et_staffname);

					cancelbtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});


					savebtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							 String emailid = email.getText().toString();
							 String sName = name.getText().toString();
							 if(isValidEmail(emailid)){
							 if(!emailid.trim().equals("") && !sName.trim().equals("")){
								 email.setText("");
								 name.setText("");
							    Staff newStaff= new Staff();
							    newStaff.set_staff_fname(sName);
							    newStaff.set_staff_email(emailid);
							    newStaff.set_staff_roll("Waiter");


								int d=DBAdapter.addStaffData(newStaff);
								newStaff.set_staff_id(d);
								//st=(ArrayList<Staff>) DBAdapter.getAllStaff();
								cardArrayAdapter.notifyDataSetChanged();
								//cardArrayAdapter.add(newStaff);
							//	cardArrayAdapter.notifyDataSetChanged();
								dialog.cancel();
							try
							 {
									 ArrayList<HashMap<String, String>> wordList;
							        wordList = new ArrayList<HashMap<String, String>>();
							        HashMap<String, String> map = new HashMap<String, String>();
							        map.put("oprn","Insert");
							        map.put("table","Staff");
							        map.put("sid",String.valueOf(d));
							        map.put("name",sName);
							        map.put("email",emailid);
							        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
							       wordList.add(map);
							        Gson gson = new GsonBuilder().create();
								 crudonserver crudop=new crudonserver();
								 crudop.callserverforcrudopern(gson.toJson(wordList),getActivity());

							 }catch(Exception e)
							 {
								// dialog.cancel();
								// error=String.valueOf(e.getMessage());
								 //Toast.makeText(context,Toast.LENGTH_LONG).show();
							 }

							 }
							 else{
								 Toast.makeText(getActivity(), "Enter Name ", Toast.LENGTH_LONG).show();

							 }
						}else{
							 Toast.makeText(getActivity(), "Enter valid email Id", Toast.LENGTH_LONG).show();

						}
						}
					});

					 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
				//	 dialog.show();

				}
			});



		return rootView;

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
