package in.app.waiter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReportArrayAdapeter  extends ArrayAdapter<ModelCart> {
    private static final String TAG = "ReportArrayAdapeter";
    private List<ModelCart> cardList = new ArrayList<ModelCart>();

    public ReportArrayAdapeter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(ModelCart object) {
        cardList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public ModelCart getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.child_item, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.line1 = (TextView) row.findViewById(R.id.item_name);
            viewHolder.line2 = (TextView) row.findViewById(R.id.price);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder)row.getTag();
        }
        ModelCart card = getItem(position);
       /* try{
			//SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
			  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
			formatter.setLenient(false);
			SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm");
			Date oldDate = formatter.parse(card.getTime());
			String oldTime = format.format(oldDate);
			viewHolder.line1.setText("");
			 viewHolder.line1.setText(oldTime);
}catch(Exception e)
{
	viewHolder.line1.setText("");
	Toast.makeText(getContext(),String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
}*/
        String strCurrentDate = card.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
		try {
			newDate = format.parse(strCurrentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        format = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        String date = format.format(newDate);

        viewHolder.line1.setAlpha(0.6f);
        viewHolder.line2.setAlpha(0.6f);
        viewHolder.line1.setText(date);
        viewHolder.line2.setText("");
        viewHolder.line2.setText("Table "+card.getTableName());
        viewHolder.line1.setTextSize(20);
        viewHolder.line2.setTextSize(20);
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    static class CardViewHolder {
        TextView line1;
        TextView line2;
    }
}
