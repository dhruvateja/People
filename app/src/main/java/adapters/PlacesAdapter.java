package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.people.R;

import java.util.ArrayList;

import beans.Places;

/**
 * Created by Svaad on 08-10-2014.
 */
public class PlacesAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Places> dataArray;

    public PlacesAdapter(Context context, ArrayList<Places> data) {
        ctx = context;
        dataArray = data;
    }

    @Override
    public int getCount() {
        return dataArray.size();
    }

    @Override
    public Places getItem(int i) {

        return dataArray.get(i);

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
      @Override
         public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder mViewHolder;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (View)inflater.inflate(R.layout.places_row,null);
            mViewHolder = new ViewHolder();
           // mViewHolder.imageView  = (ImageView)convertView.findViewById(R.id.res_img);
            mViewHolder.locNameTxt = (TextView)convertView.findViewById(R.id.place_add_txt);
            mViewHolder.resNameTxt = (TextView)convertView.findViewById(R.id.place_name_txt);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }
       // Typeface tf = Typeface.createFromAsset(ctx.getAssets(), "fonts/Roboto-font.ttf");
      //  Typeface museoFont = Typeface.createFromAsset(ctx.getAssets(), "fonts/Museo_font.otf");

       // mViewHolder.locNameTxt.setTypeface(tf);
      //  mViewHolder.resNameTxt.setTypeface(museoFont);

        if(dataArray.get(i).getName()!=null && dataArray.get(i).getName().length()>0){
            mViewHolder.resNameTxt.setText(dataArray.get(i).getName());
        }
        if(dataArray.get(i).getAddress()!=null && dataArray.get(i).getAddress().length()>0){

            mViewHolder.locNameTxt.setText(dataArray.get(i).getAddress());
            mViewHolder.locNameTxt.setVisibility(View.VISIBLE);
        }else {
            mViewHolder.locNameTxt.setVisibility(View.GONE);

        }

        return convertView;
    }

    public class ViewHolder{
        TextView resNameTxt;
        TextView locNameTxt;
        ImageView imageView;
    }
}
