package in.app.waiter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantFragment extends Fragment {
	String[] celebrities = {
            "Percentage",
            "Fixed",
         
    };
	String popUpContents[];
    PopupWindow popupWindowDogs;
   TextView qtype;
		 View.OnClickListener handler = new View.OnClickListener() {
	            public void onClick(View v) {

	                switch (v.getId()) {

	                case R.id.buttonShowDropDown:
	                    // show the list view as dropdown
	                    popupWindowDogs.showAsDropDown(v, -5, 0);
	                    break;
	                }
	            }
	        };
		 
		 public final static boolean isValidEmail(CharSequence target) {
			    if (target == null) {
			        return false;
			    } else {
			        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
			    }
			}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {


			final View rootView = inflater.inflate(R.layout.restaurant, container, false);
			 List<String> dogsList = new ArrayList<String>();
		        dogsList.add("Percentage::1");
		        dogsList.add("Fixed::2");


		        // convert to simple array
		        popUpContents = new String[dogsList.size()];
		        dogsList.toArray(popUpContents);


		        // initialize pop up window
		        popupWindowDogs = popupWindowDogs();
			final Restaurant restro=DBAdapter.getRestaurant(DBAdapter.getRestaurantId());
			LinearLayout name=(LinearLayout) rootView.findViewById(R.id.restrolinearname);
			final TextView tvname=(TextView) rootView.findViewById(R.id.restrotxtname);
			final TextView tvdescription=(TextView) rootView.findViewById(R.id.restxtdescription);
			final TextView tvphone=(TextView) rootView.findViewById(R.id.restrotxtphone);
			final TextView tvwebsite=(TextView) rootView.findViewById(R.id.restrortxtwebsite);
			final TextView tvemail=(TextView) rootView.findViewById(R.id.restrortxtemail);
			final TextView tvstreet=(TextView) rootView.findViewById(R.id.restrotxtstreet);
			final TextView tvcivic=(TextView) rootView.findViewById(R.id.restrotxtcivic);
			final TextView tvzip=(TextView) rootView.findViewById(R.id.restrotxtzipcode);
			final TextView tvcity=(TextView) rootView.findViewById(R.id.restrotxtcity);
			final TextView tvstate=(TextView) rootView.findViewById(R.id.Restrotxtprovince);
			final TextView tvcountry=(TextView) rootView.findViewById(R.id.restrotxtcountry);
			final TextView tvservice_name=(TextView) rootView.findViewById(R.id.restro_service_name);
			final TextView tvservice_qty=(TextView) rootView.findViewById(R.id.restro_service_qty);
			tvname.setText(restro.get_restro_name());
			tvdescription.setText(restro.get_restro_description());
			tvphone.setText(restro.get_restro_phone());
			tvwebsite.setText(restro.get_restro_website());
			tvemail.setText(restro.get_restro_email());
			tvstreet.setText(restro.get_restro_street());
			tvcivic.setText(restro.get_restro_civicnumber());
			tvzip.setText(restro.get_restro_zipcode());
			tvcity.setText(restro.get_restro_city());
			tvstate.setText(restro.get_restro_state());
			tvcountry.setText(restro.get_restro_country());
			final LinearLayout Rel_group_click=(LinearLayout) rootView.findViewById(R.id.Rel_group_click);
			final LinearLayout Rel_group_click2=(LinearLayout) rootView.findViewById(R.id.Rel_group_click2);
			final LinearLayout Rel_group_click3=(LinearLayout) rootView.findViewById(R.id.Rel_group_click3);
			if(DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_echarge","echarge_category","Service")){
				ECharge echarge=DBAdapter.getEChargeData("Service");
				Rel_group_click.setVisibility(View.GONE);
				tvservice_name.setText(echarge.get_name());
				if(echarge.get_param().equals("Percentage")){
					tvservice_qty.setText(echarge.get_quantity()+"%");
				}else{
					tvservice_qty.setText(echarge.get_quantity());}

				//tvservice_qty.setText(echarge.get_quantity());
				}
			else{
				Rel_group_click2.setVisibility(View.GONE);
				Rel_group_click3.setVisibility(View.GONE);
			}
			Rel_group_click3.setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View arg0) {
					final Dialog dialog = new Dialog(getActivity());
   				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
   				 dialog.setContentView(R.layout.staff_delete);
   				 //TextView tv_invite=(TextView) dialog.findViewById(R.id.tv_invite);
   				 TextView tv_invite_cancel=(TextView) dialog.findViewById(R.id.tv_invite_cancel);
   				 //tv_invite.setText("Order");
   				 tv_invite_cancel.setText("Delete");

   				 RelativeLayout Deletebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_staff_delete);

   				 Deletebtn.setOnClickListener(new View.OnClickListener() {

   					@Override
   					public void onClick(View arg0) {
   						// TODO Auto-generated method stub
   						//DBAdapter.deleteStaff(smember.get_staff_id());
   					int eid=DBAdapter.deleteServiceTax("Service");
   					try
					 {
							 ArrayList<HashMap<String, String>> wordList;
					        wordList = new ArrayList<HashMap<String, String>>();
					        HashMap<String, String> map = new HashMap<String, String>();
					        map.put("oprn","Delete");
					        map.put("table", "Echarge");
					        map.put("eid",String.valueOf(eid));
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
   					Rel_group_click.setVisibility(View.VISIBLE);
   					Rel_group_click2.setVisibility(View.GONE);
   					Rel_group_click3.setVisibility(View.GONE);
   					dialog.dismiss();
   					}
   				});
   				 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);

   				 dialog.show();
					return false;
				}
			});

			Rel_group_click.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					final Dialog dialog = new Dialog(getActivity());
					 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					 dialog.setContentView(R.layout.service_tax);
					// dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_btn);
					 final RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
					 final EditText s_name=(EditText) dialog.findViewById(R.id.Service_Name);
					 final EditText s_qty=(EditText) dialog.findViewById(R.id.service_Quantity);
					final LinearLayout buttonShowDropDown=(LinearLayout) dialog.findViewById(R.id.linearType);

					final LinearLayout linspinner1=(LinearLayout) dialog.findViewById(R.id.linspinner1);
					linspinner1.setVisibility(View.GONE);
					qtype=(TextView) dialog.findViewById(R.id.ettype);
					buttonShowDropDown.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							//dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_HIDDEN);
							dialog.getWindow().setSoftInputMode(
								    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
								);

							popupWindowDogs.showAsDropDown(v, -5, 0);

						}
					});
					/* final Spinner spnr = (Spinner)dialog.findViewById(R.id.spinner1);
					 ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				                getActivity(), android.R.layout.simple_spinner_dropdown_item, celebrities);

				        spnr.setAdapter(adapter);
				        spnr.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								 int position = spnr.getSelectedItemPosition();
			                     //   Toast.makeText(getActivity(),"You have selected "+celebrities[+position],Toast.LENGTH_LONG).show();
			                        // TODO Auto-generated method stub
								// String type=celebrities[+position].toString();

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				       */
						cancelbtn.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

						savebtn.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								try{
								if(!s_name.getText().toString().equals("")){
									if((Double.parseDouble(s_qty.getText().toString())>0) ){
									if(!DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_echarge","echarge_category","Service")){
								ECharge echarge=new ECharge();
								echarge.set_category("Service");
								echarge.set_name(s_name.getText().toString());
								echarge.set_quantity(s_qty.getText().toString());
								echarge.set_param(qtype.getText().toString());
								int eid= DBAdapter.addEChargeData(echarge);
							//	 Toast.makeText(getActivity(),spnr.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
									Rel_group_click2.setVisibility(View.VISIBLE);
									Rel_group_click3.setVisibility(View.VISIBLE);
									Rel_group_click.setVisibility(View.GONE);
									tvservice_name.setText(s_name.getText().toString());
									if(qtype.getText().toString().equals("Percentage")){
										tvservice_qty.setText(s_qty.getText().toString()+"%");
									}else{
										tvservice_qty.setText(s_qty.getText().toString());}
									try
									 {
											 ArrayList<HashMap<String, String>> wordList;
									        wordList = new ArrayList<HashMap<String, String>>();
									        HashMap<String, String> map = new HashMap<String, String>();
									        map.put("oprn","Insert");
									        map.put("table", "Echarge");
									        map.put("eid",String.valueOf(eid));
									        map.put("cat","Service");
									        map.put("name",s_name.getText().toString());
									        map.put("qty",s_qty.getText().toString());
									        map.put("param",qtype.getText().toString());
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

									//tvservice_qty.setText(s_qty.getText().toString());
									dialog.dismiss();
									}
									else{
										int eid=DBAdapter.updateECharge("'Service'", s_name.getText().toString(), s_qty.getText().toString(), qtype.getText().toString());
									//	Toast.makeText(getActivity(), String.valueOf(eid),Toast.LENGTH_LONG).show();
										tvservice_name.setText(s_name.getText().toString());
										//tvservice_qty.setText(s_qty.getText().toString());
										if(qtype.getText().toString().equals("Percentage")){
											tvservice_qty.setText(s_qty.getText().toString()+"%");
										}else{
											tvservice_qty.setText(s_qty.getText().toString());}
										try
										 {
												 ArrayList<HashMap<String, String>> wordList;
										        wordList = new ArrayList<HashMap<String, String>>();
										        HashMap<String, String> map = new HashMap<String, String>();
										        map.put("oprn","Update");
										        map.put("table", "Echarge");
										        map.put("field", "All");
										        map.put("eid",String.valueOf(eid));
										        map.put("name",s_name.getText().toString());
										        map.put("qty",s_qty.getText().toString());
										        map.put("param",qtype.getText().toString());
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

										dialog.dismiss();
									}
									}
									else{
										Toast.makeText(getActivity(),"Enter valid Qty",Toast.LENGTH_LONG).show();
									}
								}else{
									Toast.makeText(getActivity(),"Enter name",Toast.LENGTH_LONG).show();
								}

							}
								catch(Exception e){
									Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
								}
						}

						});

						dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
					 dialog.show();

				}
			});
		Rel_group_click3.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					final Dialog dialog = new Dialog(getActivity());
					 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					 dialog.setContentView(R.layout.service_tax);
					 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
					 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.Rel_Cancel_item_btn);
					 final RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.Rel_add_item_btn);
					 final EditText s_name=(EditText) dialog.findViewById(R.id.Service_Name);
					 final EditText s_qty=(EditText) dialog.findViewById(R.id.service_Quantity);
					 final LinearLayout buttonShowDropDown=(LinearLayout) dialog.findViewById(R.id.linearType);
					 final LinearLayout linspinner1=(LinearLayout) dialog.findViewById(R.id.linspinner1);
						linspinner1.setVisibility(View.GONE);
						qtype=(TextView) dialog.findViewById(R.id.ettype);
						buttonShowDropDown.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								//dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_HIDDEN);
								dialog.getWindow().setSoftInputMode(
									    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
									);
								popupWindowDogs.showAsDropDown(v, -5, 0);

							}
						});
					 ECharge echarge=DBAdapter.getEChargeData("Service");
					s_name.setText(echarge.get_name());
					s_qty.setText(echarge.get_quantity());
					qtype.setText(echarge.get_param());
				/*	 final Spinner spnr = (Spinner)dialog.findViewById(R.id.spinner1);

					 ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				                getActivity(), android.R.layout.simple_spinner_dropdown_item, celebrities);

				        spnr.setAdapter(adapter);
				        if(echarge.get_param().equals("Fixed")){
							 spnr.setSelection(1);
							 }
							 else{
								 spnr.setSelection(0);
							 }
				        spnr.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								 int position = spnr.getSelectedItemPosition();
			                     //   Toast.makeText(getActivity(),"You have selected "+celebrities[+position],Toast.LENGTH_LONG).show();
			                        // TODO Auto-generated method stub
								// String type=celebrities[+position].toString();

							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});
				       */
						cancelbtn.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});

						savebtn.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								try{
								if(!s_name.getText().toString().equals("")){
									if((Double.parseDouble(s_qty.getText().toString())>0) ){
									if(!DBAdapter.CheckIsCatAlreadyInDBorNot("tbl_echarge","echarge_category","Service")){
										ECharge echarge=new ECharge();
										echarge.set_category("Service");
										echarge.set_name(s_name.getText().toString());
										echarge.set_quantity(s_qty.getText().toString());
										echarge.set_param(qtype.getText().toString());
										int eid= DBAdapter.addEChargeData(echarge);
									//	 Toast.makeText(getActivity(),spnr.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
											Rel_group_click2.setVisibility(View.VISIBLE);
											Rel_group_click3.setVisibility(View.VISIBLE);
											Rel_group_click.setVisibility(View.GONE);
											tvservice_name.setText(s_name.getText().toString());
											if(qtype.getText().toString().equals("Percentage")){
												tvservice_qty.setText(s_qty.getText().toString()+"%");
											}else{
												tvservice_qty.setText(s_qty.getText().toString());}
											try
											 {
													 ArrayList<HashMap<String, String>> wordList;
											        wordList = new ArrayList<HashMap<String, String>>();
											        HashMap<String, String> map = new HashMap<String, String>();
											        map.put("oprn","Insert");
											        map.put("table", "Echarge");
											        map.put("eid",String.valueOf(eid));
											        map.put("cat","Service");
											        map.put("name",s_name.getText().toString());
											        map.put("qty",s_qty.getText().toString());
											        map.put("param",qtype.getText().toString());
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

											//tvservice_qty.setText(s_qty.getText().toString());
											dialog.dismiss();
									}
									else{
										int eid=DBAdapter.updateECharge("'Service'", s_name.getText().toString(), s_qty.getText().toString(), qtype.getText().toString());
										//Toast.makeText(getActivity(), String.valueOf(eid),Toast.LENGTH_LONG).show();
										tvservice_name.setText(s_name.getText().toString());
										//tvservice_qty.setText(s_qty.getText().toString());
										if(qtype.getText().toString().equals("Percentage")){
											tvservice_qty.setText(s_qty.getText().toString()+"%");
										}else{
											tvservice_qty.setText(s_qty.getText().toString());}
										try
										 {
												 ArrayList<HashMap<String, String>> wordList;
										        wordList = new ArrayList<HashMap<String, String>>();
										        HashMap<String, String> map = new HashMap<String, String>();
										        map.put("oprn","Update");
										        map.put("table", "Echarge");
										        map.put("field", "All");
										        map.put("eid",String.valueOf(eid));
										        map.put("name",s_name.getText().toString());
										        map.put("qty",s_qty.getText().toString());
										        map.put("param",qtype.getText().toString());
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

										dialog.dismiss();
									}
									}
									else{
										Toast.makeText(getActivity(),"Enter valid Qty",Toast.LENGTH_LONG).show();
									}
								}else{
									Toast.makeText(getActivity(),"Enter name",Toast.LENGTH_LONG).show();
								}
							}catch(Exception e){
								Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
							}

							}
						});
					 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
					 dialog.show();

				}
			});
			name.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {


					String name1 = "Name";
					calldialog(name1,tvname,"restro_name");


				}
			});
			LinearLayout description=(LinearLayout) rootView.findViewById(R.id.restrolineardescription);
			description.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {

					String name1 = "Description";
					calldialog(name1,tvdescription,"restro_description");


				}
			});
			LinearLayout phone=(LinearLayout) rootView.findViewById(R.id.restrolinearphone);
			phone.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {


					String name1 = "Phone";
					calldialog(name1,tvphone,"restro_phone");

					//calldialog(name1);

				}
			});
			LinearLayout website=(LinearLayout) rootView.findViewById(R.id.restrolinearwebsite);
			website.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "Website";

					calldialog(name1,tvwebsite,"restro_website");


				}
			});
			/*LinearLayout email=(LinearLayout) rootView.findViewById(R.id.restrolinearemail);
			email.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "Email";

					calldialog(name1,tvemail,"restro_email");


				}
			});*/
			LinearLayout street=(LinearLayout) rootView.findViewById(R.id.restrolinearstreet);
			street.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "Street";

					calldialog(name1,tvstreet,"restro_street");
				//	calldialog(name1);

				}
			});
			LinearLayout civic=(LinearLayout) rootView.findViewById(R.id.restrolinearcivic);
			civic.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "Civic number";

					calldialog(name1,tvcivic,"restro_civicnumber");


				}
			});
			LinearLayout zipcode=(LinearLayout) rootView.findViewById(R.id.restrolinearzipcode);
			zipcode.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "Zipcode";

					calldialog(name1,tvzip,"restro_zipcode");


				}
			});
			LinearLayout city=(LinearLayout) rootView.findViewById(R.id.restrolinearcity);
			city.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "City";

					calldialog(name1,tvcity,"restro_city");

				}
			});
			LinearLayout province=(LinearLayout) rootView.findViewById(R.id.restrolinearprovince);
			province.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "Province";

					calldialog(name1,tvstate,"restro_state");


				}
			});
			LinearLayout country=(LinearLayout) rootView.findViewById(R.id.restrolinearcountry);
			country.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String name1 = "Country";

					calldialog(name1,tvcountry,"restro_country");

				}
			});
			return rootView;

		}

		 protected void calldialog(final String name1, final TextView tvname, final String dbname) {
				final Dialog dialog = new Dialog(getActivity());
				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				 dialog.setContentView(R.layout.restrodialog);
				 dialog.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				 TextView title=(TextView) dialog.findViewById(R.id.tv_titlefordialog);
				 RelativeLayout cancelbtn=(RelativeLayout) dialog.findViewById(R.id.restroRel_Cancel_btn);
				 RelativeLayout savebtn=(RelativeLayout) dialog.findViewById(R.id.restroRel_save_btn);
				 final EditText value=(EditText) dialog.findViewById(R.id.restrodialog_editname);
				 title.setText(name1);

				//String s="Rename "+group.get_name();
				 value.setText(String.valueOf(tvname.getText()));
				 final Editable et_value=value.getText();
				cancelbtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				savebtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if(!et_value.toString().trim().equals("")|| !name1.equals("Name")){

						tvname.setText(et_value.toString());
						DBAdapter.updateRestro(DBAdapter.getRestaurantId(), dbname, et_value.toString());
						 try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Restaurant");
						        map.put("col", name1);
						        map.put("value",String.valueOf(et_value));
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
						dialog.dismiss();
						value.setText("");

						}
						else{
							Toast.makeText(getActivity(),"Enter "+name1,Toast.LENGTH_LONG).show();
						}
						// TODO Auto-generated method stub
					/*
						group.set_name(price1.toString());
												DBAdapter.updateCatName(group.get_id(), price1.toString());

						GamesFragment.ExpAdapter.notifyDataSetChanged();
						GamesFragment.closeDialogs();
						 try
						 {
								 ArrayList<HashMap<String, String>> wordList;
						        wordList = new ArrayList<HashMap<String, String>>();
						        HashMap<String, String> map = new HashMap<String, String>();
						        map.put("oprn","Update");
						        map.put("table", "Category");
						        map.put("field", "Name");
						        map.put("cid",String.valueOf(group.get_id()));
						        map.put("name",String.valueOf(price1));
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
				});

				 dialog.getWindow().setLayout(getWidth(), LayoutParams.WRAP_CONTENT);
				 dialog.show();


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

	        public PopupWindow popupWindowDogs() {
	   		 
		        // initialize a pop up window type
		        PopupWindow popupWindow = new PopupWindow(getActivity());
		 
		        // the drop down list is a list view
		        ListView listViewDogs = new ListView(getActivity());
		        
		        // set our adapter and pass our pop up window contents
		        listViewDogs.setAdapter(dogsAdapter(popUpContents));
		         
		        // set the item click listener
		        listViewDogs.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int arg2, long arg3) {
						// get the context and main activity to access variables
				        //Context mContext = v.getContext();
				        //Reports_Fragment mainActivity = ((Reports_Fragment) mContext);
				         
				        // add some animation when a list item was clicked
				        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
				        fadeInAnimation.setDuration(10);
				        v.startAnimation(fadeInAnimation);
				         
				        // dismiss the pop up
				        popupWindowDogs.dismiss();
				         
				        // get the text and set it as the button text
				        String selectedItemText = ((TextView) v).getText().toString();
				       qtype.setText(selectedItemText);
				        
				        // get the id
				         //  Toast.makeText(getActivity(), "Dog ID is: " + selectedItemText, Toast.LENGTH_SHORT).show();
				        
						
					}
				});
		 
		        // some other visual settings
		        Display display = getActivity().getWindowManager().getDefaultDisplay();
			     Point screenSize = new Point();
			     display.getSize(screenSize);
			     int width = screenSize.x;
		        popupWindow.setFocusable(true);
		        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		        
			    // layoutParams.copyFrom(dialog.getWindow().getAttributes());
			     layoutParams.width = (int) (width - (width * 0.1) ); 
		        popupWindow.setWidth(layoutParams.width);
		       // popupWindow.(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		        popupWindow.setContentView(listViewDogs);
		 
		        return popupWindow;
		    }
		 
		    /*
		     * adapter where the list values will be set
		     */
		    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {
		 
		        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dogsArray) {
		 
		            @Override
		            public View getView(int position, View convertView, ViewGroup parent) {
		 
		                // setting the ID and text for every items in the list
		                String item = getItem(position);
		                String[] itemArr = item.split("::");
		                String text = itemArr[0];
		                String id = itemArr[1];
		 
		                // visual settings for the list item
		                TextView listItem = new TextView(getActivity());
		 
		                listItem.setText(text);
		                listItem.setTag(id);
		                listItem.setTextSize(18);
		                listItem.setPadding(15, 20, 15, 20);
		                listItem.setTextColor(Color.WHITE);
		                 
		                return listItem;
		            }
		        };
		         
		        return adapter;
		    }
	}
