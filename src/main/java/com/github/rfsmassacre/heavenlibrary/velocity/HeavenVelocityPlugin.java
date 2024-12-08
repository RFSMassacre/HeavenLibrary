package com.github.rfsmassacre.heavenlibrary.velocity;

import com.github.rfsmassacre.heavenlibrary.managers.YamlManager;
import com.github.rfsmassacre.heavenlibrary.velocity.configs.VelocityConfiguration;
import com.github.rfsmassacre.heavenlibrary.velocity.configs.VelocityLocale;
import com.github.rfsmassacre.heavenlibrary.velocity.managers.VelocityYamlManager;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class HeavenVelocityPlugin
{
    protected final String id;
    @Inject
    protected final ProxyServer server;
    @Inject
    protected final Logger logger;
    @Inject
    @DataDirectory
    protected final Path dataDirectory;

    private final Map<String, VelocityYamlManager> yamlManagers;

    @Inject
    public HeavenVelocityPlugin(String id, ProxyServer server, Logger logger, @DataDirectory Path dataDirectory)
    {
        this.id = id;
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.yamlManagers = new HashMap<>();
    }

    public void addYamlManager(VelocityYamlManager yamlManager)
    {
        yamlManagers.put(yamlManager.getFileName(), yamlManager);
    }

    public <T extends YamlManager<CommentedConfigurationNode, ConfigurationNode>> T getYamlManager(String fileName,
                                                                                                   Class<T> clazz)
    {
        VelocityYamlManager yamlManager = yamlManagers.get(fileName);
        if (clazz.isInstance(yamlManager))
        {
            return clazz.cast(yamlManager);
        }

        return null;
    }

    public VelocityConfiguration getConfiguration()
    {
        return this.getYamlManager("config.yml", VelocityConfiguration.class);
    }

    public VelocityLocale getLocale()
    {
        return this.getYamlManager("locale.yml", VelocityLocale.class);
    }
}
