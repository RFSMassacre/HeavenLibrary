package com.github.rfsmassacre.heavenlibrary.velocity;

import com.github.rfsmassacre.heavenlibrary.databases.MySQLDatabase;
import com.github.rfsmassacre.heavenlibrary.databases.SQLDatabase;
import com.github.rfsmassacre.heavenlibrary.velocity.configs.VelocityConfiguration;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Getter
@Plugin
(
    id = "heavenlibrary",
    name = "HeavenLibrary",
    version = "1.0-SNAPSHOT"
)
public final class HeavenLibraryVelocity extends HeavenVelocityPlugin
{
    @Getter
    private static HeavenLibraryVelocity instance;

    @Inject
    public HeavenLibraryVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory)
    {
        super("heavenlibrary", server, logger, dataDirectory);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event)
    {
        instance = this;
        File dataFolder = dataDirectory.toFile();
        dataFolder.mkdir();
        addYamlManager(new VelocityConfiguration(this, "", "config.yml", true));
        File driverFolder = new File(dataFolder + File.separator + "drivers");
        driverFolder.mkdir();
        if (driverEnabled("mysql"))
        {
            SQLDatabase.setupDrivers(driverFolder, MySQLDatabase.DRIVER, MySQLDatabase.DRIVER_URL,
                    MySQLDatabase.CLASS_NAME);
        }
    }

    private boolean driverEnabled(String sqlType)
    {
        return getConfiguration().getBoolean("drivers." + sqlType);
    }
}
