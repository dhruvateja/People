package com.people;


import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import search.SearchService;

public class ResultAdapter   extends BaseAdapter {
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
          //  rowView = LayoutInflater.from(getApp).inflate(R.layout.list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) rowView.findViewById(R.id.name);
            viewHolder.number = (TextView) rowView
                    .findViewById(R.id.number);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Map<String, Object> searchRes = items.get(position);
        StringBuilder nameBuilder = new StringBuilder();
        if (searchRes.containsKey(SearchService.FIELD_NAME)) {
            nameBuilder.append(searchRes.get(
                    SearchService.FIELD_NAME).toString());
        } else {
            nameBuilder.append("陌生号码");
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
    private static class ViewHolder {
        public TextView name;
        public TextView number;
    }
}