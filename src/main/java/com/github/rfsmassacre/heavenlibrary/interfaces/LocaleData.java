package com.github.rfsmassacre.heavenlibrary.interfaces;

import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused"})
public interface LocaleData<T, C> extends ReloadableData
{
    /**
     * Retrieve message from given key.
     * @param key Specified message assigned to.
     * @return Specified message in locale file from the key.
     */
    default String getMessage(String key)
    {
        return getMessage(key, false);
    }

    /**
     * Retrieve message from given key.
     * @param key Specified message assigned to.
     * @param usePrefix Use prefix with message.
     * @return Message in locale file from the key.
     */
    String getMessage(String key, boolean usePrefix);

    /**
     * Easily replaces words in pairs sequentially.
     * @param string String to replace holders with values.
     * @param holders Absolutely must be divisible by two, or it will throw an error.
     * @return Formatted string.
     */
    static String replaceHolders(String string, String... holders)
    {
        for (int holder = 0; holder < holders.length; holder += 2)
        {
            string = string.replaceAll(Pattern.quote(holders[holder]), Matcher.quoteReplacement(holders[holder + 1]));
        }

        return string;
    }

    /**
     * Send formatted string to receiver.
     * @param receiver Player or console receiving message.
     * @param message Message to be sent.
     * @param holders Words to be replaced with values.
     */
    void sendMessage(T receiver, boolean usePrefix, String message, String... holders);

    C toComponent(String hover, String message, String... holders);

    /**
     * Send formatted string to receiver.
     * @param receiver Player or console receiving message.
     * @param message Message to be sent.
     * @param holders Words to be replaced with values.
     */
    default void sendMessage(T receiver, String message, String... holders)
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
    default void sendLocale(T receiver, boolean usePrefix, String key, String... holders)
    {
        sendMessage(receiver, getMessage(key, usePrefix), holders);
    }

    /**
     * Send formatted locale message to receiver.
     * @param receiver Player or console receiving message.
     * @param key Specified message assigned to.
     * @param holders Words to be replaced with values.
     */
    default void sendLocale(T receiver, String key, String... holders)
    {
        sendLocale(receiver, false, key, holders);
    }

    /**
     * Format string with colors, bolds, italics, underlines, or magic characters.
     *
     * @param string String to format.
     * @return Formatted string.
     */
    static @NotNull String format(String string)
    {
        return format(string, true, true, true, true, true, true, true);
    }

    static String undoFormat(String string)
    {
        return string.replaceAll("§", "&");
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
    static String format(String string, boolean color, boolean bold, boolean italic, boolean underline,
                         boolean strikethrough, boolean magic, boolean hex)
    {
        if (string == null)
        {
            return null;
        }

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

        return string.replaceAll("&", "§");
    }

    static List<String> formatLore(List<String> lore)
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
    static String stripColors(String string)
    {
        Pattern HEX_PATTERN = Pattern.compile("([§&])(#[A-Fa-f0-9]{6})");
        Matcher matcher = HEX_PATTERN.matcher(string);
        while (matcher.find())
        {
            string = string.replace(matcher.group(), matcher.group().replaceAll("([§&])(#[A-Fa-f0-9]{6})",
                    ""));
        }

        string = string.replaceAll("([§&])[0-9a-fk-or]", "");
        return string;
    }

    /**
     * Format the string to have a proper capitalized title. Also removes underscores with spaces.
     * @param string String to format.
     * @return Formatted string.
     */
    static String capitalize(String string)
    {
        return WordUtils.capitalizeFully(string.toLowerCase().replace("_", " "));
    }

    static String formatTime(double seconds)
    {
        return formatTime(false, seconds, false);
    }

    static String formatTime(double seconds, boolean shortHand)
    {
        return formatTime(false, seconds, shortHand);
    }

    static String formatTime(boolean showZero, double rawSeconds, boolean shortHand)
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
            hourPlural = hours + " Hr";
            minutePlural = minutes + " Min";
            secondPlural = remainingSeconds + " Sec";
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

        if (remainingSeconds == 0 & !showZero)
        {
            secondPlural = "";
        }

        return hourPlural + minutePlural + secondPlural;
    }
}
