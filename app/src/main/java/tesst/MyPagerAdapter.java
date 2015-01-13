package tesst;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Svaad on 13-01-2015.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private final String[] TITLES = {"Facebook", "Recommend"};
    private Context context;

    public MyPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        context = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Fragment.instantiate(context, Tab1Fragment.class.getName());
                break;
            case 1:
                fragment = Fragment.instantiate(context, Tab2Fragment.class.getName());
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}

