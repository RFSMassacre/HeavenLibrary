package com.github.rfsmassacre.spigot.nms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketUtil
{
    public static Class<?> getNMSClass(String name)
    {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try
        {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException exception)
        {
            exception.printStackTrace();
            return null;
        }
    }

    public static void sendPacket(Player player, Object packet)
    {
        try
        {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket",
                    getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
