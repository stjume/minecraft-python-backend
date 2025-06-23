package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;

public class CommandPostChat extends Command {

    private final String message;

    public CommandPostChat(String command) {
        this.message = command.replaceFirst("postToChat ", "");
    }

    @Override
    public void apply() {
        Bukkit.broadcastMessage(this.message);
    }
}
