package com.ibtikar.apps.wayaaak.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibtikar.apps.wayaaak.Models.Category;
import com.ibtikar.apps.wayaaak.Models.ListItem;
import com.ibtikar.apps.wayaaak.R;

import java.util.List;

public class SliderNavAdapter extends BaseExpandableListAdapter {
    Context context;
    List<Category> categories;
    FragmentManager fragmentManager;

    public SliderNavAdapter(Context context, FragmentManager fragmentManager, List<Category> categories) {
        this.categories = categories;
        this.context = context;
        this.fragmentManager = fragmentManager;

       /* for (int i = 0; i < categories.size(); i++) {
            ListItem item = new ListItem();
            item.setId(categories.get(i).getId());
            item.setName("الكل");
            categories.get(i).getSub_list().add(item);
        }*/
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categories.get(groupPosition).getSub_list().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getSub_list();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return categories.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getSub_list().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nav_main_cat_item, null);
        }

        TextView title = convertView.findViewById(R.id.main_cat_title_txt);
        title.setText(categories.get(groupPosition).getName());
        ImageView ico = convertView.findViewById(R.id.list_indicator_img);
        ico.setImageResource(isExpanded ? R.drawable.ic_action_mins : R.drawable.ic_action_plus);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.nav_sub_cat_item, null);
        }
        TextView title = convertView.findViewById(R.id.sub_cat_title_txt);
        title.setText(categories.get(groupPosition).getSub_list().get(childPosition).getName());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
