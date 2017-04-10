package in.app.waiter;

import android.content.Context;
import android.widget.ExpandableListView;

//import android.view.View.MeasureSpec;

public class CustExpListview extends ExpandableListView
{

int intGroupPosition, intChildPosition, intGroupid;

public CustExpListview(Context context) 
{
super(context);     
}

protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
{
//  widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.UNSPECIFIED);
heightMeasureSpec = MeasureSpec.makeMeasureSpec(1200, MeasureSpec.AT_MOST);
super.onMeasure(widthMeasureSpec, heightMeasureSpec);
}  
}