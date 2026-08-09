package org.sk.skMinecraft.data;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Position {
    public double x,y,z;
    public World world;

    public static Position fromString(String position, String seperator) {
        String[] pos = position.split(seperator);


        double x = Double.parseDouble(pos[0]);
        double y = Double.parseDouble(pos[1]);
        double z = Double.parseDouble(pos[2]);

        World world = Bukkit.getWorld(pos[3]);

        return new Position(x,y,z, world);
    }

    public Position(double x, double y, double z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.world = world;
    }

}
