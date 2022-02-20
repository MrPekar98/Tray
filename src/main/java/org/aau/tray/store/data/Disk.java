package org.aau.tray.store.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Disk implements Fetchable, Closeable
{
    private String fileName;
    private File path;
    private int recordCount = 0;
    private List<Integer> recordLengths = new ArrayList<>();
    private List<Integer> heap = new ArrayList<>();

    public Disk(File path)
    {
        this(path, "data.db");
    }

    public Disk(File path, String name)
    {
        this.path = path;
        this.fileName = name;
        load();
    }

    private void load()
    {
        try (FileInputStream input = new FileInputStream(this.path.getAbsolutePath() + this.fileName))
        {
            int c, length = 0;
            String offsetStr = "";
            int offset = -1;

            while ((c = input.read()) != -1)
            {
                length++;

                if (c == ':')
                {
                    offset = Integer.parseInt(offsetStr);
                    offsetStr = "";
                }

                if (offset == -1)
                    offsetStr += (char) c;

                if (c == '\n')
                {
                    this.heap.add(offset, this.recordCount);
                    this.recordLengths.add(length);
                    this.recordCount++;
                    length = 0;
                    offset = -1;
                }
            }
        }

        catch (IOException except) {}
    }

    @Override
    public int write(int offset, String data)
    {
        try (FileOutputStream output = new FileOutputStream(this.path.getAbsolutePath() + this.fileName, true))
        {
            byte[] recordData = (offset + ":" + data + '\n').getBytes();
            output.write(recordData);
            output.flush();
            this.recordLengths.add(offset, recordData.length);
            this.heap.add(offset, this.recordCount);
            this.recordCount++;

            return recordData.length;
        }

        catch (IOException except)
        {
            return -1;
        }
    }

    private int lengthUntil(int offset)
    {
        int length = 0;

        for (int i = 0; i < offset; i++)
        {
            length += this.recordLengths.get(i);
        }

        return length;
    }

    @Override
    public String read(int offset)
    {
        if (offset >= this.recordCount)
            throw new IllegalArgumentException("Offset out of range: read");

        try (RandomAccessFile input = new RandomAccessFile(this.path.getAbsolutePath() + this.fileName, "r"))
        {
            int c;
            boolean skippedOffset = false;
            StringBuilder str = new StringBuilder();
            input.seek(lengthUntil(this.heap.get(offset)));

            while ((c = input.read()) != -1 && c != '\n')
            {
                if (c == ':')
                    skippedOffset = true;

                else if (skippedOffset)
                    str.append((char) c);
            }

            return str.toString();
        }

        catch (IOException except)
        {
            return null;
        }
    }

    @Override
    public void clear()
    {
        (new File(this.path.getAbsolutePath() + this.fileName)).delete();
    }

    @Override
    public int size()
    {
        return this.recordCount;
    }

    @Override
    public void delete(int offset)
    {
        throw new UnsupportedOperationException("Operator not supported in this version");
    }

    @Override
    public String toString()
    {
        return this.path.getAbsolutePath() + this.fileName;
    }

    @Override
    public void close()
    {
        clear();
    }
}
