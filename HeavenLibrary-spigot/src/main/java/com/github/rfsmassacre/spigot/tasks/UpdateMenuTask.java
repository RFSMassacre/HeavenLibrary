package com.github.rfsmassacre.spigot.tasks;

import com.github.rfsmassacre.spigot.menu.Menu;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateMenuTask extends BukkitRunnable
{
    @Override
    public void run()
    {
        Menu.update();
    }
}
