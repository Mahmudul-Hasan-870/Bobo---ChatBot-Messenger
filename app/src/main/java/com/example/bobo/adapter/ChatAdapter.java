package com.example.bobo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bobo.R;
import com.example.bobo.database.ChatDatabaseHelper;
import com.example.bobo.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatMessage> messages = new ArrayList<>();
    private final TextView textViewEmptyConversation; // TextView to display when conversation is empty
    private final ChatDatabaseHelper dbHelper; // Helper class for database operations
    private long chatId = -1; // ID of the chat associated with the adapter

    // Constructor
    public ChatAdapter(Context context, @Nullable TextView textViewEmptyConversation) {
        this.textViewEmptyConversation = textViewEmptyConversation;
        this.dbHelper = new ChatDatabaseHelper(context); // Initialize database helper
    }

    // Set the ID of the chat associated with the adapter
    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    // Add a new message to the list
    public void addMessage(String message, boolean isBot, boolean isFromDatabase) {
        if (!message.equals("Please wait")) {
            ChatMessage chatMessage = new ChatMessage(message, isBot); // Create new ChatMessage object
            messages.add(chatMessage); // Add message to the list
            notifyDataSetChanged(); // Notify RecyclerView about data change
            checkEmptyState(); // Check if conversation is empty and update UI
            // Save message to database if chatId is set and message is not from database
            if (chatId != -1 && !isFromDatabase) {
                dbHelper.saveMessage(chatId, chatMessage.getMessage(), chatMessage.isBot());
            }
        } else {
            // Handle "Please wait" message separately without saving to database
            ChatMessage chatMessage = new ChatMessage(message, isBot); // Create new ChatMessage object
            messages.add(chatMessage); // Add message to the list
            notifyDataSetChanged(); // Notify RecyclerView about data change
            checkEmptyState(); // Check if conversation is empty and update UI
        }
    }

    // Remove a message from the list
    public void removeMessage(String message) {
        for (ChatMessage chatMessage : messages) {
            if (chatMessage.getMessage().equals(message)) {
                messages.remove(chatMessage); // Remove message from the list
                break; // Exit loop after removing the message
            }
        }
        notifyDataSetChanged(); // Notify RecyclerView about data change
        checkEmptyState(); // Check if conversation is empty and update UI
    }

    // Check if conversation is empty and update UI accordingly
    private void checkEmptyState() {
        if (messages.isEmpty() && textViewEmptyConversation != null) {
            textViewEmptyConversation.setVisibility(View.VISIBLE); // Show empty conversation message
        } else {
            if (textViewEmptyConversation != null) {
                textViewEmptyConversation.setVisibility(View.GONE); // Hide empty conversation message
            }
        }
    }

    // Inflate the item layout and create the ViewHolder
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout (item_message.xml) for RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new ChatViewHolder(view); // Return a new instance of ChatViewHolder
    }

    // Bind data to ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position); // Get message at the specified position
        holder.bind(message); // Bind message to ViewHolder
    }

    // Return the total number of items in the list
    @Override
    public int getItemCount() {
        return messages.size(); // Return size of messages list
    }

    // ViewHolder class that holds references to views for each item in RecyclerView
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView textMessageBot;
        TextView textMessageUser;
        TextView textTimestampBot;
        TextView textTimestampUser;

        // Constructor that initializes the views
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessageBot = itemView.findViewById(R.id.textMessageBot);
            textMessageUser = itemView.findViewById(R.id.textMessageUser);
            textTimestampBot = itemView.findViewById(R.id.textTimestampBot);
            textTimestampUser = itemView.findViewById(R.id.textTimestampUser);
        }

        // Bind data to views
        public void bind(ChatMessage message) {
            textTimestampBot.setVisibility(View.GONE); // Hide bot timestamp by default
            textTimestampUser.setVisibility(View.GONE); // Hide user timestamp by default

            if (message.isBot()) { // Check if message is from bot
                if (message.getMessage().equals("Please wait")) {
                    textMessageBot.setText("Please wait..."); // Set "Please wait" text for bot message
                    textMessageBot.setVisibility(View.VISIBLE); // Show bot message TextView
                    textMessageUser.setVisibility(View.GONE); // Hide user message TextView
                } else {
                    textMessageBot.setText(message.getMessage()); // Set bot message text
                    textTimestampBot.setText(message.getTimestamp()); // Set bot message timestamp
                    textMessageBot.setVisibility(View.VISIBLE); // Show bot message TextView
                    textTimestampBot.setVisibility(View.VISIBLE); // Show bot timestamp TextView
                    textMessageUser.setVisibility(View.GONE); // Hide user message TextView
                }
            } else { // Message is from user
                textMessageUser.setText(message.getMessage()); // Set user message text
                textTimestampUser.setText(message.getTimestamp()); // Set user message timestamp
                textMessageUser.setVisibility(View.VISIBLE); // Show user message TextView
                textTimestampUser.setVisibility(View.VISIBLE); // Show user timestamp TextView
                textMessageBot.setVisibility(View.GONE); // Hide bot message TextView
            }
        }
    }
}
