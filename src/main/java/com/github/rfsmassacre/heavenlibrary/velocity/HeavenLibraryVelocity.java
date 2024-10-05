package com.github.rfsmassacre.heavenlibrary.velocity;

import com.github.rfsmassacre.heavenlibrary.paper.HeavenLibraryPaper;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
@Plugin(id = "heavenlibrary", name = "HeavenLibrary", version = "1.0-SNAPSHOT")
public final class HeavenLibraryVelocity extends HeavenVelocityPlugin
{
    @Getter
    private static HeavenLibraryVelocity instance;

    @Inject
    public HeavenLibraryVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory)
    {
        super(server, logger, dataDirectory);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event)
    {
        instance = this;
    }
}
