package tesst;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.people.R;

/**
 * Created by Svaad on 13-01-2015.
 */
public class TestFragmentActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
   private ViewPager pager;
    private MyPagerAdapter adapter;
    private LinearLayout tab1Layout;
    private LinearLayout tab2Layout;
    private TextView tab1Txt;
    private TextView tab2Txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        pager = (ViewPager)findViewById(R.id.pager);
        tab1Layout = (LinearLayout)findViewById(R.id.tab1_layout);
        tab2Layout = (LinearLayout)findViewById(R.id.tab2_layout);
        tab1Txt    = (TextView)findViewById(R.id.tab1_txt);
        tab2Txt    = (TextView)findViewById(R.id.tab2_txt);
        adapter = new MyPagerAdapter(getSupportFragmentManager(),this);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
        tab1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0);
            }
        });
        tab2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1);
            }
        });

    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int pos) {
        switch (pos){
            case 0:
                tab1Txt.setAlpha(1f);
                tab2Txt.setAlpha(0.5f);
                break;
            case 1:
                tab1Txt.setAlpha(0.5f);
                tab2Txt.setAlpha(1f);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
