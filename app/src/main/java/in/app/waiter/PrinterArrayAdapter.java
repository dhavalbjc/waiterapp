package in.app.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PrinterArrayAdapter extends BaseAdapter {

    ArrayList<Printer> myList = new ArrayList<Printer>();
    LayoutInflater inflater;
    Context context;


    public PrinterArrayAdapter(Context context, ArrayList<Printer> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Printer getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.add_printer_layout, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Printer currentListData = getItem(position);

        mViewHolder.tvTitle.setText(currentListData.get_name());
        mViewHolder.tvDesc.setText(currentListData.get_mac());
       // mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());

        return convertView;
    }

    private class MyViewHolder {
        TextView tvTitle, tvDesc;
        //ImageView ivIcon;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.line1);
            tvDesc = (TextView) item.findViewById(R.id.line2);
          //  ivIcon = (ImageView) item.findViewById(R.id.ivIcon);
        }
    }
}
