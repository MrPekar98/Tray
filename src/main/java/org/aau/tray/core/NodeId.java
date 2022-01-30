package org.aau.tray.core;

public class NodeId
{
    private int id;

    NodeId(int id)
    {
        this.id = id;
    }

    public static NodeId make(Integer id)
    {
        return new NodeId(id);
    }

    public int id()
    {
        return this.id;
    }
}
