package com.github.rfsmassacre.heavenlibrary.paper;

import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperConfiguration;
import com.github.rfsmassacre.heavenlibrary.paper.configs.PaperLocale;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class HeavenPaperPlugin extends JavaPlugin
{
    protected PaperConfiguration configuration;
    protected PaperLocale locale;
}
