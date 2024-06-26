package com.example.bobo.activity;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo.R;
import com.example.bobo.adapter.ChatAdapter;
import com.example.bobo.database.ChatDatabaseHelper;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    // Declare class-level variables
    private GenerativeModelFutures model;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private EditText userInputEditText;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private ChatDatabaseHelper dbHelper;
    private long chatId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setStatusBarColor(); // Sets the status bar color, assuming this method is defined elsewhere

        // Find and initialize the UI elements
        userInputEditText = findViewById(R.id.editTextUserInput);
        recyclerView = findViewById(R.id.recyclerViewChat);
        TextView textViewEmptyConversation = findViewById(R.id.textViewEmpty);

        // Initialize the database helper
        dbHelper = new ChatDatabaseHelper(this);

        // Initialize the chat adapter and set it to the RecyclerView
        chatAdapter = new ChatAdapter(this, textViewEmptyConversation);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the generative model with the given API key from resources
        GenerativeModel gm = new GenerativeModel("gemini-pro", getString(R.string.gemini_api_key));
        model = GenerativeModelFutures.from(gm);

        // Check if a description is passed in the intent extras
        if (getIntent().hasExtra("description")) {
            String description = getIntent().getStringExtra("description");
            userInputEditText.setText(description);
        }

        // Check if chatId is passed in the intent extras
        if (getIntent().getExtras() != null) {
            chatId = getIntent().getLongExtra("chatId", -1);
            if (chatId != -1) {
                chatAdapter.setChatId(chatId);
                loadMessages(chatId);
            }
        }

        // Find and initialize the action button
        // This variable is declared but not used in the given code snippet
        ImageButton actionButton = findViewById(R.id.actionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input from the EditText and trim any leading/trailing whitespace
                String userInput = userInputEditText.getText().toString().trim();

                // Check if the user input is not empty
                if (!userInput.isEmpty()) {
                    sendMessage(); // Call the sendMessage() method to send the message
                } else {
                    // Show a toast message if the input is empty
                    Toast.makeText(ChatActivity.this, "Please type something before sending", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Find and initialize the back icon
        ImageView backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back icon click by finishing the current activity
                finish(); // Finish the current activity
            }
        });
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
        getWindow().getDecorView().setSystemUiVisibility(0); // Clear any flags
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black)); // Set status bar color to black
    }

    // Method to set the status bar for light theme
    private void setLightStatusBar() {
        // Set system UI visibility and status bar color for light theme
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // Set light status bar flag
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white)); // Set status bar color to white
    }

    // Method to load messages from the database based on chatId
    private void loadMessages(long chatId) {
        Cursor cursor = dbHelper.getMessagesByChatId(chatId); // Retrieve messages from the database using chatId
        while (cursor.moveToNext()) {
            String message = cursor.getString(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_MESSAGE)); // Get the message
            boolean isBot = cursor.getInt(cursor.getColumnIndexOrThrow(ChatDatabaseHelper.COLUMN_IS_BOT)) == 1; // Determine if the message is from the bot
            chatAdapter.addMessage(message, isBot, true); // Add messages to the adapter without saving them again
        }
        cursor.close(); // Close the cursor to avoid memory leaks
    }

    // Method to send a message from the user
    private void sendMessage() {
        String userInput = userInputEditText.getText().toString().trim(); // Get the user input and trim whitespace
        if (!userInput.isEmpty()) {
            // Check if it's a new chat session
            if (chatId == -1) {
                chatId = dbHelper.saveChatTitle(userInput); // Save the chat title and get the new chatId
                chatAdapter.setChatId(chatId); // Set the chatId in the adapter
            }

            chatAdapter.addMessage(userInput, false, false); // Add the user message to the adapter
            userInputEditText.getText().clear(); // Clear the user input EditText

            chatAdapter.addMessage("Please wait", true, true); // Add a "Please wait" message to the adapter temporarily

            Content content = new Content.Builder().addText(userInput).build(); // Build the content for the generative model

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content); // Generate content using the model
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String botResponse = result.getText(); // Get the bot's response
                    runOnUiThread(() -> {
                        chatAdapter.removeMessage("Please wait"); // Remove the "Please wait" message
                        assert botResponse != null;
                        chatAdapter.addMessage(botResponse, true, false); // Add the bot's response to the adapter
                        recyclerView.smoothScrollToPosition(chatAdapter.getItemCount() - 1); // Scroll to the latest message
                    });
                }

                @Override
                public void onFailure(@NonNull Throwable t) {
                    t.printStackTrace(); // Print the stack trace for debugging
                    runOnUiThread(() -> {
                        chatAdapter.removeMessage("Please wait"); // Remove the "Please wait" message
                        chatAdapter.addMessage("Error occurred. Please try again.", true, false); // Add an error message
                    });
                }
            }, executor); // Execute the callback on the executor
        }
    }

    @Override
    public void finish() {
        super.finish(); // Call the superclass's finish method to close the activity
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Apply the transition animations
    }
}