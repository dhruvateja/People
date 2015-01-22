package com.people;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Svaad on 14-01-2015.
 */
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private LinearLayout contactsLayout;
    private LinearLayout meLayout;
    private ImageView contactsImg;
    private ImageView meImg;
    private TextView peopleTxt;
    private ViewPager mViewPager;
    private PagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsLayout  = (LinearLayout)findViewById(R.id.contacts_link);
        meLayout        = (LinearLayout)findViewById(R.id.me_link);
        contactsImg     = (ImageView)findViewById(R.id.contacts_img);
        meImg           = (ImageView)findViewById(R.id.me_img);
        peopleTxt       = (TextView)findViewById(R.id.logo);
        mViewPager      = (ViewPager)findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(1);
        contactsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        meLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2);
            }
        });
        peopleTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1);
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {  }

    @Override
    public void onPageSelected(int pos) {
        switch (pos){
            case 0:
                peopleTxt.setAlpha(0.5f);
                contactsImg.setAlpha(1f);
                meImg.setAlpha(0.5f);
                break;
            case 1:
                peopleTxt.setAlpha(1f);
                contactsImg.setAlpha(0.5f);
                meImg.setAlpha(0.5f);
                break;
            case 2:
                peopleTxt.setAlpha(0.5f);
                contactsImg.setAlpha(0.5f);
                meImg.setAlpha(1f);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) { }
}
