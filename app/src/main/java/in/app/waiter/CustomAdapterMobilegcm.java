package in.app.waiter;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//Adapter class extends with BaseAdapter and implements with OnClickListener
public class CustomAdapterMobilegcm extends ArrayAdapter<String>{
	
    public Resources res;
    UserData tempValues=null;
    LayoutInflater inflater;
	private Activity activity;
    private ArrayList data;
	
    /*************  CustomAdapterMobilegcm Constructor *****************/
	public CustomAdapterMobilegcm(
			              ShowMessage activitySpinner, 
			              int textViewResourceId,   
			              ArrayList objects,
			              Resources resLocal
			             ) 
	 {
        super(activitySpinner, textViewResourceId, objects);
        
        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;
   
        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

    	/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_rows, parent, false);
        
        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (UserData) data.get(position);
        
        TextView Username        = (TextView)row.findViewById(R.id.username);
        TextView Userimei          = (TextView)row.findViewById(R.id.imei);
        ImageView UserImage = (ImageView)row.findViewById(R.id.image);
        
        
            // Set values for spinner each row 
        	Username.setText(tempValues.getName());
        	Userimei.setText(tempValues.getIMEI());
        	UserImage.setImageResource(res.getIdentifier("com.androidexample.mobilegcm:drawable/user_thumb",null,null));
            
           

        return row;
      }
 }
