package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.sk.skMinecraft.CentralResourceHandler;
import org.sk.skMinecraft.SkMinecraft;

import java.util.ArrayList;

public class PollChat extends Command {

    public PollChat(String s) {
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            ArrayList<String> messages = CentralResourceHandler.getChatMessages();

            String output = String.join(SkMinecraft.seperator, messages);
            this.writer.println(output);
        });
    }
}
