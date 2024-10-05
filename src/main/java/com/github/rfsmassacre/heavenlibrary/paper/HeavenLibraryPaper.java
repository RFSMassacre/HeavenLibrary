package com.github.rfsmassacre.heavenlibrary.paper;

import lombok.Getter;

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
    }
}
