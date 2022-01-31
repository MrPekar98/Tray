package org.aau.tray.core;

import java.io.File;

public class Location
{
    private File f;
    private long offset = 0;

    public Location(File file, long offset)
    {
        this.f = file;
        this.offset = offset;
    }

    public File getFile()
    {
        return this.f;
    }

    public long getOffset()
    {
        return this.offset;
    }
}
