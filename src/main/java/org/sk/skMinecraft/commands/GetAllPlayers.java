package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft;
import org.sk.skMinecraft.SkMinecraft.StringCommand;

public class GetAllPlayers extends Command {

    public GetAllPlayers(StringCommand command) {}

    @Override
    public void apply() {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        String result = "";
        for(int i = 0;i < players.length;i++) {
            result += players[i].getName();
            if(i != players.length -1) result += SkMinecraft.seperator;
        }

        this.writer.println(result);
    }
    
}
