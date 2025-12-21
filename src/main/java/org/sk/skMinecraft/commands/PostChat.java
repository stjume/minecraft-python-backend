package org.sk.skMinecraft.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.sk.skMinecraft.SkMinecraft.StringCommand;

public class PostChat extends Command {

    private final String message;

    public PostChat(StringCommand command) {
        this.message = Arrays.stream(command.arguments()).toString();
    }

    @Override
    public void apply() {
        Bukkit.broadcastMessage(this.message);
    }
}
