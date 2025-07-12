package org.sk.skMinecraft;

import org.bukkit.entity.Entity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CentralResourceHandler {

    private static ArrayList<String> chatMessages;
    private static HashMap<String, Entity> spawnedEntites;

    public static void init() {
        CentralResourceHandler.spawnedEntites = new HashMap<>();
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

    public static void addEntity(String id, Entity ent) {
        CentralResourceHandler.spawnedEntites.put(id, ent);
    }

    public static Entity getEntity(String id) {
        return CentralResourceHandler.spawnedEntites.get(id);
    }

}
