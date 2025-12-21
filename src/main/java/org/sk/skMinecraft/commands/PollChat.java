package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.sk.skMinecraft.CentralResourceHandler;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.data.ChatMessage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PollChat extends Command {

    public PollChat(StringCommand s) {}

    private String formatMessage(ChatMessage message) {
        StringBuilder builder = new StringBuilder();
        
        builder.append(message.player.getName());
        builder.append(":");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy|MM|dd|HH|mm|ss");
        builder.append(message.timestamp.format(formatter));
        builder.append(":");
        
        builder.append(SkMinecraft.playerIndexFromName(message.player.getName()));
        builder.append(":");

        builder.append(message.message);

        return builder.toString();
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            ArrayList<ChatMessage> messages = CentralResourceHandler.getChatMessages();
            String output = messages.stream()
                .map(message -> formatMessage(message))
                .reduce("", (arg, msg) -> arg == "" ? msg : arg + SkMinecraft.seperator + msg);;

            this.writer.println(output);
        });
    }
}
