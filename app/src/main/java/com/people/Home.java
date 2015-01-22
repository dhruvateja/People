package com.people;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Home extends Fragment {
    private LinearLayout list_card_layout;
    private TextView logo_text;
    private LinearLayout btn_contacts;
    private LinearLayout btn_me;
    private RelativeLayout dialer_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home,container,false);
        list_card_layout = (LinearLayout)rootView.findViewById(R.id.list_card_id);
        dialer_btn = (RelativeLayout)rootView.findViewById(R.id.home_dialer);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), User.class);
                startActivity(intent);
            }
        });
        dialer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), dialer.class);
                startActivity(intent);
            }
        });
    }


}
