package com.github.rfsmassacre.heavenlibrary.paper.items;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class HeavenItem
{
    protected ItemStack item;

    protected Material material;
    @Getter
    protected String name;
    @Getter
    protected String displayName;
    protected NamespacedKey key;
    @Getter
    protected int customModelData;
    @Getter
    protected Recipe recipe;

    public HeavenItem(JavaPlugin instance, Material material, int amount, String name,
                      String displayName, List<String> lore)
    {
        this.item = new ItemStack(material, amount);

        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.key = new NamespacedKey(instance, name);

        this.setDisplayName(displayName);
        this.setItemLore(lore);

        //NBT
        setNBT(this.key, this.name);

        this.recipe = createRecipe();
    }

    public void setCustomModelData(int customModelData)
    {
        this.customModelData = customModelData;
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(customModelData);
        item.setItemMeta(meta);
    }

    public void setNBT(NamespacedKey key, String value)
    {
        //NBT
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return;
        }

        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(key, PersistentDataType.STRING, value);
        item.setItemMeta(meta);
    }
    public String getNBT(NamespacedKey key)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return null;
        }

        PersistentDataContainer data = meta.getPersistentDataContainer();
        return data.get(key, PersistentDataType.STRING);
    }

    public boolean equals(ItemStack itemStack)
    {
        if (itemStack == null)
        {
            return false;
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null)
        {
            return false;
        }

        PersistentDataContainer data = meta.getPersistentDataContainer();
        String value = data.get(key, PersistentDataType.STRING);
        return this.name.equals(value);
    }

    public void setDisplayName(String displayName)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return;
        }

        meta.displayName(Component.text(displayName));
        item.setItemMeta(meta);
    }
    //Adds the ID to the first line to ensure when checking, it's O(1).
    public void setItemLore(List<String> lore)
    {
        List<Component> components = new ArrayList<>();
        for (String line : lore)
        {
            components.add(Component.text(line));
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return;
        }

        meta.lore(components);
        item.setItemMeta(meta);
    }

    protected void addFlag(ItemFlag... flags)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return;
        }

        meta.addItemFlags(flags);
        item.setItemMeta(meta);
    }

    public ItemStack getItemStack()
    {
        return item;
    }

    public Material getType()
    {
        return material;
    }

    /*
     * Recipe that is needed to craft item.
     */
    protected abstract Recipe createRecipe();
}
