package in.app.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


@SuppressWarnings("rawtypes")
class SelectPrinterAdapter extends ArrayAdapter {
	 
    private Context mContext;
    private ArrayList<Printer> mList;
 
    @SuppressWarnings("unchecked")
	public SelectPrinterAdapter(Context context, ArrayList<Printer> list) {
        super(context, R.layout.change_categories, list);
 
        mContext = context;
        mList = list;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        View view;
 
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.change_categories, null);
        }
        else {
            view = convertView;
        }
 
        TextView contactNameView = (TextView) view.findViewById(R.id.tv_categories);
       
 
        contactNameView.setText( mList.get(position).get_name() );
        //phoneNumberView.setText( mList.get(position).getNumber() );
 
        return view;
    }
}