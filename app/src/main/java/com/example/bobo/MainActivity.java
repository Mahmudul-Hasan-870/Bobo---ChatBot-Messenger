package com.example.bobo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bobo.activity.FillProfileActivity;
import com.example.bobo.activity.HomeActivity;
import com.example.bobo.adapter.OnboardingAdapter;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class MainActivity extends AppCompatActivity {

    // Constants for SharedPreferences
    private static final String PREF_NAME = "OnboardingPrefs";
    private static final String PREF_ONBOARDING_COMPLETED = "OnboardingCompleted";

    private ViewPager2 viewPager;
    private Button nextButton;
    private OnboardingAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the status bar color based on the theme
        setStatusBarColor();
        // Apply the saved dark mode setting
        applySavedDarkModeSetting();

        // Retrieve the onboarding completion status from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean onboardingCompleted = preferences.getBoolean(PREF_ONBOARDING_COMPLETED, false);

        // If onboarding is completed, navigate directly to HomeActivity
        if (onboardingCompleted) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return; // Exit the method to prevent further execution
        }

        // Set the content view to the onboarding layout
        setContentView(R.layout.activity_main);

        // Initialize the view components
        viewPager = findViewById(R.id.viewPager);
        nextButton = findViewById(R.id.nextButton);
        WormDotsIndicator dotsIndicator = findViewById(R.id.dotsIndicator);

        // Images and titles for the onboarding slides
        int[] images = {R.drawable.innovation_pana, R.drawable.bot_amico, R.drawable.chat_bot};
        String[] titles = {
                "Welcome To Bobo,\na great friend to\nchat with you",
                "If you are confused\nabout what to do,\njust open Bobo",
                "Bobo will be ready\nto chat and make\nyou happy"
        };

        // Initialize the adapter and set it to the ViewPager
        adapter = new OnboardingAdapter(images, titles);
        viewPager.setAdapter(adapter);

        // Set the dots indicator with the ViewPager
        dotsIndicator.setViewPager2(viewPager);

        // Set the click listener for the "Next" button
        nextButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                // Move to the next onboarding slide
                viewPager.setCurrentItem(currentItem + 1);
            }
        });

        // Register a callback to handle page changes
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Show or hide the "Next" button based on the current slide position
                if (position == adapter.getItemCount() - 1) {
                    nextButton.setVisibility(View.GONE);
                } else {
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // Method to handle the "Get Started" button click
    public void onGetStartedButtonClick(View view) {
        // Save the onboarding completion status in SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_ONBOARDING_COMPLETED, true);
        editor.apply();

        // Navigate to FillProfileActivity
        Intent intent = new Intent(this, FillProfileActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to set the status bar color based on the theme
    private void setStatusBarColor() {
        // Get the current night mode
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        // Check if it's in dark mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            setDarkStatusBar(); // Set the status bar color for dark theme
        } else {
            setLightStatusBar(); // Set the status bar color for light theme
        }
    }

    // Method to set the status bar for dark theme
    private void setDarkStatusBar() {
        // Set system UI visibility and status bar color for dark theme
        getWindow().getDecorView().setSystemUiVisibility(0);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
    }

    // Method to set the status bar for light theme
    private void setLightStatusBar() {
        // Set system UI visibility and status bar color for light theme
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
    }

    // Method to apply the saved dark mode setting
    private void applySavedDarkModeSetting() {
        // Retrieve the saved dark mode setting from SharedPreferences
        SharedPreferences darkPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isDarkModeEnabled = darkPreferences.getBoolean("darkMode", false);

        // Apply the dark mode setting to the app's theme
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
