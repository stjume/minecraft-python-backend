package org.sk.skMinecraft.data;

import org.bukkit.entity.Player;

public class ChatMessage {

    public Player player;
    public String message;

    public ChatMessage(Player player, String message) {
        this.player = player;
        this.message = message;
    }    
}