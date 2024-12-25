package com.github.rfsmassacre.heavenlibrary.paper;

import com.github.rfsmassacre.heavenlibrary.databases.MySQLDatabase;
import com.github.rfsmassacre.heavenlibrary.databases.SQLDatabase;
import com.github.rfsmassacre.heavenlibrary.databases.SQLiteDatabase;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperConfiguration;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperLocale;
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
        addYamlManager(new PaperConfiguration(this, "", "config.yml", true));
        addYamlManager(new PaperLocale(this, "", "locale.yml", true));
        File driverFolder = new File(dataFolder + File.separator + "drivers");
        driverFolder.mkdir();
        if (driverEnabled("mysql"))
        {
            SQLDatabase.setupDrivers(driverFolder, MySQLDatabase.DRIVER, MySQLDatabase.DRIVER_URL,
                    MySQLDatabase.CLASS_NAME);
        }

        if (driverEnabled("sqlite"))
        {
            SQLDatabase.setupDrivers(driverFolder, SQLiteDatabase.DRIVER, SQLiteDatabase.DRIVER_URL,
                    SQLiteDatabase.CLASS_NAME);
        }

        getServer().getPluginManager().registerEvents(new Menu.MenuListener(), this);
    }

    private boolean driverEnabled(String sqlType)
    {
        return getConfiguration().getBoolean("drivers." + sqlType);
    }
}
