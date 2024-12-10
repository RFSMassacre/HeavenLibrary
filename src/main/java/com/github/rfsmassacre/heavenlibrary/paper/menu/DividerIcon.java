package com.github.rfsmassacre.heavenlibrary.paper.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DividerIcon extends Icon
{
    public DividerIcon(int x, int y)
    {
        this(x, y, Material.BLACK_STAINED_GLASS_PANE);
    }
    public DividerIcon(int x, int y, Material material)
    {
        super(x, y, 1, false, material,"", new ArrayList<>());
    }

    @Override
    public void onClick(Player clicker)
    {
        //Do nothing
    }
}
