package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;

public class ChatCommand extends Command {

    private String command;

    public ChatCommand(String command) {
        this.command = command.replace("chatCommand ", "");
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.command);
        });
    }
}