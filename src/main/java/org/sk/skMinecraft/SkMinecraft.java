package org.sk.skMinecraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.sk.skMinecraft.commands.Command;
import org.sk.skMinecraft.data.ChatMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;

public final class SkMinecraft extends JavaPlugin implements Listener {

    private Thread tcpListenerThread;
    private ServerSocket serverSocket;

    public static String seperator = "𝇉";
    public record StringCommand(String name, String[] arguments) {};

    public static String joinWithSeperator(Object... args) {
        String result = "";

        for(int i = 0;i < args.length;i++){

            Object arg_i = args[i];
            if (arg_i == null){
                arg_i = "null";
            }

            result += arg_i.toString();
            if(i < args.length - 1) {
                result += SkMinecraft.seperator;
            }
        }  

        return result;
    } 

    public static StringCommand splitCommand(String command) {
        String[] parts = command.split(SkMinecraft.seperator);
        return new StringCommand(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
    }

    public static int playerIndexFromName(String name) {
        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        for(int i = 0;i < players.length;i++){
            if(players[i].getName().equals(name)) {
                return i;
            }
        }

        return -1;
    }

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
        ChatMessage message = new ChatMessage(event.getPlayer(), event.getMessage(), LocalDateTime.now());
        CentralResourceHandler.addChatMessage(message);
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

                StringCommand stringCommand = splitCommand(command);
                Command commandObject = commandFactory.build(stringCommand);

                if(commandObject == null) continue;

                if(!commandObject.isValid()) {
                    System.out.println("Invalid");
                    continue;
                }

                commandObject.setParameters(writer, this);

                commandObject.apply();
            }

            client.close();
        } catch (IOException e) {
            getLogger().severe("TCP client error: " + e.getMessage());
        }
    }
 }
