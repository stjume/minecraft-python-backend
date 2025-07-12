package org.sk.skMinecraft;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.sk.skMinecraft.commands.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public final class SkMinecraft extends JavaPlugin implements Listener {

    private Thread tcpListenerThread;
    private ServerSocket serverSocket;

    @Override
    public void onEnable() {
        CentralResourceHandler.init();
        getServer().getPluginManager().registerEvents(this, this);
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

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        CentralResourceHandler.addChatMessage(event.getMessage());
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
                PrintWriter writer = new PrintWriter(client.getOutputStream(), true);

        ) {
            CommandFactory commandFactory = new CommandFactory();

            String line;
            while ((line = reader.readLine()) != null) {
                final String command = line.trim();
                if (line.isEmpty()) continue;

                getLogger().info("Received: " + command);

                Command command_obj = commandFactory.build(command);

                if(!command_obj.isValid()) continue;

                command_obj.setParameters(writer, this);

                command_obj.apply();
            }

            client.close();
        } catch (IOException e) {
            getLogger().severe("TCP client error: " + e.getMessage());
        }
    }
 }
