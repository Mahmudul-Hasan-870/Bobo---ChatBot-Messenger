package com.example.bobo.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.example.bobo.R;
import com.google.android.material.materialswitch.MaterialSwitch;

public class DarkModeActivity extends AppCompatActivity {

    // Constants for shared preferences
    private static final String PREF_NAME = "MyPrefs";
    private static final String DARK_MODE_KEY = "darkMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_darkmode);

        // Set the status bar color based on the current theme
        setStatusBarColor();

        // Find the dark mode switch in the layout
        MaterialSwitch darkModeSwitch = findViewById(R.id.dark_mode_switch);

        // Load the current dark mode setting from shared preferences
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isDarkModeEnabled = preferences.getBoolean(DARK_MODE_KEY, false);
        darkModeSwitch.setChecked(isDarkModeEnabled);

        // Set a listener for changes to the dark mode switch
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the dark mode setting
            saveDarkModeSetting(isChecked);

            // Apply the selected mode
            applyDarkMode(isChecked);
        });

        // Find the back icon in the layout
        ImageView backIcon = findViewById(R.id.backIcon);

        // Set a click listener for the back icon
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back icon click here
                finish(); // Finish the current activity
            }
        });
    }

    // Method to save the dark mode setting in shared preferences
    private void saveDarkModeSetting(boolean isChecked) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(DARK_MODE_KEY, isChecked);
        editor.apply(); // Apply the changes
    }

    // Method to apply the selected dark mode
    private void applyDarkMode(boolean isEnabled) {
        if (isEnabled) {
            // Set the app's default night mode to dark
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // Set the app's default night mode to light
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate(); // Restart the activity for changes to take effect
    }

    // Method to set the status bar color based on the theme
    private void setStatusBarColor() {
        // Get the current night mode from the resources
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
        getWindow().getDecorView().setSystemUiVisibility(0); // Clear any flags
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black)); // Set to black
    }

    // Method to set the status bar for light theme
    private void setLightStatusBar() {
        // Set system UI visibility and status bar color for light theme
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Light status bar icons
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white)); // Set to white
    }

    // Override the finish method to include a custom animation
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Custom transition animation
    }
}