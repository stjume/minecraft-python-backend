package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.sk.skMinecraft.CentralResourceHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class PollChat extends Command {

    public PollChat(String s) {
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(plugin, () -> {
            ArrayList<String> messages = CentralResourceHandler.getChatMessages();

            String output = String.join("|<-->|", messages);
            this.writer.println(output);
        });
    }
}
