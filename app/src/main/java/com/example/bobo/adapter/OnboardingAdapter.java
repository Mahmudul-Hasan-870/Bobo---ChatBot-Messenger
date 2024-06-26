package com.example.bobo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final int[] images; // Array of image resources
    private final String[] titles; // Array of titles for onboarding slides

    // Constructor to initialize images and titles arrays
    public OnboardingAdapter(int[] images, String[] titles) {
        this.images = images;
        this.titles = titles;
    }

    // Create and return a new ViewHolder instance
    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the slide layout from XML
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_layout, parent, false);
        return new OnboardingViewHolder(view); // Return new ViewHolder instance
    }

    // Bind data to the ViewHolder at the specified position
    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]); // Set image resource
        holder.title.setText(titles[position]); // Set title text

        // Show "Get Started" button only on the last slide
        if (position == getItemCount() - 1) {
            holder.getStartedButton.setVisibility(View.VISIBLE);
        } else {
            holder.getStartedButton.setVisibility(View.GONE);
        }
    }

    // Return the total number of items (slides)
    @Override
    public int getItemCount() {
        return titles.length; // Return the length of titles array
    }

    // ViewHolder class to hold the views for each slide
    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView; // ImageView for slide image
        TextView title; // TextView for slide title
        Button getStartedButton; // Button to get started

        // Constructor to initialize views
        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slide_image); // Find ImageView by ID
            title = itemView.findViewById(R.id.slide_title); // Find TextView by ID
            getStartedButton = itemView.findViewById(R.id.getStartedButton); // Find Button by ID
        }
    }
}
