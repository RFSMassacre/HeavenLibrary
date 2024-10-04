package com.github.rfsmassacre.heavenlibrary.paper.tasks;

import com.github.rfsmassacre.heavenlibrary.paper.menu.Menu;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateMenuTask extends BukkitRunnable
{
    @Override
    public void run()
    {
        Menu.update();
    }
}
