package in.app.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class IremsReportArrayAdapter extends BaseAdapter {

    ArrayList<ItemsReport> myList = new ArrayList<ItemsReport>();
    LayoutInflater inflater;
    Context context;


    public IremsReportArrayAdapter(Context context, ArrayList<ItemsReport> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public ItemsReport getItem(int position) {
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
            convertView = inflater.inflate(R.layout.child_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        ItemsReport currentListData = getItem(position);

        mViewHolder.tvTitle.setText(currentListData.get_id()+" "+currentListData.get_name());
        mViewHolder.tvDesc.setText(currentListData.get_total());
       // mViewHolder.ivIcon.setImageResource(currentListData.getImgResId());
        mViewHolder.tvTitle.setAlpha(0.6f);
        mViewHolder.tvDesc.setAlpha(0.6f);
        return convertView;
    }

    private class MyViewHolder {
        TextView tvTitle, tvDesc;
        //ImageView ivIcon;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.item_name);
            tvDesc = (TextView) item.findViewById(R.id.price);
          //  ivIcon = (ImageView) item.findViewById(R.id.ivIcon);
        }
    }
}

