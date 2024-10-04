package com.github.rfsmassacre.heavenlibrary.velocity;

import com.github.rfsmassacre.heavenlibrary.velocity.configs.VelocityConfiguration;
import com.github.rfsmassacre.heavenlibrary.velocity.configs.VelocityLocale;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

@Getter
public abstract class HeavenVelocityPlugin
{
    @Inject
    protected final ProxyServer server;
    @Inject
    protected final Logger logger;
    @Inject
    @DataDirectory
    protected final Path dataDirectory;

    protected VelocityConfiguration configuration;
    protected VelocityLocale locale;

    @Inject
    public HeavenVelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory)
    {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }
}
