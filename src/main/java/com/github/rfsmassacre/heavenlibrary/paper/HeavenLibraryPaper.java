package com.github.rfsmassacre.heavenlibrary.paper;

import com.github.rfsmassacre.heavenlibrary.databases.MySQLDatabase;
import com.github.rfsmassacre.heavenlibrary.databases.SQLDatabase;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperConfiguration;
import com.github.rfsmassacre.heavenlibrary.paper.menu.Menu;
import lombok.Getter;

import java.io.File;

/**
 * Do not use this file in your implementation.
 */
public final class HeavenLibraryPaper extends HeavenPaperPlugin
{
    @Getter
    private static HeavenLibraryPaper instance;

    @Override
    public void onEnable()
    {
        instance = this;
        File dataFolder = getDataFolder();
        dataFolder.mkdir();
        this.configuration = new PaperConfiguration(this, "", "config.yml");
        File driverFolder = new File(dataFolder + File.separator + "drivers");
        driverFolder.mkdir();
        if (driverEnabled("mysql"))
        {
            SQLDatabase.setupDrivers(driverFolder, MySQLDatabase.DRIVER, MySQLDatabase.DRIVER_URL,
                    MySQLDatabase.CLASS_NAME);
        }

        getServer().getPluginManager().registerEvents(new Menu.MenuListener(), this);
    }

    private boolean driverEnabled(String sqlType)
    {
        return configuration.getBoolean("drivers." + sqlType);
    }
}
