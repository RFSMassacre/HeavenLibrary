package com.github.rfsmassacre.heavenlibrary.paper.menu;

import com.github.rfsmassacre.heavenlibrary.interfaces.LocaleData;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class PlayerIcon extends Icon
{
    protected OfflinePlayer target;

    public PlayerIcon(int x, int y, OfflinePlayer target)
    {
        super(x, y, 1, false, Material.PLAYER_HEAD, "&f" + target.getName(),
                new ArrayList<>());

        this.target = target;
    }

    @Override
    public ItemStack getItemStack()
    {
        ItemStack item = new ItemStack(material, amount);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(LocaleData.format(displayName)));
        meta.setOwningPlayer(target);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void onClick(Player player)
    {
        //Do nothing
    }
}
