package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class ShowTitle extends Command {

    private int player;
    private String title;
    private String subtitle;
    private int fadeIn;
    private int stay;
    private int fadeOut;

    public ShowTitle(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
            ArgumentParser.IntParser,
            ArgumentParser.StringParser,
            ArgumentParser.StringParser,
            ArgumentParser.IntParser,
            ArgumentParser.IntParser,
            ArgumentParser.IntParser
        );

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        this.player = result.getPositional(0);
        this.title = result.getPositional(1);
        this.subtitle = result.getPositional(2);
        this.fadeIn = result.getPositional(3);
        this.stay = result.getPositional(4);
        this.fadeOut = result.getPositional(5);
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
