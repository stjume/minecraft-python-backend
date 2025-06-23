package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;

public class PostChat extends Command {

    private final String message;

    public PostChat(String command) {
        this.message = command.replaceFirst("postChat ", "");
    }

    @Override
    public void apply() {
        Bukkit.broadcastMessage(this.message);
    }
}
