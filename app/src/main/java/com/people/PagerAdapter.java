package com.people;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tesst.Tab1Fragment;
import tesst.Tab2Fragment;

/**
 * Created by Svaad on 14-01-2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private final String[] TITLES = {"ME","Home","contacts"};
    private Context context;

    public PagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        context = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Fragment.instantiate(context, Contacts.class.getName());
                break;
            case 1:
                fragment = Fragment.instantiate(context, Home.class.getName());
                break;
            case 2:
                fragment = Fragment.instantiate(context, Me.class.getName());

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


