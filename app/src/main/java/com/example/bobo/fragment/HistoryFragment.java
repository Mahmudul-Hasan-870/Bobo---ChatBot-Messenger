package com.example.bobo.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo.R;
import com.example.bobo.activity.ChatActivity;
import com.example.bobo.adapter.HistoryAdapter;
import com.example.bobo.database.ChatDatabaseHelper;

public class HistoryFragment extends Fragment {

    private HistoryAdapter historyAdapter; // Adapter for RecyclerView
    private ChatDatabaseHelper dbHelper; // Database helper instance for chat history
    private ImageView noDataImageView; // ImageView to show when no chat history is available

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Set the status bar color based on the current theme
        setStatusBarColor();

        // Initialize RecyclerView and ImageView from the layout
        // RecyclerView to display chat history titles
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHistory);
        noDataImageView = view.findViewById(R.id.noDataImageView);

        // Initialize database helper with the context of the fragment
        dbHelper = new ChatDatabaseHelper(requireContext());

        // Initialize the history adapter with an onItemClick listener to open ChatActivity
        historyAdapter = new HistoryAdapter(requireContext(), chatId -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("chatId", chatId);
            startActivity(intent);
            // Apply the custom transition animation
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Set the adapter and layout manager for the RecyclerView
        recyclerView.setAdapter(historyAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Load chat titles from the database
        loadChatTitles();

        // Handle click on back icon to finish the current activity
        ImageView backIcon = view.findViewById(R.id.backIcon);
        backIcon.setOnClickListener(v -> requireActivity().finish());

        return view; // Return the prepared view for display
    }

    // Method to set the status bar color based on the theme
    private void setStatusBarColor() {
        // Get the current night mode
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Check if it's in dark mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Set the status bar color for dark theme
            setDarkStatusBar();
        } else {
            // Set the status bar color for light theme
            setLightStatusBar();
        }
    }

    // Method to set the status bar for dark theme
    private void setDarkStatusBar() {
        // Set system UI visibility and status bar color for dark theme
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.black));
    }

    // Method to set the status bar for light theme
    private void setLightStatusBar() {
        // Set system UI visibility and status bar color for light theme
        requireActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        requireActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.white));
    }

    // Method to load chat titles from the database and populate RecyclerView
    private void loadChatTitles() {
        Cursor cursor = dbHelper.getAllChatTitles(); // Query all chat titles from the database
        if (cursor.getCount() == 0) {
            // If no chat titles are found, show the noDataImageView
            noDataImageView.setVisibility(View.VISIBLE);
        } else {
            // If chat titles exist, hide the noDataImageView and populate the adapter with titles
            noDataImageView.setVisibility(View.GONE);
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_CHAT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_TITLE));
                historyAdapter.addChatTitle(id, title); // Add chat title to the adapter
            }
        }
        cursor.close(); // Close the cursor to release resources
    }
}
