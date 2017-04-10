package in.app.waiter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class StaffArrayAdapter extends BaseAdapter {

    ArrayList<Staff> myList = new ArrayList<Staff>();
    LayoutInflater inflater;
    Context context;


    public StaffArrayAdapter(Context context, ArrayList<Staff> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Staff getItem(int position) {
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
            convertView = inflater.inflate(R.layout.add_staff_member, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Staff currentListData = getItem(position);

        mViewHolder.tvTitle.setText(currentListData.get_staff_fname());
        mViewHolder.tvDesc.setText(currentListData.get_staff_roll());
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
