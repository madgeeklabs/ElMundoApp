package com.madgeeklabs.mglelmundo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.madgeeklabs.mglelmundo.Activities.MoreInfoActivity;
import com.madgeeklabs.mglelmundo.R;
import com.madgeeklabs.mglelmundo.base.MundoAplication;
import com.madgeeklabs.mglelmundo.models.News;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by goofyahead on 11/28/14.
 */
public class NewsAdapter extends BaseAdapter{

    @Inject
    ImageLoader mLoader;

    private ArrayList<News> news;
    private LayoutInflater mInflater;
    private Context mContext;

    public NewsAdapter (ArrayList<News> news, Context context, MundoAplication app) {
        app.inject(this);
        this.news = news;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.news_item, null);
            holder.title = (TextView) convertView.findViewById(R.id.news_title);
            holder.content = (TextView) convertView.findViewById(R.id.news_content);
            holder.image = (NetworkImageView) convertView.findViewById(R.id.news_image);
            holder.moreInfo = (TextView) convertView.findViewById(R.id.more_info);
            holder.close = (ImageButton) convertView.findViewById(R.id.delete_current);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(news.get(position).getTittle());
        holder.image.setImageUrl(news.get(position).getImageUrl(), mLoader);
        holder.content.setText(news.get(position).getContent());

        holder.close.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MoreInfoActivity.class);
                intent.putExtra("THEME", news.get(position).getTags().get(0));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public void removeItem(int currentSpeakingNews) {
        news.remove(currentSpeakingNews);
        notifyDataSetChanged();
    }


    private class ViewHolder {
        private TextView title;
        private TextView content;
        private NetworkImageView image;
        private TextView moreInfo;
        private ImageButton close;
    }

}
