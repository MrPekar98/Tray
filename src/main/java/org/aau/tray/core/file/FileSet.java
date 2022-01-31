package org.aau.tray.core.file;

import org.aau.tray.store.data.Fetchable;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public final class FileSet implements Iterable<Fetchable>
{
    Set<Fetchable> files;

    public FileSet()
    {
        this.files = new TreeSet<>();
    }

    public FileSet(Fetchable ... files)
    {
        this.files = Set.of(files);
    }

    public Fetchable getFile(Fetchable file)
    {
        return getFile(file.toString());
    }

    public Fetchable getFile(String fileName)
    {
        return this.files.stream().filter(f -> f.toString().equals(fileName)).findFirst().orElse(null);
    }

    public void addFile(Fetchable file)
    {
        this.files.add(file);
    }

    public int size()
    {
        return this.files.size();
    }

    public Iterator<Fetchable> iterator()
    {
        return this.files.iterator();
    }
}
