package org.sk.skMinecraft;

import org.bukkit.entity.Entity;
import org.sk.skMinecraft.data.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class CentralResourceHandler {

    private static ArrayList<ChatMessage> chatMessages;

    public static void init() {
        CentralResourceHandler.chatMessages = new ArrayList<ChatMessage>();
    }

    public static synchronized void addChatMessage(ChatMessage message) {
        chatMessages.add(message);
    }

    public static ArrayList<ChatMessage> getChatMessages() {
        ArrayList<ChatMessage> copy = CentralResourceHandler.chatMessages;
        CentralResourceHandler.chatMessages = new ArrayList<>();

        return copy;
    }

}
