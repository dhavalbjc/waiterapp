package in.app.waiter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddstaffActivity extends Activity {
	String popUpContents[];
    PopupWindow popupWindowDogs;
    LinearLayout buttonShowDropDown;
    Button addstaffbtn;
    TextView roll;

	    public final static boolean isValidEmail(CharSequence target) {
		    if (target == null) {
		        return false;
		    } else {
		        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		    }
		}

	    private static Account getAccount(AccountManager accountManager) {
		    Account[] accounts = accountManager.getAccountsByType("com.google");
		    Account account;
		    if (accounts.length > 0) {
		        account = accounts[0];
		    } else {
		        account = null;
		    } return account;
		}
	 
	    public  static String getEmail(Context context) {
			    AccountManager accountManager = AccountManager.get(context);
			    Account account = getAccount(accountManager);
			    if (account == null) {
			        return null;
			    } else {
			        return account.name;
			    }
			}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addstaff);

		final EditText name=(EditText) findViewById(R.id.et_staff_Name);
		final EditText email=(EditText) findViewById(R.id.et_staffemail);
		addstaffbtn=(Button) findViewById(R.id.staffbtnAdd);
	    roll=(TextView) findViewById(R.id.tv_roll);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F92D2")));
	    getActionBar().setTitle("Add Staff Member");
	    final String MyEmail=getEmail(getApplicationContext());
	    addstaffbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				 String emailid = email.getText().toString().trim();
				 String sName = name.getText().toString();
				 if(isValidEmail(emailid)){
					 if(!MyEmail.toString().trim().equals(emailid)){
				 if(!emailid.trim().equals("") && !sName.trim().equals("")){
					 email.setText("");
					 name.setText("");
				    Staff newStaff= new Staff();
				    newStaff.set_staff_fname(sName);
				    newStaff.set_staff_email(emailid);
				    newStaff.set_staff_roll(roll.getText().toString());


					int d=DBAdapter.addStaffData(newStaff);
					newStaff.set_staff_id(d);

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
				        map.put("roll",roll.getText().toString());
				        map.put("rid", String.valueOf(DBAdapter.getRestaurantId()));
				       wordList.add(map);
				        Gson gson = new GsonBuilder().create();
					 crudonserver crudop=new crudonserver();
					 crudop.callserverforcrudopern(gson.toJson(wordList),getApplicationContext());

				 }catch(Exception e)
				 {
					// dialog.cancel();
					// error=String.valueOf(e.getMessage());
					 //Toast.makeText(context,Toast.LENGTH_LONG).show();
				 }
				Intent intent11 = new Intent(getApplicationContext(), MainActivity.class);
 		    	intent11.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 		    	Bundle mBundle1 = new Bundle();
 				mBundle1.putString("ID", "4");
 				intent11.putExtras(mBundle1);
 		    	startActivity(intent11);
 		    		        	finish();
				 }
				 else{
					 Toast.makeText(getApplicationContext(), "Enter Name ", Toast.LENGTH_LONG).show();

				 }
			}else{
				Toast.makeText(getApplicationContext(), "This email already registered in server", Toast.LENGTH_LONG).show();

			}

			}

			 else{
			 Toast.makeText(getApplicationContext(), "Enter valid email Id", Toast.LENGTH_LONG).show();

			 }

			}
		});
		List<String> dogsList = new ArrayList<String>();
        dogsList.add("barman::1");
        dogsList.add("cashier::2");
        dogsList.add("cook::3");
        dogsList.add("manager::4");
        dogsList.add("owner::5");
        dogsList.add("waiter::6");
        dogsList.add("admin::7");

        // convert to simple array
        popUpContents = new String[dogsList.size()];
        dogsList.toArray(popUpContents);


        // initialize pop up window
        popupWindowDogs = popupWindowDogs();


        // button on click listener




        // our button
        buttonShowDropDown = (LinearLayout) findViewById(R.id.linearroll);
        buttonShowDropDown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getWindow().setSoftInputMode(
					    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
					);
				//

				popupWindowDogs.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
				//popupWindowDogs.showAsDropDown(v, -5, 0);
				//popupWindowDogs.setFocusable(true);
				popupWindowDogs.showAsDropDown(v,  Gravity.FILL, 0);
			}
		});


	}

	 public PopupWindow popupWindowDogs() {

	        // initialize a pop up window type
	        PopupWindow popupWindow = new PopupWindow(this);

	        // the drop down list is a list view
	        ListView listViewDogs = new ListView(this);

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

			       roll.setText(selectedItemText);

			        // get the id

			       // Toast.makeText(this, "Dog ID is: " + selectedItemTag, Toast.LENGTH_SHORT).show();


				}
			});

	        // some other visual settings
	        Display display = this.getWindowManager().getDefaultDisplay();
		     Point screenSize = new Point();
		     display.getSize(screenSize);
		     int width = screenSize.x;
	        popupWindow.setFocusable(true);
	        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		    // layoutParams.copyFrom(dialog.getWindow().getAttributes());
		     layoutParams.width = (int) (width - (width * 0.05) );
	        popupWindow.setWidth(layoutParams.width);
	       // popupWindow.(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
	        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
	        //popupWindow.showAtLocation(new View(this), Gravity.CENTER_VERTICAL, 10, 10);
	        // set the list view as pop up window content
	       // popupWindow.update(10,10, layoutParams.width,WindowManager.LayoutParams.WRAP_CONTENT);

	       // WindowManager LayoutParams parm1=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	       // parm1.setMargins(20, 20, 0, 0);
	       // layout.setLayoutParams(parm1);
	        //ratePw.addView(layout,parm1);  // removed it
	       // ratePw.setContentView(layout);

	        popupWindow.setContentView(listViewDogs);

	        return popupWindow;
	    }

	    /*
	     * adapter where the list values will be set
	     */
	    private ArrayAdapter<String> dogsAdapter(String dogsArray[]) {

	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogsArray) {

	            @Override
	            public View getView(int position, View convertView, ViewGroup parent) {

	                // setting the ID and text for every items in the list
	                String item = getItem(position);
	                String[] itemArr = item.split("::");
	                String text = itemArr[0];
	                String id = itemArr[1];

	                // visual settings for the list item
	                TextView listItem = new TextView(getApplicationContext());

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

	    @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
	    	getMenuInflater().inflate(R.menu.addstaff, menu);
			
			return true;
		}
		public boolean onOptionsItemSelected(MenuItem item) {

		    // some variable statements...

		    switch (item.getItemId()) {
		        case android.R.id.home:
		        	
		        	Intent intent = new Intent(this, MainActivity.class);  
		        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        	Bundle mBundle = new Bundle();
		        	mBundle.putString("ID", "4");
		        	intent.putExtras(mBundle);
		        	startActivity(intent);
		        	finish();
		            return true;	               

		    }
		    return super.onOptionsItemSelected(item);
		}
		
}
