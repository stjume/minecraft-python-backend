package org.sk.skMinecraft.data;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Position {
    public int x,y,z;
    public World world;

    public static Position fromString(String position, String seperator) {
        String[] pos = position.split(seperator);


        int x = Integer.parseInt(pos[0]);
        int y = Integer.parseInt(pos[1]);
        int z = Integer.parseInt(pos[2]);

        World world = Bukkit.getWorld(pos[3]);

        return new Position(x,y,z, world);
    }

    public Position(int x, int y, int z, World world) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.world = world;
    }

}
