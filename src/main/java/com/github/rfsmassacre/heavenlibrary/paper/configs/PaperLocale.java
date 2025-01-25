package com.github.rfsmassacre.heavenlibrary.paper.configs;

import com.github.rfsmassacre.heavenlibrary.interfaces.LocaleData;
import com.github.rfsmassacre.heavenlibrary.paper.managers.PaperYamlManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.Arrays;

/**
 * Handles retrieving all the values from a locale file.
 */
@SuppressWarnings("unused")
public class PaperLocale extends PaperYamlManager implements LocaleData<CommandSender, Component>
{
    /**
     * JavaPlugin and name of file will give back a fully updated YamlConfiguration.
     * @param plugin JavaPlugin handling the localization.
     * @param fileName Name of file to handle.
     */
    public PaperLocale(JavaPlugin plugin, String folderName, String fileName, boolean update)
    {
        super(plugin, folderName, fileName, update);
    }

    public PaperLocale(JavaPlugin plugin, String folderName, String fileName)
    {
        super(plugin, folderName, fileName);
    }

    /**
     * Retrieve message from given key.
     * @param key Specified message assigned to.
     * @param usePrefix Use prefix with message.
     *
     * @return Message in locale file from the key.
     */
    @Override
    public String getMessage(String key, boolean usePrefix)
    {
        String prefix = this.yaml.getString("prefix", defaultYaml.getString("prefix"));
        if (key == null || key.isBlank())
        {
            return usePrefix ? prefix : "";
        }

        String message = get(key, String.class);
        if (message == null || message.isBlank())
        {
            return null;
        }

        return usePrefix ? prefix + message : message;
    }

    public void sendMessage(CommandSender receiver, boolean usePrefix, String message, String... holders)
    {
        if (message == null || message.isBlank() || receiver == null)
        {
            return;
        }

        String string =  LocaleData.format(LocaleData.replaceHolders(message, holders));
        receiver.sendMessage(LegacyComponentSerializer.legacySection().deserialize(string));
    }

    public void sendActionMessage(Player receiver, String message, String... holders)
    {
        if (message == null || message.isBlank())
        {
            return;
        }

        String string = LocaleData.format(LocaleData.replaceHolders(message, holders));
        receiver.sendActionBar(LegacyComponentSerializer.legacySection().deserialize(string));
    }

    public void sendActionLocale(Player receiver, boolean usePrefix, String key, String... holders)
    {
        sendActionMessage(receiver, getMessage(key, usePrefix), holders);
    }

    public void sendTitleMessage(Player receiver, int fadeIn, int stay, int fadeOut, String title, String subtitle,
                                 String... holders)
    {
        Title.Times times = Title.Times.times(Duration.ofSeconds(fadeIn / 20), Duration.ofSeconds(stay / 20),
                Duration.ofSeconds(fadeOut / 20));
        Title titleMessage = Title.title(toComponent(null, title, holders), toComponent(null, subtitle,
                holders), times);
        receiver.showTitle(titleMessage);
    }

    public void sendTitleLocale(Player receiver, int fadeIn, int stay, int fadeOut, String titleKey, String subtitleKey,
                                String... holders)
    {
        sendTitleMessage(receiver, fadeIn, stay, fadeOut, getMessage(titleKey), getMessage(subtitleKey), holders);
    }

    @Override
    public Component toComponent(String hover, String message, String... holders)
    {
        if (message == null)
        {
            return Component.text().build();
        }

        String string = LocaleData.format(LocaleData.replaceHolders(message, holders));
        TextComponent text = LegacyComponentSerializer.legacySection().deserialize(string);
        if (hover != null)
        {
            text = text.hoverEvent(HoverEvent.showText(LegacyComponentSerializer.legacySection()
                    .deserialize(LocaleData.format(LocaleData.replaceHolders(hover, holders)))));
        }

        return text;
    }
}
