package com.example.designapptest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.designapptest.R;


import java.util.List;

public class AdapterGridRateStar extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Integer> lstStar;

    public AdapterGridRateStar(Context context, int layout, List<Integer> lstStar) {
        this.context = context;
        this.layout = layout;
        this.lstStar = lstStar;
    }

    @Override
    public int getCount() {
        return lstStar.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);

            AdapterGridRateStar.ViewHolder holder = new AdapterGridRateStar.ViewHolder();

            holder.img = (ImageView) view.findViewById(R.id.img_star);
            view.setTag(holder);
        }

        //gan gia tri
        AdapterGridRateStar.ViewHolder holder = (AdapterGridRateStar.ViewHolder) view.getTag();

        holder.img.setImageResource(lstStar.get(position));
        return view;
    }
    class ViewHolder{
        ImageView img;
    }
}
