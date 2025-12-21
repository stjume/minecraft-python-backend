package org.sk.skMinecraft.data;

import java.time.LocalDateTime;

import org.bukkit.entity.Player;

public class ChatMessage {

    public Player player;
    public String message;
    public LocalDateTime timestamp;

    public ChatMessage(Player player, String message, LocalDateTime timestamp) {
        this.player = player;
        this.message = message;
        this.timestamp = timestamp;
    }    
}