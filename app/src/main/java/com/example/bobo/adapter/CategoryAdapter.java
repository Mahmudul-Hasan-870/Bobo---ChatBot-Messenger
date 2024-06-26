package com.example.bobo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<String> titles;
    private final List<Integer> images;
    private final List<String> descriptions;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String description);
    }

    public CategoryAdapter(List<String> titles, List<Integer> images, List<String> descriptions, OnItemClickListener listener) {
        this.titles = titles;
        this.images = images;
        this.descriptions = descriptions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(titles.get(position), images.get(position), descriptions.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView textViewTitle;
        private final TextView textViewDescription;
        private final LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }

        public void bind(final String title, int imageRes, final String description, final OnItemClickListener listener) {
            imageView.setImageResource(imageRes);
            textViewTitle.setText(title);
            textViewDescription.setText(description);

            // Set the background based on the title using if-else
            if ("Articles".equals(title)) {
                linearLayout.setBackgroundResource(R.drawable.bg_articles);
            } else if ("Translate".equals(title)) {
                linearLayout.setBackgroundResource(R.drawable.bg_translate);
            } else if ("Essay".equals(title)) {
                linearLayout.setBackgroundResource(R.drawable.bg_essay);
            } else if ("Summarize".equals(title)) {
                linearLayout.setBackgroundResource(R.drawable.bg_summarize);
            } else if ("Story".equals(title)) {
                linearLayout.setBackgroundResource(R.drawable.bg_story);
            } else {
                linearLayout.setBackgroundResource(R.drawable.default_background);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(description);
                }
            });
        }
    }
}

