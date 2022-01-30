package org.aau.tray.system.journal;

import java.io.*;
import java.nio.file.Path;

public class Journal implements Checkpointer
{
    private File file;
    private String name;
    private static final String FILE_NAME = "journal.dat";
    private long lastCheckpoint = 0;

    public Journal(Path path, String name)
    {
        this.name = name;
        this.file = new File(path.toString() + name + "-" + FILE_NAME);
    }

    public long getLastCheckpointTimestamp()
    {
        return this.lastCheckpoint;
    }

    @Override
    public synchronized void check(Object obj) throws IOException
    {
        File newFile = new File(this.file.getAbsolutePath().replace(this.name + "-" + FILE_NAME, "temp.dat"));
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(newFile));
        output.writeObject(obj);
        output.close();

        if (file.exists())
            clear();

        if (!newFile.renameTo(this.file))
            throw new IOException("Checkpoint error: rename");

        this.lastCheckpoint = System.currentTimeMillis();
    }

    @Override
    public void clear()
    {
        this.file.delete();
    }

    @Override
    public synchronized Object readLast() throws IOException
    {
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(this.file));

        try
        {
            Object obj = input.readObject();
            input.close();
            return obj;
        }

        catch (ClassNotFoundException exc)
        {
            throw new IOException("Object read error: " + exc.getMessage());
        }
    }
}
