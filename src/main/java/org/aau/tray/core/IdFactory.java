package org.aau.tray.core;

public class IdFactory
{
    private static int counter = 0;

    public static NodeId nodeId()
    {
        return new NodeId(counter++);
    }
}
