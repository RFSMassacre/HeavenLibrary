package com.github.rfsmassacre.spigot.menu;

import com.github.rfsmassacre.spigot.files.configs.Locale;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    public static void update()
    {
        for (Iterator<UUID> iterator = VIEWERS.keySet().iterator(); iterator.hasNext();)
        {
            UUID viewerId = iterator.next();
            Player player = Bukkit.getPlayer(viewerId);
            if (player == null)
            {
                continue;
            }

            Menu menu = VIEWERS.get(player.getUniqueId());
            String title = player.getOpenInventory().getTitle();
            if (menu.getTitle().equals(title))
            {
                menu.updateInventory(player);
            }
            else
            {
                iterator.remove();
            }
        }
    }

    @Getter
    protected String title;
    @Getter
    protected int rows;
    @Getter
    protected int page;
    @Getter
    protected Map<Integer, Icon> icons;

    public Menu(String title, int rows, int page)
    {
        this.title = Locale.format(title);
        this.rows = rows;
        this.page = page;
        this.icons = new HashMap<>();
    }


    public void addIcon(Icon icon)
    {
        this.icons.put(icon.getSlot(), icon);
    }

    public boolean slotTaken(int slot)
    {
        return icons.containsKey(slot);
    }

    public Inventory createInventory(Player player)
    {
        //Retrieve the menu if it's the same inventory
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);

        //Add and place all the items.
        updateIcons(player);

        for (Icon icon : icons.values())
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
            for (Icon icon : icons.values())
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
