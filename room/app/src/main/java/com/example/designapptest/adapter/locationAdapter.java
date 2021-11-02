package com.example.designapptest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.designapptest.model.LocationModel;
import com.example.designapptest.R;
import com.example.designapptest.view.searchandmap.SearchViewActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.designapptest.adapter.AdapterRecyclerSuggestions.INTENT_DISTRICT;

public class locationAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<LocationModel> lstLocation;

    public locationAdapter(Context context, int layout, List<LocationModel> lstLocation) {
        this.context = context;
        this.layout = layout;
        this.lstLocation = lstLocation;
    }

    @Override
    public int getCount() {

        return this.lstLocation.size();
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

            ViewHolder holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.img_location);
            holder.name = (TextView) view.findViewById(R.id.txt_name_location);
            holder.room = (TextView) view.findViewById(R.id.txt_room_location);
            holder.rLTImage =view.findViewById(R.id.rLT_image);

            view.setTag(holder);
        }

        //gan gia tri
        LocationModel itemLocation = lstLocation.get(position);

        ViewHolder holder = (ViewHolder) view.getTag();

        Picasso.get().load(itemLocation.getImage()).centerCrop().fit().into(holder.image);
        //holder.image.setImageResource(itemLocation.getImage());
        holder.name.setText(itemLocation.getCounty());
        holder.room.setText(itemLocation.getRoomNumber()+" Phòng");

        holder.rLTImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SearchViewActivity.class);
                intent.putExtra(INTENT_DISTRICT,lstLocation.get(position).getCounty());
                context.startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder {
        ImageView image;
        TextView name;
        TextView room;
        RelativeLayout rLTImage;
    }
}
