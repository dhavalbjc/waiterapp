package in.app.waiter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WaiterPageAdapter extends FragmentPagerAdapter {

	public WaiterPageAdapter(FragmentManager fm) {
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
			// Top Rated fragment activity
			return new GamesFragment();

		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
