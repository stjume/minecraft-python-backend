package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.sk.skMinecraft.SkMinecraft;

public class PostChat extends Command {

    private final String message;

    public PostChat(String command) {
        this.message = command.replaceFirst("postChat" + SkMinecraft.seperator, "");
    }

    @Override
    public void apply() {
        Bukkit.broadcastMessage(this.message);
    }
}
