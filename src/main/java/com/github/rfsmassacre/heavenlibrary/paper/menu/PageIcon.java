package com.github.rfsmassacre.heavenlibrary.paper.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PageIcon extends Icon
{
    protected final Menu menu;

    public PageIcon(int x, int y, String displayName, Menu menu)
    {
        this(x, y, displayName, Material.BLACK_STAINED_GLASS_PANE, menu);
    }
    public PageIcon(int x, int y, String displayName, Material material, Menu menu)
    {
        super(x, y, 1, false, material, displayName, new ArrayList<>());

        this.menu = menu;
    }

    @Override
    public void onClick(Player clicker)
    {
        Menu.addView(clicker.getUniqueId(), menu);
        clicker.openInventory(menu.createInventory(clicker));
    }
}
