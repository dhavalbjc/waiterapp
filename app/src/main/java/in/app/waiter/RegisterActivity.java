package in.app.waiter;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
	
	// UI elements
	EditText txtName; 
	EditText txtEmail;
	
	// Register button
	Button btnRegister;

	 public final static boolean isValidEmail(CharSequence target) {
		    if (target == null) {
		        return false;
		    } else {
		        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		    }
		}

	 static String getEmail(Context context) {
		    AccountManager accountManager = AccountManager.get(context);
		    Account account = getAccount(accountManager);
		    if (account == null) {
		        return null;
		    } else {
		        return account.name;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/******************* Intialize Database *************/
		DBAdapter.init(this);

		setContentView(R.layout.register);
		 getActionBar().setDisplayHomeAsUpEnabled(true);
		//Get Global Controller Class object (see application tag in AndroidManifest.xml)
		final Controller aController = (Controller) getApplicationContext();

		// Check if Internet Connection present
		if (!aController.isConnectingToInternet()) {

			// Internet Connection is not present
			aController.showAlertDialog(RegisterActivity.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);

			// stop executing code by return
			return;
		}
		// Check if GCM configuration is set
		if (Config.YOUR_SERVER_URL == null ||
			Config.GOOGLE_SENDER_ID == null ||
			Config.YOUR_SERVER_URL.length() == 0 ||
			Config.GOOGLE_SENDER_ID.length() == 0)
		{

			// GCM sernder id / server url is missing
			aController.showAlertDialog(RegisterActivity.this,
					"Configuration Error!",
					"Please set your Server URL and GCM Sender ID",
					false);

			// stop executing code by return
			 return;
		}

		txtName = (EditText) findViewById(R.id.txtName);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		txtEmail.setText(getEmail(this));

				// Click event on Register button
	        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		btnRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Get data from EditText
				String name = txtName.getText().toString();
				String email = txtEmail.getText().toString();
				if(isValidEmail(email)){
				// Check if user filled the form
				if(name.trim().length() > 0 && email.trim().length() > 0){

					// Launch Main Activity
					Intent i = new Intent(getApplicationContext(), ServerRegister.class);

					// Registering user on our server
					// Sending registraiton details to MainActivity
					i.putExtra("name", name);
					i.putExtra("email", email);
					startActivity(i);
					finish();

				}else{


					// user doen't filled that data
					aController.showAlertDialog(RegisterActivity.this,
							"Registration Error!",
							"Please enter your details",
							false);
				}
				}
				else{
					aController.showAlertDialog(RegisterActivity.this,
							"Registration Error!",
							"Please enter your details",
							false);
				}
			}
		});
	}

		public boolean onOptionsItemSelected(MenuItem item) {

		    // some variable statements...

		    switch (item.getItemId()) {
		        case android.R.id.home:
		        	
		        	Intent intent = new Intent(this, Main.class);  
		        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        	
		        	startActivity(intent);
		        	finish();
		            return true;	            
		            

		    }
		    return super.onOptionsItemSelected(item);
		}
}
