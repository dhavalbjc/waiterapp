package in.app.waiter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;
 
public class CustomAutoCompleteTextChangedListener implements TextWatcher{
 
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
     
    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }
     
    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
 
        try{
             
            // if you want to see in the logcat what the user types
            Log.e(TAG, "User input: " + userInput);
     
            //Item_selected mainActivity = new Item_selected();
            // Toast.makeText(context, "ontextchange",Toast.LENGTH_LONG).show();
            // update the adapater
            Item_selected.myAdapter.notifyDataSetChanged();
             
            // get suggestions from the database
            Item[] myObjs = DBAdapter.read(userInput.toString());
            // Toast.makeText(context, myObjs.toString(),Toast.LENGTH_LONG).show();
            // update the adapter
            //Item_selected.myAdapter = new AutocompleteCustomArrayAdapter(context, R.layout.list_view_row, myObjs);
          //  Toast.makeText(context, "ontextchange2",Toast.LENGTH_LONG).show();
            Item_selected.myAutoComplete.setAdapter(Item_selected.myAdapter);
          //  Toast.makeText(context, "ontextchange3",Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(context, "ontextchange2",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
     
     
 
}