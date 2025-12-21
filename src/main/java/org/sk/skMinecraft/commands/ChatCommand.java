package org.sk.skMinecraft.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.sk.skMinecraft.SkMinecraft.StringCommand;

public class ChatCommand extends Command {

    private String command;

    public ChatCommand(StringCommand command) {
        this.command = Arrays.stream(command.arguments()).toString();
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
        });
    }
}