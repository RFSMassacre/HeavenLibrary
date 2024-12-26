package com.github.rfsmassacre.heavenlibrary.paper.utils;

import com.github.rfsmassacre.heavenlibrary.paper.HeavenLibraryPaper;
import com.github.rfsmassacre.heavenlibrary.paper.HeavenPaperPlugin;
import com.github.rfsmassacre.heavenlibrary.paper.tasks.UpdateMenuTask;

public class LibraryTaskUtil extends PaperTaskUtil
{
    public LibraryTaskUtil()
    {
        super(HeavenLibraryPaper.getInstance());
    }

    @Override
    public void startTimers()
    {
        startTimer(new UpdateMenuTask(), 0L, getLong("gui"));
    }
}
