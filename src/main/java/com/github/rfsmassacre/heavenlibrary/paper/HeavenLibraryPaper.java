package com.github.rfsmassacre.heavenlibrary.paper;

import com.github.rfsmassacre.heavenlibrary.databases.MariaDatabase;
import com.github.rfsmassacre.heavenlibrary.databases.SQLDatabase;
import lombok.Getter;

import java.io.File;

/**
 * Do not use this file in your implementation.
 */
@Getter
@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
public class HeavenLibraryPaper extends HeavenPaperPlugin
{
    @Getter
    private static HeavenLibraryPaper instance;

    private File driverFile;

    @Override
    public void onEnable()
    {
        instance = this;

        //This is to load the library onto a server.
        File folder = getDataFolder();
        if (!folder.exists())
        {
            folder.mkdir();
        }

        try
        {
            this.driverFile = new File(getDataFolder(), MariaDatabase.DRIVER);
            if (!driverFile.exists())
            {
                getLogger().info("MariaDB drivers not found. Downloading...");
                SQLDatabase.downloadDrivers(driverFile, MariaDatabase.DRIVER_URL);
            }

            SQLDatabase.loadDriver(driverFile, MariaDatabase.CLASS_NAME);
            getLogger().info("MariaDB drivers has been installed!");
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
