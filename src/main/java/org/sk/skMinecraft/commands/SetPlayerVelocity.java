package org.sk.skMinecraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.sk.skMinecraft.SkMinecraft.StringCommand;
import org.sk.skMinecraft.commands.ArgumentParser.ParseResult;

public class SetPlayerVelocity extends Command {

    private enum Type {
        UP,
        DOWN,
        BACK,
        LOOKING,
        VECTOR
    };

    private Type type;
    private int playerIndex;
    private double strength;
    private boolean add;
    private double x, y, z;

    public SetPlayerVelocity(StringCommand command) {
        ArgumentParser parser = new ArgumentParser();

        parser.addPositionalArguments(
                ArgumentParser.StringParser,
                ArgumentParser.IntParser,
                ArgumentParser.DoubleParser);

        parser.addOptionalArgument("x", ArgumentParser.DoubleParser, 0);
        parser.addOptionalArgument("y", ArgumentParser.DoubleParser, 0);
        parser.addOptionalArgument("z", ArgumentParser.DoubleParser, 0);

        parser.addFlag("add");

        ParseResult result = parser.parse(command.arguments());

        if (!result.isValid()) {
            this.valid = false;
            return;
        }

        this.type = Type.valueOf(result.getPositional(0));
        this.playerIndex = result.getPositional(1);
        this.strength = result.getPositional(2);
        this.add = result.getFlag("add");

        this.x = result.getOptional("x");
        this.y = result.getOptional("y");
        this.z = result.getOptional("z");
    }

    @Override
    public void apply() {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            if (playerIndex < 0 || playerIndex >= players.length) {
                return;
            }

            Player target = players[this.playerIndex];

            switch (this.type) {
                case UP -> useNewVelocity(target, new Vector(0, 1, 0).multiply(this.strength));
                case DOWN -> useNewVelocity(target, new Vector(0, -1, 0).multiply(this.strength));
                case BACK -> {
                    Vector backDir = target.getLocation().getDirection();
                    backDir.setY(0); // remove vertical component
                    if (backDir.lengthSquared() == 0) {
                        backDir = new Vector(0, 0, -1); // fallback just in case
                    }
                    useNewVelocity(target, backDir.normalize().multiply(-this.strength));
                }
                case LOOKING ->
                    useNewVelocity(target, target.getLocation().getDirection().normalize().multiply(this.strength));
                case VECTOR ->
                    useNewVelocity(target, new Vector(this.x, this.y, this.z).normalize().multiply(this.strength));

            }
        });
    }

    private void useNewVelocity(Player target, Vector velocity) {
        if (this.add) {
            target.setVelocity(target.getVelocity().add(velocity));
        } else {
            target.setVelocity(velocity);
        }
    }
}
