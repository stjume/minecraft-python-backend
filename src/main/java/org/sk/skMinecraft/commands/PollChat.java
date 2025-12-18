package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.sk.skMinecraft.CentralResourceHandler;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.data.ChatMessage;

import java.util.ArrayList;

public class PollChat extends Command {

    public PollChat(String s) {
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            ArrayList<ChatMessage> messages = CentralResourceHandler.getChatMessages();
            String output = messages.stream()
                .map(message -> message.player.getName() +  ":" + message.message)
                .reduce("", (arg, msg) -> arg == "" ? msg : arg + SkMinecraft.seperator + msg);

            this.writer.println(output);
        });
    }
}
