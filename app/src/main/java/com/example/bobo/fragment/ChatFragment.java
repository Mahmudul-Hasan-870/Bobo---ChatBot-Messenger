package com.example.bobo.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bobo.R;
import com.example.bobo.activity.ChatActivity;
import com.example.bobo.database.ProfileHelper;

public class ChatFragment extends Fragment {

    private ProfileHelper dbHelper; // Database helper instance
    private TextView profileNameTextView; // TextView to display profile name

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize profileNameTextView from layout
        profileNameTextView = view.findViewById(R.id.profile_name);

        // Initialize dbHelper with the context of the fragment
        dbHelper = new ProfileHelper(requireContext());

        // Load profile name into profileNameTextView
        loadProfileName(view);

        // Set status bar color based on the current theme
        setStatusBarColor();

        // Set onClickListener for the getStartedButton
        view.findViewById(R.id.getStartedButton).setOnClickListener(this::onGetStartedButtonClick);

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

    // Method to load profile name from database and display it
    private void loadProfileName(View view) {
        Cursor cursor = dbHelper.getAllProfiles(); // Query all profiles from database
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name")); // Get profile name from cursor
            profileNameTextView.setText("Welcome, " + name + "!👋"); // Set profile name in TextView
        }
        cursor.close(); // Close cursor to release resources
    }

    // Method to handle onClick for getStartedButton
    public void onGetStartedButtonClick(View view) {
        openChatActivity(); // Open ChatActivity when getStartedButton is clicked
    }

    // Method to open ChatActivity
    private void openChatActivity() {
        Intent intent = new Intent(requireContext(), ChatActivity.class); // Create intent for ChatActivity
        startActivity(intent); // Start ChatActivity
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Apply custom transition animation
    }

}
