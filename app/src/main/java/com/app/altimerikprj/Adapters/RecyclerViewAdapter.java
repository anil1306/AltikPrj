package com.app.altimerikprj.Adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.altimerikprj.Activities.NewsFeedActivity;
import com.app.altimerikprj.Model.NewsFeed;
import com.app.altimerikprj.R;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Anil on 12/3/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private static ClickListener clickListener;
    Context context;
    private List<NewsFeed> news = new ArrayList<>();

    public RecyclerViewAdapter() {
        return;
    }

    public void RecyclerViewAdapter(List<NewsFeed> mListNewsFeedAdapter, final Context context,final NewsFeedActivity activity) {
        this.context = context;
        this.news = mListNewsFeedAdapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewAdapter.ViewHolder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_view, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsFeed getDataAdapter1 = news.get(position);
        ((ViewHolder) holder).news_title.setText(getDataAdapter1.getTitle());
        ((ViewHolder) holder).title_by.setText("By: "+getDataAdapter1.getBy());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView news_title, title_by;
        public ViewHolder(View itemView) {
            super(itemView);
            news_title = (TextView) itemView.findViewById(R.id.textView_title);
            title_by=(TextView) itemView.findViewById(R.id.textView_by);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

}