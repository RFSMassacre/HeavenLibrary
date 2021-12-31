package com.github.rfsmassacre.spigot.files.configs;

import com.github.rfsmassacre.spigot.files.YamlManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Handles retrieving all the values from a locale file.
 */
public class Locale extends YamlManager
{
    private String fileName;

    /**
     * JavaPlugin and name of file will give back a fully updated YamlConfiguration.
     * @param plugin JavaPlugin handling the localization.
     * @param fileName Name of file to handle.
     */
    public Locale(JavaPlugin plugin, String folderName, String fileName)
    {
        super(plugin, folderName, fileName);

        this.fileName = fileName;
    }

    /**
     * Provide easy function to reload configuration without needing parameters.
     */
    public void reload()
    {
        this.yaml = read(fileName);
    }

    /**
     * Retrieve message from given key.
     * @param key Specified message assigned to.
     * @return Specified message in locale file from the key.
     */
    private String getMessage(String key)
    {
        String message = yaml.getString(key, defaultYaml.getString(key));
        if (message == null)
        {
            message = "";
        }
        return message;
    }

    /**
     * Retrieve message from given key.
     * @param key Specified message assigned to.
     * @param usePrefix Use prefix with message.
     * @return Message in locale file from the key.
     */
    private String getMessage(String key, boolean usePrefix)
    {
        String prefix = yaml.getString("prefix");
        String message = yaml.getString(key, defaultYaml.getString(key));
        if (message == null || message.isEmpty())
        {
            prefix = "";
            message = "";
        }
        return usePrefix ? prefix + message : message;
    }

    /**
     * Easily replaces words in pairs sequentially.
     * @param string String to replace holders with values.
     * @param holders Absolutely must be divisible by two, or it will throw an error.
     * @return Formatted string.
     */
    private String replaceHolders(String string, String[] holders)
    {
        for (int holder = 0; holder < holders.length; holder += 2)
        {
            string = string.replace(holders[holder], holders[holder + 1]);
        }
        return string;
    }

    /**
     * Send formatted string to receiver.
     * @param receiver Player or console receiving message.
     * @param message Message to be sent.
     * @param holders Words to be replaced with values.
     */
    public void sendMessage(CommandSender receiver, String message, String...holders)
    {
        receiver.sendMessage(format(replaceHolders(message, holders)));
    }

    /**
     * Send formatted locale message to receiver.
     * @param receiver Player or console receiving message.
     * @param key Specified message assigned to.
     * @param usePrefix Use prefix with message.
     * @param holders Words to be replaced with values.
     */
    public void sendLocale(CommandSender receiver, boolean usePrefix, String key, String...holders)
    {
        sendMessage(receiver, getMessage(key, usePrefix), holders);
    }

    /**
     * Send formatted string to receiver's action bar.
     * @param player Player receiving message.
     * @param message Message to be sent.
     * @param holders Words to be replaced with values.
     */
    public void sendActionMessage(Player player, String message, String... holders)
    {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent(TextComponent.fromLegacyText(replaceHolders(message, holders))));
    }

    /**
     * Send formatted locale message to receiver's action bar.
     * @param player Player receiving message.
     * @param key Specified message assigned to.
     * @param usePrefix Use prefix with message.
     * @param holders Words to be replaced with values.
     */
    public void sendActionLocale(Player player, boolean usePrefix, String key, String...holders)
    {
        sendActionMessage(player, getMessage(key, usePrefix), holders);
    }

    /**
     * Send formatted string to receiver's title screen.
     * @param player Player receiving message.
     * @param fadeIn Ticks it takes to fade in.
     * @param stay Ticks it takes to stay on screen.
     * @param fadeOut Ticks it takes to fade out.
     * @param title The top portion of the title screen where the string will be shown.
     * @param subtitle The bottom portion of the title screen where the string will be shown.
     * @param replacers Words to be replaced with values.
     */
    public void sendTitleMessage(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle,
                                 String...replacers)
    {
        title = format(replaceHolders(title, replacers));
        subtitle = format(replaceHolders(subtitle, replacers));

        player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
    }

    /**
     * Send formatted locale message to receiver's title screen.
     * @param player Player receiving message.
     * @param usePrefix Use prefix with message.
     * @param fadeIn Ticks it takes to fade in.
     * @param stay Ticks it takes to stay on screen.
     * @param fadeOut Ticks it takes to fade out.
     * @param titleKey Specified message assigned to the top of the title screen.
     * @param subtitleKey Specified message assigned to the bottom of the title screen.
     * @param replacers Words to be replaced with values.
     */
    public void sendTitleLocale(Player player, boolean usePrefix, int fadeIn, int stay, int fadeOut, String titleKey,
                                String subtitleKey, String...replacers)
    {
        String title = usePrefix ? getMessage("prefix") + getMessage(titleKey) : getMessage(titleKey);
        String subtitle = usePrefix ? getMessage("prefix") + getMessage(subtitleKey) : getMessage(subtitleKey);

        sendTitleMessage(player, fadeIn, stay, fadeOut, title, subtitle, replacers);
    }

    /**
     * Format string with colors, bolds, italics, underlines, or magic characters.
     * @param string String to format.
     * @return Formatted string.
     */
    public static String format(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Format string with colors, bolds, italics, underlines, or magic characters if enabled.
     * @param string String to format.
     * @param color Color.
     * @param bold Bold.
     * @param italic Italic.
     * @param underline Underline.
     * @param strikethrough Strikethrough.
     * @param magic Magic.
     * @return Formatted string with only enabled parts.
     */
    public static String format(String string, boolean color, boolean bold, boolean italic, boolean underline,
                                boolean strikethrough, boolean magic)
    {
        if (!color)
        {
            string = string.replaceAll("\\§[1-9]", "");
            string = string.replaceAll("\\§[a-f]", "");
            string = string.replaceAll("\\&[1-9]", "");
            string = string.replaceAll("\\&[a-f]", "");
            string = string.replaceAll("\\§[A-F]", "");
            string = string.replaceAll("\\&[A-F]", "");
            string = string.replaceAll("\\&(r|R)", "");
        }
        if (!bold)
        {
            string = string.replaceAll("\\§(l|L)", "");
            string = string.replaceAll("\\&(l|L)", "");
        }
        if (!italic)
        {
            string = string.replaceAll("\\§(o|O)", "");
            string = string.replaceAll("\\&(o|O)", "");
        }
        if (!underline)
        {
            string = string.replaceAll("\\§(n|N)", "");
            string = string.replaceAll("\\&(n|N)", "");
        }
        if (!strikethrough)
        {
            string = string.replaceAll("\\§(m|M)", "");
            string = string.replaceAll("\\&(m|M)", "");
        }
        if (!magic)
        {
            string = string.replaceAll("\\§(k|K)", "");
            string = string.replaceAll("\\&(k|K)", "");
        }

        return format(string);
    }

    /**
     * Strips away the format given from format(String).
     * @param string String to strip.
     * @return Stripped string.
     */
    public static String stripColors(String string)
    {
        return ChatColor.stripColor(format(string));
    }

    /**
     * Format the string to have a proper capitalized title. Also removes underscores with spaces.
     * @param string String to format.
     * @return Formatted string.
     */
    public static String capitalize(String string)
    {
        return WordUtils.capitalizeFully(string.toLowerCase().replace("_", " "));
    }
}
