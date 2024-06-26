package com.example.bobo.model;

public class ChatHistory {
    private final long chatId;
    private final String title;

    public ChatHistory(long chatId, String title) {
        this.chatId = chatId;
        this.title = title;
    }

    public long getChatId() {
        return chatId;
    }

    public String getTitle() {
        return title;
    }
}
