package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ibtikar.apps.wayaaak.Models.Category;
import com.ibtikar.apps.wayaaak.R;

import java.util.List;

public class CategorySpinerAdapter extends ArrayAdapter<Category> {
    LayoutInflater flater;
    onClickListner listner;

    public CategorySpinerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Category> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public void registerOnClickListner(onClickListner listner) {
        this.listner = listner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return rowview(convertView, position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return rowview(convertView, position, parent);
    }

    private View rowview(View convertView, int position, ViewGroup parent) {

        final Category rowItem = getItem(position);

        viewHolder holder;
        View rowview = convertView;
        if (rowview == null) {

            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowview = flater.inflate(R.layout.spiner_item_layout, parent, false);

            holder.title = rowview.findViewById(R.id.title);
            rowview.setTag(holder);
        } else {
            holder = (viewHolder) rowview.getTag();
        }

        holder.title.setText(rowItem.getName());


        return rowview;
    }

    public interface onClickListner {
        void onClick(int id, String name);
    }

    private class viewHolder {
        TextView title;
    }

}
