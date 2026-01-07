package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class GetPlayerByName extends Command {

    private String name;

    public GetPlayerByName(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(ArgumentParser.StringParser);

        ParseResult result = parser.parse(command.arguments());

        if(!result.isValid()) {
            this.valid = false;
            return;
        }

        name = result.getPositional(0);
    }

    @Override
    public void apply() {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        Player target = null;
        int playerIndex = 0;
        for(int i = 0;i < players.length;i++) {
            if(players[i].getName().equals(name)) {
                target = players[i];
                playerIndex = i;
                break;
            }
        }
        
        if(target == null) {
            writer.println("error player_not_found");
            return;
        }

        String result = GetPlayer.convertPlayerToString(target, playerIndex);

        writer.println(result);
    }
    
}
