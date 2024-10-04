package com.github.rfsmassacre.heavenlibrary.velocity;

import com.github.rfsmassacre.heavenlibrary.databases.MariaDatabase;
import com.github.rfsmassacre.heavenlibrary.databases.SQLDatabase;
import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
@Plugin(id = "heavenlibrary", name = "HeavenLibrary", version = "1.0-SNAPSHOT")
public class HeavenLibraryVelocity
{
    @Inject
    protected final ProxyServer server;
    @Inject
    protected final Logger logger;
    @Inject
    @DataDirectory
    protected final Path dataDirectory;

    @Inject
    public HeavenLibraryVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory)
    {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event)
    {
        //This is to load the library onto a server.
        if (Files.notExists(dataDirectory))
        {
            try
            {
                Files.createDirectory(dataDirectory);
            }
            catch (IOException exception)
            {
                logger.debug("Data directory could not be created.");
            }
        }

        try
        {
            File file = new File(dataDirectory.toFile(), MariaDatabase.DRIVER);
            if (!file.exists())
            {
                getLogger().info("MySQL drivers not found. Downloading...");
                SQLDatabase.downloadDrivers(file, MariaDatabase.DRIVER_URL);
            }

            SQLDatabase.loadDriver(file, MariaDatabase.CLASS_NAME);
            getLogger().info("MySQL drivers installed!");
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
