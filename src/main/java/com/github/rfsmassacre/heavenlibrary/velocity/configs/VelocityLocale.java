package com.github.rfsmassacre.heavenlibrary.velocity.configs;

import com.github.rfsmassacre.heavenlibrary.interfaces.LocaleData;
import com.github.rfsmassacre.heavenlibrary.velocity.HeavenVelocityPlugin;
import com.github.rfsmassacre.heavenlibrary.velocity.managers.VelocityYamlManager;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles retrieving all the values from a locale file.
 */
@SuppressWarnings({"unused"})
public class VelocityLocale extends VelocityYamlManager implements LocaleData<CommandSource, TextComponent>
{
    /**
     * JavaPlugin and name of file will give back a fully updated YamlConfiguration.
     * @param fileName Name of file to handle.
     */
    public VelocityLocale(HeavenVelocityPlugin plugin, String folderName, String fileName)
    {
        super(plugin, folderName, fileName);
    }

    /**
     * Provide easy function to reload configuration without needing parameters.
     */
    public void reload()
    {
        this.yaml = read();
    }

    /**
     * Retrieve message from given key.
     * @param key Specified message assigned to.
     * @param usePrefix Use prefix with message.
     * @return Message in locale file from the key.
     */
    @Override
    public String getMessage(String key, boolean usePrefix)
    {
        String prefix = yaml.node("prefix").getString();
        if (prefix == null)
        {
            prefix = defaultYaml.node("prefix").getString();
        }

        if (key == null || key.isEmpty())
        {
            return usePrefix ? prefix : "";
        }

        String message = yaml.node(splitKeys(key)).getString();
        if (message == null)
        {
            message = defaultYaml.node(splitKeys(key)).getString();
        }

        if (message == null || message.isBlank())
        {
            prefix = "";
            message = "";
        }

        return usePrefix ? prefix + message : message;
    }

    /**
     * Send formatted string to receiver.
     * @param receiver Player or console receiving message.
     * @param message Message to be sent.
     * @param holders Words to be replaced with values.
     */
    @Override
    public void sendMessage(CommandSource receiver, boolean usePrefix, String message, String...holders)
    {
        if (receiver == null)
        {
            return;
        }

        String string = LocaleData.format(getMessage("", usePrefix) +
                LocaleData.replaceHolders(message, holders));
        receiver.sendMessage(LegacyComponentSerializer.legacySection().deserialize(string));
    }

    @Override
    public TextComponent toComponent(String hover, String message, String... holders)
    {
        if (message == null)
        {
            return Component.text().build();
        }

        String formatMessage = LocaleData.format(LocaleData.replaceHolders(message, holders));
        TextComponent text = LegacyComponentSerializer.legacySection().deserialize(formatMessage);
        if (hover != null)
        {
            String formatHover = LocaleData.format(LocaleData.replaceHolders(hover, holders));
            TextComponent textHover = LegacyComponentSerializer.legacySection().deserialize(formatHover);
            text = text.hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, textHover));
        }

        return text;
    }

    /**
     * Send formatted string to receiver.
     * @param receiver Player or console receiving message.
     * @param message Message to be sent.
     * @param holders Words to be replaced with values.
     */
    @Override
    public void sendMessage(CommandSource receiver, String message, String...holders)
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
    @Override
    public void sendLocale(CommandSource receiver, boolean usePrefix, String key, String... holders)
    {
        sendMessage(receiver, getMessage(key, usePrefix), holders);
    }

    /**
     * Send formatted locale message to receiver.
     * @param receiver Player or console receiving message.
     * @param key Specified message assigned to.
     * @param holders Words to be replaced with values.
     */
    @Override
    public void sendLocale(CommandSource receiver, String key, String...holders)
    {
        sendMessage(receiver, getMessage(key, true), holders);
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
            string = string.replaceAll("[&§][1-9]", "");
            string = string.replaceAll("[&§][a-f]", "");
            string = string.replaceAll("[&§][A-F]", "");
            string = string.replaceAll("[&§]([rR])", "");
        }
        if (!bold)
        {
            string = string.replaceAll("[&§]([lL])", "");
        }
        if (!italic)
        {
            string = string.replaceAll("[&§]([oO])", "");
        }
        if (!underline)
        {
            string = string.replaceAll("[&§]([nN])", "");
        }
        if (!strikethrough)
        {
            string = string.replaceAll("[&§]([mM])", "");
        }
        if (!magic)
        {
            string = string.replaceAll("[&§]([kK])", "");
        }

        if (!hex)
        {
            string = string.replaceAll("[&§](#)", "");
        }
        else
        {
            Pattern HEX_PATTERN = Pattern.compile("([§&])(#[A-Fa-f0-9]{6})");
            Matcher matcher = HEX_PATTERN.matcher(string.replaceAll("([§&])", ""));
            while (matcher.find())
            {
                TextColor hexColor = TextColor.fromHexString(matcher.group());
                string = string.replace(matcher.group(), "" + hexColor);
            }
        }

        string = string.replaceAll("&", "§");
        TextComponent component = Component.text(string);
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}
