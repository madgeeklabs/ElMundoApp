package com.madgeeklabs.mglelmundo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.models.Category;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter {

    //    private HashMap<String, Integer> categoriesMap = new HashMap<>();
    private ArrayList<Category> categories;
    private LayoutInflater mInflater;

    public MenuAdapter(Context context, ArrayList<Category> categories) {
        this.categories = categories;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.categories_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.categories_item_name);
            holder.image = (ImageView) convertView.findViewById(R.id.category_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(categories.get(position).getCategoryName());
        holder.image.setImageResource(categories.get(position).getResourceImg());
        return convertView;
    }


    private class ViewHolder {
        private TextView name;
        private ImageView image;
    }
}
