package com.github.rfsmassacre.spigot.menu;

import com.github.rfsmassacre.spigot.files.configs.Locale;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.Map.Entry;

public abstract class Menu
{
    private static final Map<UUID, Menu> VIEWERS = new HashMap<>();

    public static Menu getView(UUID playerId)
    {
        return VIEWERS.get(playerId);
    }

    public static UUID getViewer(Menu menu)
    {
        for (Entry<UUID, Menu> entry : VIEWERS.entrySet())
        {
            if (entry.getValue().equals(menu))
            {
                return entry.getKey();
            }
        }

        return null;
    }

    public static void addView(UUID playerId, Menu menu)
    {
        VIEWERS.put(playerId, menu);
    }

    public static void removeView(UUID playerId)
    {
        VIEWERS.remove(playerId);
    }

    public static Map<UUID, Menu> getViewers()
    {
        return VIEWERS;
    }

    @Getter
    protected String title;
    @Getter
    protected int rows;
    @Getter
    protected int page;
    @Getter
    protected List<Icon> icons;

    public Menu(String title, int rows, int page)
    {
        this.title = Locale.format(title);
        this.rows = rows;
        this.page = page;
        this.icons = new ArrayList<>();
    }


    public void addIcon(Icon icon)
    {
        this.icons.add(icon);
    }

    public boolean slotTaken(int slot)
    {
        for (Icon icon : icons)
        {
            if (icon.getSlot() == slot)
            {
                return true;
            }
        }

        return false;
    }

    public Inventory createInventory(Player player)
    {
        //Retrieve the menu if it's the same inventory
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);

        //Add and place all the items.
        updateIcons(player);

        for (Icon icon : icons)
        {
            inventory.setItem(icon.getSlot(), icon.getItemStack());
        }

        return inventory;
    }

    public void updateInventory(Player player)
    {
        String inventoryTitle = player.getOpenInventory().getTitle();
        if (inventoryTitle.equals(title))
        {
            Inventory inventory = player.getOpenInventory().getTopInventory();

            //Remove all icons first
            inventory.clear();
            icons.clear();

            //Add and place all the items.
            updateIcons(player);
            for (Icon icon : icons)
            {
                inventory.setItem(icon.getSlot(), icon.getItemStack());
            }
        }
    }

    public boolean inInventory(Player player)
    {
        return player.getOpenInventory().getTitle().equals(title);
    }

    public void divideIcons(List<Icon> iconList, int iconLimit, int leftY, int rightY, int topX,
            int bottomX)
    {
        List<List<Icon>> pages = Lists.partition(iconList, iconLimit);
        if (!pages.isEmpty())
        {
            for (Icon icon : pages.get(page - 1))
            {
                for (int y = leftY; y <= rightY; y++)
                {
                    boolean placed = false;
                    for (int x = topX; x <= bottomX; x++)
                    {
                        if (!slotTaken(icon.getSlot()))
                        {
                            addIcon(icon);
                            placed = true;
                            break;
                        }
                    }

                    if (placed)
                    {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Replace all icons with up-to-date ones.
     */
    public abstract void updateIcons(Player player);
}
