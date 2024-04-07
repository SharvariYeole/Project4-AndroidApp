package com.sharvari.project4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.DisplayViewHolder> {
    private final List<DisplayItem> dataList;

    public DisplayAdapter(List<DisplayItem> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new DisplayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayViewHolder holder, int position) {
        DisplayItem item = dataList.get(position);
        holder.tv_count.setText(item.count);
        holder.tv_title.setText(item.title);
        holder.tv_author.setText(item.author);
        holder.tv_date.setText(item.date);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class DisplayViewHolder extends RecyclerView.ViewHolder {
        TextView tv_count;
        TextView tv_title;
        TextView tv_author;
        TextView tv_date;
        View view;

        public DisplayViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_count = (TextView)itemView.findViewById(R.id.tvCount);
            tv_title = (TextView)itemView.findViewById(R.id.tvTitle);
            tv_author = (TextView)itemView.findViewById(R.id.tvAuthor);
            tv_date = (TextView)itemView.findViewById(R.id.tvDate);

            view  = itemView;
        }
    }
}
