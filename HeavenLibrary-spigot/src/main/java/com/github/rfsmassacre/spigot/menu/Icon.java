package com.github.rfsmassacre.spigot.menu;

import com.github.rfsmassacre.spigot.files.configs.Locale;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class Icon
{
    @Getter
    protected int x; //Between 1 and 9.
    @Getter
    protected int y; //Between 1 and 6 depending on the size of the GUI.
    @Getter
    protected int amount;
    @Getter
    protected boolean glowing;
    @Getter
    protected Material material;
    @Getter
    protected String displayName;
    @Getter
    protected List<String> lore;

    public Icon(int x, int y, int amount, boolean glowing, Material material, String displayName, List<String> lore)
    {
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.glowing = glowing;
        this.material = material;
        setDisplayName(displayName);
        setLore(lore);
    }

    public int getSlot()
    {
        return (x + ((y - 1) * 9)) - 1;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = Locale.format(displayName);
    }

    public void setLore(List<String> lore)
    {
        if (this.lore == null)
        {
            this.lore = new ArrayList<>();
        }

        this.lore.clear();
        for (String line : lore)
        {
            this.lore.add(Locale.format(line));
        }
    }

    public ItemStack getItemStack()
    {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return item;
        }

        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (glowing)
        {
            meta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Implement method to run when player clicks icon.
     * @param player Player clicking icon.
     */
    public abstract void onClick(Player player);
}
