package org.sk.skMinecraft.commands;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft;

public class ShowTitle extends Command {

    private int player;
    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public ShowTitle(String command) {
        String[] parts = command.split(SkMinecraft.seperator);

        if(parts.length != 7) {
            this.valid = false;
            return;
        }

        this.title = parts[2];
        this.subtitle = parts[3];

        try {
            this.player = Integer.parseInt(parts[1]);
            this.fadeIn = Integer.parseInt(parts[4]);
            this.stay = Integer.parseInt(parts[5]);
            this.fadeOut = Integer.parseInt(parts[6]);
        } catch(NumberFormatException e) {
            this.valid = false;
        }
    }

    @Override
    public void apply() {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        if(this.player < 0) {
            for(int i = 0;i < players.length;i++) {
                players[i].sendTitle(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
            }    
        } else {
            if(this.player > players.length) return;
            
            players[this.player].sendTitle(this.title, this.subtitle, this.fadeIn, this.stay, this.fadeOut);
        }
        
    }

    
}
