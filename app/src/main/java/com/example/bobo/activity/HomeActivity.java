package com.example.bobo.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bobo.R;
import com.example.bobo.fragment.CategoryFragment;
import com.example.bobo.fragment.ChatFragment;
import com.example.bobo.fragment.HistoryFragment;
import com.example.bobo.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottom_navigation); // Find the BottomNavigationView in your layout file by its ID

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // Get the ID of the selected menu item

                updateIconState(itemId); // Update the state of the icons based on the selected item

                // Handle navigation based on the selected item ID
                if (itemId == R.id.navigation_chat) { // If "Chat" menu item is selected
                    loadFragment(new ChatFragment()); // Load ChatFragment into the fragment container
                    return true; // Return true to indicate the item selection is handled
                } else if (itemId == R.id.navigation_category) { // If "Category" menu item is selected
                    loadFragment(new CategoryFragment()); // Load CategoryFragment into the fragment container
                    return true; // Return true to indicate the item selection is handled
                } else if (itemId == R.id.navigation_history) { // If "History" menu item is selected
                    loadFragment(new HistoryFragment()); // Load HistoryFragment into the fragment container
                    return true; // Return true to indicate the item selection is handled
                } else if (itemId == R.id.navigation_profile) { // If "Profile" menu item is selected
                    loadFragment(new ProfileFragment()); // Load ProfileFragment into the fragment container
                    return true; // Return true to indicate the item selection is handled
                }

                return false; // Return false if none of the above conditions match
            }
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_chat); // Set "Chat" as the default selected item
        }
    }

    private void loadFragment(Fragment fragment) {
        // Begin a transaction to replace the current fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Check if there's already a current fragment
        if (currentFragment != null) {
            // Determine the animation based on the transition between currentFragment and the new fragment
            if (currentFragment instanceof ChatFragment) {
                if (fragment instanceof CategoryFragment) {
                    // Going from ChatFragment to CategoryFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (fragment instanceof HistoryFragment) {
                    // Going from ChatFragment to HistoryFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (fragment instanceof ProfileFragment) {
                    // Going from ChatFragment to ProfileFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            } else if (currentFragment instanceof CategoryFragment) {
                if (fragment instanceof ChatFragment) {
                    // Going from CategoryFragment to ChatFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (fragment instanceof HistoryFragment) {
                    // Going from CategoryFragment to HistoryFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (fragment instanceof ProfileFragment) {
                    // Going from CategoryFragment to ProfileFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            } else if (currentFragment instanceof HistoryFragment) {
                if (fragment instanceof ChatFragment) {
                    // Going from HistoryFragment to ChatFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (fragment instanceof CategoryFragment) {
                    // Going from HistoryFragment to CategoryFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (fragment instanceof ProfileFragment) {
                    // Going from HistoryFragment to ProfileFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            } else if (currentFragment instanceof ProfileFragment) {
                if (fragment instanceof ChatFragment) {
                    // Going from ProfileFragment to ChatFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (fragment instanceof CategoryFragment) {
                    // Going from ProfileFragment to CategoryFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if (fragment instanceof HistoryFragment) {
                    // Going from ProfileFragment to HistoryFragment with specific animations
                    transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        }

        // Replace the current fragment container with the new fragment
        transaction.replace(R.id.nav_host_fragment, fragment);

        // Commit the transaction to apply the changes
        transaction.commit();

        // Update the currentFragment to the newly loaded fragment
        currentFragment = fragment;
    }

    private void updateIconState(int selectedItemId) {
        // Iterate through all menu items in the BottomNavigationView
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            MenuItem menuItem = bottomNavigationView.getMenu().getItem(i); // Get each MenuItem at position i

            // Check if the current MenuItem matches the selectedItemId
            if (menuItem.getItemId() == selectedItemId) {
                menuItem.setIcon(getSelectedIcon(selectedItemId)); // Set selected icon for the matching MenuItem
            } else {
                menuItem.setIcon(getUnselectedIcon(menuItem.getItemId())); // Set unselected icon for non-matching MenuItems
            }
        }
    }

    private int getSelectedIcon(int itemId) {
        // Return the appropriate selected icon resource ID based on itemId
        if (itemId == R.id.navigation_chat) {
            return R.drawable.chat_selected; // Return selected icon for Chat
        } else if (itemId == R.id.navigation_category) {
            return R.drawable.category_selected; // Return selected icon for Category
        } else if (itemId == R.id.navigation_history) {
            return R.drawable.history_selected; // Return selected icon for History
        } else if (itemId == R.id.navigation_profile) {
            return R.drawable.profile_selected; // Return selected icon for Profile
        } else {
            return 0; // Return 0 if no matching icon is found (though this case should not occur)
        }
    }

    private int getUnselectedIcon(int itemId) {
        // Return the appropriate unselected icon resource ID based on itemId
        if (itemId == R.id.navigation_chat) {
            return R.drawable.chat_unselected; // Return unselected icon for Chat
        } else if (itemId == R.id.navigation_category) {
            return R.drawable.category_unselected; // Return unselected icon for Category
        } else if (itemId == R.id.navigation_history) {
            return R.drawable.history_unselected; // Return unselected icon for History
        } else if (itemId == R.id.navigation_profile) {
            return R.drawable.profile_unselected; // Return unselected icon for Profile
        } else {
            return 0; // Return 0 if no matching icon is found (though this case should not occur)
        }
    }
}
