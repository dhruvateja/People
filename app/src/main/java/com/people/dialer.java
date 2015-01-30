package com.people;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import search.AbstractSearchService;
import search.SearchCallback;
import search.SearchService;


public class dialer extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG = dialer.class.getSimpleName();

    private static final int MAX_HITS = 20;
    /**
     * Called when the activity is first created.
     */
    private AbstractSearchService searchService;
    private TextView mInput;
    private ListView list;
    private ResultAdapter resultAdapter = new ResultAdapter();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        mInput  = (TextView)findViewById(R.id.text_field);
        list = ((ListView) findViewById(R.id.search_list));
        list.setAdapter(resultAdapter);
        list.setOnItemClickListener(this);
        searchService = SearchService.getInstance(this);
        search(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dialer, menu);
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
    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.fromParts("tel", number, null));
        startActivity(intent);
    }

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.delete_layout:
                delChar();
                break;
            default:
              String str = (String) view.getTag();
                if(str!=null && str.length()>0){
                    addChar(str);
                }

               // addChar(((TextView) view).getText().toString());
        }
        String searchText = mInput.getText().toString();
        if (TextUtils.isEmpty(searchText)) {
            search(null);
        } else if ("*#*".equals(searchText)) {
            searchService.asyncRebuild(true);
        } else if (searchText.indexOf('*') == -1) {
            search(searchText);
        }
    }

    private void addChar(String c) {
        System.out.println("phani char "+c);
        c = c.toLowerCase(Locale.UK);
        mInput.setText(mInput.getText() + String.valueOf(c.charAt(0)));
    }

    private void delChar() {
        String text = mInput.getText().toString();
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
            mInput.setText(text);
        }
    }

    private void search(String query) {
        SearchCallback searchCallback = new SearchCallback() {
            private long start = System.currentTimeMillis();

            @Override
            public void onSearchResult(String query, long hits,
                                       final List<Map<String, Object>> result) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultAdapter.setItems(result);
                        resultAdapter.notifyDataSetChanged();
                        list.smoothScrollToPosition(0);
                    }
                });
                Log.v(SearchService.TAG, "query:" + query + ",result: " + result.size() + ",time used:" + (System.currentTimeMillis() - start));
            }
        };
        searchService.query(query, MAX_HITS, true, searchCallback);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    private static class ViewHolder {
        public TextView name;
        public TextView number;
        public LinearLayout dial_laLayout;
    }

    private class ResultAdapter extends BaseAdapter {
        private List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

        @Override
        public synchronized int getCount() {
            return items.size();
        }

        public synchronized void setItems(List<Map<String, Object>> items) {
            this.items = items;
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                rowView = LayoutInflater.from(getBaseContext()).inflate(
                        R.layout.list_card, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView) rowView.findViewById(R.id.contact_name);
                viewHolder.number = (TextView) rowView.findViewById(R.id.contact_num);
                viewHolder.dial_laLayout = (LinearLayout)findViewById(R.id.dial_layout);
                rowView.setTag(viewHolder);
            }
            ViewHolder holder = (ViewHolder) rowView.getTag();
            Map<String, Object> searchRes = items.get(position);
            StringBuilder nameBuilder = new StringBuilder();
            if (searchRes.containsKey(SearchService.FIELD_NAME)) {
                nameBuilder.append(searchRes.get(
                        SearchService.FIELD_NAME).toString());
            } else {
                nameBuilder.append("No Number");
            }
            nameBuilder.append(' ');
            if (searchRes.containsKey(SearchService.FIELD_PINYIN)) {
                nameBuilder.append(searchRes.get(SearchService.FIELD_PINYIN).toString());
            }
            StringBuilder numberBuilder = new StringBuilder();
            if (searchRes.containsKey(SearchService.FIELD_HIGHLIGHTED_NUMBER)) {
                numberBuilder.append(searchRes.get(SearchService.FIELD_HIGHLIGHTED_NUMBER).toString());
            } else if (searchRes.containsKey(SearchService.FIELD_NUMBER)) {
                numberBuilder.append(searchRes.get(SearchService.FIELD_NUMBER));
            }
            holder.name.setText(Html.fromHtml(nameBuilder.toString()));
            holder.number.setText(Html.fromHtml(numberBuilder.toString()));
            return rowView;
        }

    }
}