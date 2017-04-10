package in.app.waiter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Movies fragment activity
			return new TopRatedFragment();
			
		case 1:
			// Top Rated fragment activity
			
			return new TablesFragment();
		case 2:
			// Games fragment activity
			//Context c=MainActivit;
			return new GamesFragment();
		case 3:
			// Movies fragment activity
			return new StaffFragment();
		case 4:
			// Movies fragment activity
			return new Reports_Fragment();
		case 5:
			// Movies fragment activity
			return new PrintFragment();
		case 6:
			// Movies fragment activity
			return new RestaurantFragment();	
		
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 7;
	}

}
