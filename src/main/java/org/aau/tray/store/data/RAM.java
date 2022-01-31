package org.aau.tray.store.data;

import java.util.ArrayList;
import java.util.List;

public class RAM implements Fetchable
{
    private List<String> data = new ArrayList<>();
    private String name;

    public RAM(String id)
    {
        this.name = id;
    }

    @Override
    public void clear()
    {
        this.data.clear();
    }

    @Override
    public int write(int offset, String data)
    {
        this.data.add(offset, data);
        return data.length();
    }

    @Override
    public String read(int offset)
    {
        return this.data.get(offset);
    }

    @Override
    public int size()
    {
        return this.data.size();
    }

    @Override
    public void delete(int offset)
    {
        if (offset < 0 || offset >= this.data.size())
            throw new IllegalArgumentException("Offset out of bounds: delete");

        this.data.remove(offset);
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
