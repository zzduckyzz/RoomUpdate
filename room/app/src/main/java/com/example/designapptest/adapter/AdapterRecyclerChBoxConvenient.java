package com.example.designapptest.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.designapptest.domain.ConvenientFilter;
import com.example.designapptest.R;
import com.example.designapptest.view.searchandmap.SearchViewActivity;

import java.util.List;

public class AdapterRecyclerChBoxConvenient extends RecyclerView.Adapter<AdapterRecyclerChBoxConvenient.ViewHolder> {

    Context context;
    int resource;
    List<ConvenientFilter> convenientFilterList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox chBoxElement;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chBoxElement = itemView.findViewById(R.id.chBox_element);
        }
    }

    public AdapterRecyclerChBoxConvenient(Context context, int resource, List<ConvenientFilter> convenientFilterList) {
        this.context = context;
        this.resource = resource;
        this.convenientFilterList = convenientFilterList;
    }

    @NonNull
    @Override
    public AdapterRecyclerChBoxConvenient.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resource, viewGroup, false);
        AdapterRecyclerChBoxConvenient.ViewHolder viewHolder = new AdapterRecyclerChBoxConvenient.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ConvenientFilter convenientFilter = convenientFilterList.get(i);
        Drawable leftImage = context.getResources().getDrawable(convenientFilter.getImageResource());
        viewHolder.chBoxElement.setText(convenientFilter.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewHolder.chBoxElement.setCompoundDrawablesRelativeWithIntrinsicBounds(leftImage,null,null,null);
        }
        viewHolder.chBoxElement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ((SearchViewActivity)context).addFilter(convenientFilterList.get(i));
                }else {
                    ((SearchViewActivity)context).removeFilter(convenientFilterList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return convenientFilterList.size();
    }

}
