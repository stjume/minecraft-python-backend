package org.sk.skMinecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public final class SkMinecraft extends JavaPlugin {

    private Thread tcpListenerThread;
    private ServerSocket serverSocket;

    @Override
    public void onEnable() {
        startTcpListener();
    }

    @Override
    public void onDisable() {
        stopTcpListener();
    }

    private void startTcpListener() {
        tcpListenerThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(12345); // Use your desired port
                getLogger().info("TCP server started on port 12345");

                while (!serverSocket.isClosed()) {
                    Socket client = serverSocket.accept();
                    getLogger().info("Client connected: " + client.getRemoteSocketAddress());

                    // Handle the client in a new thread
                    new Thread(() -> handleClient(client)).start();
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    getLogger().severe("TCP server error: " + e.getMessage());
                }
            }
        });
        tcpListenerThread.start();
    }

    private void stopTcpListener() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (tcpListenerThread != null && tcpListenerThread.isAlive()) {
                tcpListenerThread.interrupt();
            }
            getLogger().info("TCP server stopped.");
        } catch (IOException e) {
            getLogger().severe("Failed to close TCP server: " + e.getMessage());
        }
    }

    private void handleClient(Socket client) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String command = line.trim();
                getLogger().info("Received: " + command);

                if (command.startsWith("setBlock")) {
                    String[] parts = command.split("\\s+");
                    if (parts.length == 5) {
                        try {
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            int z = Integer.parseInt(parts[3]);
                            String blockId = parts[4].toUpperCase();

                            Material material = Material.matchMaterial(blockId);
                            if (material == null || !material.isBlock()) {
                                writer.println("error invalid_block");
                                continue;
                            }

                            Bukkit.getScheduler().runTask(this, () -> {
                                World world = Bukkit.getWorlds().get(0); // default world
                                Location loc = new Location(world, x, y, z);
                                loc.getBlock().setType(material);
                            });



                        } catch (NumberFormatException e) {
                            writer.println("error bad_coords");
                        }
                    } else {
                        writer.println("error bad_args");
                    }

                } else if (command.startsWith("getPlayerLoc")) {
                    String[] parts = command.split("\\s+");
                    if (parts.length == 2) {
                        try {
                            int index = Integer.parseInt(parts[1]);
                            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

                            if (index >= 0 && index < players.length) {
                                Player target = players[index];
                                // String name = target.getDisplayName();
                                String name = target.getName();
                                Location loc = target.getLocation();
                                int x = loc.getBlockX();
                                int y = loc.getBlockY();
                                int z = loc.getBlockZ();
                                writer.println(index + " " + name + " " + x + " " + y + " " + z);
                                writer.flush();
                            } else {
                                writer.println("error invalid_index");

                            }
                        } catch (NumberFormatException e) {
                            writer.println("error bad_index");
                        }
                    } else {
                        writer.println("error bad_args");
                    }
                }

            }

            client.close();
        } catch (IOException e) {
            getLogger().severe("TCP client error: " + e.getMessage());
        }
    }




}
