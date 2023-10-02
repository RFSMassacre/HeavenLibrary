package com.github.rfsmassacre.spigot.items;

import com.github.rfsmassacre.spigot.files.configs.Locale;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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
        addNBT("IID", this.name);

        this.recipe = createRecipe();
    }

    public void addNBT(String id, String value)
    {
        //NBT
        NBTItem nbtItem = new NBTItem(getItemStack());
        NBTCompound compound = nbtItem.getCompound("HeavenPlugin");
        if (compound == null)
        {
            compound = nbtItem.addCompound("HeavenPlugin");
        }

        compound.setString(id, value);
        nbtItem.applyNBT(item);
    }
    public String getNBT(String id)
    {
        NBTItem nbtItem = new NBTItem(getItemStack());
        NBTCompound compound = nbtItem.getCompound("HeavenPlugin");
        if (compound == null)
        {
            return null;
        }

        return compound.getString(id);
    }

    public boolean equals(ItemStack itemStack)
    {
        if (itemStack == null || itemStack.getType().equals(Material.AIR))
        {
            return false;
        }

        NBTItem otherItem = new NBTItem(itemStack);
        NBTCompound compound = otherItem.getCompound("HeavenPlugin");
        if (compound == null)
        {
            //Bukkit.broadcastMessage("Is Null!");
            return false;
        }

        String value = compound.getString("IID");
        //Bukkit.broadcastMessage(value);
        return this.name.equals(value);
    }

    public void setDisplayName(String displayName)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return;
        }

        meta.setDisplayName(Locale.format(displayName));
        item.setItemMeta(meta);
    }
    //Adds the ID to the first line to ensure when checking, it's O(1).
    public void setItemLore(List<String> lore)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
        {
            return;
        }

        meta.setLore(lore);
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
