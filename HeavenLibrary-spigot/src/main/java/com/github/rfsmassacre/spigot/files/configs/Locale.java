package com.github.rfsmassacre.spigot.files.configs;

import com.github.rfsmassacre.spigot.files.YamlManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles retrieving all the values from a locale file.
 */
public class Locale extends YamlManager
{
    private final String fileName;

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
    public String getMessage(String key)
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
    public String getMessage(String key, boolean usePrefix)
    {
        String prefix = yaml.getString("prefix");
        if (key == null || key.isEmpty())
        {
            return usePrefix ? prefix : "";
        }

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
    public String replaceHolders(String string, String[] holders)
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
    public void sendMessage(CommandSender receiver, boolean usePrefix, String message, String...holders)
    {
        if (receiver == null)
        {
            return;
        }

        receiver.sendMessage(format(getMessage("", usePrefix) + replaceHolders(message, holders)));
    }

    /**
     * Send formatted string to receiver.
     * @param receiver Player or console receiving message.
     * @param message Message to be sent.
     * @param holders Words to be replaced with values.
     */
    public void sendMessage(CommandSender receiver, String message, String...holders)
    {
        sendMessage(receiver, false, message, holders);
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
     * Send formatted locale message to receiver.
     * @param receiver Player or console receiving message.
     * @param key Specified message assigned to.
     * @param holders Words to be replaced with values.
     */
    public void sendLocale(CommandSender receiver, String key, String...holders)
    {
        sendMessage(receiver, getMessage(key, true), holders);
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
                new TextComponent(TextComponent.fromLegacyText(format(replaceHolders(message, holders)))));
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
        return format(string, true, true, true, true, true, true, true);
    }

    public static String undoFormat(String string)
    {
        return string.replace("§", "&");
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
                                boolean strikethrough, boolean magic, boolean hex)
    {
        if (!color)
        {
            string = string.replaceAll("\\§[1-9]", "");
            string = string.replaceAll("\\§[a-f]", "");
            string = string.replaceAll("\\&[1-9]", "");
            string = string.replaceAll("\\&[a-f]", "");
            string = string.replaceAll("\\§[A-F]", "");
            string = string.replaceAll("\\&[A-F]", "");
            string = string.replaceAll("\\§(r|R)", "");
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

        if (!hex)
        {
            string = string.replaceAll("\\§(#)", "");
            string = string.replaceAll("\\&(#)", "");
        }
        else
        {
            Pattern HEX_PATTERN = Pattern.compile("(\\§|\\&)(#[A-Fa-f0-9]{6})");
            Matcher matcher = HEX_PATTERN.matcher(string);
            while (matcher.find())
            {
                string = string.replace(matcher.group(), "" + net.md_5.bungee.api.ChatColor.of(matcher.group()
                        .replaceAll("(\\§|\\&)", "")));
            }
        }

        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> formatLore(List<String> lore)
    {
        List<String> newLore = new ArrayList<>();
        for (String line : lore)
        {
            newLore.add(format(line));
        }

        return newLore;
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

    public static String formatTime(double seconds)
    {
        return formatTime(false, seconds, false);
    }

    public static String formatTime(double seconds, boolean shortHand)
    {
        return formatTime(false, seconds, shortHand);
    }

    public static String formatTime(boolean showZero, double rawSeconds, boolean shortHand)
    {
        if (rawSeconds <= 0)
        {
            return "";
        }

        if (rawSeconds < 1.0)
        {
            return String.format("%.1f", rawSeconds) + (shortHand ? " S" : " Seconds");
        }

        int seconds = (int)rawSeconds;
        int hours = seconds / 3600;
        int minutes = seconds % 3600 / 60;
        int remainingSeconds = seconds % 60;

        String hourPlural = hours == 1 ? hours + " Hour" : hours + " Hours";
        String minutePlural = minutes == 1 ? minutes + " Minute" : minutes + " Minutes";
        String secondPlural = remainingSeconds == 1 ? remainingSeconds + " Second" : remainingSeconds + " Seconds";

        if (shortHand)
        {
            hourPlural = hours + " H";
            minutePlural = minutes + " M";
            secondPlural = seconds + " S";
        }

        //Format time to be exact. (I'm picky.)
        if (hours == 0 & !showZero)
        {
            hourPlural = "";
        }
        else if (minutes > 0)
        {
            hourPlural += ", ";
        }

        if (minutes == 0 & !showZero)
        {
            minutePlural = "";
        }
        else if (remainingSeconds > 0)
        {
            minutePlural += ", ";
        }

        return hourPlural + minutePlural + secondPlural;
    }
}
