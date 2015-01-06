package com.people;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Home extends Activity {
    private LinearLayout list_card_layout;
    private TextView logo_text;
    private LinearLayout btn_contacts;
    private LinearLayout btn_me;
    private RelativeLayout dialer_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        list_card_layout = (LinearLayout)findViewById(R.id.list_card_id);
        list_card_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, User.class);
                startActivity(intent);
            }
        });

        logo_text = (TextView)findViewById(R.id.logo);
        logo_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Tcards.class);
                startActivity(intent);
            }
        });

        btn_contacts = (LinearLayout)findViewById(R.id.contacts_link);
        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Contacts.class);
                startActivity(intent);
            }
        });

        btn_me = (LinearLayout)findViewById(R.id.me_link);
        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Me.class);
                startActivity(intent);
            }
        });

        dialer_btn = (RelativeLayout)findViewById(R.id.home_dialer);
        dialer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, dialer.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
