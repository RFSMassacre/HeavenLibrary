package com.github.rfsmassacre.heavenlibrary.paper.menu;

import com.github.rfsmassacre.heavenlibrary.interfaces.LocaleData;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Icon
{
    protected int x; //Between 1 and 9.
    protected int y; //Between 1 and 6 depending on the size of the GUI.
    protected int amount;
    protected int customModelData;
    protected boolean glowing;
    protected Material material;
    protected String displayName;
    protected List<String> lore;

    public Icon(int x, int y, int amount, int customModelData, boolean glowing, Material material, String displayName, List<String> lore)
    {
        this.x = x;
        this.y = y;
        this.amount = amount;
        this.customModelData = customModelData;
        this.glowing = glowing;
        this.material = material;
        setDisplayName(displayName);
        setLore(lore);
    }

    public Icon(int x, int y, int amount, boolean glowing, Material material, String displayName, List<String> lore)
    {
        this(x, y, amount, 0, glowing, material, displayName, lore);
    }

    public int getSlot()
    {
        return (x + ((y - 1) * 9)) - 1;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = LocaleData.format(displayName);
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
            this.lore.add(LocaleData.format(line));
        }
    }

    protected List<Component> getComponentLore()
    {
        List<Component> components = new ArrayList<>();
        for (String line : lore)
        {
            components.add(LegacyComponentSerializer.legacySection().deserialize(LocaleData.format(line))
                    .decoration(TextDecoration.ITALIC, false));
        }

        return components;
    }

    public ItemStack getItemStack()
    {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return item;
        }

        meta.displayName(LegacyComponentSerializer.legacySection().deserialize(LocaleData.format(displayName))
                .decoration(TextDecoration.ITALIC, false));
        meta.lore(getComponentLore());
        meta.setCustomModelData(customModelData);
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
