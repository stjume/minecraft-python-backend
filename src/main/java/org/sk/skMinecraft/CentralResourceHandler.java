package org.sk.skMinecraft;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class CentralResourceHandler {

    private static ArrayList<String> chatMessages;

    public static void init() {
        CentralResourceHandler.chatMessages = new ArrayList<String>();
    }

    public static synchronized void addChatMessage(String message) {
        chatMessages.add(message);
    }

    public static ArrayList<String> getChatMessages() {
        ArrayList<String> copy = CentralResourceHandler.chatMessages;
        CentralResourceHandler.chatMessages = new ArrayList<>();

        return copy;
    }

}
